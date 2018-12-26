package com.example.ashishkumar.anew;

import android.content.Intent;
import android.os.PatternMatcher;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Signup extends AppCompatActivity {
    Button lin,sign;
    private FirebaseAuth mAuth;

    EditText mails,passs,confirmPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Intent in=getIntent();
        mAuth = FirebaseAuth.getInstance();
        lin=(Button)findViewById(R.id.lin);
        sign=(Button)findViewById(R.id.sign);

        mails=(EditText)findViewById(R.id.mails);
        passs=(EditText)findViewById(R.id.passs);
        confirmPass=findViewById(R.id.confirmPass);
        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent in1=new Intent(Signup.this, MainActivity.class);
                startActivity(in1);

            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email= mails.getText().toString().trim();
        String password= passs.getText().toString();
        String cPass=confirmPass.getText().toString();

        if(email.isEmpty())
        {
            mails.setError("Email address should not be empty.");
            mails.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            mails.setError("Invalid Email address.");
            mails.requestFocus();
            return;

        }
        if(password.isEmpty())
        {
            passs.setError("Passsword field should not be empty.");
            passs.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            passs.setError("Minimum length should be more than 6 characters.");
            passs.requestFocus();
            return;
        }
        if(!cPass.equals(password))
        {
            confirmPass.setError("Passwords dont match.");
            confirmPass.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    finish();
                Intent in2 = new Intent(Signup.this, Tutorial.class);

                startActivity(in2);
                }
                else
                {
                    if(task.getException()instanceof FirebaseAuthUserCollisionException)
                        Toast.makeText(getApplicationContext(),"You are already registered.",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }


            }
        });
    }
}
