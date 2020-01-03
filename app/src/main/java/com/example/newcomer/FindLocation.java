package com.example.newcomer;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class FindLocation extends AppCompatActivity {
    private AutoCompleteTextView enterLocation;
    private PlaceAutocompleteAdapter mPlaceAutoCompleteAdapater;
    private GoogleSignInOptions gso;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new  LatLng(-40,-168),new LatLng(71,136));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

  //      mPlaceAutoCompleteAdapater = new PlaceAutocompleteAdapter(this,gso,LAT_LNG_BOUNDS,null);
//        enterLocation.setAdapter(mPlaceAutoCompleteAdapater);//Everytime we type in it should give us search suggestsions

    }
}
