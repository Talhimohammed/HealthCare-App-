package com.example.healthcare.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthcare.Model.medcin;
import com.example.healthcare.R;

import java.sql.ResultSet;
import java.util.List;

import io.opencensus.tags.Tag;

public class medcinadapter extends ArrayAdapter<medcin> {

    private static final String TAG = "medcinadapter" ;
    private Context mcontext ;
    private int nressource ;


    public medcinadapter(@NonNull Context context, int resource, @NonNull List<medcin> objects) {
        super(context, resource, objects);
        mcontext = context ;
        nressource = resource ;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getFullname();
        String specialite = getItem(position).getSpecialite();

        LayoutInflater inflater =  LayoutInflater.from(mcontext);
        convertView = inflater.inflate(nressource,parent,false);

        TextView fullname = (TextView)convertView.findViewById(R.id.nom);
        TextView spe  = (TextView)convertView.findViewById(R.id.specialite) ;

        fullname.setText(name);
        spe.setText(specialite);

        return convertView ;

    }
}
