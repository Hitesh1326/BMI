package com.example.shindehitesh.bmi;

import android.content.Intent;
import android.content.SharedPreferences;
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
    Button btnNext;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

//Checking
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(PersonalActivity.this, LoginActivity.class));

        }

        databaseReference = firebaseDatabase.getInstance().getReference("members");

        //Binding
        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        etPhone = (EditText) findViewById(R.id.etPhone);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        btnNext = (Button) findViewById(R.id.btnNext);
        sp = getSharedPreferences("p1", MODE_PRIVATE); //For saving name


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = etName.getText().toString();
                String a = etAge.getText().toString();
                String p = etPhone.getText().toString();
                int id = rgGender.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) rgGender.findViewById(id);
                String r = rb.getText().toString();

                if (n.length() == 0 & a.length() == 0 & p.length()==0)
                {
                    etName.setError("Enter Name");
                    etAge.setError("Enter Age");
                    etPhone.setError("Enter Phone Number");
                    etName.requestFocus();
                    return;
                }
                SharedPreferences.Editor editor = sp.edit(); //for saving Data in internal memory
                editor.putString("n", n);
                editor.putString("a",a);
                editor.putString("p",p);
                editor.putString("r",r);
                editor.commit();
                Members();
                startActivity(new Intent(PersonalActivity.this, BMIActivity.class));
                finish();




            }
        });
    }
    //for firebase
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
