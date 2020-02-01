package com.example.newcomer;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;
import java.util.Date;

public class GroupTiming extends AppCompatActivity implements AnalogTime.OnFragmentInteractionListener {
    private CalendarView calendar;
    public static final long HOUR = 3600*1000; // in milli-seconds.
    private Button timing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_timing);

        calendar = findViewById(R.id.calendarView);
        timing = findViewById(R.id.button2);

        //Then we will also need to get hte current date and times
        Date currentTime= Calendar.getInstance().getTime();
        calendar.setDate(currentTime.getTime(), false,true);
        calendar.setMaxDate(currentTime.getTime() + (HOUR * 14 * 24));
        calendar.setMinDate(currentTime.getTime());

        timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Then we will open the fragment that contains the clock that we can use to
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); //begin the transaction

                final AnalogTime analogTime = AnalogTime.newInstance(); //Pass both the progress and the thumb position to be used in coordinating the seek bar's position
                fragmentTransaction.add(R.id.constraintLayout, analogTime,"analogtime").commit();

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
