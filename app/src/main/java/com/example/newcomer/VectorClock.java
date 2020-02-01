package com.example.newcomer;

import android.content.Context;
import android.util.AttributeSet;
import com.turki.vectoranalogclockview.VectorAnalogClock;

public class VectorClock extends VectorAnalogClock {

    private void init(){
        //use this for the default Analog Clock (recommended)
        initializeSimple();

        //or use this if you want to use your own vector assets (not recommended)
        //initializeCustom(faceResourceId, hourResourceId, minuteResourceId, secondResourceId);
    }

    //mandatory constructor
    public VectorClock(Context ctx) {
        super(ctx);
        init();
    }

    // the other constructors are in case you want to add the view in XML

    public VectorClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VectorClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public VectorClock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
}