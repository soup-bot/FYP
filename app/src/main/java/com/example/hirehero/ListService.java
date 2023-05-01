package com.example.hirehero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListService extends AppCompatActivity implements View.OnClickListener {
    private TextView username;
    private EditText price, contact, details;
    private Spinner service;
    private Button submitListingButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // service = findViewById(R.id.service);
        price = findViewById(R.id.price);
        details = findViewById(R.id.details);
        contact = findViewById(R.id.contact);
        service = findViewById(R.id.service);

        submitListingButton = findViewById(R.id.submitListingButton);
        submitListingButton.setOnClickListener(this);

        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(url).getReference();
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = mDatabase.child("Users").child(userId);

        //testing if Uid exists in this class
        String test = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("MyApp", "UID = " + test);

        String[] serviceTags = {"Select Service", "Cleaning", "Painting", "Plumbing", "Electrical", "Landscaping", "Carpentry", "Moving", "Roofing", "Flooring", "HVAC", "Pest Control", "Pool Maintenance", "Handyman", "Window Cleaning"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinneritem, serviceTags);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        service.setAdapter(adapter);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitListingButton:
                Log.d("Profile", "submitListingButton clicked");
                submitListing();
                break;
        }
    }

    private void submitListing() {
        Log.d("ListService", "submitListing() called");
        String serviceInput = service.getSelectedItem().toString().toLowerCase();
        String priceInput = price.getText().toString();
        String detailsInput = details.getText().toString().toLowerCase();
        String contactInput = contact.getText().toString();
        String userId = mAuth.getCurrentUser().getUid();
        String listingId = mDatabase.child("Listings").child(userId).push().getKey();

        if (serviceInput.length() > 20) {
            Toast.makeText(this, "Service name should not exceed 20 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if (detailsInput.length() > 150) {
            Toast.makeText(this, "Details should not exceed 150 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (priceInput.length() > 11) {
            Toast.makeText(this, "Price should not exceed 20 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if (contactInput.length() > 15) {
            Toast.makeText(this, "Contact number should not exceed 150 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("e", serviceInput);
        if (serviceInput.equals("select service") || TextUtils.isEmpty(priceInput) || TextUtils.isEmpty(contactInput)) {
            Toast.makeText(ListService.this, "Please enter a service, price and contact", Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseReference userRef = mDatabase.child("Users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // get user's name from the dataSnapshot
                String userName = dataSnapshot.child("name").getValue(String.class);
                Log.d("d",userName);

                // create a new listing object
                Listing newListing = new Listing(serviceInput, priceInput, detailsInput, contactInput, listingId, userId, userName);

                // save the listing to firebase
                String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
                FirebaseDatabase.getInstance(url).getReference("Listings").child(listingId).setValue(newListing)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ListService.this, "Listing added successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ListService.this, "Failed to add listing", Toast.LENGTH_LONG).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ListService", "loadUserName:onCancelled", databaseError.toException());
            }
        });
    }
}