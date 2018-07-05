package com.example.shindehitesh.bmi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BMIActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    TextView tvWelcome, tvLocation, tvTemp;
    GoogleApiClient gac;
    Location loc;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button btnLogout, btnCalculate,btnHistory;
    SharedPreferences sp, sp1;
    Spinner spnFeet, spnInch;
    EditText etWeight;
    double height, heightf, heightin;
    static DatabaseSQL db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvTemp = (TextView) findViewById(R.id.tvTemp);
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addOnConnectionFailedListener(this);
        builder.addConnectionCallbacks(this);
        databaseReference = firebaseDatabase.getInstance().getReference("members/");

        gac = builder.addApi(AppIndex.API).build();
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnHistory = (Button)findViewById(R.id.btnHistory);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        firebaseAuth = FirebaseAuth.getInstance();
        spnFeet = (Spinner) findViewById(R.id.spnFeet);
        spnInch = (Spinner) findViewById(R.id.spnInch);
        etWeight = (EditText) findViewById(R.id.etWeight);
        sp = getSharedPreferences("p1", MODE_PRIVATE);
        sp1 = getSharedPreferences("p2", MODE_PRIVATE);
        String n = sp.getString("n", "");
        tvWelcome.setText("Welcome " + n);

        db = new DatabaseSQL(this);



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
                double hin;
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
                if (Objects.equals(etWeight.getText().toString(), "")) {
                    etWeight.setError("Invalid Weight");
                    etWeight.requestFocus();
                    return;
                } else {

                    int weight = Integer.parseInt(etWeight.getText().toString());
                    height = heightf + heightin;
                    float bmi_val = (float) ((weight) / (height * height));


                    if (bmi_val > 45) {
                        Toast.makeText(BMIActivity.this, "Invalid BMI Value ", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        int w = weight ;
                        String resu = String.format("%.02f", bmi_val);
                        //Storing into SQL
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd:MM:yyy");
                        SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss a");
                        String date = df.format(c.getTime());
                        String time = tf.format(c.getTime());
                        new DatabaseSQL(getApplicationContext()).addRecord(date,time,resu,w);

                        //Storing into Firebase
                        BmiResult bmiResult = new BmiResult(resu,w);
                        firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        databaseReference.child(user.getUid()).push().setValue(bmiResult);
                        SharedPreferences.Editor editor1 = sp1.edit();
                        editor1.putFloat("BMI", bmi_val);
                        editor1.commit();
                        startActivity(new Intent(BMIActivity.this, BMIResultActivity.class));
                    }
                }
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BMIActivity.this, ViewActivity.class));


            }
        });



    }


    //Creating Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about)

        {
            Toast.makeText(this, "App Developed By Hitesh", Toast.LENGTH_SHORT).show();

        }
        if (item.getItemId() == R.id.website) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://" + "www.google.com"));
            startActivity(i);
        }
        if (item.getItemId() == R.id.btnLogout){
            firebaseAuth.signOut();
            startActivity(new Intent(BMIActivity.this, LoginActivity.class));
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    //location
    @Override
    protected void onResume() {
        super.onResume();
        if (gac != null)
            gac.connect();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (gac !=null)
            gac.disconnect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        loc = LocationServices.FusedLocationApi.getLastLocation(gac);

        if (loc != null) {
            double lat = loc.getLatitude();
            double lon = loc.getLongitude();
            Geocoder g = new Geocoder(this, Locale.ENGLISH);
            try {
                List<Address> la = g.getFromLocation(lat, lon, 1);
                Address add = la.get(0);
                String msg = add.getLocality();
                tvLocation.setText(msg);
                //Temp
                String c = msg;
                String url = "https://api.openweathermap.org/data/2.5/weather?units=metric";
                String api = "0d9833baf8423e128062040fb88397b5";
                MyTask mt = new MyTask();
                mt.execute(url + "&q=" + c + "&appid=" +api);




            } catch (Exception e) {
                Toast.makeText(this, " Location Issue " + e, Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();

    }

    //Temperature working
    class MyTask extends AsyncTask<String, Void, Double> {
        double temp;
        @Override
        protected Double doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();
                InputStreamReader isr = new InputStreamReader(con.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String json = "", line = " ";
                while
                        ((line = br.readLine())!=null)
                {
                    json = json + line + "\n";
                }

                JSONObject j = new JSONObject(json);
                JSONObject p =j.getJSONObject("main");
                temp = p.getDouble("temp");
            }
            catch ( Exception e)
            {
                Toast.makeText(BMIActivity.this, "Issue" + e, Toast.LENGTH_SHORT).show();
            }
            return  temp;
        }


        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            tvTemp.setText("Temp " + aDouble);
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("Do you want to Exit ?");


        ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });


        ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });

        AlertDialog a = ab.create();
        a.setTitle("Exit");
        a.show();

    }


}
