package com.example.healthcare.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.healthcare.Medicalfiles;
import com.example.healthcare.Model.fiche;
import com.example.healthcare.R;
import com.example.healthcare.signup;
import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MedicalFileAdapter  extends ArrayAdapter<HashMap<String,fiche>> {

    private static final String TAG = "Filesadapter" ;
    private Context mcontext ;
    private int nressource ;
    private  List<HashMap<String,fiche>> hashMap ;


    public MedicalFileAdapter(@NonNull Context context, int resource, @NonNull List<HashMap<String,fiche>>objects) {
        super(context, resource, objects);
        this.mcontext = context;
        this.nressource = resource ;
        this.hashMap = objects ;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        String id = null;
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        for ( String key : getItem(position).keySet() ) {
            id = key ;
        }

        final String weight = getItem(position).get(id).getPoid() ;
        final String surgery = getItem(position).get(id).getOperation();
        final String Bgroup = getItem(position).get(id).getGroupe_san();
        final String disease = getItem(position).get(id).getMaladie();
        final String size = getItem(position).get(id).getTaille();






        LayoutInflater inflater =  LayoutInflater.from(mcontext);
        convertView = inflater.inflate(nressource,parent,false);

        TextView w = (TextView)convertView.findViewById(R.id.weight);
        TextView s = (TextView)convertView.findViewById(R.id.surgery);
        TextView b = (TextView)convertView.findViewById(R.id.blood);
        TextView d = (TextView)convertView.findViewById(R.id.disease);
        TextView si = (TextView)convertView.findViewById(R.id.size);
        final TextView i = (TextView)convertView.findViewById(R.id.id);

        final Button edit = (Button)convertView.findViewById(R.id.edit);
        final Button delete = (Button)convertView.findViewById(R.id.delete);

        w.setText(weight);
        s.setText(surgery);
        b.setText(Bgroup);
        d.setText(disease);
        si.setText(size);
        i.setText(id);


        final String finalId = id;
        delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               final SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE);
               dialog.setTitle("Delete");
               dialog.setTitleText("Do you really want to delete this file !");
               dialog.setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                   @Override
                   public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialog.dismiss();
                   }
               });

              dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                   @Override
                   public void onClick(SweetAlertDialog sweetAlertDialog) {
                       db.collection("fiches").document(finalId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               hashMap.remove(position);
                               notifyDataSetChanged();
                               dialog.dismiss();
                               Toast.makeText(mcontext,"File has been deleted successfully ",Toast.LENGTH_SHORT).show();
                           }
                       });

                   }
               });

               dialog.show();


           }
       });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editclass e = new editclass(weight,surgery,Bgroup,disease,size,i.getText().toString(),hashMap,v,position);

                e.show(((FragmentActivity)v.getContext()).getSupportFragmentManager(),"dialog");

            }
        });


        return  convertView ;


    }



}