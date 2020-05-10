package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

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

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class homedoctor extends AppCompatActivity {
    StorageReference storageReference ;
    CircleImageView profile_image ;
    private FirebaseFirestore db1;
    String EmailDoct ;
    TextView fullname1;
    CardView prfdoctor;
    CardView exit;
    CardView mypt ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homedoctor);


        db1 = FirebaseFirestore.getInstance();
        EmailDoct = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        fullname1=findViewById(R.id.doctname);



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

                    profile_image = (CircleImageView) findViewById(R.id.home_profile_doctor);

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
