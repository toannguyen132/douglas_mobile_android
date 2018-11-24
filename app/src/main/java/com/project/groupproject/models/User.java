package com.project.groupproject.models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable{
    private String id;
    public String firstname;
    public String lastname;
    public String email;
    public String avatar;
    public List<String> liked_events;

    public User() {
        liked_events = new ArrayList<>();
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static User parseFromDocument(DocumentSnapshot document) {
        User user = document.toObject(User.class);
        user.setId(document.getId());
        return user;
    }
}
