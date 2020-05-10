package com.example.healthcare.firestore;

import com.example.healthcare.Model.doctors;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class database {
    FirebaseFirestore db  = FirebaseFirestore.getInstance();
    List <doctors> doctors;

     public  void getallmedcins(){



         db.collection("doctors").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                      List<DocumentSnapshot> p  =   queryDocumentSnapshots.getDocuments() ;
                       for (DocumentSnapshot d:p) {
                        doctors med = d.toObject(doctors.class);
                        doctors.add(med);

                       }
             }
         });





     }
}
