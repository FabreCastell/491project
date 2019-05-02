package com.trackingTransfers.nice.a491;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

import static com.trackingTransfers.nice.a491.App.CHANNEL_1_ID;


public class SendGps extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;

    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private android.widget.Button btnLocation, stopButton;
    private TextView txtLocation;
    private android.widget.ImageButton btnContinueLocation;
    private TextView txtContinueLocation;
    private StringBuilder stringBuilder;
    private String user, id;
    private NotificationManagerCompat notiManager;
    private TextView showgps;

    private int restarttrack = 0;

    private boolean isContinue = false;
    private boolean isGPS = false;
    private double dblat = 0.0 , dblng = 0.0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_gps);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        showgps = findViewById(R.id.showgps);
        showgps.setVisibility(View.GONE);

        Intent passID = getIntent();
        user = passID.getStringExtra("user");
        id = passID.getStringExtra("id");

        notiManager = NotificationManagerCompat.from(this);
        this.txtContinueLocation = (TextView) findViewById(R.id.txtContinueLocation);
        this.btnContinueLocation =  findViewById(R.id.btnContinueLocation);
        stopButton = findViewById(R.id.stop);
        stopButton.setVisibility(View.GONE);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000); // 10 seconds
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
                        dblat = wayLatitude;
                        dblng = wayLongitude;
                        showgps.setText(String.valueOf("GPS ตอนนี้ : Lat " +wayLatitude + " Lng " +wayLongitude ));

                        Log.d("DBSentlat", "dblat : " + dblat);
                        Log.d("DBSentlng", "dblng : " + dblng);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                          DatabaseReference mylat = database.getReference("List/"+id).child("lat");
                      //  DatabaseReference mylat = database.getReference("testgps").child("lat");
                        mylat.setValue(dblat);
                        DatabaseReference mylng = database.getReference("List/"+id).child("lng");
                      //  DatabaseReference mylng = database.getReference("testgps").child("lng");
                        mylng.setValue(dblng);
                        if (!isContinue) {
                            txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                        } else {
                            Log.d("DBSentlat", "function reached : ");
                            stringBuilder.append(wayLatitude);
                            stringBuilder.append(" and ");
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

/*
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isGPS) {
                    Toast.makeText(SendGps.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
                    return;
                }
                isContinue = false;
              //  SendGps.this.getLocation();
                mFusedLocationClient.removeLocationUpdates(locationCallback);
                Log.d("DBSentstop" , " remove by button");
            }
        });
*/
        btnContinueLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGPS) {
                    Toast.makeText(SendGps.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
                    return;
                }
                stopButton.setVisibility(View.VISIBLE);
                showgps.setVisibility(View.VISIBLE);
                toastMessage("Start Tracking");
                notificationCall();
                isContinue = true;
                stringBuilder = new StringBuilder();
                SendGps.this.getLocation();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        mFusedLocationClient.removeLocationUpdates(locationCallback);
        Log.d("DBSentstop" , " remove by onpause");
        notiManager.cancelAll();
        stopButton.setVisibility(View.GONE);
        showgps.setVisibility(View.GONE);
        restarttrack = 0;

    }

    @Override
    protected void onStop() {
        super.onStop();
        mFusedLocationClient.removeLocationUpdates(locationCallback);
        Log.d("DBSentstop" , " remove by onstop");
        notiManager.cancelAll();
        restarttrack = 0;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFusedLocationClient.removeLocationUpdates(locationCallback);
        Log.d("DBSentstop" , " remove by onDestroy");
        notiManager.cancelAll();
        restarttrack = 0;

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("DBSentstop" , " remove by onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("DBSentstop" , " remove by onResume");
        if( restarttrack == 0){
            Log.d("DBSentstop" , " restarttrack = 0");

        }
        else{
            Log.d("DBSentstop" , " restarttrack = 1");
            btnContinueLocation.performClick();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("DBSentstop" , " remove by onRestart");
        testAutotrack();
    }

    private void testAutotrack(){

        Log.d("DBSentstop" , " tracking restart");
        restarttrack = 1;

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

    public void process(View v) {
        if (v.getId() == R.id.finish){
            isContinue = false;
            mFusedLocationClient.removeLocationUpdates(locationCallback);
            Log.d("DBSentstop" , " remove by button");
            changeState();
            nextPage(MainActivity.class,user," ");
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }else if (v.getId() == R.id.stop) {
            isContinue = false;
            mFusedLocationClient.removeLocationUpdates(locationCallback);
            toastMessage("Stop Tracking");
            stopButton.setVisibility(View.GONE);
            showgps.setVisibility(View.GONE);
            notiManager.cancelAll();




//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//        }else if (v.getId() == R.id.add){
//            nextPage(AddActivity.class,user," ");
//        }else if (v.getId() == R.id.submit){
//            addData(agetv.getText().toString(), numbertv.getText().toString());
//
        }
        hideKeyboardInput(v);
    }

    private void nextPage(Class page, String user, String id){
        Intent next = new Intent(this,page);
        next.putExtra("user", user);
        next.putExtra("state","1");
        next.putExtra("id", id);

        startActivity(next);
    }
    // To hide Android soft keyboard
    private void hideKeyboardInput(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void changeState() {
        DatabaseReference myRef = database.getReference("List/" + id);
        myRef.child("transfer").setValue("ถึงโรงพยาบาลปลายทางแล้ว");
    }


    public void notificationCall(){
        Notification notification = new NotificationCompat.Builder(this , CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ambu2)
                .setContentTitle("กำลังส่ง GPS")
                .setContentText("ขณะนี้กำลังส่ง GPS อยู่")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notiManager.notify(1,notification);
    }

}
