package com.example.healthcare.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.healthcare.Model.fiche;
import com.example.healthcare.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class add_record extends AppCompatDialogFragment {

    private EditText weight  ;
    private EditText surgery ;
    private Spinner spinner ;
    private EditText disease ;
    private EditText size ;
    private FirebaseFirestore firestore ;
    private editclass.DialogListener listener ;
    private List<HashMap<String, fiche>> itemlist ;
    private Context context ;

    String patient_email ;

    public add_record(String patient_email , Context context , List<HashMap<String, fiche>> itemlist){
        this.patient_email = patient_email ;
        this.itemlist = itemlist ;
        this.context = context ;
     }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_record,null);

        weight = view.findViewById(R.id.weight);
        surgery = view.findViewById(R.id.surgery);
        spinner = view.findViewById(R.id.blood);
        disease = view.findViewById(R.id.disease);
        size = view.findViewById(R.id.size);

        firestore = FirebaseFirestore.getInstance();

        ArrayList<String> area = new ArrayList<>();
        area.add("--");
        area.add("A+");
        area.add("A-");
        area.add("O+");
        area.add("O-");
        area.add("AB+");
        area.add("AB-");

        spinner.setAdapter(new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_list_item_1,area));


        builder.setView(view).setTitle("New Record").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String sweight = weight.getText().toString() ;
                String ssurgey = surgery.getText().toString() ;
                String sspiner =  spinner.getSelectedItem().toString() ;
                String sdisease = disease.getText().toString() ;
                String ssize = size.getText().toString() ;
                String posed_by = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                final fiche fi = new fiche(sweight,ssurgey,sspiner,sdisease,ssize,posed_by,patient_email);

                firestore.collection("fiches").add(fi).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        HashMap<String,fiche> ff = new HashMap<>();
                        ff.put(documentReference.getId(),fi);
                        itemlist.add(ff);
                        listener.Refresh();
                        Toast.makeText(context,"New record is added to the medical folder",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        return  builder.create() ;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (editclass.DialogListener) context ;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"false");
        }
    }

    public interface DialogListener{
        public void Refresh();
    }
}
