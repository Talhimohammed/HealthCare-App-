package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.healthcare.Model.fiche;
import com.example.healthcare.ui.MedicalFileAdapter;
import com.example.healthcare.ui.add_record;
import com.example.healthcare.ui.editclass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Medical_Folder_Of_My_Patient extends AppCompatActivity implements editclass.DialogListener ,add_record.DialogListener{

    private ListView  records ;
     private List<HashMap<String,fiche>> map;
     private FirebaseFirestore db ;
     private ImageView Addfile ;
     private ImageView Refresh ;
     private Button Back ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical__folder__of__my__patient);
        records = (ListView)findViewById(R.id.Records);
        db = FirebaseFirestore.getInstance();
        map = new ArrayList<>();

        Addfile = (ImageView)findViewById(R.id.imageView34);
        Refresh = (ImageView)findViewById(R.id.imageView33);
        Back = (Button)findViewById(R.id.button_back2);

       final String patient_email = getIntent().getStringExtra("patient_email") ;

        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecords(v.getContext(),patient_email);
                Toast.makeText(v.getContext(),"Refreshed",Toast.LENGTH_SHORT).show();
            }
        });


        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Addfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 add_record a =  new add_record(patient_email,v.getContext(),map) ;
                 a.show(((FragmentActivity)v.getContext()).getSupportFragmentManager(),"CONTACT");
            }
        });

        setRecords(this,patient_email);
    }

    public void setRecords(final Context context, final String patient_email){
        map.clear();
        db.collection("fiches").whereEqualTo("patient_email",patient_email).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot>  p = queryDocumentSnapshots.getDocuments();

                        for(DocumentSnapshot d : p){
                            fiche f = d.toObject(fiche.class);
                            HashMap<String,fiche> hash = new HashMap<>() ;
                            hash.put(d.getId(),f);
                            map.add(hash);
                        }
                            MedicalFileAdapter adapter = new MedicalFileAdapter(context,R.layout.medicalfileslist,map,patient_email);
                            records.setAdapter(adapter);
                    }
                });
    }
    @Override
    public void Refresh() {
        records.invalidateViews();
    }
}
