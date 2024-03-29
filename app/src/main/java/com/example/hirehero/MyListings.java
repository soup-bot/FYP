//This class is used to display only the user's own service listings to the user and allows them to be deleted or to view the bids

package com.example.hirehero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyListings extends AppCompatActivity implements ServiceAdapter.OnDeleteClickListener, BidAdapter.OnViewClickListener, View.OnClickListener{

    RecyclerView recyclerView2;
    DatabaseReference database;
    ServiceAdapter myAdapter;
    ArrayList<Listing> list;
    private ImageButton homebutton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);
        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
        recyclerView2 = findViewById(R.id.mylisting);
        database = FirebaseDatabase.getInstance(url).getReference("Listings");
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new ServiceAdapter(this, list,true,this,this);
        recyclerView2.setAdapter(myAdapter);

        homebutton = (ImageButton) findViewById(R.id.homeButton);
        homebutton.setOnClickListener(this::onClick);


        showCurrentUserListings();
    }

    //method to display current user's listings in db
    private void showCurrentUserListings(){
        list.clear();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = database.orderByChild("userID").equalTo(currentUserId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Listing listing = dataSnapshot.getValue(Listing.class);
                    //add listings to the list, where id = current uid (current users listings)
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
        //get position on the list to delete
        Listing listing = list.get(position);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (listing.getUserID().equals(currentUserId)) {
            //if listing belongs to the current user, allow them to delete it
            database.child(listing.getListingID()).removeValue();
            Toast.makeText(this, "Listing deleted", Toast.LENGTH_SHORT).show();
            list.remove(position);
            myAdapter.notifyDataSetChanged();
        } else {
            //otherwise dont allow deletion
            Toast.makeText(this, "You can only delete your own listings", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewClick(int position) {
        Listing listing = list.get(position);
        Intent intent = new Intent(MyListings.this, ViewBidsActivity.class);
        //pass listing id to viewbidsactivity
        intent.putExtra("listingId", listing.getListingID());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //when home button pressed, go back to userprofile
            case R.id.homeButton:
                startActivity(new Intent(this,UserProfile.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }
}