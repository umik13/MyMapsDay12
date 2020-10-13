package com.example.mymapsday12;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends AppCompatActivity implements LocationListener, View.OnClickListener, OnMapReadyCallback {

    GoogleMap googleMap;
    double latitude;
    double longitude;
    Button koordinat;
    Button posisi_user;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        koordinat = (Button) findViewById(R.id.koordinat);
        posisi_user = (Button) findViewById(R.id.posisi_user);

        koordinat.setOnClickListener(this);
        posisi_user.setOnClickListener(this);

        SupportMapFragment fm = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        CekGPS();

        fm.getMapAsync(this);

        googleMap.setMyLocationEnabled(true);

        if (latitude != 0 && longitude != 0) {
            Toast.makeText(getApplicationContext(), "Latitude : " + latitude + " Longitude : " + longitude, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == koordinat) {

            if (latitude != 0 && longitude != 0) {
                Toast.makeText(getApplicationContext(), "Latitude : " + latitude + " Longitude : " + longitude, Toast.LENGTH_LONG).show();
            }
        } else if (v == posisi_user) {
            LatLng user = new LatLng(latitude, longitude);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user, 12));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void CekGPS() {
        try {

            /* Pengecekan GPS hidup / tidak */
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Info");
                builder.setMessage("Anda akan mengaktifkan GPS ?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int witch) {
                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int witch) {

                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        } catch (Exception e) {
            // TODO: handle exception

        }
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // menampilkan status google play service
        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {
            // Google Play Service Tersedia
            try {
                LocationManager locationManager = (LocationManager)
                        getSystemService(LOCATION_SERVICE);

                // Membuat Kriteria Untuk Penumpangan Provider
                Criteria criteria = new Criteria();

                // Mencari Provider Terbaik
                String provider = locationManager.getBestProvider(criteria, true);

                // Mendapatkan Lokasi Terakhir
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                Location location = locationManager.getLastKnownLocation(provider);

                if (location != null) {
                    onLocationChanged(location);
                }
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(provider, 5000, 0, this);
            } catch (Exception e){
            }
        }
    }

    @Override
    public void onLocationChanged(Location lokasi){
        //TODO Auto-generated method stub
        latitude = lokasi.getLatitude();
        longitude = lokasi.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider){
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider){
        // TODO Auto-generated method stud
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){
        // TODO Auto-generated method stub
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}

