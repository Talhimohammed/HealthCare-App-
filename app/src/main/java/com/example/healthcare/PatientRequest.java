package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.healthcare.MailAPI.MailAPI;
import com.example.healthcare.Model.DPR;
import com.example.healthcare.Model.doctors;
import com.example.healthcare.Model.fiche;
import com.example.healthcare.Model.patient;
import com.example.healthcare.ui.MedicalFileAdapter;
import com.example.healthcare.ui.Request_Doctor_Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PatientRequest extends AppCompatActivity {

    ListView listView ;
    FirebaseFirestore db ;
    private List<HashMap<String,DPR>> hashmap;
    private Button  back ;
    private ImageView emptylist ;
    private ImageView refresh ;
    private Button refuseall ;
    private Button accepteall ;
    SweetAlertDialog dialog  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_request);
        listView = (ListView)findViewById(R.id.request_list);
        db = FirebaseFirestore.getInstance();
        back = (Button)findViewById(R.id.button_back);
        emptylist =  (ImageView)findViewById(R.id.emptylist);
        refresh = (ImageView)findViewById(R.id.refresh);
        refuseall =  (Button)findViewById(R.id.refuseAll);
        accepteall = (Button)findViewById(R.id.acceptAll);
        dialog = new SweetAlertDialog(PatientRequest.this, SweetAlertDialog.PROGRESS_TYPE);





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setALLrequest(this);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                Toast.makeText(v.getContext(),"Refreshed",Toast.LENGTH_SHORT).show();

            }
        });

        accepteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitleText("Accepting all patient requests");
                dialog.show();
                getData_AND_UPDATE("Accepted");
            }
        });

        refuseall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitleText("Refusing all patient requests");
                dialog.show();
                getData_AND_UPDATE("Refused");

            }
        });



    }

    public void setALLrequest(final Context context){
        hashmap = new ArrayList<>();
        hashmap.clear();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail() ;
        db.collection("RDP").whereEqualTo("email_doctor",email).whereEqualTo("etat_DPR","In Progress")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : p) {
                        HashMap<String,DPR> m = new HashMap<String,DPR>();
                        m.put(d.getId(),d.toObject(DPR.class));
                        hashmap.add(m);
                    }

                    Request_Doctor_Adapter adapter = new Request_Doctor_Adapter(context,R.layout.request_adapter,hashmap);
                    listView.setAdapter(adapter);

                }else {
                    listView.setVisibility(View.GONE);
                    emptylist.setVisibility(View.VISIBLE);

                    accepteall.setEnabled(false);
                    refuseall.setEnabled(false);

                }

            }
        });

    }

    void getData_AND_UPDATE(final String value) {

      String email_doctor  = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                   db.collection("RDP").whereEqualTo("email_doctor",email_doctor).whereEqualTo("etat_DPR","In Progress")
                           .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }

                    updateData((ArrayList)list,value); // *** new ***
                } else {

                }
            }
        });

    }
    void updateData(ArrayList list, final String value) {

        // Get a new write batch
        WriteBatch batch = db.batch();

        for (int k = 0; k < list.size(); k++) {

            DocumentReference ref = db.collection("RDP").document(""+list.get(k));
            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DPR pat = documentSnapshot.toObject(DPR.class);
                    send_acceptence_email(pat.getEmail_patient(),FirebaseAuth.getInstance().getCurrentUser().getEmail());
                }
            });
            batch.update(ref, "etat_DPR", value);

        }
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                Toast.makeText(getBaseContext(),"All patient requests are "+value,Toast.LENGTH_SHORT).show();
                listView.setVisibility(View.GONE);
                emptylist.setVisibility(View.VISIBLE);
            }
        });

    }
    public void send_acceptence_email(final String email_patient,String email_doctor){
        final String subject = "HEALTH CARE : REQUEST ACCEPTENCE" ;

        db.collection("doctors").whereEqualTo("email",email_doctor).limit(1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                        doctors doc  = null;
                        for (DocumentSnapshot d : p) {
                            doc = d.toObject(doctors.class);
                        }
                        String message = "Dr."+doc.getFullname()+" has just accepted your request" ;
                        MailAPI mailAPI = new MailAPI(email_patient,subject,message);
                        mailAPI.execute();

                    }


                });

    }
}
