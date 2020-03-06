package com.example.newcomer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;

public class GroupDescription extends AppCompatActivity implements CustomLocationPrompt.OnInputListener {


    private PageText pageText;

    private Spinner ageDropDown; //This variable represents the age of the user

    public class PageText{
        private TextView eventName;
        private TextView eventLocation;
        private TextView groupSize;
        private TextView number1;
        private TextView number2;
        private TextView number3;

        private EditText eventNameET;
        private EditText eventLocationET;
        private Button test;

        TextView next;

        public PageText() {
            eventName = findViewById(R.id.textView17);
            eventLocation = findViewById(R.id.textView29);
            groupSize = findViewById(R.id.textView31);

            next = findViewById(R.id.next3);
            eventNameET = findViewById(R.id.EventName);
            eventLocationET = findViewById(R.id.eventLocation);

            number1 = findViewById(R.id.textView28);
            number2 = findViewById(R.id.textView30);
            number3 = findViewById(R.id.textView32);

            number1.setVisibility(View.INVISIBLE);
            number2.setVisibility(View.INVISIBLE);
            number3.setVisibility(View.INVISIBLE);

            eventName.setVisibility(View.INVISIBLE);
            eventLocation.setVisibility(View.INVISIBLE);
            groupSize.setVisibility(View.INVISIBLE);
        }
        private void setVisiblility_firstStep(){
            this.number1.setVisibility(View.VISIBLE);
            this.eventName.setVisibility(View.VISIBLE);

        }
        private void setVisibility_secondStep(){
            this.number2.setVisibility(View.VISIBLE);
            this.eventLocation.setVisibility(View.VISIBLE);

        }
        private void setVisibility_thirStep(){
            this.number3.setVisibility(View.VISIBLE);
            this.groupSize.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_description);



        pageText = new PageText(); //Declaring the page text which is responsible for holding all of the elements related to the apge text

        ageDropDown = findViewById(R.id.spinner3);
        String[] arr = {"2 People","3 People","4 People","5 People ","6+ People"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arr);

        ageDropDown.setAdapter(arrayAdapter);
        ageDropDown.setVisibility(View.INVISIBLE);

        firstStep();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                if (eventLocation.getText().toString().equals("") == true){
                    //Then we send a warning to th user
                    eventLocation.setError("Please enter a valid location");
                    eventLocation.requestFocus();
                    flag = true;
                }
                if (eventName.getText().toString().equals("") == true){
                    eventName.setError("Please enter a valid name");
                    eventName.requestFocus();
                    flag = true;
                }
                if (flag == false){
                    //Then we can go the next locaiton in the uI
                    Log.e("Step Completion"," User has completed the step");
                }

            }
        });
    }

    private void firstStep() {
      //This function is responsible for the first step elements
        pageText.setVisiblility_firstStep(); //Sets the visibility of the first step
    }

    @Override
    public void sendInput(LatLng latLng, String string) {
        eventLocation.setText(string);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
