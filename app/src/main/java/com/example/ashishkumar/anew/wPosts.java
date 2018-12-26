package com.example.ashishkumar.anew;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class wPosts extends ArrayAdapter<User> {
    private Activity context;
    private List<User> userPosts;


    public wPosts(Activity context, List<User> userPosts)
    {
        super(context,R.layout.activity_w_posts2,userPosts);
        this.context=context;
        this.userPosts=userPosts;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View item=inflater.inflate(R.layout.activity_w_posts2,null,true);
        TextView username=(TextView)item.findViewById(R.id.usern);
        TextView post=(TextView)item.findViewById(R.id.postt);
        final TextView nl=item.findViewById(R.id.nolike);
        final ImageView imageView=item.findViewById(R.id.like);
        final User use=userPosts.get(position);
        username.setText((use.getUn()));
        post.setText((use.getT()));
        nl.setText("   "+use.getNoOfLikes());
        DatabaseReference d=FirebaseDatabase.getInstance().getReference("Like");
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();

        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(use.getIdt()).hasChild(mAuth.getCurrentUser().getUid()))
                {
                    imageView.setImageResource(R.drawable.like);
                    nl.setText("   " + use.getNoOfLikes() + "   likes");
                }
                else {
                    imageView.setImageResource(R.drawable.dislike);
                    nl.setText("   " + use.getNoOfLikes() + "   likes     Long tap to like");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return item;

    }


}