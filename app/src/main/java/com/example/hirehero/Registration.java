package com.example.hirehero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity implements View.OnClickListener {
    private LottieAnimationView registerUser;
    private TextView banner;
    private EditText editTextTextEmail, editTextTextPassword, editTextTextName;
    private FirebaseAuth mAuth;


//animation when phone back button is pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

//on create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        banner = (TextView) findViewById(R.id.hirehero2);
        banner.setOnClickListener(this);

        registerUser = (LottieAnimationView) (findViewById(R.id.registerbutton));
        registerUser.setOnClickListener(this);

        editTextTextEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
        editTextTextName = (EditText) findViewById(R.id.editTextTextPersonName);
    }

//button actions
    @Override
    public void onClick(View view) {
        switch (view.getId()){
        case R.id.hirehero2:
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            break;

        case R.id.registerbutton:
            registerUser();
            break;
    }

    }
   // validate email, name and password for user registration
    private void registerUser() {
        String email = editTextTextEmail.getText().toString().trim();
        String password = editTextTextPassword.getText().toString().trim();
        String name = editTextTextName.getText().toString().trim();
        float rating = 0;
        int numOfRatings=0;

        if(name.isEmpty()){
            editTextTextName.setError("Name is required");
            editTextTextName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextTextEmail.setError("Email is required");
            editTextTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextTextEmail.setError("Invalid Email");
            return;
        }
        if(password.isEmpty()){
            editTextTextPassword.setError("Password is required");
            editTextTextPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            editTextTextPassword.setError("Password length must be atleast 6 characters");
            editTextTextPassword.requestFocus();
            return;
        }



        //user account registration to firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){
                        User user = new User(name,email,rating, numOfRatings);
                        String url = "https://hirehero-386df-default-rtdb.asia-southeast1.firebasedatabase.app";
                        FirebaseDatabase.getInstance(url).getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()){
                                        //if registration successful
                                        Toast.makeText(Registration.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                    }else{
                                        //else
                                        Toast.makeText(Registration.this, "Registration Failed", Toast.LENGTH_LONG).show();
                                    }

                                });

                    }else{
                        Toast.makeText(Registration.this, "Registration Failed", Toast.LENGTH_LONG).show();
                    }
                });



    }

}