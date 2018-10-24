package com.project.groupproject.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class User implements Serializable{
    private String id;
    public String firstname;
    public String lastname;
    public String email;

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
