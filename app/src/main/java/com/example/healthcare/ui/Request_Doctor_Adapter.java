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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthcare.MailAPI.MailAPI;
import com.example.healthcare.Model.DPR;
import com.example.healthcare.Model.doctors;
import com.example.healthcare.Model.patient;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

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
        final String date = getItem(position).get(id).getDate_creation();
        final String email_patient = getItem(position).get(id).getEmail_patient();

        LayoutInflater inflater =  LayoutInflater.from(ncontext);
        convertView = inflater.inflate(nresource,parent,false);

        TextView date_textview = (TextView)convertView.findViewById(R.id.date_sent_request);
        TextView fullname  = (TextView)convertView.findViewById(R.id.patient_name);
        ImageView profile_p = (ImageView)convertView.findViewById(R.id.profile_p);
        Button Accept = (Button)convertView.findViewById(R.id.accept);
        Button refuse = (Button)convertView.findViewById(R.id.refuse);

        date_textview.setText(date);
        set_fullname_by_email(fullname,email_patient);
        set_profile_pic(email_patient,profile_p);

        final String finalId = id;
        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                accept_request(finalId,v);


                send_acceptence_email(email_patient,FirebaseAuth.getInstance().getCurrentUser().getEmail());

                objects.remove(position);
                notifyDataSetChanged();


            }
        });

        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE);

                dialog.setTitle("");
                dialog.setTitleText("Do you realy want to refuse this patient request");
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                         refuse_request(finalId,position);
                         notifyDataSetChanged();
                         dialog.dismiss();
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

        return  convertView ;

    }

    public void accept_request(String id , final View v){
        db = FirebaseFirestore.getInstance();
        db.collection("RDP").document(id).update("etat_DPR","Accepted")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        final SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.SUCCESS_TYPE);
                        dialog.setTitle("");
                        dialog.setTitleText("You can now maintain the medical record of this patient");
                        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                dialog.dismiss();
                                if(objects.size() == 0) {
                                    //Refresh the activity without animation
                                    Intent intent = ((Activity)ncontext).getIntent();
                                    ((Activity)ncontext).overridePendingTransition(0, 0);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    ((Activity)ncontext).finish();
                                    ((Activity)ncontext).overridePendingTransition(0, 0);
                                    ((Activity)ncontext).startActivity(intent);

                                }
                            }
                        });
                        dialog.setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        Toast.makeText(getContext(),"Request is accepted",Toast.LENGTH_SHORT).show();




                    }
                });
    }


    public void refuse_request(String id , final int position){

        db = FirebaseFirestore.getInstance();
        db.collection("RDP").document(id).update("etat_DPR","Refused")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        objects.remove(position);
                        Toast.makeText(getContext(),"Request is refused",Toast.LENGTH_SHORT).show();
                        if(objects.size() == 0) {
                            //Refresh the activity without animation
                            Intent intent = ((Activity)ncontext).getIntent();
                            ((Activity)ncontext).overridePendingTransition(0, 0);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            ((Activity)ncontext).finish();
                            ((Activity)ncontext).overridePendingTransition(0, 0);
                            ((Activity)ncontext).startActivity(intent);

                        }


                    }
                });


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

        public void send_acceptence_email(final String email_patient,String email_doctor){
           final String subject = "HEALTH CARE : REQUEST ACCEPTENCE" ;

            db = FirebaseFirestore.getInstance();
            db.collection("doctors").whereEqualTo("email",email_doctor).limit(1).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                            doctors  doc  = null;
                            for (DocumentSnapshot d : p) {
                                doc = d.toObject(doctors.class);
                            }
                            String message = "Dr."+doc.getFullname()+" has just accepted your request" ;
                            MailAPI mailAPI = new MailAPI(email_patient,subject,message);
                            mailAPI.execute();

                        }


                    });

        }



}
