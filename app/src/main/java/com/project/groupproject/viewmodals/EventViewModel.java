package com.project.groupproject.viewmodals;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.project.groupproject.models.Event;

import java.util.List;

public class EventViewModel extends ViewModel {

    private MutableLiveData<Event> event = new MutableLiveData<>();
    private MutableLiveData<List<Event>> eventList = new MutableLiveData<>();

    public LiveData<Event> getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event.setValue(event);
    }

    public LiveData<List<Event>> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> events) {
        this.eventList.setValue(events);
    }

}
