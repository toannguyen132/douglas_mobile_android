package com.project.groupproject.viewmodals;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.groupproject.models.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EventsListViewModel extends ViewModel {
    public static final String NAME = "events";

    private CollectionReference collection = FirebaseFirestore.getInstance().collection(NAME);

    private MutableLiveData<List<Event>> eventList = new MutableLiveData<>();
    public LiveData<List<Event>> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> events) {
        this.eventList.setValue(events);
    }

    public void filterEvent(String query) {
        long today = (new Date()).getTime();

        collection.whereArrayContains("tags", query).whereGreaterThan("start_date", today).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Event> events = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Map<String, Object> data =  document.getData();
                        Event event = document.toObject(Event.class);
                        event.id = document.getId();
                        events.add(event);
                        Log.d("event id ", document.getId());
                    }
                    eventList.setValue(events);
                }
            }
        });
    }

    /** database **/
    public void queryEvents(){
        long today = (new Date()).getTime();

        collection.whereGreaterThan("start_date", today).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Event> events = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Map<String, Object> data =  document.getData();
                        Event event = document.toObject(Event.class);
                        event.id = document.getId();
                        events.add(event);
                        Log.d("event id ", document.getId());
                    }
                    eventList.setValue(events);
                }
            }
        });
    }
}
