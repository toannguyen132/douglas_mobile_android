package com.project.groupproject;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.project.groupproject.adapters.ListEventsAdapter;
import com.project.groupproject.models.Event;
import com.project.groupproject.viewmodals.EventsListViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserEventsListActivity extends AppCompatActivity implements Observer<List<Event>>{

    ListView eventsListView;
    ListEventsAdapter adapter;
    ArrayList<Event> eventsList;
    EventsListViewModel viewModel;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_events_list);

        // prepare view
        eventsListView = findViewById(R.id.events_list);


        // prepare adapter
        eventsList = new ArrayList<>();
        adapter = new ListEventsAdapter(this, eventsList);

        //bind the adapter to the listview
        eventsListView.setAdapter(adapter);

        // get viewmodel
        viewModel = ViewModelProviders.of(this).get(EventsListViewModel.class);
        viewModel.getEventList().observe(this, this);

        // get user id from bundle
        Bundle bundle = getIntent().getExtras();
        String uid = bundle.getString("uid");

        if (uid != null) {
            viewModel.queryUserEvents(uid);
        } else {
            Toast.makeText(this, "User ID is not found", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onChanged(@Nullable List<Event> events) {
        eventsList.addAll(events);
        adapter.notifyDataSetChanged();
    }
}
