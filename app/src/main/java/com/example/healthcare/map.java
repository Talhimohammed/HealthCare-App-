package com.example.healthcare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class map extends FragmentActivity {

    GoogleMap map ;
    FusedLocationProviderClient client ;
    SupportMapFragment mapFragment ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
         mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);

   //    mapFragment.getMapAsync(this);
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(map.this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();


        } else {
               ActivityCompat.requestPermissions(map.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);

        }

    }


    private void getCurrentLocation() {
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
             //   Toast.makeText(getBaseContext(),location.getLatitude()+"",Toast.LENGTH_SHORT).show();

                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                          //  LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
                          //  LatLng d = new LatLng(location.getLatitude(),location.getLongitude());
                         //  LatLng d = new LatLng(19.169257,73.341601);

                       //     String adr = "Casablan ca" ;
                            String adr = getIntent().getStringExtra("Adress");
                            List<Address> addresslist = null ;
                            Geocoder a = new Geocoder(map.this);

                            try {
                                addresslist = a.getFromLocationName(adr,1);
                                Toast.makeText(getBaseContext(),addresslist.size()+"",Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Address adresse = addresslist.get(0);
                            LatLng d = new LatLng(adresse.getLatitude(),adresse.getLongitude());

                            MarkerOptions options = new MarkerOptions().position(d).title("i am there");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(d,10));
                            googleMap.addMarker(options);
                        }
                    });

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                getCurrentLocation();
            }
        }
    }
}
