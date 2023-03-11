package com.example.hirehero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyListings extends AppCompatActivity implements MyAdapter.OnDeleteClickListener {

    RecyclerView recyclerView2;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<Listing> list;
    EditText listingsearch;
    ImageView searchButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_listings);
        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
        recyclerView2 = findViewById(R.id.mylisting);
        database = FirebaseDatabase.getInstance(url).getReference("Listings");
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
     //   listingsearch = findViewById(R.id.listingsearch);
        list = new ArrayList<>();
        myAdapter = new MyAdapter(this, list,true,this);
        recyclerView2.setAdapter(myAdapter);
      //  searchButton = findViewById(R.id.searchButton);
      //  searchButton.setOnClickListener(this);
        showCurrentUserListings();
    }
/*
    @Override
    public void onClick(View view) {
        String searchText = listingsearch.getText().toString().toLowerCase();
        switch (view.getId()) {
            case R.id.searchButton:
                if (TextUtils.isEmpty(searchText)) {
                    // if search field is empty, display all listings
                    showCurrentUserListings();
                } else {
                    // filter listings based on search text
                    Query query = database.orderByChild("service").startAt(searchText).endAt(searchText + "\uf8ff");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Listing listing = dataSnapshot.getValue(Listing.class);
                                list.add(listing);

                            }
                            myAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                break;
        }
    }

*/
    //method to display all listings in the database
    private void showCurrentUserListings(){
        list.clear();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("UID", "UID = "+ currentUserId);
        Query query = database.orderByChild("userID").equalTo(currentUserId);
        query.addValueEventListener(new ValueEventListener() {
      //  database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("MyListings", "onDataChange() called with snapshot: " + snapshot);
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
        Listing listing = list.get(position);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (listing.getUserID().equals(currentUserId)) {
            database.child(listing.getListingID()).removeValue();
            Toast.makeText(this, "Listing deleted", Toast.LENGTH_SHORT).show();
            list.remove(position);
            myAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "You can only delete your own listings", Toast.LENGTH_SHORT).show();
        }
    }
}