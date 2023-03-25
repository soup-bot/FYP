package com.example.hirehero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {
    CardView viewmylist, bidonservice, listaservice, viewmybids;
    ImageButton logout;
    private FirebaseAuth mFirebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mFirebaseAuth = FirebaseAuth.getInstance();

        viewmylist = (CardView) findViewById(R.id.viewmylist);
        bidonservice = (CardView) findViewById(R.id.bidonservice);
        listaservice = (CardView) findViewById(R.id.listaservice);
        viewmybids = (CardView) findViewById(R.id.viewmybids);
        logout = (ImageButton) findViewById(R.id.logoutButton);


        viewmylist.setOnClickListener(this);
        bidonservice.setOnClickListener(this);
        listaservice.setOnClickListener(this);
        viewmybids.setOnClickListener(this);
        logout.setOnClickListener(this);

        //get users name from database and greet the user
        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        String userId = currentUser.getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance(url).getReference("Users").child(userId);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String fullName = snapshot.child("name").getValue(String.class);
                    //split name and only extract the first name to greet user with
                    String firstName = fullName.split(" ")[0];
                    TextView welcomeText = findViewById(R.id.dashboardtext);
                    welcomeText.setText("Hello, " + firstName + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error handling
            }
        });


}

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.listaservice:
                startActivity(new Intent(this,ListService.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.bidonservice:
                startActivity(new Intent(this, servicelist.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.viewmylist:
                startActivity(new Intent(this, MyListings.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.viewmybids:
                startActivity(new Intent(this, MyBids.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.logoutButton:
                mFirebaseAuth.signOut();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finishAffinity();
                break;

        }
    }
}