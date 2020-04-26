package com.example.healthcare.firestore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.healthcare.Model.medcin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class database {
    FirebaseFirestore db  = FirebaseFirestore.getInstance();
    List <medcin> medcins ;

     public  void getallmedcins(){



         db.collection("Medcin").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                      List<DocumentSnapshot> p  =   queryDocumentSnapshots.getDocuments() ;
                       for (DocumentSnapshot d:p) {
                        medcin med = d.toObject(medcin.class);
                        medcins.add(med);

                       }
             }
         });





     }
}
