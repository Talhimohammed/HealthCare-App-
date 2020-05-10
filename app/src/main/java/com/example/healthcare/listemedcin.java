package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.healthcare.Model.doctors;
import com.example.healthcare.ui.medcinadapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class listemedcin extends AppCompatActivity {

    ListView listView ;
    List<doctors> list ;
    List<doctors> doctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listemedcin);
        listView = (ListView)findViewById(R.id.listmedcinview);
        list = new ArrayList<>();
        FirebaseFirestore db  = FirebaseFirestore.getInstance();

        list.add(new doctors("talhimohammed","talhi mohammed",991199191,"helloworld","j","k","Cardiologue"));
        list.add(new doctors("talhimohammed","boumair hamza",991199191,"helloworld","j","k","Cardiologue"));

        doctors = new ArrayList<>();
        db.collection("doctors").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

             if (!queryDocumentSnapshots.isEmpty()) {
                 List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                 for (DocumentSnapshot d : p) {
                     doctors med = d.toObject(doctors.class);
                     doctors.add(med);

                     medcinadapter adapter = new medcinadapter(getBaseContext(),R.layout.medcinadapter, doctors);

                     listView.setAdapter(adapter);

                 }


             }
            }
        });






    }
}
