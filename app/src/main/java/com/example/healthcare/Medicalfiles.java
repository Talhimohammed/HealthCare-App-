package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.healthcare.Model.fiche;
import com.example.healthcare.Model.medcin;
import com.example.healthcare.ui.MedicalFileAdapter;
import com.example.healthcare.ui.editclass;
import com.example.healthcare.ui.medcinadapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Medicalfiles extends AppCompatActivity implements editclass.DialogListener {

    private ListView view;
    private Button back;
    private ImageView addfile;
    private FirebaseFirestore firestore;
    private List<fiche> listfiche;
    private List<HashMap<String, fiche>> map;
    private ImageView refresh;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicalfiles);
        back = (Button) findViewById(R.id.back);
        addfile = (ImageView) findViewById(R.id.addfile);
        view = (ListView) findViewById(R.id.fileslist);
        firestore = FirebaseFirestore.getInstance();
        refresh = (ImageView) findViewById(R.id.imageView17);


        // listfiche = new ArrayList<> ();

        setListfiche();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Add new fiche
        addfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent p = new Intent(getBaseContext(), FichePatient.class);
                p.putExtra("test", 1);
                startActivity(p);
            }
        });



        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListfiche();
            }
        });


    }

    public void setListfiche() {
        map = new ArrayList<>();
        map.clear();


        firestore.collection("fiches").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : p) {

                        fiche f = d.toObject(fiche.class);
                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

                        if ((f.getPostedby()).equals(email)) {

                            HashMap<String, fiche> m = new HashMap<String, fiche>();
                            m.put(d.getId(), f);
                            map.add(m);

                        }
                    }

                    MedicalFileAdapter adapter = new MedicalFileAdapter(getBaseContext(), R.layout.medicalfileslist, map);
                    view.setAdapter(adapter);


                }

            }
        });

   /*     final Timer t = new Timer();
        TimerTask p = new TimerTask(){
            @Override
            public void run() {

                try {
                    synchronized (this) {
                        wait(5000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }};
        t.schedule(p,0,1000);
      */

    }

    @Override
    public void Refresh() {
        view.invalidateViews();
    }




}
