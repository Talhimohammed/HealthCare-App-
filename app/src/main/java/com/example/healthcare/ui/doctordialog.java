package com.example.healthcare.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.healthcare.R;



public class doctordialog extends AppCompatDialogFragment {

    private Button   back ;
    private TextView email ;
    private TextView pnumber;
    private TextView ville;

    private String e ;
    private String n ;
    private String v;


    public doctordialog (String email , String number , String ville){

        this.e=email  ;
        this.n=number ;
        this.v=ville  ;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.doctorinformation,null);


        builder.setView(view);

        back = view.findViewById(R.id.baack);
        email = view.findViewById(R.id.email);
        pnumber = view.findViewById(R.id.phonenumber);
        ville = view.findViewById(R.id.ville);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        email.setText(e);
        pnumber.setText(n);
        ville.setText(v);


        return  builder.create();

    }
}
