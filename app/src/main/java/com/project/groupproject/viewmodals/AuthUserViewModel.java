package com.project.groupproject.viewmodals;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.groupproject.models.Event;
import com.project.groupproject.models.User;

import java.util.ArrayList;
import java.util.List;

public class AuthUserViewModel extends ViewModel {
    public static final String NAME = "users";
    public static final String EVENT_NAME = "events";
    private CollectionReference collection = FirebaseFirestore.getInstance().collection(NAME);
    private CollectionReference collectionEvent = FirebaseFirestore.getInstance().collection(EVENT_NAME);

    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<List<Event>> createdEvents = new MutableLiveData<>();
    private final MutableLiveData<List<Event>> likedEvents = new MutableLiveData<>();

    public void setUser(User user) {
        this.user.setValue(user);
    }
    public void setCreatedEvents(List<Event> events) {
        this.createdEvents.setValue(events);
    }
    public void setLikedEvents(List<Event> events) {
        this.likedEvents.setValue(events);
    }

    public LiveData<User> getUser() {
        return user;
    }
    public LiveData<List<Event>> getCreatedEvents() {
        return createdEvents;
    }
    public LiveData<List<Event>> getLikedEvents() {
        return likedEvents;
    }

    /**
     * public functions
     */

    public void fetchUser(final String uid) {
        collection.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(final DocumentSnapshot document) {
                User user = document.toObject(User.class);
                user.setId(document.getId());
                setUser(user);

                collectionEvent.whereEqualTo("uid", uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        List<Event> events = new ArrayList<>();
                        for (DocumentSnapshot document :  documentSnapshots.getDocuments()) {
                            events.add(Event.parseFromDocument(document));
                        }
                        setCreatedEvents(events);
                    }
                });

                collectionEvent.whereArrayContains("likes", uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        List<Event> events = new ArrayList<>();
                        for (DocumentSnapshot document :  documentSnapshots.getDocuments()) {
                            events.add(Event.parseFromDocument(document));
                        }
                        setLikedEvents(events);
                    }
                });
            }
        });
    }

    public void fetchCurrentUser() {
        this.fetchUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public void saveUser(String uid, User user){
        final User newUser = user;
        collection.document(uid).set(user);
    }

    public void saveCurrentUser(User user){
        User currentUser = this.user.getValue();
        saveUser(currentUser.getId(), user);
    }


}
