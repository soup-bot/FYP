package com.example.hirehero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BiddingActivity extends AppCompatActivity {
    EditText biddername, biddercontact;
    String uid;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

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
        biddername = findViewById(R.id.biddername);
        biddercontact = findViewById(R.id.biddercontact);

        Button submitBidButton = findViewById(R.id.submit_bid);
        submitBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listing != null && listing.getListingID() != null) {
                    Log.d("ListingID", listing.getListingID());
                } else {
                    Log.e("ListingID", "Error retrieving listing ID");
                }
                EditText bidAmountEditText = findViewById(R.id.bid_amount);
                String bidAmountString = bidAmountEditText.getText().toString();
                if (!TextUtils.isEmpty(bidAmountString)) {
                    int bidAmount = Integer.parseInt(bidAmountString);
                    String bidderName = biddername.getText().toString();
                    String bidderContact = biddercontact.getText().toString();
                    if (!TextUtils.isEmpty(bidderName) && !TextUtils.isEmpty(bidderContact)) {
                        uid = currentUser.getUid();
                        Log.d("ListingID", listing.getListingID());
                       // Bid bid = new Bid(bidderName, bidderContact, bidAmount, uid, listing.getListingID());
                        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
                       // DatabaseReference listingRef = FirebaseDatabase.getInstance(url).getReference("Listings").child(listing.getListingID());
                       // DatabaseReference bidRef = listingRef.child("bids").push();
                       // bidRef.setValue(bid);
                        DatabaseReference bidsRef = FirebaseDatabase.getInstance(url).getReference("Listings").child(listing.getListingID()).child("bids");
                        String bidId = bidsRef.push().getKey();
                        Log.e("bidID", "BID ID:" +bidId);

                        // Create a Bid object with the bid data
                        Bid bid = new Bid(bidderName, bidderContact, bidAmount, uid, listing.getListingID(), bidId);

                        // Store the Bid object in the database
                        bidsRef.child(bidId).setValue(bid);
                        Toast.makeText(BiddingActivity.this, "Bid submitted!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(BiddingActivity.this, "Please enter your name and contact information", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BiddingActivity.this, "Please enter a bid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}