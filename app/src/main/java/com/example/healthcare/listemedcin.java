package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ListView;

import com.example.healthcare.Model.doctors;
import com.example.healthcare.ui.medcinadapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class listemedcin extends AppCompatActivity {

    ListView listView ;
    List<doctors> list ;
    List<doctors> doctors;
    private CircleImageView profile_img;
    StorageReference storageReferenc;


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


        storageReferenc = FirebaseStorage.getInstance().getReference("Images").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
        final File localFile;
        try {
            localFile = File.createTempFile("images", "jpg");

            profile_img = (CircleImageView) findViewById(R.id.searchdoctor);

            storageReferenc.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            profile_img.setImageBitmap(myBitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }






    }
}
