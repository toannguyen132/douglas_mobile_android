package com.project.groupproject.lib;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.groupproject.models.Event;

public class EventFactory {
    public static final String DB_NAME = "events";

    /**
     * create event
     * @param event
     * @param listener
     */
    public static void createEvent(Event event, DatabaseReference.CompletionListener listener) {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("events");

        String key = mReference.push().getKey();

        mReference.child(key).setValue(event.toMap(), listener);
    }
}
