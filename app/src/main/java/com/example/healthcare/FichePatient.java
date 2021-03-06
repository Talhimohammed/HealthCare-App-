package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.healthcare.Model.fiche;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

public class FichePatient extends AppCompatActivity {

    private Spinner spinner ;
    private Button back ;
    private Button save ;
    private EditText weight ;
    private EditText surgery ;
    private EditText disease ;
    private EditText size ;
    private ProgressBar bar ;
    private FirebaseFirestore db  ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_patient);
        spinner = (Spinner)findViewById(R.id.blood);
        back = (Button)findViewById(R.id.back);
        save = (Button)findViewById(R.id.save);

        weight = (EditText)findViewById(R.id.weight);
        surgery = (EditText)findViewById(R.id.surgery);
        disease = (EditText)findViewById(R.id.disease);
        size = (EditText)findViewById(R.id.size);
        bar = (ProgressBar)findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance() ;

        //adding the values in the spinner  :
        ArrayList<String> area = new ArrayList<>();
        area.add("--");
        area.add("A+");
        area.add("A-");
        area.add("O+");
        area.add("O-");
        area.add("AB+");
        area.add("AB-");
        spinner.setAdapter(new ArrayAdapter<String>(this
                , android.R.layout.simple_list_item_1,area));
        //

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String w = weight.getText().toString();
                String s = surgery.getText().toString() ;
                String d = disease.getText().toString() ;
                String si = size.getText().toString();
                String sp = spinner.getSelectedItem().toString() ;
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                bar.setVisibility(View.VISIBLE);
                final fiche f = new fiche(w,s,sp,d,si,email,email);
                db.collection("fiches").document().set(f).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        bar.setVisibility(View.GONE);

                         int test = getIntent().getIntExtra("test",0);
                         if (test == 1) {

                             finish();

                         }else {

                             startActivity(new Intent(bar.getContext(),Medicalfiles.class));

                               }

                    }
                });


            }
        });





    }
    public String generatecode(){
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        return n+"" ;
    }





}
