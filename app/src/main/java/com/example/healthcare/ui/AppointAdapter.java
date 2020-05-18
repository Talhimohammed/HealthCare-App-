package com.example.healthcare.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthcare.Model.appointement;
import com.example.healthcare.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AppointAdapter extends ArrayAdapter<appointement> {
    private static final String TAG = "Appointemens" ;
    private  Context context ;
    private int nresource ;
    private FirebaseFirestore db ;
    private List<appointement> list ;



    public AppointAdapter(@NonNull Context context, int resource, @NonNull List<appointement> objects) {
        super(context, resource, objects);
        this.context = context    ;
        this.nresource = resource ;
        this.list = objects ;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name_doc = getItem(position).getDoctor_name();
        String email = getItem(position).getDoctor_ref();
        String date = getItem(position).getDate();
        String hour = getItem(position).getHour();

        LayoutInflater inflater =  LayoutInflater.from(context);
        convertView = inflater.inflate(nresource,parent,false);
        db = FirebaseFirestore.getInstance();

        TextView d = (TextView)convertView.findViewById(R.id.date);
        final TextView fn = (TextView)convertView.findViewById(R.id.fullname);
       // TextView e = (TextView)convertView.findViewById(R.id.email);
        TextView h = (TextView)convertView.findViewById(R.id.hour);
        Button cancel = (Button)convertView.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE);
                dialog.setTitle("Delete");
                dialog.setTitleText("Do you really want to cancel this appointement !");
                dialog.setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialog.dismiss();
                    }
                });

                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        String code = getItem(position).getApp_code()+"";
                        db.collection("appointement").document(code).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                list.remove(position);
                                notifyDataSetChanged();
                                dialog.dismiss();
                                Toast.makeText(context,"the appointement has been deleted successfully ",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                dialog.show();
            }
        });



        fn.setText("Dr."+name_doc);
        d.setText(date);
        //e.setText(email);
        h.setText(hour);

        return  convertView ;


    }
}
