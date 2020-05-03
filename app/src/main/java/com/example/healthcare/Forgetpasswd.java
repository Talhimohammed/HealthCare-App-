package com.example.healthcare;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Forgetpasswd extends AppCompatActivity {

    EditText Useremail ;
    Button Envoi ;
    EditText Email;
    String mail;


    FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpasswd);


        Useremail = (EditText) findViewById(R.id.UserEmail);
        Envoi = findViewById(R.id.BtnForgetPass);



        firebaseAuth  = FirebaseAuth.getInstance();

        Envoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mail = Useremail.getText().toString();
                if(TextUtils.isEmpty(mail)){
                    SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitle("Password reset");
                    dialog.setTitleText("please enter your email");
                    dialog.show();

                }else{
                    firebaseAuth.sendPasswordResetEmail(Useremail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                SweetAlertDialog dialog = new SweetAlertDialog(Forgetpasswd.this, SweetAlertDialog.SUCCESS_TYPE);
                                dialog.setTitle("Password reset");
                                dialog.setTitleText("Password Sent to your email");
                                dialog.show();

                            } else {
                                SweetAlertDialog dialog = new SweetAlertDialog(Forgetpasswd.this, SweetAlertDialog.ERROR_TYPE);
                                dialog.setTitle(task.getException().getMessage());
                                dialog.show();

                            }


                        }
                    });
                }
            }
        });


    }
}
