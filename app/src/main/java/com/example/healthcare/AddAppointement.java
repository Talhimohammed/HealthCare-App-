package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcare.Model.appointement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddAppointement extends AppCompatActivity {

     private Button   datep ;
     private TextView date  ;
     private Spinner hoursspinner  ;
     private Button check ;
     private Button confirm ;
     private FirebaseFirestore db ;
     private CollectionReference appoint ;
     private Button back ;

    private  DatePickerDialog.OnDateSetListener mdatelistener ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointement);
        datep = (Button)findViewById(R.id.datepicker);
        date = (TextView)findViewById(R.id.datetext);
        hoursspinner = (Spinner)findViewById(R.id.hours);
        check = (Button)findViewById(R.id.check);
        confirm = (Button)findViewById(R.id.confirm);
        db =FirebaseFirestore.getInstance();
        appoint = db.collection("Appointements_adapter");
        back = (Button)findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

  // adding hours :
        ArrayList<String> area = new ArrayList<>();
        area.add("--")  ;
        area.add("9:00");
        area.add("9:20");
        area.add("9:40");
        area.add("10:00");
        area.add("10:20");
        area.add("10:40");
        area.add("11:00");
        area.add("11:20");
        area.add("10:40");
        area.add("12:00");
        area.add("12:20");
        area.add("12:40");
        area.add("14:40");
        area.add("15:00");
        area.add("15:20");
        area.add("15:40");
        area.add("16:00");
        area.add("16:20");
        area.add("16:40");
        area.add("17:00");


        hoursspinner.setAdapter(new ArrayAdapter<String>(this
                , android.R.layout.simple_list_item_1,area));

   //
        datep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog dialog = new DatePickerDialog(AddAppointement.this, mdatelistener, year,month,day);

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                dialog.show();

            }
        });

        mdatelistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int newmonth = month + 1 ;
                date.setText(dayOfMonth+"/"+newmonth+"/"+year);
                confirm.setVisibility(View.GONE);

            }
        };

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String email = getIntent().getStringExtra("Doc_Ref");
                String d = date.getText().toString();
                String h = hoursspinner.getSelectedItem().toString();

              //  Query query = appoint.whereEqualTo("doctor_ref",email).whereEqualTo("date",d).whereEqualTo("hour",h);

              db.collection("appointement").whereEqualTo("doctor_ref",email).whereEqualTo("date",d).whereEqualTo("hour",h).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      boolean isEmpty = task.getResult().isEmpty();

                      if(isEmpty) {

                          SweetAlertDialog dialog = new SweetAlertDialog(AddAppointement.this, SweetAlertDialog.SUCCESS_TYPE);
                          dialog.setTitle("Success");
                          dialog.setTitleText("the doctor is available for this appointement , click confirm to register");
                          confirm.setVisibility(View.VISIBLE);
                          dialog.show();

                      }else {

                          SweetAlertDialog dialog = new SweetAlertDialog(AddAppointement.this, SweetAlertDialog.WARNING_TYPE);
                          dialog.setTitle("Warning");
                          dialog.setTitleText("the doctor isn't available for this appointement , change the date ");
                          dialog.show();

                      }

                  }
              });






            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                int g = generatecode() ;
                appointement po = new appointement(g,getIntent().getStringExtra("Doc_Ref"),hoursspinner.getSelectedItem().toString(),date.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(),getIntent().getStringExtra("Doc_Name"),"Not Confirmed");
                db.collection("appointement").document(g+"").set(po).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        SweetAlertDialog dialog = new SweetAlertDialog(AddAppointement.this, SweetAlertDialog.SUCCESS_TYPE);
                        dialog.setTitle("Success ! ");
                        dialog.setTitleText("doctor appointment booking has been registered successfully , Please wait your doctor to confirm your appointement ");dialog.show();
                    }
                });
            }
        });

        hoursspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                confirm.setVisibility(View.GONE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public int generatecode(){
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        return n ;
    }
}
