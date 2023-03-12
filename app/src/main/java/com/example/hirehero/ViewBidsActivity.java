package com.example.hirehero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewBidsActivity extends AppCompatActivity implements BidAdapter.OnViewClickListener {
    private RecyclerView mRecyclerView;
    private ArrayList<Bid> mBidsList;
    private BidAdapter mBidAdapter;
    private DatabaseReference mDatabase;
    private String mListingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bids);
        mRecyclerView = findViewById(R.id.bids);
        mListingId = getIntent().getStringExtra("listingId");
        Log.d("UID", "MID = "+ mListingId);
        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
        mDatabase = FirebaseDatabase.getInstance(url).getReference("Listings").child(mListingId).child("bids");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBidsList = new ArrayList<>();
        mBidAdapter = new BidAdapter(mBidsList, this);
        mRecyclerView.setAdapter(mBidAdapter);
        showBids();
    }

    private void showBids() {
       // Query query = mDatabase.orderByChild("listingID").equalTo(mListingId);
      //  query.addValueEventListener(new ValueEventListener() {
        mDatabase.orderByChild("bidAmount").addListenerForSingleValueEvent(new ValueEventListener() {
      //  mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mBidsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Bid bid = dataSnapshot.getValue(Bid.class);
                    mBidsList.add(bid);
                }
                mBidAdapter.notifyDataSetChanged();
                mDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewBidsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewClick(int position) {

    }
}