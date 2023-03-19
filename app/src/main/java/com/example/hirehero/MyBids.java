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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyBids extends AppCompatActivity implements BidAdapter.OnViewClickListener {
    private RecyclerView mRecyclerView;
    private ArrayList<Bid> mBidsList;
    private BidAdapter mBidAdapter;
    private DatabaseReference mDatabase;
    private String mListingId;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_bids);
        mRecyclerView = findViewById(R.id.mybids);
        mListingId = getIntent().getStringExtra("listingId");
        Log.d("UID", "MID = "+ mListingId);

      //  mDatabase = FirebaseDatabase.getInstance(url).getReference("Listings").child(mListingId).child("bids");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBidsList = new ArrayList<>();
        mBidAdapter = new BidAdapter(mBidsList, this);
        mRecyclerView.setAdapter(mBidAdapter);
        showBids();

    }
    private DatabaseReference bidsRef;

    private void showBids() {
        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
        DatabaseReference listingsRef = FirebaseDatabase.getInstance(url).getReference("Listings");
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        listingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot listingSnapshot : snapshot.getChildren()) {
                    String listingId = listingSnapshot.getKey();
                    DatabaseReference bidsRef = FirebaseDatabase.getInstance(url).getReference("Listings").child(listingId).child("bids");
                    bidsRef.orderByChild("uid").equalTo(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot bidSnapshot : snapshot.getChildren()) {
                                Bid bid = bidSnapshot.getValue(Bid.class);
                                mBidsList.add(bid);
                            }
                            mBidAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MyBids.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyBids.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewClick(int position) {

    }
}