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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    private TextView username;
    private EditText service, price, contact, details;
    private Button submitListingButton;
    private Button listings;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        service = findViewById(R.id.service);
        price = findViewById(R.id.price);
        details = findViewById(R.id.details);
        contact = findViewById(R.id.contact);

        submitListingButton = findViewById(R.id.submitListingButton);
        submitListingButton.setOnClickListener(this);
        listings = findViewById(R.id.listings);
        listings.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = mDatabase.child("Users").child(userId);

        //testing if Uid exists in this class
        String test = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("MyApp", "UID = " + test);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitListingButton:
                Log.d("Profile", "submitListingButton clicked");
                submitListing();
                break;
            case R.id.listings:
                startActivity(new Intent(this, servicelist.class));
                break;
        }
    }

    private void submitListing() {
        String serviceInput = service.getText().toString().toLowerCase();
        String priceInput = price.getText().toString();
        String detailsInput = details.getText().toString().toLowerCase();
        String contactInput = contact.getText().toString();
        String userId = mAuth.getCurrentUser().getUid();
        String listingId = mDatabase.child("Listings").child(userId).push().getKey();

        if (TextUtils.isEmpty(serviceInput) || TextUtils.isEmpty(priceInput) || TextUtils.isEmpty(contactInput)) {
            Toast.makeText(Profile.this, "Please enter a service, price and contact", Toast.LENGTH_LONG).show();
            return;
        }

        Listing newListing = new Listing(serviceInput, priceInput, detailsInput, contactInput, listingId, userId);
        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
        //FirebaseDatabase.getInstance(url).getReference("Listings").child(userId).child(listingId).setValue(newListing)
        FirebaseDatabase.getInstance(url).getReference("Listings").child(listingId).setValue(newListing)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Profile.this, "Listing added successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Profile.this, "Failed to add listing", Toast.LENGTH_LONG).show();
                    }
                });
      //  FirebaseDatabase.getInstance(url).getReference("Listings").child(listingId).child("bidAmount").setValue(0);
    }
}