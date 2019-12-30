package com.example.newcomer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, textbox.OnFragmentInteractionListener {

    private static final String TEXTDISPLAY_FRAGMENT = "Text Display";

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION = 102 ;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    int MY_LOCATION_REQUEST_CODE;
    private boolean permissionIsGranted = false;
    private ImageButton backButton;
    private SeekBar location_radius;
    Circle circle;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Initialize all of the variables ......
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        location_radius = (SeekBar) findViewById(R.id.seekBar);
        userData = (UserData) getApplicationContext();
        backButton = (ImageButton) findViewById(R.id.imageButton3);

        //Initialize the parameters around the user data
        location_radius.setKeyProgressIncrement(1);

        int radius = userData.getRadiusDistance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            location_radius.setMin(0);
        }
        location_radius.setMax(30); //100 kms
        location_radius.setProgress(radius);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the back button and then we update the radius as needed
                userData.setRadiusDistance(location_radius.getProgress()/1000); //Convert it to km
            }
        });

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
            public void onSuccess(final Location location) {
                if (location != null){
                    //Get the last location
                    final Location currentLocation = location;

                    LatLng currL = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(currL).title("Your location" ));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currL));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currL, 10));

                    addCircleRadius(mMap, location_radius.getProgress(),currentLocation) ;

                    location_radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            //Then we want to update the map on the map
                            if (progress > 29){
                                //Then we want to display the dialogue box telling them to set their custom
                            }
                            addCircleRadius(mMap, progress,currentLocation);
                            addText(mMap,progress,currentLocation);

                            setFragment(Integer.toString(progress));
                        }
                        public void setFragment(String progress){
                            //Get the x,y coordinates of the seek bar thumb
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); //begin the transaction

                            float thumbPos = getSeekbarThumbPosition(); //Get the thumb seek bar position

                            textbox text = textbox.newInstance(progress,thumbPos); //Pass both the progress and the thumb position to be used in coordinating the seek bar's position

                            fragmentTransaction.replace(R.id.textDisplay, text);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                }
            }
        });
    }

    private void addText(GoogleMap mMap, int progress, Location currentLocation) {
        //This function will add the

    }

    private float getSeekbarThumbPosition() {

        float width = location_radius.getWidth()
                - location_radius.getPaddingLeft()
                - location_radius.getPaddingRight();

        float thumbPos = location_radius.getPaddingLeft()
                + width
                * (location_radius.getProgress())
                / location_radius.getMax();
        return thumbPos;
    }

    private void addCircleRadius(GoogleMap mMap,int radius,Location currentLocation) {
        //This function adds a circle to the map

        LatLng currL = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        CircleOptions encirclement = new CircleOptions();//Create the circle

            encirclement.center(currL);
            encirclement.radius(radius*1000); //Get the current circle value on the map
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
