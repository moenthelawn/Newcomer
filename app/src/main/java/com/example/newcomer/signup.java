package com.example.newcomer;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//This class creates the flow for signing up a user
public class signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button signup = findViewById(R.id.button);

        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String em = email.toString().trim();
                String pass = password.toString().trim();
                if (em.isEmpty()){
                    email.setError("Invalid email");

                }
                if (pass.isEmpty()){
                    password.setError("Invalid password");

                }
                if (pass.length() > 0 && em.length() > 0){
                    //Then we can move on to the next part of the sign up process
                    authorizeUser(em,pass);
                }

            }

            private void authorizeUser(String em, String pass) {
                mAuth.createUserWithEmailAndPassword(em,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Use registered successfully", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
}
