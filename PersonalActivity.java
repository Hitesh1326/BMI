package com.bmi.hitesh.bmi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PersonalActivity extends AppCompatActivity {

    EditText etName,etAge,etPhone;
    RadioGroup rgGender;
    Button  btnNext;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
  //Checking user is logged in or not .
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(PersonalActivity.this, LoginActivity.class));

        }
   //Binding work
        databaseReference = firebaseDatabase.getInstance().getReference();
        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        etPhone = (EditText) findViewById(R.id.etPhone);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        btnNext = (Button) findViewById(R.id.btnNext);

//Button Next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Members();
                startActivity(new Intent(PersonalActivity.this, BMIActivity.class));
                finish();
  }  });


    }//end of Oncreate
    
    
//One method called Members which is pushing data into data base with the current user id.
    public void Members(){
        String n = etName.getText().toString();
        String a = etAge.getText().toString();
        String p = etPhone.getText().toString();

        int id = rgGender.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) rgGender.findViewById(id);
        String r = rb.getText().toString();

        Members members = new Members(Integer.parseInt(a), p, n, r);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(members);

    }
}
