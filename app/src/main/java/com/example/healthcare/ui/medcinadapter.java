package com.example.healthcare.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.healthcare.AddAppointement;
import com.example.healthcare.Model.doctors;
import com.example.healthcare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class medcinadapter extends ArrayAdapter<doctors> {

    private static final String TAG = "medcinadapter" ;
    private Context mcontext ;
    private int nressource ;


    public medcinadapter(@NonNull Context context, int resource, @NonNull List<doctors> objects) {
        super(context, resource, objects);
        mcontext = context ;
        nressource = resource ;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getFullname();
        String specialite = getItem(position).getSpecialite();
        String email = getItem(position).getEmail();

        LayoutInflater inflater =  LayoutInflater.from(mcontext);
        convertView = inflater.inflate(nressource,parent,false);
        StorageReference storageReference ;

        TextView fullname = (TextView)convertView.findViewById(R.id.nom);
        TextView spe  = (TextView)convertView.findViewById(R.id.specialite) ;
        Button moreinfo = (Button)convertView.findViewById(R.id.moreinformation);
        Button appoint = (Button)convertView.findViewById(R.id.bookappoint);
        final   CircularImageView picdoc = (CircularImageView)convertView.findViewById(R.id.picdoctor);






        storageReference = FirebaseStorage.getInstance().getReference("Images").child(FirebaseAuth.getInstance().getCurrentUser().getEmail() + ".jpg");
        final File localFile;
        try {
            localFile = File.createTempFile("images", "jpg");

            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            picdoc.setImageBitmap(myBitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


        //
        moreinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctordialog d = new doctordialog(getItem(position).getEmail().toString(),""+getItem(position).getPhone(),getItem(position).getVille().toString());
                d.show(((FragmentActivity)v.getContext()).getSupportFragmentManager(),"Information");
            }
        });

        appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(v.getContext(), AddAppointement.class);
                p.putExtra("Doc_Ref", getItem(position).getEmail())  ;
                p.putExtra("Doc_Name",getItem(position).getFullname());
                v.getContext().startActivity(p);
            }
        });

        fullname.setText(name);
        spe.setText(specialite);


        return convertView ;




    }
}
