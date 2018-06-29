package com.bmi.hitesh.bmi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BMIResultActivity extends AppCompatActivity {

    TextView tvBMIValue,tvUnder,tvNormal,tvOver,tvObese;
    SharedPreferences sp,sp1;
    Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmiresult);

        tvBMIValue = (TextView)findViewById(R.id.tvBMIValue);
        tvUnder = (TextView)findViewById(R.id.tvUnder);
        tvNormal = (TextView)findViewById(R.id.tvNormal);
        tvOver = (TextView)findViewById(R.id.tvOver);
        tvObese = (TextView)findViewById(R.id.tvObese);
        sp1 = getSharedPreferences("p2",MODE_PRIVATE);
        btnShare = (Button)findViewById(R.id.btnShare);
        sp = getSharedPreferences("p1",MODE_PRIVATE);



        final float bmi_val = sp1.getFloat("BMI",0);
        final String re = String.format("%.02f", bmi_val);
        final String bmishow;

            if (bmi_val<18.5)
            {
                tvBMIValue.setText("Your BMI is " + re + "and you are Underweight");
                tvBMIValue.setTextColor(Color.parseColor("#A71931"));
                bmishow = "Your BMI is " + re + " and you are Underweight";

                tvUnder.setText("Below 18.5 is Underweight");
                tvUnder.setTextColor(Color.parseColor("#A71931"));
                tvNormal.setText("Between 18 to 25 is Normal");
                tvOver.setText("Between 25 to 30 is Overweight");
                tvObese.setText("More then 30 is Obese");

            }
        else if (bmi_val>=18.5 && bmi_val<=25)
            {
                tvBMIValue.setText("Your BMI is " + re + " and you are Normal");
                tvBMIValue.setTextColor(Color.parseColor("#A71931"));
                bmishow = "Your BMI is " + re + " and you are Normal";

                tvUnder.setText("Below 18 is Underweight");
                tvNormal.setText("Between 18.5 to 25 is Normal");
                tvNormal.setTextColor(Color.parseColor("#A71931"));
                tvOver.setText("Between 25 to 30 is Overweight");
                tvObese.setText("More then 30 is Obese");
            }
        else if (bmi_val>=25.01 && bmi_val<=29.9 )
            {
                tvBMIValue.setText("Your BMI is " + re + " and you are Overweight");
                tvBMIValue.setTextColor(Color.parseColor("#A71931"));
                bmishow = "Your BMI is " + re + " and you are Overweight";

                tvUnder.setText("Below 18 is Underweight");
                tvNormal.setText("Between 18 to 25 is Normal");
                tvOver.setText("Between 25 to 30 is Overweight");
                tvOver.setTextColor(Color.parseColor("#A71931"));
                tvObese.setText("More then 30 is Obese");
            }
        else
            {
                tvBMIValue.setText("Your BMI is " + re + " and you are Obese");
                tvBMIValue.setTextColor(Color.parseColor("#A71931"));
                bmishow = "Your BMI is " + re + " and you are Obese";

                tvUnder.setText("Below 18 is Underweight");
                tvNormal.setText("Between 18 to 25 is Normal");
                tvOver.setText("Between 25 to 30 is Overweight");
                tvObese.setText("More then 30 is Obese");
                tvObese.setTextColor(Color.parseColor("#A71931"));
            }

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp = getSharedPreferences("p1", MODE_PRIVATE);
                String n = sp.getString("n","");
                String a = sp.getString("a","");
                String p = sp.getString("p","");
                String r = sp.getString("r", "");


                String msg = "Name =" +n+ "\n" + "Age =" +a+ "\n" + "Phone =" +p+ "\n" + "Gender =" +r+ "\n" + "Details =" +bmishow ;

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, msg);
                startActivity(i);




            }
        });





    }
}
