package com.example.newcomer;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private EditText editTextMobile;
    private TextView accountSignUp;
    UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMobile = findViewById(R.id.editTextMobile);
        accountSignUp = findViewById(R.id.textView3);
        userData = (UserData) getApplicationContext();

        accountSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Then we go to the account sign up page
                Intent intent = new Intent(MainActivity.this,signup.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextMobile.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 10){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }

                userData.setPhoneNumber(mobile);

                Intent intent = new Intent(MainActivity.this, authentication.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });
    }
}
