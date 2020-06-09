package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.healthcare.Model.appointement;
import com.example.healthcare.ui.AppointAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class myappointements extends AppCompatActivity {

    private ListView appointements ;
    private List<appointement> Alist ;
    private FirebaseFirestore db ;
    private Button search ;
    private EditText date ;
    private  DatePickerDialog.OnDateSetListener mdatelistener ;
    private ImageView refr ;
    private ImageView finish ;
    AppointAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myappointements);
        appointements = (ListView)findViewById(R.id.myapp);
        search = (Button)findViewById(R.id.search);
        date = (EditText)findViewById(R.id.date_sent_request);
        refr = (ImageView)findViewById(R.id.refresh);
        finish = (ImageView)findViewById(R.id.finish);
        db = FirebaseFirestore.getInstance();
        Alist = new ArrayList<>();


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setmyappointements();
                appointements.invalidateViews();
                date.setText(null);
            }
        });




        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(MotionEvent.ACTION_UP == event.getAction()) {


                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog dialog = new DatePickerDialog(myappointements.this, mdatelistener, year, month, day);

                    dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                    dialog.show();
                }

                return true;
            }
        });

        mdatelistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int mn = month + 1 ;
                date.setText(dayOfMonth+"/"+mn+"/"+year);


            }
        };

        setmyappointements();


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d= date.getText().toString();
                searchbydate(d);

            }
        });




     }

     public void setmyappointements(){
        Alist.clear();
         String email_user = FirebaseAuth.getInstance().getCurrentUser().getEmail();

         db.collection("appointement").whereEqualTo("postedby",email_user).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                 List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                 for (DocumentSnapshot d : p) {

                     appointement a = d.toObject(appointement.class);
                     Alist.add(a);



                 }
                  adapter = new  AppointAdapter(getBaseContext(),R.layout.appoint_adapter,Alist);
                 appointements.setAdapter(adapter);




             }
         });


     }



     public void searchbydate(String date){
        Alist.clear();
        db.collection("appointement").whereEqualTo("date",date).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : p) {

                    appointement a = d.toObject(appointement.class);
                    Alist.add(a);

                }

                appointements.invalidateViews();


            }
        });
     }







}
