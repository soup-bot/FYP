package com.example.hirehero;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView registertext, forgotpass;
    private EditText editTextTextEmail, editTextTextPassword;
    private LottieAnimationView login;
    private FirebaseAuth mAuth;

//on create
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        registertext = (TextView) findViewById(R.id.registertext);
        registertext.setOnClickListener(this);
        login = (LottieAnimationView) findViewById(R.id.loginbutton);
        login.setOnClickListener(this);
        forgotpass = (TextView) findViewById(R.id.forgotpasstext);
        forgotpass.setOnClickListener(this);
        editTextTextEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword= (EditText) findViewById(R.id.editTextTextPassword);
        mAuth = FirebaseAuth.getInstance();
    }

//switch case for button presses
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registertext:
                startActivity(new Intent(this,Registration.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.loginbutton:
                userLogin();
                break;
            case R.id.forgotpasstext:
                startActivity(new Intent(this, ForgotPassword.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }

    }

    private void userLogin() {
        String email = editTextTextEmail.getText().toString().trim();
        String password = editTextTextPassword.getText().toString().trim();

        if (email.isEmpty()){
            editTextTextEmail.setError("Email is required");
            editTextTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextTextEmail.setError("Please enter a valid email");
            editTextTextEmail.requestFocus();
            return;

        }
        if(password.isEmpty()){
            editTextTextPassword.setError("Password is required");
            editTextTextPassword.requestFocus();
            return;
        }
        if (password.length()<6) {
            editTextTextPassword.setError("Password length must be atleast 6 characters");
            editTextTextPassword.requestFocus();
            return;
        }







        //login logic
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //if user email is verified, take them to user profile. else send email verification and notify user to verify email
                if(user.isEmailVerified()){startActivity(new Intent(MainActivity.this, UserProfile.class));
                }else {
                    user.sendEmailVerification();
                    Toast.makeText(MainActivity.this,"Please verify your email",Toast.LENGTH_LONG).show();
                }


            }else{
                //fail login if password and email doesnt match database
                Toast.makeText(MainActivity.this,"Failed login", Toast.LENGTH_LONG).show();
            }

        });
    }
}