package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.Model.medcin;
import com.example.healthcare.Model.patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference ;
import com.google.firebase.database.FirebaseDatabase ;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class signup extends AppCompatActivity {

    private  EditText fullName ;
    private  EditText bday ;
    private  String   bdayDate ;
    private  EditText phonenumber ;
    private  EditText password ;
    private  EditText email ;
    private  DatePickerDialog.OnDateSetListener mdatelistener ;
    private Button btn ;
    private LinearLayout layoutinfo ;
    private Button patient ;
    private Button doctor ;
    private TextView aryou ;
    private String type_utilisateur ;
    private Spinner specialite ;
    private EditText ville ;
    private ProgressBar pbar ;

    FirebaseDatabase database ;
    DatabaseReference users ;

      DatabaseReference databaseReference ;
      FirebaseAuth firebaseAuth ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fullName = (EditText)findViewById(R.id.nameinput);
        bday = (EditText)findViewById(R.id.birthdayinput);
        phonenumber = (EditText)findViewById(R.id.phoneinput);
        password = (EditText)findViewById(R.id.passwordinput);
        email = (EditText)findViewById(R.id.emailinput);
        layoutinfo = (LinearLayout)findViewById(R.id.infolayout);
        btn = (Button)findViewById(R.id.send);
        patient = (Button)findViewById(R.id.patient);
        doctor = (Button)findViewById(R.id.doctor);
        aryou = (TextView)findViewById(R.id.areyou);
        specialite = (Spinner)findViewById(R.id.spinner);
        ville = (EditText)findViewById(R.id.ville);
        pbar = (ProgressBar)findViewById(R.id.pbar);









         /*  database = FirebaseDatabase.getInstance();

           users = database.getReference("Users"); */

       //   databaseReference = FirebaseDatabase.getInstance().getReference("users");
          firebaseAuth = FirebaseAuth.getInstance();
         FirebaseFirestore db = FirebaseFirestore.getInstance();










        bday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(signup.this, android.R.style.Theme_Holo_Dialog_MinWidth, mdatelistener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mdatelistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    bday.setText(month+"/"+dayOfMonth+"/"+year);
            }
        };



      btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pbar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Email Vérification :
                                    firebaseAuth.getCurrentUser().sendEmailVerification();


                                    if (type_utilisateur.equals("doctor")) {

                                        medcin doctor = new medcin(email.getText().toString(), fullName.getText().toString(), Integer.parseInt(phonenumber.getText().toString()), password.getText().toString(), bday.getText().toString(),ville.getText().toString(),specialite.getSelectedItem().toString());
                                        FirebaseFirestore.getInstance().collection("doctors").add(doctor).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                SweetAlertDialog dialog = new SweetAlertDialog(signup.this, SweetAlertDialog.SUCCESS_TYPE);
                                                SweetAlertDialog dialog1 = new SweetAlertDialog(signup.this, SweetAlertDialog.PROGRESS_TYPE);
                                                dialog1.show();
                                                dialog.setTitle("Welcome " + fullName.getText());
                                                dialog.setTitleText("You have signed up successfully , please check your email for verification");
                                                dialog.show();

                                                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        finish();
                                                    }
                                                });




                                            }
                                        });


                                    }else {
                                        patient person = new patient(email.getText().toString(), fullName.getText().toString(), type_utilisateur, Integer.parseInt(phonenumber.getText().toString()), password.getText().toString(), bday.getText().toString());
                                     /*   FirebaseDatabase.getInstance().getReference("patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(person).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                SweetAlertDialog dialog = new SweetAlertDialog(signup.this, SweetAlertDialog.SUCCESS_TYPE);
                                                dialog.setTitle("Welcome " + fullName.getText());
                                                dialog.setTitleText("You have signed up successfully , please check your email for verification");

                                                dialog.show();

                                                                      REAL TIME DATABASE
                                            }

                                        }); */

                                    FirebaseFirestore.getInstance().collection("patient").add(person).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                         @Override
                                         public void onSuccess(DocumentReference documentReference) {
                                             SweetAlertDialog dialog = new SweetAlertDialog(signup.this, SweetAlertDialog.SUCCESS_TYPE);
                                             dialog.setTitle("Welcome " + fullName.getText());
                                             dialog.setTitleText("You have signed up successfully , please check your email for verification");
                                             dialog.show();
                                             dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                 @Override
                                                 public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                     finish();
                                                 }
                                             });

                                             






                                         }
                                     }).addOnFailureListener(new OnFailureListener() {
                                         @Override
                                         public void onFailure(@NonNull Exception e) {

                                             SweetAlertDialog dialog = new SweetAlertDialog(signup.this, SweetAlertDialog.ERROR_TYPE);

                                             dialog.setTitleText(" NOT DONE");

                                             dialog.show();




                                         }
                                     });








                                    }

                                    pbar.setVisibility(View.GONE);
                                }else{

                                    SweetAlertDialog dialog = new SweetAlertDialog(signup.this, SweetAlertDialog.ERROR_TYPE);
                                    dialog.setTitle(task.getException().getMessage());
                                    dialog.show();




                            }


                            }
                        });




            }});


      patient.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
                    type_utilisateur ="patient" ;
                    layoutinfo.setVisibility(View.VISIBLE);
                    ville.setVisibility(View.GONE);
                    specialite.setVisibility(View.GONE);
                    btn.setVisibility(View.VISIBLE);
                    doctor.setVisibility(View.GONE);
                    patient.setVisibility(View.GONE);
                    aryou.setVisibility(View.GONE);

          }
      });

      doctor.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              type_utilisateur = "doctor" ;
              layoutinfo.setVisibility(View.VISIBLE);
              btn.setVisibility(View.VISIBLE);
              doctor.setVisibility(View.GONE);
              patient.setVisibility(View.GONE);
              aryou.setVisibility(View.GONE);

          }
      });




    }
}

