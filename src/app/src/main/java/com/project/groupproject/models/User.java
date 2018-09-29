package com.project.groupproject.models;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {

    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;
    String FirstName;
    String LastName;
    String UID;

    public static User getInstance(String UID){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return new User();
    }
}
