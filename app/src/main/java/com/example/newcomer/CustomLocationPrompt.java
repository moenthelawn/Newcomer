package com.example.newcomer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;


public class CustomLocationPrompt extends DialogFragment {

    private ImageButton exitButton;
    private OnInputListener mListener;

    public CustomLocationPrompt() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CustomLocationPrompt newInstance(String title) {
        CustomLocationPrompt frag = new CustomLocationPrompt();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.custom_dialog, container);
        //Set the paramaters as needed
        Places.initialize(getContext(),"AIzaSyAjGcF4XC-OEVJHKPmPefDUxGjxiSCbFK8");
        PlacesClient placesClient = Places.createClient(getContext());

        exitButton = inflate.findViewById(R.id.imageButton9);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss(); //Dismiss the dialog
            }
        });
        AutocompleteSupportFragment autocompleteFragment  = (AutocompleteSupportFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        int AUTOCOMPLETE_REQUEST_CODE = 1;

    // Set the fields to specify which types of place data to
    // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(getContext());


        // Specify the types of place data to return.

        autocompleteFragment.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                String name = place.getName().toString();

                double lat, lng;

                if (place.getLatLng() !=null){
                    lat =place.getLatLng().latitude;
                    lng =place.getLatLng().longitude; //Now we want to do a callback and set the location as appropriately for the main rame
                    mListener.sendInput(new LatLng(lat,lng), name);  //Set the appropriate call back
                    getDialog().dismiss();

                }
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

        return inflate;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        // Fetch arguments from bundle and set title
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInputListener) {
            mListener = (OnInputListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnInputListener {
        // TODO: Update argument type and name
        void sendInput(LatLng latLng, String string);
    }

}