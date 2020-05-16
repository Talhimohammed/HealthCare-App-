package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
      EditText password;
      EditText email ;
      TextView textView ;
      TextView regis ;
      Button signin ;
      TextView ForgetPass;
      private FirebaseFirestore db ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        textView = (TextView)findViewById(R.id.ToggleTextView);
        regis = (TextView)findViewById(R.id.register);
        signin = (Button)findViewById(R.id.signin);
        ForgetPass = (TextView)findViewById(R.id.btnForgetPass);
        db = FirebaseFirestore.getInstance();


        textView.setVisibility(View.GONE);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        final FirebaseAuth firebaseAuth ;


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(password.getText().length()>0) {
                            textView.setVisibility(View.VISIBLE);
                        }else {
                            textView.setVisibility(View.GONE);
                        }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView.getText() == "SHOW"){
                     textView.setText("HIDE");
                     password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                     password.setSelection(password.length());
                }else {
                    textView.setText("SHOW");
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.length());

                }
            }
        });


        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSignUp();
            }
        });

        //Sign in
         firebaseAuth = FirebaseAuth.getInstance();
         signin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(final View v) {
                 firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()) {
                             if (firebaseAuth.getCurrentUser().isEmailVerified()) {

                                   tohomebytype(email.getText().toString());

                             }else {

                                 SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                                 dialog.setTitle("Email Address Verificication");
                                 dialog.setTitleText("your email address have not been verified , please check your email for verification so that you can sign in . thank you");
                                 dialog.show();


                             }


                         }else{
                             SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
                             dialog.setTitle(task.getException().getMessage());
                             dialog.show();

                         }

                     }
                 });

             }
         });
        ForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),Forgetpasswd.class));


            }
        });





    }

     public void toSignUp(){
        Intent intent = new Intent(this,signup.class);
        startActivity(intent);
    }

    public void tohomebytype(String em){



        db.collection("doctors").whereEqualTo("email",em).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>0) {
                    startActivity(new Intent(getBaseContext(),homedoctor.class));

                }


            }
        });

        db.collection("patient").whereEqualTo("email",em).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>0) {
                    startActivity(new Intent(getBaseContext(),home.class));

                }


            }
        });


    }



}
