package com.example.hirehero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BiddingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding);
        Listing listing = (Listing) getIntent().getSerializableExtra("listing");

        TextView listingDetails = findViewById(R.id.listing_details);
        String detailsText = "Service: " + listing.getService() + "\n" +
                "Price: " + listing.getPrice() + "\n" +
                "Details: " + listing.getDetails() + "\n" +
                "Contact: " + listing.getContact();
        listingDetails.setText(detailsText);


    }
}