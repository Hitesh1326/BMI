package com.bmi.hitesh.bmi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BMIActivity extends AppCompatActivity {

    TextView tvWelcome,tvResult;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Button btnLogout, btnCalculate;
    SharedPreferences sp,sp1;
    Spinner spnFeet,spnInch;
    EditText etWeight;
    double height, bmi_val, heightf, heightin;
    int weight;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tvResult = (TextView)findViewById(R.id.tvResult);
        btnLogout = (Button)findViewById(R.id.btnLogout);
        btnCalculate = (Button)findViewById(R.id.btnCalculate);
        firebaseAuth = FirebaseAuth.getInstance();
        spnFeet = (Spinner)findViewById(R.id.spnFeet);
        spnInch =(Spinner)findViewById(R.id.spnInch);
        etWeight = (EditText)findViewById(R.id.etWeight);
        sp = getSharedPreferences("p1", MODE_PRIVATE);
        sp1 = getSharedPreferences("p2", MODE_PRIVATE);
        String n = sp.getString("n", "");
        tvWelcome.setText("Welcome " + n);

// Creating Array list for Feet
        final ArrayList<String> feet = new ArrayList<>();
        feet.add("4");
        feet.add("5");
        feet.add("6");
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, feet);
        spnFeet.setAdapter(adapter);
        spnFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String fe = adapterView.getItemAtPosition(i).toString();
                double hf;
                hf = Double.parseDouble(fe);
                heightf = hf * 0.3048;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//Array list for inch
        final ArrayList<String> inch = new ArrayList<>();
        inch.add("0");
        inch.add("1");
        inch.add("2");
        inch.add("3");
        inch.add("4");
        inch.add("5");
        inch.add("6");
        inch.add("7");
        inch.add("8");
        inch.add("9");
        inch.add("10");
        inch.add("11");
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, inch);
        spnInch.setAdapter(adapter1);
        spnInch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
         String in = adapterView.getItemAtPosition(i).toString();
         double hin ;
             hin = Double.parseDouble(in);
            heightin = hin * 0.0254;

            }

                 @Override
                 public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });







        //calculation
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int weight = Integer.parseInt(etWeight.getText().toString());
                height = heightf + heightin;
                float bmi_val = (float)((weight) / (height * height));
                String r = String.format("%.02f",bmi_val);
                if ( bmi_val > 45)
                    Toast.makeText(BMIActivity.this, "Invalid ", Toast.LENGTH_SHORT).show();
                else
                {
                    SharedPreferences.Editor editor1 = sp1.edit();
                    editor1.putFloat("BMI", bmi_val);
                    editor1.commit();
                    startActivity(new Intent(BMIActivity.this, BMIResultActivity.class));
                }

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(BMIActivity.this, LoginActivity.class));
                finish();


            }
        });




    }






}

