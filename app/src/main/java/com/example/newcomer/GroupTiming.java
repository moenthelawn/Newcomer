package com.example.newcomer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
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
import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupTiming extends AppCompatActivity implements CalendarDialogFragment.OnClickDate, TimePickerFragment.OnInputListener, RangeSeekBar.OnRangeSeekBarChangeListener {
    //CONSTANTS
    public static final long HOUR = 3600*1000; // in milli-seconds.

    private TextView timeStamp;
    private EditText eventTime;
    private EditText eventDate;
    private EditText eventLength;
    private EditText minAge;
    private EditText maxAge;
    private RangeSeekBar<Integer> ageRange;



    private Spinner timeFrameDrop;
    private TextView next;

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

        //Event detail specifics/
        eventDate = findViewById(R.id.editText4);
        eventTime = findViewById(R.id.editText5);
        eventLength = findViewById(R.id.editText7);

        //Layout dividers (visual representations)

        layout2 = findViewById(R.id.linearLayout6);
        layout3 = findViewById(R.id.linearLayout7);

        pageText.setTextBold(1);

        //Make sure that they are invisible
        layout2.setVisibility(View.INVISIBLE);
        layout3.setVisibility(View.INVISIBLE);

        //Seekbar age range
        //ageRange.setRangeValues(18,50);
        FrameLayout layout = (FrameLayout) findViewById(R.id.seekbar_placeholder);

        ageRange = new RangeSeekBar<Integer>(this);
        ageRange.setRangeValues(15,100);
        ageRange.setTextAboveThumbsColor(R.color.Black);

        ageRange.setSelectedMinValue(18);
        ageRange.setSelectedMaxValue(25);
        layout.addView(ageRange);

        //For now we want to make it invisible until we get to the fourth step
        ageRange.setVisibility(View.INVISIBLE);

        eventDate.setShowSoftInputOnFocus(false);
        eventTime.setShowSoftInputOnFocus(false);
        minAge = findViewById(R.id.editText6);
        maxAge = findViewById(R.id.editText8);

        minAge.setVisibility(View.INVISIBLE);
        maxAge.setVisibility(View.INVISIBLE);


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
        pageText.setNumberColours(4);
        pageText.number4.setVisibility(View.VISIBLE);
        pageText.eventAgeRange.setVisibility(View.VISIBLE);
        next.setTextColor(ContextCompat.getColor(GroupTiming.this,R.color.Red));

        minAge.setVisibility(View.VISIBLE);
        maxAge.setVisibility(View.VISIBLE);

        setAgeDisplays(); //This function will set the min and max values of the age display
        ageRange.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                //The user has dragged the emblem, then we will update the age for min/max'

                setAgeDisplays(); //This function will set the min and max values of the age display

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Now after we have it, we can set the onclick
                Intent intent = new Intent(GroupTiming.this,GroupDescription.class);
                startActivity(intent);

            }
        });
    }

    private void setAgeDisplays() {

        int miniValue = ageRange.getSelectedMinValue();
        int maxiValue = ageRange.getSelectedMaxValue();
        String minValue_str = String.valueOf(miniValue);
        String maxValue_str = String.valueOf(maxiValue);

        minAge.setText(minValue_str);
        maxAge.setText(maxValue_str);
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

        if (date_text.equals("") == false && time_text.equals("") == false){

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

    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {


    }
}