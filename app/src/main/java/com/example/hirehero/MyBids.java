package com.example.hirehero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyBids extends AppCompatActivity implements BidAdapter.OnViewClickListener, BidAdapter.OnDeleteClickListener {
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

        //  mDatabase = FirebaseDatabase.getInstance(url).getReference("Listings").child(mListingId).child("bids");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBidsList = new ArrayList<>();
        mBidAdapter = new BidAdapter(mBidsList, this, this);
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
                    mListingId = listingId;
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

    @Override
    public void onDeleteClick(int position) {
        // Get the bid that was clicked
        Bid bidToDelete = mBidsList.get(position);

        // Get a reference to the bids node for the listing
        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
        DatabaseReference bidsRef = FirebaseDatabase.getInstance(url).getReference("Listings").child(bidToDelete.getListingid()).child("bids");

        // Query the bids node to find the bid with the same bidId as the one to delete
        Query deleteQuery = bidsRef.orderByChild("bidid").equalTo(bidToDelete.getBidid());

        // Add a listener to the query to delete the bid when the data is loaded
        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot bidSnapshot : snapshot.getChildren()) {
                    bidSnapshot.getRef().removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Remove the bid from the list and update the RecyclerView
                                    mBidsList.remove(position);
                                    mBidAdapter.notifyItemRemoved(position);
                                    mBidAdapter.notifyItemRangeChanged(position, mBidsList.size());
                                    Log.d("onDeleteClick", "Bid removed from RecyclerView");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MyBids.this, "Failed to delete bid: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("onDeleteClick", "Failed to delete bid: " + e.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyBids.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("onDeleteClick", "Failed to query bids node: " + error.getMessage());
            }
        });
    }
}
