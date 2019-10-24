package com.imastudio.mapsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import mumayank.com.airlocationlibrary.AirLocation;

public class MainActivity extends AppCompatActivity {

    private AirLocation location;
    private TextView tvLatLong, tvLokasi;
    double lat, longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLatLong = findViewById(R.id.tv_latlong);
        tvLokasi = findViewById(R.id.tv_lokasi);
    }

    public void getLocation(View view) {

        location = new AirLocation(MainActivity.this, true, true, new AirLocation.Callbacks() {

            @Override
            public void onSuccess(Location location) {

                lat = location.getLatitude();
                longi = location.getLongitude();

                if (lat != 0.0 && longi != 0.0){
                    String kota = addresses(lat, longi).get(0).getAdminArea();
                    String kecamatan = addresses(lat, longi).get(0).getLocality();

                    tvLatLong.setText(lat + "\n" + longi);
                    tvLokasi.setText("Kota : " + kota + "\n" + "Kecamatan : " + kecamatan);
                }
            }

            @Override
            public void onFailed(AirLocation.LocationFailedEnum locationFailedEnum) {
                Toast.makeText(MainActivity.this, locationFailedEnum.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Address> addresses(double latitude, double longitude) {
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(this);

        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e){
            e.printStackTrace();
        }

        return addressList;
    }

    public void viewLocation(View view) {
        if (lat != 0.0 && longi != 0.0) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra(MapsActivity.KEY_LATITUDE, lat);
            intent.putExtra(MapsActivity.KEY_LONGITUDE, longi);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Click GET LOCATION first!", Toast.LENGTH_SHORT).show();
        }
    }
}