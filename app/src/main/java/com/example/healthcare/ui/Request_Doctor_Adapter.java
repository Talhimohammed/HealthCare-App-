package com.example.healthcare.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthcare.Model.DPR;
import com.example.healthcare.Model.doctors;
import com.example.healthcare.R;
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
import java.util.HashMap;
import java.util.List;

public class Request_Doctor_Adapter extends ArrayAdapter<HashMap<String, DPR>> {
    private Context ncontext ;
    private int nresource ;
    private List<HashMap<String, DPR>> objects ;
    private FirebaseFirestore  db ;
    private StorageReference storageReference1 ;

    public Request_Doctor_Adapter(@NonNull Context context, int resource, @NonNull List<HashMap<String, DPR>> objects) {
        super(context, resource, objects);
        this.ncontext  = context  ;
        this.nresource = resource ;
        this.objects   = objects  ;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String id = null ;
        for ( String key : getItem(position).keySet() ) {
            id = key ;
        }

        String date = getItem(position).get(id).getDate_creation();
        String email_patient = getItem(position).get(id).getEmail_patient();

        LayoutInflater inflater =  LayoutInflater.from(ncontext);
        convertView = inflater.inflate(nresource,parent,false);

        TextView date_textview = (TextView)convertView.findViewById(R.id.date_sent_request);
        TextView fullname  = (TextView)convertView.findViewById(R.id.patient_name);
        ImageView profile_p = (ImageView)convertView.findViewById(R.id.profile_p);

        date_textview.setText(date);
        set_fullname_by_email(fullname,email_patient);
        set_profile_pic(email_patient,profile_p);


        return  convertView ;

    }

    public void set_fullname_by_email(final TextView fullname , final String email){
        db = FirebaseFirestore.getInstance();
        db.collection("patient").whereEqualTo("email",email).limit(1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                            doctors  doc  = null;
                            for (DocumentSnapshot d : p) {
                                  doc = d.toObject(doctors.class);
                                }
                            fullname.setText(doc.getFullname());

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
