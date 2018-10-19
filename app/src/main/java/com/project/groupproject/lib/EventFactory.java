package com.project.groupproject.lib;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.groupproject.models.Event;

public class EventFactory {
    public static final String COLLECTION = "events";

    /**
     * create event
     * @param event
     * @param listener
     */
    public static void createEvent(Event event, OnSuccessListener<DocumentReference> listener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection(COLLECTION).add(event.toMap())
                .addOnSuccessListener(listener);
    }

    /**
     * Get event collection ref
     * ref can use to query data
     */
    public static CollectionReference getEvents(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        return firestore.collection(COLLECTION);

    }
}
