package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
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

    private TextView search  ;
    private CardView profile ;
    CircleImageView profile_image ;
    StorageReference storageReference ;
    private  CardView medicalfolder ;
    private FirebaseFirestore db ;
    private  CardView appoinement;
    CardView logt ;
    private  CardView bmi ;


    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        search  = (TextView)findViewById(R.id.search);
        profile =  (CardView) findViewById(R.id.profile);
        profile_image = (CircleImageView)findViewById(R.id.home_profile_image);
        medicalfolder = (CardView) findViewById(R.id.medicalfolder);
        appoinement = (CardView)findViewById(R.id.Appoinement);
        logt = (CardView)findViewById(R.id.logout);
        db = FirebaseFirestore.getInstance() ;
        bmi = (CardView)findViewById(R.id.bmi);




        appoinement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),AddAppointement.class));
            }
        });


        bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),BMI.class));
            }
        });


        logt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                builder.setTitle("Logout");

                builder.setMessage("Do you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                firebaseAuth.getInstance().signOut() ;
                                finish();
                                Intent  intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Logout Successfull!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();



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
             String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
             db.collection("fiches").whereEqualTo("postedby",email).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
