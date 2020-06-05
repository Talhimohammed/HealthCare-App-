package com.example.healthcare.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.healthcare.R;
import com.example.healthcare.contact_email;

public class contactDialog extends AppCompatDialogFragment {

    private editclass.DialogListener listener ;
    private String patient_email ;
    private String doctor_email ;

    public contactDialog(String patient_email,String doctor_email){

        this.doctor_email = doctor_email   ;
        this.patient_email = patient_email ;

    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.contactdialog, null);

        ImageView g = (ImageView)view.findViewById(R.id.gmail);

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


}
