package com.example.hirehero;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    private EditText emailEditText;
    private TextView banner;
    private Button resetPassButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();

        emailEditText = (EditText) findViewById(R.id.editTextForgotPassEmail);
        resetPassButton = (Button) findViewById(R.id.resetpassbutton);
        banner = (TextView) findViewById(R.id.hirehero3);
        banner.setOnClickListener(this);
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
            case R.id.hirehero3:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            case R.id.resetpassbutton:
                //registerUser();
                break;
        }
    }
}