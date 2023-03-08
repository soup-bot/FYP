package com.example.hirehero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {
    CardView viewmylist, bidonservice, listaservice, viewmybids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();

        viewmylist = (CardView) findViewById(R.id.viewmylist);
        bidonservice = (CardView) findViewById(R.id.bidonservice);
        listaservice = (CardView) findViewById(R.id.listaservice);
        viewmybids = (CardView) findViewById(R.id.viewmybids);

        viewmylist.setOnClickListener(this);
        bidonservice.setOnClickListener(this);
        listaservice.setOnClickListener(this);
        viewmybids.setOnClickListener(this);



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
        }
    }
}