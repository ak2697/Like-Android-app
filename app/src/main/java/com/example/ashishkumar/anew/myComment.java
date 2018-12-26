package com.example.ashishkumar.anew;

public class myComment {

    String username;
    String comment;
    String id;
    String idt;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String email;
    public myComment() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIdt() {
        return idt;
    }

    public void setIdt(String idt) {
        this.idt = idt;
    }

    public String getId() {
        return id;

    }

    public void setId(String id) {
        this.id = id;
    }

    public myComment(String username, String comment, String id,String idt,String email) {

        this.username = username;
        this.comment = comment;
        this.id = id;
        this.idt=idt;
        this.email=email;
    }
}
