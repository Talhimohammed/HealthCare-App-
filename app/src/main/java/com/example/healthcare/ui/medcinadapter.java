package com.example.healthcare.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.healthcare.AddAppointement;
import com.example.healthcare.Model.DPR;
import com.example.healthcare.Model.doctors;
import com.example.healthcare.R;
import com.example.healthcare.signup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class medcinadapter extends ArrayAdapter<doctors> implements Filterable {

    private static final String TAG = "medcinadapter" ;
    private Context mcontext ;
    private int nressource ;
    private FirebaseFirestore firestore ;
    private StorageReference storageReference1 ;
    private List<doctors> doctorsList ;
    private List<doctors> fulldoctorlist ;


    public medcinadapter(@NonNull Context context, int resource, @NonNull List<doctors> objects) {
        super(context, resource, objects);
        mcontext = context ;
        nressource = resource ;
        this.doctorsList = objects ;
        this.fulldoctorlist = new ArrayList<>(doctorsList);


    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getFullname();
        String specialite = getItem(position).getSpecialite();
        final String email = getItem(position).getEmail();

        LayoutInflater inflater =  LayoutInflater.from(mcontext);
        convertView = inflater.inflate(nressource,parent,false);

        TextView fullname = (TextView)convertView.findViewById(R.id.nom);
        TextView spe  = (TextView)convertView.findViewById(R.id.specialite) ;
        Button moreinfo = (Button)convertView.findViewById(R.id.moreinformation);
        Button appoint = (Button)convertView.findViewById(R.id.bookappoint);
        Button sendR = (Button) convertView.findViewById(R.id.SendButton);
        final  ImageView profilepic = (ImageView)convertView.findViewById(R.id.picdoctor);

        //retrieve the pic from the database storage

        set_profile_pic(email,profilepic);




        // Sending request to a doctor

        sendR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.NORMAL_TYPE);

                dialog.setTitle("Request");
                dialog.setTitleText("Your doctors can maintain your medical record , do you really want to send a request to this doctor ?");
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        addDPR(getItem(position).getEmail(), FirebaseAuth.getInstance().getCurrentUser().getEmail());
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
        CHECK_IF_THE_REQUEST_WAS_ALREADY_SENT(sendR,getItem(position).getEmail(),FirebaseAuth.getInstance().getCurrentUser().getEmail());

       //
        moreinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctordialog d = new doctordialog(getItem(position).getEmail().toString(),""+getItem(position).getPhone(),getItem(position).getVille().toString());
                d.show(((FragmentActivity)v.getContext()).getSupportFragmentManager(),"Information");
            }
        });

        appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(v.getContext(), AddAppointement.class);
                p.putExtra("Doc_Ref", getItem(position).getEmail())  ;
                p.putExtra("Doc_Name",getItem(position).getFullname());
                v.getContext().startActivity(p);
            }
        });

        fullname.setText(name);
        spe.setText(specialite);

        return convertView ;

    }

    private void set_profile_pic(String email, final ImageView profilepic) {
        storageReference1 = FirebaseStorage.getInstance().getReference("Images").child(email+".jpg");

        final File localFile;
        try {
            localFile = File.createTempFile("images", "jpg");


            storageReference1.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            profilepic.setImageBitmap(myBitmap);

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

    public  void addDPR(String email_doctor , String email_patient ){
        firestore = FirebaseFirestore.getInstance();
        DPR d = new DPR();
        d.setDate_creation(getCurrentDate());
        d.setEmail_doctor(email_doctor);
        d.setEmail_patient(email_patient);
        d.setEtat_DPR("In Progress");

        firestore.collection("RDP").document(generatecode()+"").set(d).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(),"Your request has been sent successfully to the doctor",Toast.LENGTH_LONG).show();
            }
        });

    }
    public int generatecode(){
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        return n ;
    }

    public String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        return formattedDate ;
    }

    public void CHECK_IF_THE_REQUEST_WAS_ALREADY_SENT(final Button A, String Doctor_EMAIL , String Patient_Email) {

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("RDP").whereEqualTo("email_doctor",Doctor_EMAIL)
                .whereEqualTo("email_patient",Patient_Email).whereEqualTo("etat_DPR","In Progress").limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() !=0){
                    A.setText("the request was already sent ");
                    A.setEnabled(false);

                }
            }
        });


    }

    @Override
    public Filter getFilter(){
        return Doctorfilter ;
    }

    public Filter Doctorfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           List<doctors> doctorsfiltred = new ArrayList<>() ;
                 if (constraint == null || constraint.length() == 0){
                     doctorsfiltred.addAll(fulldoctorlist);
                 }else{
                     String FilterPatern = constraint.toString().toLowerCase().trim();
                        for (doctors item : fulldoctorlist){
                             if (item.getFullname().contains(FilterPatern)){
                                 doctorsfiltred.add(item);
                             }
                        }
                 }
                 FilterResults results = new FilterResults();
                 results.values = doctorsfiltred ;
                 return  results ;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            doctorsList.clear();
            doctorsList.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

}
