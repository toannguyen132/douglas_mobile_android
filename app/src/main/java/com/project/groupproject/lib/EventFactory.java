package com.project.groupproject.lib;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.groupproject.models.Event;

public class EventFactory {
    public static final String DB_NAME = "events";

    /**
     * create event
     * @param event
     * @param listener
     */
    public static void createEvent(Event event, OnSuccessListener<DocumentReference> listener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection(DB_NAME).add(event.toMap())
                .addOnSuccessListener(listener);
    }
}
