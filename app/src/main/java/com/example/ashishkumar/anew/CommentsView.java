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
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentsView extends AppCompatActivity {
    ListView listView;
    DatabaseReference commentDatabase;
    List<myComment> commentList;
    FirebaseAuth mAuth;
    EditText un,comnts;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_view);
        Intent com=getIntent();
        final String idt=com.getStringExtra("Id");
        listView=findViewById(R.id.cmntList);
        commentDatabase= FirebaseDatabase.getInstance().getReference("Comment") ;
        commentList=new ArrayList<>();
        mAuth=FirebaseAuth.getInstance();
        un=findViewById(R.id.unC);
        comnts=findViewById(R.id.cmnt);
        Button post=findViewById(R.id.postCmnt);


        mAuth=FirebaseAuth.getInstance();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addComment(idt);
                un.setText("");
                comnts.setText("");
            }
        });

    }


    private void addComment(String idt) {
        String un1=un.getText().toString().trim();
        String comnts1=comnts.getText().toString().trim();
        String id=commentDatabase.push().getKey();
        if(un1.isEmpty())
            un1="Anonymus";
        if(comnts1.isEmpty())
        {
            comnts.setError("Nothing to post :(");
            comnts.requestFocus();
            return;
        }
        Intent c=getIntent();
        String email=c.getStringExtra("Email");
        myComment mC=new myComment(un1,comnts1,id,idt,email);
        commentDatabase.child(id).setValue(mC);
    }



    @Override
    protected void onStart() {
        super.onStart();

        commentDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent com=getIntent();
                String idt=com.getStringExtra("Id");
                commentList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    myComment coms=dataSnapshot1.getValue(myComment.class);
                    if(coms.getIdt().equals(idt))

                        commentList.add(coms);

                }
                comments adapter=new comments(CommentsView.this,commentList);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Intent c=getIntent();
        String email=c.getStringExtra("Email");
        if(email.equals(mAuth.getCurrentUser().getEmail()))
        {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myComment myC=commentList.get(position);
                upComment(myC);
            }


        });}
    }

    private void upComment(final myComment myC) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.comment_update,null);
        builder.setView(dialogView);
        final EditText updateName=dialogView.findViewById(R.id.ceUn);
        final EditText comment=dialogView.findViewById(R.id.ceCom);
        final Button update=dialogView.findViewById(R.id.updtCom);
        final Button delete=dialogView.findViewById(R.id.delCom);

        updateName.setText(myC.getUsername());
        comment.setText(myC.getComment());
        builder.setTitle("Update your comment");
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n=updateName.getText().toString().trim();
                String t=comment.getText().toString().trim();
                if(n.isEmpty())
                    n="Anonymus";
                if(t.isEmpty())
                {
                    comment.setError("Type in here :)");
                    comment.requestFocus();
                    return;
                }
                updatePost(n,t,myC);
                alertDialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Comment").child(myC.id);
                databaseReference.removeValue();
                alertDialog.dismiss();
                Toast.makeText(CommentsView.this, "Post deleted :)", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updatePost(String n,String t,myComment myC) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Comment").child(myC.id);
        Intent c=getIntent();
        String email=c.getStringExtra("Email");
        myComment my=new myComment(n,t, myC.id,myC.idt,email);
        databaseReference.setValue(my);
        Toast.makeText(getApplicationContext(),"Comment updated successfully",Toast.LENGTH_SHORT).show();
    }
}
