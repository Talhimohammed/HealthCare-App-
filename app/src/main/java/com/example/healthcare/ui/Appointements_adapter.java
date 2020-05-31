package com.example.healthcare.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthcare.AddAppointement;
import com.example.healthcare.MailAPI.MailAPI;
import com.example.healthcare.Model.appointement;
import com.example.healthcare.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Appointements_adapter extends ArrayAdapter<appointement> {

    private Context ncontext ;
    private int nressource ;
    private List<appointement >object;
    private FirebaseFirestore db ;

    public Appointements_adapter(@NonNull Context context, int resource, @NonNull List<appointement> objects) {
        super(context, resource, objects);
        this.ncontext = context ;
        this.nressource = resource ;
        this.object = objects ;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final String Hour = getItem(position).getHour();
        final String date = getItem(position).getDate();
        final String doctor_name = getItem(position).getDoctor_name();
        final String patient_email = getItem(position).getPostedby() ;

        LayoutInflater inflater =  LayoutInflater.from(ncontext);
        convertView = inflater.inflate(nressource,parent,false);

        TextView d = (TextView)convertView.findViewById(R.id.date);
        TextView hour = (TextView)convertView.findViewById(R.id.hour);
        final Button confirm = (Button)convertView.findViewById(R.id.confirm);
        final Button cancel = (Button)convertView.findViewById(R.id.cancel);
        final ImageView c = (ImageView)convertView.findViewById(R.id.confirmed);




        hour.setText(Hour);
        d.setText(date);




        db = FirebaseFirestore.getInstance() ;
        final int id = getItem(position).getApp_code();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitle("");
                dialog.setTitleText("Do you really want to confirm this appointment");
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        db.collection("appointement").document(id+"").update("etat","Confirmed")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(ncontext,"Appointment is confirmed",Toast.LENGTH_SHORT).show();
                                        send_cofirmation_email(patient_email,doctor_name,date,Hour);
                                        c.setVisibility(View.VISIBLE);
                                        confirm.setVisibility(View.GONE);
                                        ViewGroup.LayoutParams params = cancel.getLayoutParams();
                                        params.width = 1000;
                                        cancel.setLayoutParams(params);
                                        dialog.dismiss();
                                    }
                                });
                    }
                });

                dialog.setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE);
                dialog.setTitle("");
                dialog.setTitleText("Do you really want to cancel this appointment !");

                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        db.collection("appointement").document(id+"").delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        object.remove(position);
                                        notifyDataSetChanged();
                                        send_cancelation_email(patient_email,doctor_name);
                                        dialog.dismiss();

                                        if(object.size() == 0) {
                                            //Refreshing the activity without animation
                                            Intent intent = ((Activity)ncontext).getIntent();
                                            ((Activity)ncontext).overridePendingTransition(0, 0);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            ((Activity)ncontext).finish();
                                            ((Activity)ncontext).overridePendingTransition(0, 0);
                                            ((Activity)ncontext).startActivity(intent);

                                        }

                                        Toast.makeText(ncontext,"Appointment has been deleted successfully ",Toast.LENGTH_SHORT).show();


                                    }
                                });

                    }
                });

                dialog.setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                       dialog.dismiss();
                    }
                });


                 dialog.show();


            }
        });

        return convertView ;
    }

    public void send_cofirmation_email(String patient_email, String doctorName , String date , String time){
        String subject = "Healthcare APP : APPOINTMENT CONFIRMATION";
        String message = "Doctor "+doctorName+" just confirmed your Appointment \n Appointment details : \n Date : "+date+"\n Time :"+time+"\n"+"FOR MORE INFORMATION CHECK YOUR APP";
        MailAPI mailAPI = new MailAPI(patient_email,subject,message);
        mailAPI.execute();
    }

    public void  send_cancelation_email(String Email,String DoctorName){

        String subject = " HealthCare app : APPOINTEMENT CANCELLATION" ;
        String message = "Your appointment with Dr."+DoctorName+" has just been cancelled" ;
        MailAPI mailAPI = new MailAPI(Email,subject,message);
        mailAPI.execute();

    }
}
