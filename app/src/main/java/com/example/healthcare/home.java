package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class home extends AppCompatActivity {

    Button search  ;
    Button profile ;
    CircleImageView profile_image ;
    StorageReference storageReference ;
    Button medicalfolder ;
    private FirebaseFirestore db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        search  = (Button)findViewById(R.id.search);
        profile =  (Button)findViewById(R.id.profile);
        profile_image = (CircleImageView)findViewById(R.id.home_profile_image);
        medicalfolder = (Button)findViewById(R.id.medicalfolder);
        db = FirebaseFirestore.getInstance() ;



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),listemedcin.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),PatientProfile.class));
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("Images").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg");
        final File localFile;
        try {
            localFile = File.createTempFile("images", "jpg");


            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            profile_image.setImageBitmap(myBitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                 }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        medicalfolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("fiches").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size()>0) {

                            startActivity(new Intent(getBaseContext(),Medicalfiles.class));

                        }else {
                            startActivity(new Intent(getBaseContext(),MedicalFolder.class));

                        }
                    }
                });

            }
        });


    }
}
