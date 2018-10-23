package com.project.groupproject.viewmodals;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.project.groupproject.models.Event;

public class EventViewModel extends ViewModel {

    private MutableLiveData<Event> event = new MutableLiveData<>();

    public LiveData<Event> getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event.setValue(event);
    }

}
