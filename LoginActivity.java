package com.bmi.hitesh.bmi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnForget, btnSignIn,btnSignUp;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnForget = (Button)findViewById(R.id.btnForget);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        
        //To Check whether the user is logged in or not 
        if (firebaseAuth.getCurrentUser() != null) {
            // User is signed in
            Intent i = new Intent(LoginActivity.this, BMIActivity.class);
           //ignore  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String e = etEmail.getText().toString();
                String p = etPassword.getText().toString();

                if (e.length() == 0 && p.length() == 0 )
                {
                    Toast.makeText(LoginActivity.this, "Please Enter Details ", Toast.LENGTH_SHORT).show();
                    etEmail.requestFocus();
                    return;

                }
                firebaseAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this, "SignIn Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, PersonalActivity.class));
                            finish();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "SignIn Error " + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }});



        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });


    }
}
