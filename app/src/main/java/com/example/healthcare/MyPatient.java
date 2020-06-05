package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.healthcare.Model.DPR;
import com.example.healthcare.Model.patient;
import com.example.healthcare.ui.mypatient_adapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyPatient extends AppCompatActivity {

    private FirebaseFirestore db ;
    private ImageView refresh ;
    private ListView pa ;
    private List<DPR> patients ;
    private Button back ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patient);
        db = FirebaseFirestore.getInstance();
        refresh = (ImageView)findViewById(R.id.refresh);
        pa = (ListView)findViewById(R.id.patient_list);
        back =(Button)findViewById(R.id.back);
        patients = new ArrayList<>();

        mypatients(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mypatients(v.getContext());
                Toast.makeText(v.getContext(),"Refreshing...",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void mypatients(final Context context){

        patients.clear();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail() ;
        db.collection("RDP").whereEqualTo("email_doctor",email).whereEqualTo("etat_DPR","Accepted")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                  if(!queryDocumentSnapshots.isEmpty()) {
                      List<DocumentSnapshot> d = queryDocumentSnapshots.getDocuments();
                      for (DocumentSnapshot p : d ) {
                         DPR a = p.toObject(DPR.class);
                         patients.add(a);
                      }
                      mypatient_adapter adapter =  new mypatient_adapter(context,R.layout.patients_adapter,patients);
                      pa.setAdapter(adapter);
                  }
            }
        });
    }

}
