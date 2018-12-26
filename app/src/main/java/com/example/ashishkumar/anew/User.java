package com.example.ashishkumar.anew;

import android.provider.ContactsContract;

public class User {
    String id,un,t,idt;
    String iduser;
    int noOfLikes;
    User()
    {

    }



    User(String idd, String idOfT, String username, String thoughts, String email,int nl)
        {
        id=idd;
        un=username;
        t=thoughts;
        iduser=email;
        idt=idOfT;
        noOfLikes=nl;

    }
    public String getIduser() {
        return iduser;
    }

    public int getNoOfLikes() {
        return noOfLikes;
    }

    public String getId() {
        return id;

    }

    public String getUn() {
        return un;
    }
    public String getT() {
        return t;
    }

    public String getIdt() {
        return idt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public void setT(String t) {
        this.t = t;
    }

    public void setIdt(String idt) {
        this.idt = idt;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public void setNoOfLikes(int noOfLikes) {
        this.noOfLikes = noOfLikes;
    }
}
