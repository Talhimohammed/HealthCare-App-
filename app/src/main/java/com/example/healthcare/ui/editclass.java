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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.healthcare.Model.fiche;
import com.example.healthcare.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

public class editclass extends AppCompatDialogFragment {
    @NonNull


    private EditText weight  ;
    private EditText surgery ;
    private Spinner  spinner ;
    private EditText disease ;
    private EditText size ;
    private String id ;
    private String s1 ;
    private String s2 ;
    private String s3 ;
    private String s4 ;
    private String s5 ;
    private  int pos ;
    private List<HashMap<String, fiche>> itemlist ;
    private FirebaseFirestore firestore ;
    View v ;
    private DialogListener listener ;
    private  int position ;



    public editclass(String s1,String s2 , String s3 , String s4 , String s5 , String id ,List<HashMap<String, fiche>> itemlist , View v , int position){
        this.s1 = s1 ;
        this.s2 = s2 ;
        this.s3 = s3 ;
        this.s4 = s4 ;
        this.s5 = s5 ;
        this.id = id ;
        this.itemlist = itemlist ;
        this.v = v  ;
        this.position = position ;
    }


    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.editlayout,null);



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

        // I created these conditions to pick up the position of blood type in the spinner
        if (s3.equals("A+")) {
             pos = 1 ;
        }else if( s3.equals("A-")) {
             pos = 2 ;
        }else if (s3.equals("O+")) {
             pos = 3 ;
        }else if (s3.equals("O-")) {
             pos = 4 ;
        }else  if (s3.equals("AB+")) {
             pos = 5 ;
        }else  if (s3.equals("AB-")) {
             pos = 6 ;
        }




        spinner.setAdapter(new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_list_item_1,area));
        spinner.setSelection(position,true);


       weight.setText(s1);
       surgery.setText(s2);
       disease.setText(s4);
       size.setText(s5);


        builder.setView(view).setTitle("Edit Medical file").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                String w = weight.getText().toString();
                String s = surgery.getText().toString() ;
                String d = disease.getText().toString() ;
                String si = size.getText().toString();
                String sp = spinner.getSelectedItem().toString() ;
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

               final fiche a = new fiche(w,s,sp,d,si,email);
                firestore.collection("fiches").document(id).set(a).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(v.getContext(),"this medical file has been updated successfully",Toast.LENGTH_SHORT).show();
                        HashMap<String,fiche> hash = new HashMap<String,fiche>();
                        hash.put(id,a);

                        //updating the list :
                        itemlist.set(position,hash);

                        listener.Refresh();
                        dialog.dismiss();

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
            listener = (DialogListener) context ;
        } catch (ClassCastException e) {
          throw new ClassCastException(context.toString()+"false");
        }
    }

    public interface DialogListener{
       public void Refresh();
    }
}
