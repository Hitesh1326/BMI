package com.bmi.hitesh.bmi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BMIActivity extends AppCompatActivity {

    TextView tvWelcome;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseauth;
    FirebaseUser user;
    Button btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        btnLogout = (Button)findViewById(R.id.btnLogout);
        firebaseauth = FirebaseAuth.getInstance();


//Temporary logout button, afterwards we will move it into menu !
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseauth.signOut();
                startActivity(new Intent(BMIActivity.this, LoginActivity.class));
                finish();


            }
        });




    }


}

