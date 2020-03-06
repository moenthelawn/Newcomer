package com.example.newcomer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupTiming extends AppCompatActivity implements CalendarDialogFragment.OnClickDate, TimePickerFragment.OnInputListener {
    //CONSTANTS
    public static final long HOUR = 3600*1000; // in milli-seconds.

    private UserData userData;

    private EditText eventTime;
    private EditText eventDate;
    private EditText eventLength;
    private EditText minAge;
    private EditText maxAge;

    private CrystalRangeSeekbar ageRange;

    private Spinner timeFrameDrop;

    private TextView next;
    private TextView timeStamp;

    private LinearLayout layout2;
    private LinearLayout layout3;

    private PageText pageText;

    public class PageText{
        TextView number1;
        TextView number2;
        TextView number3;
        TextView number4;
        TextView eventAgeRange;
        TextView minAge_text;
        TextView maxAge_text;

        //Titles
        TextView estEventLength;
        TextView eventDate_txt;
        TextView eventStartTime;
        TextView eventLength;

        public PageText() { //This class is a holder for all of the text paramaters that we hold in the UI
            this.number1 = findViewById(R.id.textView23);//Number 1 Bubble
            this.number2 = findViewById(R.id.textView24);//Number 2 Bubble
            this.number3 = findViewById(R.id.textView25);//Number 3 Bubble
            this.number4 = findViewById(R.id.textView26);//Number 4 Bubble
            this.eventAgeRange = findViewById(R.id.textView22); //Choose the event age range
            this.minAge_text = findViewById(R.id.textView21); //Min. Age
            this.maxAge_text = findViewById(R.id.textView27); //Max. Age
            this.estEventLength = findViewById(R.id.textView20);//Estimate the Event Length
            this.eventDate_txt = findViewById(R.id.textView15); //Choose the event date
            this.eventStartTime = findViewById(R.id.textView18);
            this.eventLength = findViewById(R.id.textView20);

            this.number2.setVisibility(View.INVISIBLE);
            this.number3.setVisibility(View.INVISIBLE);
            this.number4.setVisibility(View.INVISIBLE);
            this.eventAgeRange.setVisibility(View.INVISIBLE);
            this.minAge_text.setVisibility(View.INVISIBLE);
            this.maxAge_text.setVisibility(View.INVISIBLE);
            this.estEventLength.setVisibility(View.INVISIBLE);
            this.eventStartTime.setVisibility(View.INVISIBLE);
            this.eventLength.setVisibility(View.INVISIBLE);

        }
        private void setTextBold(int number){
            //This function will set the boldness of the text based on what step we are on


            //This function will create the number colo
            if (number == 1){
                this.eventDate_txt.setTypeface(null, Typeface.BOLD);
                this.eventStartTime.setTypeface(null,Typeface.NORMAL);
                this.eventLength.setTypeface(null,Typeface.NORMAL);
                this.eventAgeRange.setTypeface(null,Typeface.NORMAL);
            }
            else if(number == 2){
                this.eventDate_txt.setTypeface(null, Typeface.NORMAL);
                this.eventStartTime.setTypeface(null,Typeface.BOLD);
                this.eventLength.setTypeface(null,Typeface.NORMAL);
                this.eventAgeRange.setTypeface(null,Typeface.NORMAL);
            }
            else if(number == 3){
                this.eventDate_txt.setTypeface(null, Typeface.NORMAL);
                this.eventStartTime.setTypeface(null,Typeface.NORMAL);
                this.eventLength.setTypeface(null,Typeface.BOLD);
                this.eventAgeRange.setTypeface(null,Typeface.NORMAL);
            }
            else{
                this.eventDate_txt.setTypeface(null, Typeface.NORMAL);
                this.eventStartTime.setTypeface(null,Typeface.NORMAL);
                this.eventLength.setTypeface(null,Typeface.NORMAL);
                this.eventAgeRange.setTypeface(null,Typeface.BOLD);
            }
        }
        private void setNumberColours(int number) {
            //This function will create the number colo
            if (number == 1){
                pageText.number1.setBackgroundResource(R.drawable.number);
                pageText.number2.setBackgroundResource(R.drawable.number_grayed);
                pageText.number3.setBackgroundResource(R.drawable.number_grayed);
                pageText.number4.setBackgroundResource(R.drawable.number_grayed);
            }
            else if(number == 2){
                pageText.number1.setBackgroundResource(R.drawable.number_grayed);
                pageText.number2.setBackgroundResource(R.drawable.number);
                pageText.number3.setBackgroundResource(R.drawable.number_grayed);
                pageText.number4.setBackgroundResource(R.drawable.number_grayed);
            }
            else if(number == 3){
                pageText.number1.setBackgroundResource(R.drawable.number_grayed);
                pageText.number2.setBackgroundResource(R.drawable.number_grayed);
                pageText.number3.setBackgroundResource(R.drawable.number);
                pageText.number4.setBackgroundResource(R.drawable.number_grayed);
            }
            else{
                pageText.number1.setBackgroundResource(R.drawable.number_grayed);
                pageText.number2.setBackgroundResource(R.drawable.number_grayed);
                pageText.number3.setBackgroundResource(R.drawable.number_grayed);
                pageText.number4.setBackgroundResource(R.drawable.number);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_timing);

        pageText = new PageText();
        //userData = (UserData) getApplicationContext(); //This will get the overarching class that manages the user data
        //userData.createGroup();

        //Event detail specifics/
        eventDate = findViewById(R.id.editText4);
        eventTime = findViewById(R.id.editText5);
        eventLength = findViewById(R.id.editText7);

        //Layout dividers (visual representations)

        layout2 = findViewById(R.id.linearLayout6);
        layout3 = findViewById(R.id.linearLayout7);

        pageText.setTextBold(1);

        //ageRange.setColorFilter(R.color.DefaultCyan);

        //Make sure that they are invisible
        layout2.setVisibility(View.INVISIBLE);
        layout3.setVisibility(View.INVISIBLE);

        //Seekbar age range
        //ageRange.setRangeValues(18,50);

        ageRange = findViewById(R.id.rangeSeekbar5);
        ageRange.setVisibility(View.INVISIBLE);
        ageRange.setMinStartValue(18f).apply();
        ageRange.setMaxStartValue(100f).apply();
        //For now we want to make it invisible until we get to the fourth step

        eventDate.setShowSoftInputOnFocus(false);
        eventTime.setShowSoftInputOnFocus(false);
        //minAge.set
        disableSoftInputFromAppearing(eventDate);
        next = findViewById(R.id.next2);

        //eventDate.setText("2"); //Set the default ot be 2 (hours) at the beginning which is a safe estimate for how long the event will take place

        timeFrameDrop = findViewById(R.id.spinner2);
        String[] rr = {"minutes","hours"};

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,rr);
        timeFrameDrop.setAdapter(myAdapter);
        
        setTimeVisibility("INVISIBLE");
        setEstLengthVisibility("INVISIBLE"); 
        
        //timeFrameDrop.setPromptId(0);

        timeStamp = findViewById(R.id.textView19);
        timeStamp.setVisibility(View.INVISIBLE);

        eventDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                displayCalendarDialog();
                return false;
            }
        });

    }
    public static GradientDrawable drawCircle(int backgroundColor, int borderColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        shape.setColor(backgroundColor);
        shape.setStroke(10, borderColor);
        return shape;
    }
    @SuppressLint("ClickableViewAccessibility")
    private void setEstLengthVisibility(String invisible) {

        EditText est_edit = findViewById(R.id.editText7);


        Spinner spinner = findViewById(R.id.spinner2);

        if (invisible.equals("INVISIBLE") == true){
            //Then we set all of the paramaters to be invisible
            pageText.estEventLength.setVisibility(View.INVISIBLE);
            est_edit.setVisibility(View.INVISIBLE);

            spinner.setVisibility(View.INVISIBLE);
            layout3.setVisibility(View.INVISIBLE);
            pageText.number3.setVisibility(View.INVISIBLE);
            timeFrameDrop.setVisibility(View.INVISIBLE);
        }
        else{
            pageText.estEventLength.setVisibility(View.VISIBLE);
            est_edit.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            est_edit.requestFocus();
            layout3.setVisibility(View.VISIBLE);

            pageText.number3.setVisibility(View.VISIBLE);
            pageText.setNumberColours(3);
            pageText.setTextBold(3);
            est_edit.requestFocus();
            timeFrameDrop.setVisibility(View.VISIBLE);

            est_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    displayAgeRange();
                    return false;
                }
            });
        }
    }

    public static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private void setTimeVisibility(String invisible) {

        TextView time = findViewById(R.id.textView18);

        if (invisible.equals("INVISIBLE") == true){
            //Then we set all of the paramaters to be invisible
            time.setVisibility(View.INVISIBLE);
            eventTime.setVisibility(View.INVISIBLE);
            layout2.setVisibility(View.INVISIBLE);
            pageText.number2.setVisibility(View.INVISIBLE);

            //eventLength.setVisibility(View.INVISIBLE);
            //number3.setVisibility(View.INVISIBLE);
        }
        else{
            time.setVisibility(View.VISIBLE);
            pageText.setNumberColours(2);
            pageText.setTextBold(2);

            pageText.number2.setVisibility(View.VISIBLE);
            eventTime.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            eventTime.requestFocus();

            eventTime.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //Then we will collaborate the findings
                    displayTimeDialog();
                    return false;
                }

            });

        }
    }



    public void displayCalendarDialog(){

        Fragment datepicker = getSupportFragmentManager().findFragmentByTag("datepicker");
        DialogFragment datepicker_fram = (DialogFragment) datepicker;

        if (datepicker_fram == null){

            DialogFragment newFragment = new CalendarDialogFragment();
            newFragment.show(getSupportFragmentManager(), "datepicker");

        }

    }

    public void displayAgeRange(){
        //Display the age rage seekbar

        ageRange.setVisibility(View.VISIBLE);
        EditText est_edit = findViewById(R.id.editText7);

        displayTextString();
        est_edit.clearFocus(); //We no longer need the focus of this anymore

        pageText.setNumberColours(4);
        pageText.setTextBold(4);
        pageText.number4.setVisibility(View.VISIBLE);
        pageText.eventAgeRange.setVisibility(View.VISIBLE);
        next.setTextColor(ContextCompat.getColor(GroupTiming.this,R.color.Red));

        pageText.maxAge_text.setVisibility(View.VISIBLE);
        pageText.minAge_text.setVisibility(View.VISIBLE);

        ageRange.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                int low = minValue.intValue();
                int high = maxValue.intValue();
                setAgeDisplays(low,high); //This function will set the min and max values of the age display
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Now after we have it, we can set the onclick

                if (pageText.minAge_text.getText().toString().equals("") == false && pageText.maxAge_text.getText().toString().equals("") == false){
                        //Before we move on to the next location, we are going to want to update the user database

                        Intent intent = new Intent(GroupTiming.this,GroupDescription.class);
                        startActivity(intent);
                }


              /*
              //Here's what we need from this page
              1. Event Date
              2. Event Age Range
               */

            }
        });
    }

    private void setAgeDisplays(int low, int high) {

        String minValue_str = String.valueOf(low);
        String maxValue_str = String.valueOf(high);

        pageText.minAge_text.setText(minValue_str);
        pageText.maxAge_text.setText(maxValue_str);
    }


    @SuppressLint("ClickableViewAccessibility")
    public void displayTimeDialog(){
        Fragment timepicker = getSupportFragmentManager().findFragmentByTag("timepicker");
        DialogFragment timepicker_fram = (DialogFragment) timepicker;

        if (timepicker_fram == null){

            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "timepicker");

        }

        //time_edit.setVisibility(View.VISIBLE);
        //time_edit.requestFocus();


    }
    public void displayTextString(){
        String date_text = eventDate.getText().toString();
        String time_text = eventTime.getText().toString();
        String estTime_text = eventLength.getText().toString();
        int spinnerId = timeFrameDrop.getId();

        if (date_text.equals("") == false && time_text.equals("") == false && estTime_text.equals("") == false){
            timeStamp.setVisibility(View.VISIBLE);
            timeStamp.setText(date_text + "at " + time_text);
        }

    }

    @Override
    public void sendDate(int year, int month, int dayOfMonth) {

        SimpleDateFormat newFormat = new SimpleDateFormat("MMMM, dd ");
        Date date = new Date(year,month,dayOfMonth);

        String dateText = newFormat.format(date);

        eventDate.setText(dateText);
        displayTextString();
        setTimeVisibility("VISIBLE");
    }

    @Override
    public void sendDate_clock(TimePicker view, int hourOfDay, int minute) {
        SimpleDateFormat newFormat = new SimpleDateFormat("hh:mm a");
        Date date = new Date();

        date.setHours(hourOfDay);
        date.setMinutes(minute);

        layout2.setVisibility(View.VISIBLE);

        String timeText = newFormat.format(date);
        eventTime.setText(timeText);
        displayTextString();
        setEstLengthVisibility("VISIBLE");
    }


}
