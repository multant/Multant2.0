package com.androstock.multant.ActiveDesk;

import com.google.firebase.auth.FirebaseUser;

public class User {
    FirebaseUser user;
    private String e_mail;

    public User(){
    }

    public User(FirebaseUser user){
        this.user = user;
    }
    public User(String e_mail){
        this.e_mail = e_mail;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getE_mail() {
        return e_mail;
    }
}
