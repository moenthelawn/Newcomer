package com.example.newcomer;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class GroupDescription extends AppCompatActivity implements CustomLocationPrompt.OnInputListener {


    private PageText pageText;
    private double lat, lng;
    private Spinner ageDropDown; //This variable represents the age of the user
    private String locationName;

    public class PageText {
        private TextView eventName;
        private TextView eventLocation;
        private TextView groupSize;
        private TextView groupNotes;

        private TextView number1;
        private TextView number2;
        private TextView number3;
        private TextView number4;

        AutocompleteSupportFragment autocompleteFragment;
        private EditText eventNameET;
        private EditText groupNotesET;

        private ImageView surroundingBox;
        private ImageView groupIcon;
        private ImageView notepad;

        private Spinner group_dropDown;

        private LinearLayout step1;
        private LinearLayout step2;
        private LinearLayout step3;
        private LinearLayout step3_bottom;

        private TextView progressNum;

        private TextView next;
        private TextView prev;

        public PageText() {
            eventName = findViewById(R.id.textView17);
            eventLocation = findViewById(R.id.textView29);
            groupSize = findViewById(R.id.textView31);
            groupNotes = findViewById(R.id.textView35);

            progressNum = findViewById(R.id.textView33);

            step1 = findViewById(R.id.linearLayout3);
            step2 = findViewById(R.id.linearLayout2);
            step3 = findViewById(R.id.linearLayout4);
            step3_bottom = findViewById(R.id.linearLayout5);

            next = findViewById(R.id.next3);
            prev = findViewById(R.id.prev4);
            notepad = findViewById(R.id.imageView8);

            eventName.setHint("Choose a catchy name!");

            eventNameET = findViewById(R.id.EventName);
            groupNotesET = findViewById(R.id.editText6);
            surroundingBox = findViewById(R.id.imageView6);

            number1 = findViewById(R.id.textView28);
            number2 = findViewById(R.id.textView30);
            number3 = findViewById(R.id.textView32);
            number4 = findViewById(R.id.textView36);

            group_dropDown = findViewById(R.id.spinner3);

            initializePlacesComplete();

            step1.setVisibility(View.INVISIBLE);
            step2.setVisibility(View.INVISIBLE);
            step3.setVisibility(View.INVISIBLE);
            step3_bottom.setVisibility(View.INVISIBLE);

            group_dropDown.setVisibility(View.INVISIBLE);

            setProgressNum(3);

            number1.setVisibility(View.INVISIBLE);
            number2.setVisibility(View.INVISIBLE);
            number3.setVisibility(View.INVISIBLE);
            number4.setVisibility(View.INVISIBLE);

            groupNotes.setVisibility(View.INVISIBLE);

            notepad.setVisibility(View.INVISIBLE);

            groupIcon = findViewById(R.id.imageView5);

            eventName.setVisibility(View.INVISIBLE);
            eventLocation.setVisibility(View.INVISIBLE);
            groupSize.setVisibility(View.INVISIBLE);

            groupIcon.setVisibility(View.INVISIBLE);
            surroundingBox.setVisibility(View.INVISIBLE);
            eventNameET.setVisibility(View.INVISIBLE);
            groupNotesET.setVisibility(View.INVISIBLE);

            this.prev.setTextColor(ContextCompat.getColor(GroupDescription.this, R.color.Red));
        }

        private void setProgressNum(int i) {
            //This function sets the progress number of the current step in the progress
            this.progressNum.setText("Step " + String.valueOf(i) + " out of 4");
            this.progressNum.setTypeface(null, Typeface.ITALIC);
        }

        private void setVisiblility_firstStep() {
            this.step1.setVisibility(View.VISIBLE);
            this.step2.setVisibility(View.VISIBLE);
            setNumberColours(1);
            setTypeFace(1);
            this.eventName.setVisibility(View.VISIBLE);
            this.number1.setVisibility(View.VISIBLE);
            this.eventNameET.setVisibility(View.VISIBLE);
            this.eventName.setTypeface(null, Typeface.BOLD);
            this.eventLocation.setTypeface(null, Typeface.NORMAL);
            this.groupSize.setTypeface(null, Typeface.NORMAL);
        }

        private void setVisibility_secondStep() {
            setNumberColours(2);
            this.step3.setVisibility(View.VISIBLE);
            setTypeFace(2);
            this.autocompleteFragment.getView().requestFocus();

            this.number2.setVisibility(View.VISIBLE);
            this.eventLocation.setVisibility(View.VISIBLE);
            this.eventName.setTypeface(null, Typeface.NORMAL);
            this.autocompleteFragment.getView().setVisibility(View.VISIBLE);
            this.eventLocation.setTypeface(null, Typeface.BOLD);
            this.groupSize.setTypeface(null, Typeface.NORMAL);
            this.surroundingBox.setVisibility(View.VISIBLE);

        }

        private void setVisibility_fourthStep() {
            //This sets the visibility of all of the paramaters made in the fourth step
            this.groupNotes.setVisibility(View.VISIBLE);
            this.groupNotes.setTypeface(null, Typeface.BOLD);
            setTypeFace(4);

            this.groupNotesET.setVisibility(View.VISIBLE);
            this.notepad.setVisibility(View.VISIBLE);
            this.number4.setVisibility(View.VISIBLE);
            setNumberColours(4);

            this.next.setTextColor(ContextCompat.getColor(GroupDescription.this, R.color.Red));

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean flag = false;
                    if (pageText.eventLocation.getText().toString().equals("") == true) {
                        //Then we send a warning to th user
                        pageText.eventLocation.setError("Please enter a valid location");
                        pageText.eventLocation.requestFocus();
                        flag = true;
                    }
                    if (pageText.eventName.getText().toString().equals("") == true) {
                        pageText.eventName.setError("Please enter a valid name");
                        pageText.eventName.requestFocus();
                        flag = true;
                    }
                    if (flag == false) {
                        //Then we can go the next locaiton in the uI
                        Log.e("Step Completion", " User has completed the step");

                        //We want to pass in the paramaters of hte current location

                        LatLng latLng = new LatLng(lat,lng);

                        Bundle args = new Bundle();
                        args.putParcelable("latlng",latLng);

                        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                        intent.putExtra("latlng",args);
                        intent.putExtra("locationname",locationName);

                        startActivity(intent);
                    }

                }
            });
        }

        private void setVisibility_thirStep() {

            setNumberColours(3);
            this.step3_bottom.setVisibility(View.VISIBLE);
            this.number3.setVisibility(View.VISIBLE);
            this.groupSize.setVisibility(View.VISIBLE);
            this.groupIcon.setVisibility(View.VISIBLE);

            setTypeFace(3);

            this.group_dropDown.setVisibility(View.VISIBLE);
            this.group_dropDown.setVisibility(View.VISIBLE);
            this.eventNameET.clearFocus();

            //sets the next button
            this.group_dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setVisibility_fourthStep();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        private void initializePlacesComplete() {
            //Set the paramaters as needed
            Places.initialize(getApplicationContext(), "AIzaSyAjGcF4XC-OEVJHKPmPefDUxGjxiSCbFK8");
            PlacesClient placesClient = Places.createClient(getApplicationContext());

            // Initialize the AutocompleteSupportFragment.
            this.autocompleteFragment = (AutocompleteSupportFragment)
                    getSupportFragmentManager().findFragmentById(R.id.autocomplete1);
            this.autocompleteFragment.getView().setVisibility(View.INVISIBLE);

        }

        private void setNumberColours(int number) {
            //This function will create the number colo
            if (number == 1) {
                pageText.number1.setBackgroundResource(R.drawable.number);
                pageText.number2.setBackgroundResource(R.drawable.number_grayed);
                pageText.number3.setBackgroundResource(R.drawable.number_grayed);
                pageText.number4.setBackgroundResource(R.drawable.number_grayed);
            } else if (number == 2) {
                pageText.number1.setBackgroundResource(R.drawable.number_grayed);
                pageText.number2.setBackgroundResource(R.drawable.number);
                pageText.number3.setBackgroundResource(R.drawable.number_grayed);
                pageText.number4.setBackgroundResource(R.drawable.number_grayed);
            } else if (number == 3) {
                pageText.number1.setBackgroundResource(R.drawable.number_grayed);
                pageText.number2.setBackgroundResource(R.drawable.number_grayed);
                pageText.number3.setBackgroundResource(R.drawable.number);
                pageText.number4.setBackgroundResource(R.drawable.number_grayed);
            } else {
                pageText.number1.setBackgroundResource(R.drawable.number_grayed);
                pageText.number2.setBackgroundResource(R.drawable.number_grayed);
                pageText.number3.setBackgroundResource(R.drawable.number_grayed);
                pageText.number4.setBackgroundResource(R.drawable.number);
            }


        }

        private void setTypeFace(int number) {
            //This function will create the number colo
            if (number == 1) {
                this.eventName.setTypeface(null, Typeface.BOLD);
                this.eventLocation.setTypeface(null, Typeface.NORMAL);
                this.groupSize.setTypeface(null, Typeface.NORMAL);
                this.groupNotes.setTypeface(null, Typeface.NORMAL);
            } else if (number == 2) {
                this.eventName.setTypeface(null, Typeface.NORMAL);
                this.eventLocation.setTypeface(null, Typeface.BOLD);
                this.groupSize.setTypeface(null, Typeface.NORMAL);
                this.groupNotes.setTypeface(null, Typeface.NORMAL);
            } else if (number == 3) {
                this.eventName.setTypeface(null, Typeface.NORMAL);
                this.eventLocation.setTypeface(null, Typeface.NORMAL);
                this.groupSize.setTypeface(null, Typeface.BOLD);
                this.groupNotes.setTypeface(null, Typeface.NORMAL);
            } else {
                this.eventName.setTypeface(null, Typeface.NORMAL);
                this.eventLocation.setTypeface(null, Typeface.NORMAL);
                this.groupSize.setTypeface(null, Typeface.NORMAL);
                this.groupNotes.setTypeface(null, Typeface.BOLD);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_group_description);
        getSupportActionBar().hide();

        pageText = new PageText(); //Declaring the page text which is responsible for holding all of the elements related to the apge text

        ageDropDown = findViewById(R.id.spinner3);
        String[] arr = {"2 People","3 People","4 People","5 People ","6+ People"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arr);

        ageDropDown.setAdapter(arrayAdapter);
        ageDropDown.setVisibility(View.INVISIBLE);

        pageText.prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupDescription.this,GroupTiming.class);
                startActivity(intent);
            }
        });

        firstStep();

    }

    private void firstStep() {
      //This function is responsible for the first step elements
        pageText.setVisiblility_firstStep(); //Sets the visibility of the first step
        this.pageText.eventNameET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                secondStep();
                return false;
            }
        });
    }
    private void secondStep(){
        pageText.setVisibility_secondStep();
        getLocationInput();

    }

    @Override
    public void sendInput(LatLng latLng, String string) {
        pageText.eventLocation.setText(string);
        //Now we display the third step
    }
    private void getLocationInput(){

        // Specify the types of place data to return.
        pageText.autocompleteFragment.setPlaceFields(Arrays.asList(
                Place.Field.NAME,
                Place.Field.LAT_LNG
        ));

        pageText.autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                String name = place.getName();
                locationName = name;

                pageText.autocompleteFragment.setText(name);


                if (place.getLatLng() !=null){

                    lat =place.getLatLng().latitude;
                    lng =place.getLatLng().longitude; //Now we want to do a callback and set the location as appropriately for the main rame
                    pageText.setVisibility_thirStep();

                }
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
