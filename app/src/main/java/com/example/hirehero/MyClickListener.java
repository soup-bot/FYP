package com.example.hirehero;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyClickListener implements View.OnClickListener {
    private servicelist activity;
    private EditText listingsearch;
    private DatabaseReference database;
    private ArrayList<Listing> list;
    private MyAdapter myAdapter;

    public MyClickListener(servicelist activity, EditText listingsearch, DatabaseReference database, ArrayList<Listing> list, MyAdapter myAdapter) {
        this.activity = activity;
        this.listingsearch = listingsearch;
        this.database = database;
        this.list = list;
        this.myAdapter = myAdapter;
    }

    @Override
    public void onClick(View view) {
        String searchText = listingsearch.getText().toString().toLowerCase();
        switch (view.getId()) {
            case R.id.searchButton:
                if (TextUtils.isEmpty(searchText)) {
                    // if search field is empty, display all listings
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