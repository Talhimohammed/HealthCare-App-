package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class home extends AppCompatActivity {

    private TextView search;
    private CardView profile;
    private CircularImageView profile_image;
    StorageReference storageReference;
    private CardView medicalfolder;
    private FirebaseFirestore db;
    private CardView appoinement;
    private CardView logt;
    private CardView bmi;


    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        search = (TextView) findViewById(R.id.search);
        profile = (CardView) findViewById(R.id.profile);
        profile_image = (CircularImageView) findViewById(R.id.home_profile_image);
        medicalfolder = (CardView) findViewById(R.id.medicalfolder);
        appoinement = (CardView) findViewById(R.id.Appm);
        logt = (CardView) findViewById(R.id.logoutt);
        db = FirebaseFirestore.getInstance();
        bmi = (CardView) findViewById(R.id.bmi);


        appoinement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), myappointements.class));
            }
        });


        bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), BMI.class));
            }
        });


        logt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SweetAlertDialog dialog = new SweetAlertDialog(home.this, SweetAlertDialog.WARNING_TYPE);
                dialog.setTitle("Logout");
                dialog.setTitleText("Do you really want to logout ? ");

                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        firebaseAuth.getInstance().signOut();
                        finish();
                        Toast.makeText(getApplicationContext(), "Logout Successfull!", Toast.LENGTH_SHORT).show();

                    }
                });

                dialog.setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        dialog.dismiss();

                    }
                });

                dialog.show();


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

       storageReference = FirebaseStorage.getInstance().getReference("Images").child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+".jpg");
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
             db.collection("fiches").whereEqualTo("patient_email",email).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

        appoinement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),myappointements.class));
            }
        });



    }


}
