package com.example.newcomer;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class profile extends AppCompatActivity {
    //Add the firebase authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FloatingActionButton back = findViewById(R.id.back);
        FloatingActionButton editProfile = findViewById(R.id.edit);

        EditText name = findViewById(R.id.name);
        EditText location = findViewById(R.id.location);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //THen navigate to another fragment where we get the user's location
                Intent intent = new Intent(profile.this, MapsActivity.class);
                startActivity(intent);//Start the location finding of the user
            }
        });

    }


}
