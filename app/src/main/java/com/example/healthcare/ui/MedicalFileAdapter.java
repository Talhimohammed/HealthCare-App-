package com.example.healthcare.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthcare.Model.fiche;
import com.example.healthcare.R;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MedicalFileAdapter  extends ArrayAdapter<HashMap<String,fiche>>{

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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String id = null;

        for ( String key : getItem(position).keySet() ) {
            id = key ;
        }

        String weight = getItem(position).get(id).getPoid() ;
        String surgery = getItem(position).get(id).getOperation();
        String Bgroup = getItem(position).get(id).getGroupe_san();
        String disease = getItem(position).get(id).getMaladie();
        String size = getItem(position).get(id).getTaille();




     /*  String weight = getItem(position).getPoid();
        String surgery = getItem(position).getOperation();
        String Bgroup = getItem(position).getGroupe_san();
        String disease = getItem(position).getMaladie();
        String size = getItem(position).getTaille(); */

        LayoutInflater inflater =  LayoutInflater.from(mcontext);
        convertView = inflater.inflate(nressource,parent,false);

        TextView w = (TextView)convertView.findViewById(R.id.weight);
        TextView s = (TextView)convertView.findViewById(R.id.surgery);
        TextView b = (TextView)convertView.findViewById(R.id.blood);
        TextView d = (TextView)convertView.findViewById(R.id.disease);
        TextView si = (TextView)convertView.findViewById(R.id.size);
        TextView i = (TextView)convertView.findViewById(R.id.id);

        w.setText(weight);
        s.setText(surgery);
        b.setText(Bgroup);
        d.setText(disease);
        si.setText(size);
        i.setText(id);


        return  convertView ;


    }
}
