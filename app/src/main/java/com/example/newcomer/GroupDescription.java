package com.example.newcomer;

import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class GroupDescription extends AppCompatActivity {
    private EditText eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_description);

        eventName = findViewById(R.id.EventName);
        eventName.setHint("Event Name");


    }
}
