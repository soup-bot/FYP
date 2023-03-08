package com.example.hirehero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class servicelist extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<Listing> list;
    EditText listingsearch;
    private Button searchButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_servicelist);
        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
        recyclerView = findViewById(R.id.servicelist);
        database = FirebaseDatabase.getInstance(url).getReference("Listings");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listingsearch = findViewById(R.id.listingsearch);
        list = new ArrayList<>();
        myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        showAllListings();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}