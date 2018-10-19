package com.project.groupproject.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Event {
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

    public Event() {
    }

    public Event(String uid, String name, String description, String location, long start_date, long end_date) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.location = location;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public void generateCoordinate(Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocationName(this.location, 1);
        if (addresses.size() > 0){
            this.lat = addresses.get(0).getLatitude();
            this.lng = addresses.get(0).getLatitude();
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

        return result;
    }
}
