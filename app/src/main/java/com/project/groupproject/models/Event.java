package com.project.groupproject.models;

import java.util.HashMap;
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
