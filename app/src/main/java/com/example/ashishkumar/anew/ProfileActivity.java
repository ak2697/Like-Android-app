package com.example.ashishkumar.anew;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    EditText thoughts,un;

    Button post,world,upost;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar=findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        thoughts=findViewById(R.id.thoughts);
        post=findViewById(R.id.post);
        upost=findViewById(R.id.upost);
        world=findViewById(R.id.world);
        un=findViewById(R.id.un);


        databaseReference= FirebaseDatabase.getInstance().getReference("User");

        Intent in2=getIntent();
        in2.getFlags();
        mAuth=FirebaseAuth.getInstance();
        if(!mAuth.getCurrentUser().isEmailVerified())
            mAuth.getCurrentUser().sendEmailVerification();
        upost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().reload();

                if(!mAuth.getCurrentUser().isEmailVerified())
                {
                    Toast.makeText(getApplicationContext(),"Email Not Verified",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(ProfileActivity.this, uPostsView.class);
                    startActivity(intent);
                }
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().reload();
                if(!mAuth.getCurrentUser().isEmailVerified())
                {

                    Toast.makeText(getApplicationContext(),"Email Not Verified",Toast.LENGTH_SHORT).show();
                }
                else {
                    addPost();
                    un.setText("");
                    thoughts.setText("");
                }


            }
        });
        world.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().reload();
                if(!mAuth.getCurrentUser().isEmailVerified())
                {
                    Toast.makeText(getApplicationContext(),"Email Not Verified",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(ProfileActivity.this, wPostsView.class);
                    startActivity(i);
                }
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()== null)
        {
            finish();
            startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        }

    }

    private void load() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ml:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                break;
        }
        return true;
    }

    private void addPost() {
        String id=mAuth.getCurrentUser().toString();
        String t=thoughts.getText().toString().trim();
        String user=un.getText().toString().trim();
        if(user.isEmpty())
            user="Anonymus";
        if(t.isEmpty()) {
            thoughts.setError("Nothing to post :(");
            thoughts.requestFocus();
            return;
        }
        else
        {
            String idOfThought=databaseReference.push().getKey();

            User u=new User(id,idOfThought,user,t,mAuth.getCurrentUser().getEmail().toString(),0);

            databaseReference.child(idOfThought).setValue(u);
            Toast.makeText(getApplicationContext(),"Posted successfully :)",Toast.LENGTH_SHORT).show();
        }

    }
}
