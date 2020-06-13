package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcare.Model.doctors;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DocProfile extends AppCompatActivity {

    private  CircularImageView  profile_image;
    private TextView emaildoc ;
    private TextView password;
    private TextView phone;
    private TextView specialite;
    private TextView fullname;
    private TextView adresse;
    String EmailDoc ;
    private ImageView delep ;
    private ImageView plusprofile ;
    Button backhome;
    Uri pickedpic;
    private TextView adressedoc ;
    static int PReqCode = 1 ;
    static int reqcode = 1 ;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    private StorageReference storageReference1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == reqcode && data != null) {

            pickedpic = data.getData();
            profile_image.setImageURI(pickedpic);
            SweetAlertDialog dialog = new SweetAlertDialog(DocProfile.this, SweetAlertDialog.SUCCESS_TYPE);

            imageUploader();

            dialog.setTitle("Profile picture");
            dialog.setTitleText("Profile picture has been updated successfully");
            dialog.show();


        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_profile);

        emaildoc = (TextView)findViewById(R.id.email_doc);
        fullname = (TextView)findViewById(R.id.doc_name);
        password = (TextView)findViewById(R.id.password_doc);
        phone = (TextView)findViewById(R.id.phone_doc);
        plusprofile = (ImageView)findViewById(R.id.plusprofiledoc);
        profile_image = (CircularImageView )findViewById(R.id.image_doc);
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        adressedoc = (TextView)findViewById(R.id.address_doc);
        specialite = (TextView) findViewById(R.id.specialite_doc);
        delep = (ImageView)findViewById(R.id.deletepicdoc);
        backhome = findViewById(R.id.back_homedoc);

        FirebaseFirestore db  = FirebaseFirestore.getInstance();

        EmailDoc = FirebaseAuth.getInstance().getCurrentUser().getEmail();



        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), homedoctor.class));
            }
        });


        db.collection("doctors").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : p) {
                        doctors md = d.toObject(doctors.class);
                        if (md.getEmail().equals(EmailDoc)) {
                            emaildoc.setText(md.getEmail());
                            password.setText(md.getPassword());
                            phone.setText("(+212)"+md.getPhone());
                            specialite.setText(md.getSpecialite());
                            fullname.setText("Dr."+md.getFullname());
                            adressedoc.setText(md.getAddress());


                        }



                    }





                }

            }
        });

        //Uploading profile picture :
        plusprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT > 22) {

                    CheckandRequestforPermission();
              }else{

                   Opengallery();


                }

           }
        });


        //retrieve profile image from firebase  :

        storageReference1 = FirebaseStorage.getInstance().getReference("Images").child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+".jpg");


        final File localFile;
        try {
            localFile = File.createTempFile("images", "jpg");


            storageReference1.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            profile_image.setImageBitmap(myBitmap);




                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    // ...
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        //delete profile pic :

        delep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              storageReference1 = FirebaseStorage.getInstance().getReference("Images").child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+".jpg");



              final SweetAlertDialog dialog = new SweetAlertDialog(DocProfile.this, SweetAlertDialog.WARNING_TYPE);
               dialog.setTitle("Profile picture");
                dialog.setTitleText("Do you really want to delete your profile picture");

             dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                   @Override
                   public void onClick(SweetAlertDialog sweetAlertDialog) {

                      storageReference1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                      public void onSuccess(Void aVoid) {

                           profile_image.setImageResource(R.drawable.image_2);
                           Toast.makeText(DocProfile.this,"Profile picture deleted ... ",Toast.LENGTH_SHORT);
                             dialog.dismiss();

                         }
                    });


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





    }

    private void Opengallery() {


        Intent gal = new Intent(Intent.ACTION_GET_CONTENT);
        gal.setType("image/*");
        startActivityForResult(gal,reqcode);



    }

    public void  CheckandRequestforPermission(){

        if(ContextCompat.checkSelfPermission(DocProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(DocProfile.this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(DocProfile.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }else {

                ActivityCompat.requestPermissions(DocProfile.this,
                        new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);

            }

        }else {

            Opengallery();

        }

    }

    public String getExtention (Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    public void imageUploader(){

        StorageReference ref = storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail()+".jpg");
        ref.putFile(pickedpic)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });




    }
}
