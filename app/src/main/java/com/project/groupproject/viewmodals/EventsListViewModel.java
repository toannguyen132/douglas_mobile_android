package com.project.groupproject.viewmodals;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.groupproject.models.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EventsListViewModel extends ViewModel {
    public static final String NAME = "events";

    private CollectionReference collection = FirebaseFirestore.getInstance().collection(NAME);
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private MutableLiveData<List<Event>> eventList = new MutableLiveData<>();
    public LiveData<List<Event>> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> events) {
        this.eventList.setValue(events);
    }

    public void filterEvent(final String query) {
        long today = (new Date()).getTime();

        OnCompleteListener<QuerySnapshot> listener = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    final List<Event> events = new ArrayList<>();
                    String s = query.trim().toLowerCase();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Event event = Event.parseFromDocument(document);

                        // check condition here
                        if (Strings.isEmptyOrWhitespace(query)) {
                            events.add(event);
                        } else {
                            if (event.location.toLowerCase().indexOf(s) >= 0) {
                                events.add(event);
                            }
                        }

                        Log.d("event id ", document.getId());
                    }

                    eventList.setValue(events);
                }
            }
        };

        if (TextUtils.isEmpty(query)){
            collection.whereGreaterThan("start_date", today).get().addOnCompleteListener(listener);
        } else {
            collection.whereGreaterThan("start_date", today).get().addOnCompleteListener(listener);
        }


    }

    /** database **/
    public void queryEvents(){
        long today = (new Date()).getTime();

        collection.whereGreaterThan("start_date", today).orderBy("start_date", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Event> events = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Event event = Event.parseFromDocument(document);

                        events.add(event);
                        Log.d("event id ", document.getId());
                    }

                    eventList.setValue(events);
                }
            }
        });
    }

    public void queryUserEvents(String uid) {
        collection.whereEqualTo("uid", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
//                    setEventList();
                    List<Event> events = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()){
                        Event event = Event.parseFromDocument(document);
                        events.add(event);
                    }
                    eventList.setValue(events);
                }
            }
        });
    }
}
