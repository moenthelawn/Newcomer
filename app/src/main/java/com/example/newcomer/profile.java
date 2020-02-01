package com.example.newcomer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class profile extends AppCompatActivity {
    //Add the firebase authentication
    private BottomNavigationView bottomNavigationView;
    private EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageButton back = findViewById(R.id.imageButton5);
        //FloatingActionButton editProfile = findViewById(R.id.edit);
        final UserData userData = (UserData) getApplicationContext();

        name = findViewById(R.id.name);

        String currUserName = userData.getUserName();
        if (currUserName.equals("") == false){
            name.setText(currUserName);
        }

        EditText location = findViewById(R.id.location);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.group_add:
                        //Then we have that the user has selected "create a group"
                        //userData.updateUserData();
                        //We want to be sure as well that it is fairly consistent for the current get location does not deviate
                        Intent intent = new Intent(profile.this,CreateGroup.class);
                        startActivity(intent);
                }
                return false;
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //THen navigate to another fragment where we get the user's location
                Intent intent = new Intent(profile.this, MapsActivity.class);
                startActivity(intent);//Start the location finding of the user
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userData.setUserName(s.toString());
            }
        });

    }
}
