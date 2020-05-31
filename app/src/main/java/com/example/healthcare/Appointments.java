package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.healthcare.Model.appointement;
import com.example.healthcare.ui.Appointements_adapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Appointments extends AppCompatActivity {
    private EditText date ;
    private  DatePickerDialog.OnDateSetListener mdatelistener ;
    private ListView appointements ;
    private Button back ;
    private FirebaseFirestore db ;
    private List<appointement> app ;
    private Button search ;
    private ImageView refresh ;
    private ImageView EMPTY ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        db = FirebaseFirestore.getInstance();
        date = (EditText)findViewById(R.id.date_sent_request);
        back = (Button)findViewById(R.id.back);
        appointements = (ListView)findViewById(R.id.appointments_listview);
        search = (Button)findViewById(R.id.search2);
        refresh = (ImageView)findViewById(R.id.refresh);
        EMPTY = (ImageView)findViewById(R.id.emptylist);
        app = new ArrayList<>();


        setAllappointments(this);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllappointments(v.getContext());
                appointements.invalidateViews();
                date.setText(null);
                Toast.makeText(v.getContext(),"Refreshing ....",Toast.LENGTH_SHORT).show();
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d= date.getText().toString();
                searchbydate(d);

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


                    DatePickerDialog dialog = new DatePickerDialog(Appointments.this, mdatelistener, year, month, day);

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void setAllappointments(final Context context){

        app.clear();
        String email_doc = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("appointement").whereEqualTo("doctor_ref",email_doc).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                appointement a = d.toObject(appointement.class);
                                app.add(a);
                            }

                            Appointements_adapter adapter = new Appointements_adapter(context, R.layout.appointments_adapter, app);
                            appointements.setAdapter(adapter);
                        }else {
                                  appointements.setVisibility(View.GONE);
                                  EMPTY.setVisibility(View.VISIBLE);

                        }
                    }
                });
     }

    public void searchbydate(String date){
        app.clear();
        db.collection("appointement").whereEqualTo("date",date).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> p = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : p) {

                    appointement a = d.toObject(appointement.class);
                    app.add(a);

                }

                appointements.invalidateViews();


            }
        });
    }




}
