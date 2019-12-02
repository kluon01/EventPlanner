package com.example.eventplanner.model;

public class User {
    private String name;
    private String email;
    //private String uid;

    public User(){

    }

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }*/
}
