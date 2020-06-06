package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthcare.MailAPI.MailAPI;
import com.example.healthcare.Model.doctors;
import com.example.healthcare.Model.patient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class contact_email extends AppCompatActivity {
    private Button send  ;
    private Button back  ;
    private EditText subject  ;
    private EditText message  ;
    private String patient_email ;
    private String doctor_email  ;
    private FirebaseFirestore db ;
    private SweetAlertDialog d ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_email);

        send = (Button)findViewById(R.id.send);
        back = (Button)findViewById(R.id.cancel);
        subject = (EditText)findViewById(R.id.subject);
        message = (EditText)findViewById(R.id.message);
        db = FirebaseFirestore.getInstance();

        patient_email = getIntent().getStringExtra("patient_email");
        doctor_email  =  getIntent().getStringExtra("doctor_email");




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d = new SweetAlertDialog(contact_email.this, SweetAlertDialog.PROGRESS_TYPE);
                d.setTitleText("Sending");
                d.show();

               final String sub  =  subject.getText().toString();
               final String mess = message.getText().toString();

                db.collection("doctors").whereEqualTo("email",doctor_email).limit(1).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                                doctors doc  = null;
                                String mes = null ;
                                for (DocumentSnapshot d : p) {
                                    doc = d.toObject(doctors.class);
                                    mes  = " Greetings , you have new email form your doctor : Drr."+doc.getFullname()+"<"+doc.getEmail()+"> \n "+ mess;
                                }
                                MailAPI mailAPI = new MailAPI(patient_email,sub,mes) ;
                                mailAPI.execute();
                                d.dismiss();
                                Toast.makeText(getBaseContext(),"message has been sent",Toast.LENGTH_SHORT).show();
                            }


                        });


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
