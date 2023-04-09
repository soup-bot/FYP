package com.example.hirehero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    private EditText emailEditText;
    private TextView banner;
    private Button resetPassButton;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = (EditText) findViewById(R.id.editTextForgotPassEmail);
        resetPassButton = (Button) findViewById(R.id.resetpassbutton);
        auth = FirebaseAuth.getInstance();

        resetPassButton.setOnClickListener(this);

    }
    //animation when phone back button is pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resetpassbutton:
                resetPassword();
                break;
        }
    }
        private void resetPassword() {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()){
                emailEditText.setError("Email is required");
                emailEditText.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailEditText.setError("Please enter a valid email");
                emailEditText.requestFocus();
                return;

            }





            //reset password logic
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //if reset pass successful then show user a message
                        Toast.makeText(ForgotPassword.this,"Check your email to reset password", Toast.LENGTH_LONG).show();

                    }else{
                        //otherwise show try again message
                        Toast.makeText(ForgotPassword.this,"Try again", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
