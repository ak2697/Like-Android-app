package com.example.ashishkumar.anew;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class wPostsView extends AppCompatActivity {
    ListView list;
    private DatabaseReference databaseReference;
    private DatabaseReference comment;
    List<User> postList;
    int count=0,n;
    private boolean like=false;
    private DatabaseReference likeData;
    ImageView image;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_w_posts_view);
        Intent i=getIntent();
        list=findViewById(R.id.wPosts);
        databaseReference= FirebaseDatabase.getInstance().getReference("User") ;
        postList=new ArrayList<>();
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        comment=FirebaseDatabase.getInstance().getReference("User").child("Comment");

        likeData=FirebaseDatabase.getInstance().getReference("Like");
        likeData.keepSynced(true);
        databaseReference.keepSynced(true);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User cUser=postList.get(position);
                Intent com=new Intent(wPostsView.this,CommentsView.class);
                com.putExtra("Id",cUser.getIdt());
                com.putExtra("Email",cUser.getIduser());

                startActivity(com);
            }
        });

       list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final User user=postList.get(position);
                final DatabaseReference databaseReferenc=FirebaseDatabase.getInstance().getReference("User")
                        .child(user.getIdt());
                final String post_key=postList.get(position).getIdt();

                //likeData=FirebaseDatabase.getInstance().getReference("User").child(post_key).child("Like");
                //likeData.keepSynced(true);
                image=view.findViewById(R.id.like);
                likeData.keepSynced(true);
                like=true;



                likeData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(like)
                        {
                           if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid()))
                           {
                                likeData.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                like=false;
                                user.noOfLikes--;

                               image.setImageResource(R.drawable.dislike);


                           }
                           else
                           {
                               likeData.child(post_key).child(mAuth.getCurrentUser().
                                       getUid()).setValue(mAuth.getCurrentUser().getEmail());
                               like=false;

                                user.noOfLikes++;


                               image.setImageResource(R.drawable.like);

                           }
                            databaseReference.child(user.getIdt()).setValue(user);


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                return true;
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    User users=postSnapshot.getValue(User.class);
                    postList.add(users);


                }
                wPosts adapter=new wPosts(wPostsView.this,postList);
                list.setAdapter(adapter);

                Collections.reverse(postList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
  }
