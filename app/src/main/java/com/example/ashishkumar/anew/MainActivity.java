package com.example.ashishkumar.anew;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText maill,passl;
    Button log,sup;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log=(Button)findViewById(R.id.log);
        sup=(Button)findViewById(R.id.sup);
        mAuth=FirebaseAuth.getInstance();
        maill=(EditText)findViewById(R.id.maill);
        passl=(EditText)findViewById(R.id.passl);
        Intent in1=getIntent();
        sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent in=new Intent(MainActivity.this,Signup.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    userLogin();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        }
    }

    private void userLogin() {
        String email=maill.getText().toString().trim();
        String password=passl.getText().toString();

        if(email.isEmpty())
        {
            maill.setError("Email address should not be empty.");
            maill.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            maill.setError("Invalid Email address.");
            maill.requestFocus();
            return;

        }
        if(password.isEmpty())
        {
            passl.setError("Passsword field should not be empty.");
            passl.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent in2=new Intent(MainActivity.this,ProfileActivity.class);
                    in2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    finish();
                    startActivity(in2);

                }
                else
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
