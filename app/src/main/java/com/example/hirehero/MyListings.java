package com.example.hirehero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyListings extends AppCompatActivity implements MyAdapter.OnDeleteClickListener, BidAdapter.OnViewClickListener{

    RecyclerView recyclerView2;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<Listing> list;



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
        list = new ArrayList<>();
        myAdapter = new MyAdapter(this, list,true,this,this);
        recyclerView2.setAdapter(myAdapter);


        showCurrentUserListings();
    }

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

    @Override
    public void onViewClick(int position) {
        Log.d("MyListings", "onViewClick() called");
        Listing listing = list.get(position);
        Intent intent = new Intent(MyListings.this, ViewBidsActivity.class);
        intent.putExtra("listingId", listing.getListingID());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {

    }
}