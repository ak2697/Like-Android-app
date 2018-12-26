package com.example.ashishkumar.anew;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class uPostsView extends AppCompatActivity {
    ListView list;
    DatabaseReference databaseReference;
    List<User> postList;
    FirebaseAuth mAuth;
    private boolean like=false;
    private DatabaseReference likeData;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_posts_view);
        Intent i=getIntent();
        list=findViewById(R.id.wPosts);
        databaseReference= FirebaseDatabase.getInstance().getReference("User") ;
        postList=new ArrayList<>();
        mAuth=FirebaseAuth.getInstance();
        likeData=FirebaseDatabase.getInstance().getReference("Like");
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User cUser=postList.get(position);
                Intent com=new Intent(uPostsView.this,CommentsView.class);
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
//                likeData.keepSynced(true);
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
                public void onDataChange(final DataSnapshot dataSnapshot) {
                User uu=new User("a","a","A","a","a",0);
                postList.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                   try {
                       User users = postSnapshot.getValue(User.class);

                    if((users.getIduser().equals(mAuth.getCurrentUser().getEmail().toString())))
                    postList.add(users);
                   }catch (Exception e)
                   {

                   }
                }
                uPosts adapter=new uPosts(uPostsView.this,postList);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User userk=postList.get(position);
                upDialog(userk.getId(),userk.getIdt(),userk.getUn(),userk.getT(),userk.getIduser(),userk.getNoOfLikes());


            }
        });

    }
    private void upDialog(final String idd, final String idOfT, String ne, String ut, final String email, final int nl)
    {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.update_dialog,null);
        builder.setView(dialogView);
        final EditText updateName=dialogView.findViewById(R.id.newName);
        final EditText thought=dialogView.findViewById(R.id.editThought);
        final Button update=dialogView.findViewById(R.id.update);
        final Button delete=dialogView.findViewById(R.id.delete);

        updateName.setText(ne);
        thought.setText(ut);
        builder.setTitle("Update your post");
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n=updateName.getText().toString().trim();
                String t=thought.getText().toString().trim();
                if(n.isEmpty())
                    n="Anonymus";
                if(t.isEmpty())
                {
                    thought.setError("Type in here :)");
                    thought.requestFocus();
                    return;
                }
               updatePost(idd,idOfT,n,t,email,nl);
                alertDialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("User").child(idOfT);
                databaseReference.removeValue();
                alertDialog.dismiss();
                Toast.makeText(uPostsView.this, "Post deleted :)", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void updatePost(String idd, String idOfT, String ne, String ut, String email,int nl){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("User").child(idOfT);
        User u=new User(idd,idOfT,ne,ut,email,nl);
        databaseReference.setValue(u);
        Toast.makeText(getApplicationContext(),"Post updated successfully",Toast.LENGTH_SHORT).show();
    }
}
