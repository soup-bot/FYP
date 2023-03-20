package com.example.hirehero;

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
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

        }
    }
}