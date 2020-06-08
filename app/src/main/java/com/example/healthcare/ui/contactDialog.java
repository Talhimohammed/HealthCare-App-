package com.example.healthcare.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.healthcare.Model.doctors;
import com.example.healthcare.Model.patient;
import com.example.healthcare.R;
import com.example.healthcare.contact_email;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class contactDialog extends AppCompatDialogFragment {

    private editclass.DialogListener listener ;
    private String patient_email ;
    private String doctor_email ;
    private Context context ;
    private FirebaseFirestore db ;

    private static final int REQUEST_CALL = 1 ;

    public contactDialog(String patient_email,String doctor_email,Context context){
        this.doctor_email = doctor_email   ;
        this.patient_email = patient_email ;
        this.context = context ;

    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.contactdialog, null);

        ImageView g = (ImageView)view.findViewById(R.id.gmail);
        ImageView v = (ImageView)view.findViewById(R.id.phone);

        db = FirebaseFirestore.getInstance() ;


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               makePhoneCall();
            }
        });

        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(v.getContext(), contact_email.class);
               i.putExtra("patient_email",patient_email);
               i.putExtra("doctor_email" ,doctor_email);
               startActivity(i);
            }
        });


        builder.setView(view).setTitle("CONTACT").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            } });

         return builder.create();

    }

    public void makePhoneCall(){

                db.collection("patient").whereEqualTo("email",patient_email).limit(1).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                   List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : p) {
                                    patient a = d.toObject(patient.class);
                                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:0"+a.getPhone())));
                                }
                            }
                        });

        if (ContextCompat.checkSelfPermission(context,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( (Activity) context,new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                makePhoneCall();

            } else {
                Toast.makeText(context,"Access denied",Toast.LENGTH_SHORT).show();
             }
        }
    }
}
