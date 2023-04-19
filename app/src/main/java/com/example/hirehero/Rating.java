package com.example.hirehero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Rating extends AppCompatActivity {

    private RatingBar mRatingBar;
    private Button mSubmitButton;
    private String mBidderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        // Get the bidder ID from the intent extras
        Intent intent = getIntent();
        mBidderId = intent.getStringExtra("bidderId");
        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
        mRatingBar = findViewById(R.id.ratingBar);
        mSubmitButton = findViewById(R.id.submitRating);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float newRating = mRatingBar.getRating();

                // Get a reference to the bidder's user node in Firebase
                DatabaseReference bidderRef = FirebaseDatabase.getInstance(url).getReference().child("Users").child(mBidderId);

                // Update the bidder's rating in the database
                bidderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Get the current rating and number of ratings from the database
                        Object currentRatingObj = snapshot.child("rating").getValue();
                        float currentRating = 0;
                        if (currentRatingObj != null) {
                            if (currentRatingObj instanceof String) {
                                currentRating = Float.parseFloat((String) currentRatingObj);
                            } else if (currentRatingObj instanceof Long) {
                                currentRating = ((Long) currentRatingObj).floatValue();
                            } else if (currentRatingObj instanceof Double) {
                                currentRating = ((Double) currentRatingObj).floatValue();
                            } else {
                                // Handle unknown type
                            }
                        }
                        int numOfRatings = snapshot.child("numOfRatings").getValue(Integer.class);

                        // Calculate the new rating using a weighted average
                        float newAvgRating = (currentRating * numOfRatings + newRating) / (numOfRatings + 1);
                        String formattedRating = String.format("%.1f", newAvgRating); // format to 1 decimal place

                        // Update the bidder's rating and number of ratings in the database
                        bidderRef.child("rating").setValue(formattedRating);
                        bidderRef.child("numOfRatings").setValue(numOfRatings + 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Rating updated successfully
                                    Toast.makeText(Rating.this, "Rating submitted successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // Error updating rating
                                    Toast.makeText(Rating.this, "Error submitting rating", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database read error
                    }
                });
            }
        });
    }
}