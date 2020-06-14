package com.example.healthcare.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.healthcare.Medical_Folder_Of_My_Patient;
import com.example.healthcare.Model.DPR;
import com.example.healthcare.Model.patient;
import com.example.healthcare.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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



public class mypatient_adapter extends ArrayAdapter<DPR> {

    private Context ncontext ;
    private int nresource ;
    private List<DPR> objects ;
    private FirebaseFirestore  db ;
    private StorageReference storageReference1 ;
    public mypatient_adapter(@NonNull Context context, int resource, @NonNull List<DPR> objects) {
        super(context, resource, objects);
        this.ncontext  =  context   ;
        this.nresource = resource ;
        this.objects = objects ;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final String email_patient = getItem(position).getEmail_patient();

        LayoutInflater inflater =  LayoutInflater.from(ncontext);
        convertView = inflater.inflate(nresource,parent,false);
        TextView e = (TextView)convertView.findViewById(R.id.Email);
        TextView n = (TextView)convertView.findViewById(R.id.fullname);
        ImageView profile = (ImageView)convertView.findViewById(R.id.profile_p);
        final Button opendialog = (Button)convertView.findViewById(R.id.opendialog);
        final Button medicalfolder = (Button)convertView.findViewById(R.id.cmf);

        medicalfolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), Medical_Folder_Of_My_Patient.class);
                intent.putExtra("patient_email",email_patient);
                v.getContext().startActivity(intent);

            }
        });

        opendialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog d = new contactDialog(email_patient,FirebaseAuth.getInstance().getCurrentUser().getEmail(),v.getContext());
                d.show(((FragmentActivity)ncontext).getSupportFragmentManager(),"CONTACT");
            }
        });

        set_fullname_by_email(n,email_patient);
        set_profile_pic(email_patient,profile);
        e.setText(email_patient);

        return  convertView ;

    }
    public void set_fullname_by_email(final TextView fullname , final String email){
        db = FirebaseFirestore.getInstance();
        db.collection("patient").whereEqualTo("email",email).limit(1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                        patient pat  = null;
                        for (DocumentSnapshot d : p) {
                            pat = d.toObject(patient.class);
                        }
                        fullname.setText(pat.getFullname());

                    }


                });
    }

    public void set_profile_pic(String email_patient , final ImageView profile_image){

        storageReference1 = FirebaseStorage.getInstance().getReference("Images").child(email_patient+".jpg");

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

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
