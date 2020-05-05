package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class home extends AppCompatActivity {

    TextView search  ;
    LinearLayout profile ;
    CircleImageView profile_image ;
    StorageReference storageReference ;
    LinearLayout medicalfolder ;
    private FirebaseFirestore db ;
    LinearLayout logt ;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        search  = (TextView)findViewById(R.id.search);
        profile =  (LinearLayout)findViewById(R.id.profile);
        profile_image = (CircleImageView)findViewById(R.id.home_profile_image);
        medicalfolder = (LinearLayout)findViewById(R.id.medicalfolder);
        logt = (LinearLayout)findViewById(R.id.logout);
        db = FirebaseFirestore.getInstance() ;


        logt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getInstance().signOut();
                finish();
              Intent  intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                 Toast.makeText(getApplicationContext(), "Logout Successful!", Toast.LENGTH_SHORT).show();
            }
        });




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
