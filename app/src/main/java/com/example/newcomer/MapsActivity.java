package com.example.newcomer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, distanceDisplay.OnFragmentInteractionListener,textbox.OnFragmentInteractionListener, CustomLocationPrompt.OnInputListener {

    private static final String TEXTDISPLAY_FRAGMENT = "Text Display";

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION = 102 ;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    int MY_LOCATION_REQUEST_CODE;
    private boolean permissionIsGranted = false;

    private ImageButton backButton;
    private SeekBar location_radius;
    private BottomNavigationView bottomNavigationView;

    public LatLng location_set; //Location of the user
    public Location location_tot;
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

        //setDistance_display(radius);

        //Getting the relevant information for the bottom navigation feed where the user will be able to choose
        //any of three options

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.group_add:
                            //Then we have that the user has selected "create a group"
                            //userData.updateUserData();
                            Intent intent = new Intent(MapsActivity.this,CreateGroup.class);
                            startActivity(intent);
                    }
                    return false;
            }
        });
    }
    public void setDistance_display(int distance){
        //Set the distance display fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); //begin the transaction
        final distanceDisplay disa = distanceDisplay.newInstance(distance); //Pass both the progress and the thumb position to be used in coordinating the seek bar's position
        fragmentTransaction.add(R.id.distDisplay, disa,"distancedisplay").commit();

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
                if (location == null){
                    //Then we call the API to get the location
                    //prompt the user to enter their desired location
                    Intent intent = new Intent(MapsActivity.this,FindLocation.class);
                    startActivity(intent);
                }
                if (location != null){
                    //Get the last location
                    location_tot = location;
                    LatLng currL = getLatestLocation(location_tot);

                    float zoomLevel = getZoomLevel(location_radius.getProgress()); //Pass in the current radius of the circle
                    setMarkerOptions(currL,"initialprogress");
                    moveCamera(currL,"initialprogress",location_radius.getProgress(),false);

                    addCircleRadius(mMap, location_radius.getProgress(),currL) ;

                    setDistance_display(location_radius.getProgress());

                    location_radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            //Then we want to update the map on the map
                            if (progress > 29){
                                //Then we want to get the user to update their instance
                                EditText editText = findViewById(R.id.editText2);
                                editText.requestFocus();
                            }

                            LatLng currL = getLatestLocation(location_tot);

                            addCircleRadius(mMap, progress,currL);

                            setFragment_display_box(Integer.toString(progress));
                            setDistance_display(progress);

                        }
                        public void setFragment_display_box(String progress){

                            //Get the x,y coordinates of the seek bar thumb
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); //begin the transaction

                            float thumbPos = getSeekbarThumbPosition(); //Get the thumb seek bar position
                            final textbox text = textbox.newInstance(progress,thumbPos); //Pass both the progress and the thumb position to be used in coordinating the seek bar's position

                            //Set the runtime to be 10 seconds

                            new CountDownTimer(2500, 1000) {

                                public void onTick(long millisUntilFinished) {

                                }

                                public void onFinish() {
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    Fragment textdisplay = fragmentManager.findFragmentByTag("textdisplay");
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); //begin the transaction
                                    fragmentTransaction.remove(textdisplay);
                                    fragmentTransaction.commit();
                                }
                            }.start();

                            fragmentTransaction.replace(R.id.textDisplay, text,"textdisplay");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            //Then we listen for the tag before we subtract out the fragment that we were listening to
                            LatLng currL = getLatestLocation(location_tot);
                            moveCamera(currL,"camupdate",location_radius.getProgress(),false); //false = do not clear the map, true = clear the map
                        }
                    });

                }
            }
        });
    }

    private LatLng getLatestLocation(Location currentLocation) {
        LatLng currL;
        if (location_set != null){
            currL =location_set;
        }
        else{
            currL = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        }
        return currL;
    }

    private float getZoomLevel(int radius) {
        float zoomLevel = 0;
        //This function returns the optimal zoom level based on the passed in radius

        double scale = (double) (radius * 1000) / 500; //Convert the radius to be metres (not km)
        zoomLevel =(float) (16 - Math.log(scale) / Math.log(2));

        return zoomLevel - 1.0f;
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

    private void addCircleRadius(GoogleMap mMap,int radius,LatLng currL) {
        //This function adds a circle to the map

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
        if (uri.toString() == "save_changes"){
            //Then we know that the changes hve been saved
            //Navigate to another UI
            userData.setRadiusDistance(location_radius.getProgress()); //Set the progress to be the one of the one that we want
            Intent intent = new Intent(MapsActivity.this, FindLocation.class);
            startActivity(intent);
        }
        else if (uri.toString() == "change_location"){
            displayCustomInputPrompt();
        }
        else if (uri.toString() == "reset_text"){
            //The user has selected to edit the text
        }
    }
    @Override
    public void sendDistanceUpdate(int progress){

        //The user has selected to edit the distance so we should update the radius distance

        LatLng currL = getLatestLocation(location_tot);
        addCircleRadius(mMap, progress,currL);
        moveCamera(currL,"test",progress,false);
    }
    private void displayCustomInputPrompt() {
        CustomLocationPrompt customLocationPrompt = CustomLocationPrompt.newInstance("Some Title");
        customLocationPrompt.show(getSupportFragmentManager(),"insertlocation");
    }

    @Override
    public void sendInput(LatLng latLng, String string) {
            //Once we get the input then we will adjust the bounds as appropriately
        moveCamera(latLng, "marker",location_radius.getProgress(),true);
        location_set = latLng;
    }

    public void moveCamera(LatLng latLng, String title, int progress, boolean clear){
        //Move the camera to the appropriate location

        if (clear == true){
            mMap.clear(); //Erase all contents on the map before preceeding. At this point, we know there are already components on the map
        }
        float zoomLevel = getZoomLevel(progress);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel));
        setMarkerOptions(latLng,title);

    }

    private void setMarkerOptions(LatLng latLng, String title) {
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);
    }

}