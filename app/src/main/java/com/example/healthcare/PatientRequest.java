package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.healthcare.Model.DPR;
import com.example.healthcare.Model.fiche;
import com.example.healthcare.ui.MedicalFileAdapter;
import com.example.healthcare.ui.Request_Doctor_Adapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientRequest extends AppCompatActivity {

    ListView listView ;
    FirebaseFirestore db ;
    private List<HashMap<String,DPR>> hashmap;
    private Button  back ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_request);
        listView = (ListView)findViewById(R.id.request_list);
        db = FirebaseFirestore.getInstance();
        back = (Button)findViewById(R.id.button_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        setALLrequest();


    }

    public void setALLrequest(){
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


                    Request_Doctor_Adapter adapter = new Request_Doctor_Adapter(getBaseContext(),R.layout.request_adapter,hashmap);
                    listView.setAdapter(adapter);

                }

            }
        });


    }
}
