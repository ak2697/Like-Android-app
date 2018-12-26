package com.example.ashishkumar.anew;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class comments extends ArrayAdapter<myComment> {
    private Activity context;
    private List<myComment> ucomments;
    public comments(Activity context, List<myComment> ucomments)
    {
        super(context,R.layout.comments,ucomments);
        this.context=context;
        this.ucomments=ucomments;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View item=inflater.inflate(R.layout.comments,null,true);
        TextView usernameCom=item.findViewById(R.id.usernameComment);
        TextView comment=item.findViewById(R.id.com);
        myComment c=ucomments.get(position);
        TextView un=item.findViewById(R.id.usernameComment);
        TextView com=item.findViewById(R.id.com);
        un.setText(c.getUsername());
        com.setText(c.getComment());
        return item;


    }
}
