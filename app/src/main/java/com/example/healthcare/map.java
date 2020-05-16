package com.example.healthcare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.healthcare.DirectionHelpers.FetchURL;
import com.example.healthcare.DirectionHelpers.TaskLoadedCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class map extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap mMap ;
    private MarkerOptions place1, place2 ;
    Button getDirection ;
    private Polyline currentPolyline ;
    private LatLng d1 ;
    private LatLng d2 ;

    private FusedLocationProviderClient fusedLocationProviderClient ;

    private Location CurrentLocation ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getDirection = findViewById(R.id.btnGetDirection);

        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(map.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(map.this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                         FetchMyLastLocation();

        } else {
            ActivityCompat.requestPermissions(map.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String adr = getIntent().getStringExtra("Adress");
        List<Address> addresslist = null ;
        Geocoder a = new Geocoder(map.this);
        try {
            addresslist = a.getFromLocationName(adr,1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Address adresse = addresslist.get(0);
        d1 = new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude()) ;
        d2 = new LatLng(adresse.getLatitude(), adresse.getLongitude()) ;


        place1 = new MarkerOptions().position(d1).title("My Current location");
        place2 = new MarkerOptions().position(d2).title(adr);


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(d1,15));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(d2,15));
        mMap.addMarker(place1);
        mMap.addMarker(place2);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service

        //Enable the billing in the google cloud project so that you can use directions api
        // this following key in the url is disabled change the for your enbaled key .
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyAmvIm6ob3atr22G2-jKtwWOSDSVO99G_g";

        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    private void FetchMyLastLocation(){
        Task<Location> task = fusedLocationProviderClient.getLastLocation();



        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                CurrentLocation = location ;

                if (CurrentLocation == null ) {
                    SweetAlertDialog dialog = new SweetAlertDialog(map.this, SweetAlertDialog.ERROR_TYPE);
                    dialog.setTitleText("Something is wrong , your location is not fetched Please OPEN GPS application");
                    dialog.show();

                }else {

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(map.this);

                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                FetchMyLastLocation();

            }
        }
    }
}
