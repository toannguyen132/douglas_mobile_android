package com.project.groupproject.viewmodals;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.groupproject.R;
import com.project.groupproject.models.Event;
import com.project.groupproject.models.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EventViewModel extends ViewModel {
    public static final String NAME = "events";

    private CollectionReference collection = FirebaseFirestore.getInstance().collection(NAME);
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private MutableLiveData<Event> event = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();

    public LiveData<Event> getEvent() {
        return event;
    }
    public LiveData<User> getUser() { return user; }
    public void setEvent(Event event) {
        this.event.setValue(event);
    }
    public void setUser(User user) {
        this.user.setValue(user);
    }

    public void fetchEvent(String id) {
        collection.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
            final Event fetchedEvent = Event.parseFromDocument(document);
            // event.setValue(fetchedEvent);

            // query user id
            if (fetchedEvent.uid != null){
                FirebaseFirestore.getInstance().collection(AuthUserViewModel.NAME).document(fetchedEvent.uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user.setValue(User.parseFromDocument(documentSnapshot));
                        event.setValue(fetchedEvent);
                    }
                });
            }
            }
        });
    }

    public void likeEvent(final String uid) {
        Event currentEvent = event.getValue();
        final String eventId = currentEvent.id;

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference eventRef = db.collection(NAME).document(eventId);
        final DocumentReference userRef = db.collection("users").document(uid);

        db.runTransaction(new Transaction.Function<Event>() {
            @Override
            public Event apply(Transaction transaction) throws FirebaseFirestoreException {
            Event tranEvent =  Event.parseFromDocument(transaction.get(eventRef));
            User tranUser = User.parseFromDocument(transaction.get(userRef));

            // validate if user liked event or not
            if (!tranEvent.likes.contains(uid)) {
                // event update
                tranEvent.num_like++;
                tranEvent.likes.add(uid);
                HashMap<String, Object> data = new HashMap<>();
                data.put("num_like", tranEvent.num_like);
                data.put("likes", tranEvent.likes);

                transaction.update(eventRef, data);

                // user update
                tranUser.liked_events.add(eventId);
                transaction.update(userRef, "liked_events", tranUser.liked_events);

                return tranEvent;
            } else {
                throw new FirebaseFirestoreException("You have already liked event", FirebaseFirestoreException.Code.ABORTED);
            }
            }
        }).addOnSuccessListener(new OnSuccessListener<Event>() {
            @Override
            public void onSuccess(Event event) {
                setEvent(event);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(NAME, "transaction failed", e);
            }
        });
    }

    public void unlike(final String uid) {
        Event currentEvent = event.getValue();
        final String eventId = currentEvent.id;

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference eventRef = db.collection(NAME).document(eventId);
        final DocumentReference userRef = db.collection("users").document(uid);

        db.runTransaction(new Transaction.Function<Event>() {
            @Override
            public Event apply(Transaction transaction) throws FirebaseFirestoreException {
            Event tranEvent =  Event.parseFromDocument(transaction.get(eventRef));
            User tranUser = User.parseFromDocument(transaction.get(userRef));

            // verify if user liked the event
            if ( tranEvent.num_like > 0 && tranEvent.likes.contains(uid)) {
                // event update
                tranEvent.num_like--;
                tranEvent.likes.remove(uid);
                HashMap<String, Object> data = new HashMap<>();
                data.put("num_like", tranEvent.num_like);
                data.put("likes", tranEvent.likes);

                transaction.update(eventRef, data);

                // user update
                tranUser.liked_events.remove(eventId);
                transaction.update(userRef, "liked_events", tranUser.liked_events);

                return tranEvent;
            } else {
                throw new FirebaseFirestoreException("You didn't like event yet", FirebaseFirestoreException.Code.ABORTED);
            }

            }
        }).addOnSuccessListener(new OnSuccessListener<Event>() {
            @Override
            public void onSuccess(Event event) {
                setEvent(event);
            }
        });
    }

    static public Task<DocumentReference> createEvent(Event event) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        return firestore.collection(NAME).add(event);
    }

    static public Task<Void> createEvent(String id, Event event) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        event.id = id;

        return firestore.collection("events").document(id)
                .set(event.toMap());
    }

}
