package com.example.shindehitesh.bmi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    EditText etSignUpEmail, etSignUpPassword;
    Button btnRegister;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etSignUpEmail = (EditText)findViewById(R.id.etSignUpEmail);
        etSignUpPassword = (EditText)findViewById(R.id.etSignUpPassword);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        firebaseAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = etSignUpEmail.getText().toString();
                String p = etSignUpPassword.getText().toString();
                if (e.length() == 0 && p.length() == 0)
                {
                    etSignUpEmail.setError("Enter Email");
                    etSignUpPassword.setError("Enter Password");
                    etSignUpEmail.requestFocus();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(SignUpActivity.this, "Registered ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, PersonalActivity.class));
                            finish();
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
    }
}
