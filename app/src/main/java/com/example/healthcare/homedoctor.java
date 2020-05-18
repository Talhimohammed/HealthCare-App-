package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcare.Model.doctors;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class homedoctor extends AppCompatActivity {
    StorageReference storageReference ;
    CircularImageView profile_image ;
    private FirebaseFirestore db1;
    String EmailDoct ;
    TextView fullname1;
    CardView prfdoctor;
    CardView exit;
    CardView mypt ;
    CardView rdv ;
    String mailDoc ;
    TextView specialitee;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homedoctor);


        db1 = FirebaseFirestore.getInstance();
        EmailDoct = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        fullname1=findViewById(R.id.doctname);
        mypt = findViewById(R.id.my_patient);
        exit = findViewById(R.id.logouta);
        prfdoctor = findViewById(R.id.profildoc);
        rdv = findViewById(R.id.RDV);
        specialitee = findViewById(R.id.specialitedoct);
        FirebaseFirestore db2  = FirebaseFirestore.getInstance();
        mailDoc = FirebaseAuth.getInstance().getCurrentUser().getEmail();






        db2.collection("doctors").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : p) {
                        doctors mda = d.toObject(doctors.class);
                        if (mda.getEmail().equals(mailDoc)) {
                            specialitee.setText(mda.getSpecialite());



                        }



                    }

                }

            }
        });





                    rdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


        prfdoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), DocProfile.class));
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final SweetAlertDialog dialog = new SweetAlertDialog(homedoctor.this, SweetAlertDialog.WARNING_TYPE);
                dialog.setTitle("Logout");
                dialog.setTitleText("Do you really want to logout ? ");

                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        firebaseAuth.getInstance().signOut();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
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






















         Task<QuerySnapshot> querySnapshotTask = db1.collection("doctors").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                 if (!queryDocumentSnapshots.isEmpty()) {
                     List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                     for (DocumentSnapshot d : p) {
                         doctors md = d.toObject(doctors.class);
                         if (md.getEmail().equals(EmailDoct)) {
                             fullname1.setText("Dr." + md.getFullname());

                         }
                     }
                 }
             }
         });











                storageReference = FirebaseStorage.getInstance().getReference("Images").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
                final File localFile;
                try {
                    localFile = File.createTempFile("images", "jpg");

                    profile_image = (CircularImageView ) findViewById(R.id.home_profile_doctor);

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


            }
        };
