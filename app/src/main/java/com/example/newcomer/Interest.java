package com.example.newcomer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class Interest extends View {

    public Interest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(null);
    }

    public Interest(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(null);
    }

    public Interest(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(null);
    }

    public Interest(Context context) {
        super(context);
        init(null);

    }

    private void init(@Nullable AttributeSet set){

    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);

    }
}
