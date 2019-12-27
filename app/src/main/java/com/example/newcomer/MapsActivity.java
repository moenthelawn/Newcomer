package com.example.newcomer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION = 102 ;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    int MY_LOCATION_REQUEST_CODE;
    private boolean permissionIsGranted = false;
    private SeekBar location_radius;
    Circle circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        location_radius = (SeekBar) findViewById(R.id.seekBar);

        UserData userData = (UserData) getApplicationContext();
        //Initialize the paramaters around the user data


        location_radius.setKeyProgressIncrement(10);

        int radius = userData.getRadiusDistance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            location_radius.setMin(0);
        }
        location_radius.setMax(100); //100 kms
        location_radius.setProgress(radius);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            // Request permission.
            if (Build.VERSION. SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    //Get the last location
                    final Location currentLocation = location;

                    LatLng currL = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                    
                    mMap.addMarker(new MarkerOptions().position(currL).title("Your location" ));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currL));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currL, 10));

                    location_radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            //THen we want to update the map on the map
                            LatLng currL = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                            addCircleRadius(mMap,currL, progress) ;
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
                    //LatLngBounds mapWidth =
                    //mMap.setLatLngBoundsForCameraTarget();
                }
            }
        });
    }

    private void addCircleRadius(GoogleMap mMap, LatLng currL,int radius) {

            CircleOptions encirclement = new CircleOptions();//Create the circle

            encirclement.center(currL);
            encirclement.radius((double) radius * 1000); //Get the current circle value on the map
            encirclement.fillColor(0x550000FF);
            if (circle != null){
                //Then we have already added it
                circle.remove();
            }
            circle = mMap.addCircle(encirclement);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode){
            case MY_PERMISSION_REQUEST_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permissions granted
                    permissionIsGranted = true;
                }else{
                    //permission is denied
                    permissionIsGranted = false;
                    Toast.makeText(MapsActivity.this,"This app requires location permissions to be granted",Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSION_REQUEST_COARSE_LOCATION:
                break; 
        }
    }
}
