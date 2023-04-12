package com.example.hirehero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BiddingActivity extends AppCompatActivity implements View.OnClickListener{
    EditText biddername, biddercontact;
    String uid;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ImageButton homebutton;
    FirebaseUser currentUser = mAuth.getCurrentUser();
    DatabaseReference mDatabase;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding);
        Listing listing = (Listing) getIntent().getSerializableExtra("listing");

        TextView getservice = findViewById(R.id.listingdisplay1);
        TextView getdetails = findViewById(R.id.listingdisplay2);
        TextView getcontact = findViewById(R.id.listingdisplay3);
        TextView getprice = findViewById(R.id.listingdisplay4);

        String service = listing.getService();
        String details = listing.getDetails();
        String contact = listing.getContact();
        String price = listing.getPrice();

        getservice.setText(service);
        getdetails.setText(details);
        getcontact.setText(contact);
        getprice.setText(price);

        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(url).getReference();
        homebutton = (ImageButton) findViewById(R.id.homeButton);
        homebutton.setOnClickListener(this);
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
                    int bidAmount = (int) Long.parseLong(bidAmountString);
                    String bidderContact = biddercontact.getText().toString();
                    if (!TextUtils.isEmpty(bidderContact)) {
                        if (bidderContact.length() > 15) {
                            Toast.makeText(BiddingActivity.this, "Bidder contact should not exceed 15 characters", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (bidAmountString.length() > 11) {
                            Toast.makeText(BiddingActivity.this, "Bid amount should not exceed 11 characters", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        uid = currentUser.getUid();
                        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
                        DatabaseReference bidsRef = FirebaseDatabase.getInstance(url).getReference("Listings").child(listing.getListingID()).child("bids");
                        String bidId = bidsRef.push().getKey();
                        DatabaseReference userRef = mDatabase.child("Users").child(uid);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Retrieve the bidder's name from the dataSnapshot
                                String bidderName = dataSnapshot.child("name").getValue(String.class);
                                // Create a Bid object with the bid data
                                Bid bid = new Bid(bidderName, bidderContact, bidAmount, uid, listing.getListingID(), bidId);

                                // Store the Bid object in the database
                                bidsRef.child(bidId).setValue(bid);
                                Toast.makeText(BiddingActivity.this, "Bid submitted!", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                } else {
                    Toast.makeText(BiddingActivity.this, "Please enter your name and contact information", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.homeButton:
                startActivity(new Intent(this,UserProfile.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }
}

