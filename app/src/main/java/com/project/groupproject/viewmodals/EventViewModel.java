package com.project.groupproject.viewmodals;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.groupproject.models.Event;
import com.project.groupproject.models.User;

public class EventViewModel extends ViewModel {
    public static final String NAME = "events";

    private CollectionReference collection = FirebaseFirestore.getInstance().collection(NAME);

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
                event.setValue(fetchedEvent);

                // query user id
                if (fetchedEvent.uid != null){
                    FirebaseFirestore.getInstance().collection(AuthUserViewModal.NAME).document(fetchedEvent.uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            user.setValue(User.parseFromDocument(documentSnapshot));
                        }
                    });
                }

            }
        });
    }

    static public Task<DocumentReference> createEvent(Event event) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        return firestore.collection(NAME).add(event.toMap());
    }
}
