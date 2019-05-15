package com.trackingTransfers.nice.a491;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private Handler loopHandler = new Handler();
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private final LatLng DefaultLocation = new LatLng(18.795711, 98.952684);
    private Integer ToggleLoop = 0;
    private Integer Count = 0;
    private double lat = 18.795711;
    private double lng = 98.952684;
    private String TypeIntent;
    private String user, id;
    private double latdb = 0;
    private double lngdb = 0;
    private double latIntent;
    private double lngIntent;
    private double startcamera = 0;
    //FireBase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        TypeIntent = getIntent().getStringExtra("type");
        user = getIntent().getStringExtra("user");
        id = getIntent().getStringExtra("id");
        latIntent = getIntent().getDoubleExtra("lat",18.795000);
        lngIntent = getIntent().getDoubleExtra("lng",98.952000);

    }

    public void updateGPStoDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mylat = database.getReference("List/"+id).child("lat");
        mylat.setValue(latdb);
        DatabaseReference mylng = database.getReference("List/"+id).child("lng");
        mylng.setValue(lngdb);
        //    mylng.setValue(lngdb+Count);

    }
    public void startloop(Location lct){
        latdb = lct.getLatitude();
        lngdb = lct.getLongitude();
        loopRunnable.run();
    //    onMyLocationClick(lct);
    }

    public void stoploop(){
        loopHandler.removeCallbacks(loopRunnable);
        //Toast.makeText(MapsActivity.this,"Stop Loop Now", Toast.LENGTH_SHORT).show();
    }

    private Runnable loopRunnable = new Runnable() {
        @Override
        public void run() {
         //   Count++;
          //  Toast.makeText(MapsActivity.this, "Loop Count" + Count, Toast.LENGTH_SHORT).show();
            updateGPStoDB();
            loopHandler.postDelayed(this,5000);
        }
    };

    private void InitFirebase() {
        DatabaseReference myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        if(TypeIntent.equals("1")){
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
            enableMyLocation();
        }

        if(TypeIntent.equals("2")){
            DatabaseReference ref = database.getReference("List/" + id);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                       // Log.d("zzzzzzzzzzzzz" , "1 " + lat + " " +lng);
                        lat = dataSnapshot.child("lat").getValue(Double.class);
                        lng = dataSnapshot.child("lng").getValue(Double.class);
                      //  Log.d("zzzzzzzzzzzzz" , "2 " + lat + " " +lng);
                       mMap.clear();
                       addmarker();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("ggg", "failread realtimedb");
                }
            });

        }





    }
    public void addmarker(){


        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng))
                .title("Lat : "+lat+" Lng : "+lng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.a0)));
        if(startcamera == 0) {
            startcamera = 1;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10));
        }
    }


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        double lon = (location.getLongitude());
        double lat = (location.getLatitude());
        Toast.makeText(this, "Current location: Lat " + lat + "\n Lon :"+ lon , Toast.LENGTH_LONG).show();
        startloop(location);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Camera Reset", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
//        if(ToggleLoop.equals(0)){
//            startloop();
//            ToggleLoop = 1;
//        }
//        else{
//            stoploop();
//            ToggleLoop = 0;
//
//        }
        stoploop();
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;

        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

}
