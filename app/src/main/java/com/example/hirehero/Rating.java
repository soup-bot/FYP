package com.example.hirehero;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

public class Rating extends AppCompatActivity {

    private RatingBar mRatingBar;
    private Button mSubmitButton;
    private String mBidderId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        // Get the bidder ID from the intent extras
        Intent intent = getIntent();
        mBidderId = intent.getStringExtra("bidderId");

        mRatingBar = findViewById(R.id.ratingBar);
        mSubmitButton = findViewById(R.id.submitRating);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = mRatingBar.getRating();
                
                finish();
            }
        });
    }
}