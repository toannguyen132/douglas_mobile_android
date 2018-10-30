package com.project.groupproject.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Event implements Serializable {
    @Exclude
    public String id;

    public String uid;
    public String name;
    public String description;
    public String location;
    public double lng;
    public double lat;
    public long start_date;
    public long end_date;
    public long num_like;
    public long num_follow;
    public List<String> tags;

    String title;
    String desc;
    int icon;

    public Event() {
    }

    public Event(String uid, String name, String description, String location, long start_date, long end_date) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.location = location;
        this.start_date = start_date;
        this.end_date = end_date;
        //
        this.extractTags();
    }

    public void extractTags() {
        String[] tags = this.location.toLowerCase().replace(",", "").split(" ");
        this.tags = Arrays.asList(tags);
    }

    public void generateCoordinate(Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocationName(this.location, 1);
        if (addresses.size() > 0) {
            this.lat = addresses.get(0).getLatitude();
            this.lng = addresses.get(0).getLongitude();
        }
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("description", description);
        result.put("location", location);
        result.put("lng", lng);
        result.put("lat", lat);
        result.put("start_date", start_date);
        result.put("end_date", end_date);
        result.put("num_like", num_like);
        result.put("num_follow", num_follow);
        result.put("tags", tags);

        return result;
    }

    static public Event fromMap(Map<String, Object> data) {
        Event event = new Event();
        return event;
    }

    static public Event parseFromDocument(DocumentSnapshot document) {
        Event event = document.toObject(Event.class);
        event.id = document.getId();
        return event;
    }


    public String getTitle() {
        return this.title;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getIcon() {
        return this.icon;
    }

    public static ArrayList<Event> seedEvents(){
        ArrayList<Event> events = new ArrayList<Event>();

        Event event1 = new Event("1", "test", "test description", "location 1", new Date().getTime(), new Date().getTime());
        Event event2 = new Event("2", "test 2", "test description 2", "location 3", new Date().getTime(), new Date().getTime());

        events.add(event1);
        events.add(event2);
        events.add(event2);

        return events;
    }

}
