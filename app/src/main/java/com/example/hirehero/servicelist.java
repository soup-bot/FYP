package com.example.hirehero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class servicelist extends AppCompatActivity implements ServiceAdapter.OnDeleteClickListener, View.OnClickListener {

    RecyclerView recyclerView;
    DatabaseReference database;
    ServiceAdapter myAdapter;
    ArrayList<Listing> list;
    Spinner filterservices;
    EditText listingsearch;
    ImageView searchButton;
    private ImageButton homebutton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicelist);
        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
        recyclerView = findViewById(R.id.servicelist);
        database = FirebaseDatabase.getInstance(url).getReference("Listings");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //listingsearch = findViewById(R.id.listingsearch);
        list = new ArrayList<>();
        myAdapter = new ServiceAdapter(this, list,false,null,null);
        recyclerView.setAdapter(myAdapter);
        searchButton = findViewById(R.id.searchButton);
        filterservices = findViewById(R.id.listingsearch);
        String[] services = {"All Services", "Cleaning", "Painting", "Plumbing", "Electrical", "Landscaping", "Carpentry", "Moving", "Roofing", "Flooring", "HVAC", "Pest Control", "Pool Maintenance", "Handyman", "Window Cleaning"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinneritem, services);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterservices.setAdapter(adapter);
        MyClickListener myClickListener = new MyClickListener(this, filterservices, database, list, myAdapter);
        searchButton.setOnClickListener(myClickListener);
        homebutton = (ImageButton) findViewById(R.id.homeButton);
        homebutton.setOnClickListener(this);


        showAllListings();
    }



    //method to display all listings in the database
    private void showAllListings(){
        list.clear();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Listing listing = dataSnapshot.getValue(Listing.class);
                    list.add(listing);
                }
                myAdapter.notifyDataSetChanged();
                database.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(int position) {

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