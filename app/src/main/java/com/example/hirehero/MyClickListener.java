package com.example.hirehero;

import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyClickListener implements View.OnClickListener {
    private servicelist activity;
    private Spinner listingsearch;
    private DatabaseReference database;
    private ArrayList<Listing> list;
    private ServiceAdapter myAdapter;

    public MyClickListener(servicelist activity, Spinner listingsearch, DatabaseReference database, ArrayList<Listing> list, ServiceAdapter myAdapter) {
        this.activity = activity;
        this.listingsearch = listingsearch;
        this.database = database;
        this.list = list;
        this.myAdapter = myAdapter;
    }

    @Override
    public void onClick(View view) {
        String searchText = listingsearch.getSelectedItem().toString().toLowerCase();
        switch (view.getId()) {
            case R.id.searchButton:
                if (searchText.equals("all services")) {
                    showAllListings();
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
}