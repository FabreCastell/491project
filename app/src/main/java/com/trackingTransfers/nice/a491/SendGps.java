package com.trackingTransfers.nice.a491;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;


public class SendGps extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;

    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private android.widget.Button btnLocation;
    private TextView txtLocation;
    private android.widget.Button btnContinueLocation;
    private TextView txtContinueLocation;
    private StringBuilder stringBuilder;
    private String user, id;

    private boolean isContinue = false;
    private boolean isGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_gps);

        Intent passID = getIntent();
        user = passID.getStringExtra("user");
        id = passID.getStringExtra("id");

        this.txtContinueLocation = (TextView) findViewById(R.id.txtContinueLocation);
        this.btnContinueLocation = (Button) findViewById(R.id.btnContinueLocation);
        this.txtLocation = (TextView) findViewById(R.id.txtLocation);
        this.btnLocation = (Button) findViewById(R.id.btnLocation);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        if (!isContinue) {
                            txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                        } else {
                            stringBuilder.append(wayLatitude);
                            stringBuilder.append("-");
                            stringBuilder.append(wayLongitude);
                            stringBuilder.append("\n\n");
                            txtContinueLocation.setText(stringBuilder.toString());
                        }
                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isGPS) {
                    Toast.makeText(SendGps.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
                    return;
                }
                isContinue = false;
                SendGps.this.getLocation();
            }
        });

        btnContinueLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGPS) {
                    Toast.makeText(SendGps.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
                    return;
                }
                isContinue = true;
                stringBuilder = new StringBuilder();
                SendGps.this.getLocation();
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(SendGps.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(SendGps.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SendGps.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);

        } else {
            if (isContinue) {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(SendGps.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                        } else {
                            if (ActivityCompat.checkSelfPermission(SendGps.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SendGps.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                        }
                    }
                });
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (isContinue) {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    } else {
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(SendGps.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    wayLatitude = location.getLatitude();
                                    wayLongitude = location.getLongitude();
                                    txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                                } else {
                                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }
}
