package com.bmi.hitesh.bmi;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {


    EditText etForgetEmail;
    Button btnForgetEmail;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        etForgetEmail = (EditText) findViewById(R.id.etForgetEmail);
        btnForgetEmail = (Button) findViewById(R.id.btnForgetEmail);
        firebaseAuth = FirebaseAuth.getInstance();



        btnForgetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = etForgetEmail.getText().toString();
                if (e.length() == 0)
                {
                    etForgetEmail.setError("Enter Email");
                    etForgetEmail.requestFocus();
                    return;
                }
                firebaseAuth.sendPasswordResetEmail(e).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgetActivity.this, "Check your Email", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ForgetActivity.this, "Error " + task.getException() , Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });


    }
}
