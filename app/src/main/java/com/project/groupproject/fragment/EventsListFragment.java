package com.project.groupproject.fragment;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project.groupproject.R;
import com.project.groupproject.adapters.ListEventsAdapter;
import com.project.groupproject.models.Event;
import com.project.groupproject.viewmodals.EventsListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsListFragment extends Fragment {

    ListView eventsView;
    ArrayList<Event> eventsList;
    ListEventsAdapter adapter;
    EventsListViewModel viewModel;

    public EventsListFragment() {
        // Required empty public constructor
    }

    static public EventsListFragment getInstance() {
        return new EventsListFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);

        String[] title = new String[]{"Neighbor Community Potluck", "Andre Nickatina", "DON DIABLO", "DIM SUM Making", "My BizDay Vancouver"};
        String[] description = new String[]{"Date: Tue, 16 Oct 2018\nTime: 6:00pm – 8:00pm",
                "Date: Fri, 16 Nov 2018\nTime: 9:00pm – 1:00am",
                "Date: Sun, 11 Nov 2018\nTime: 8:00pm - 1:00am",
                "Date: Wed, 21 Nov 2018\nTime: 6:30pm – 9:00pm",
                "Date: Thu, 18 Oct 2018\nTime: 10:00am – 5:00pm"};

        eventsView = view.findViewById(R.id.listView);

        // demo only, add static event

        eventsList = Event.seedEvents();

        //pass results to listViewAdapter class
        adapter = new ListEventsAdapter(getContext(), eventsList);

        //bind the adapter to the listview
        eventsView.setAdapter(adapter);

        // query
        viewModel = ViewModelProviders.of(getActivity()).get(EventsListViewModel.class);

        // watch the event data
        final LiveData<List<Event>> eventList = viewModel.getEventList();
        eventList.observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                eventsList.clear();
                eventsList.addAll(events);
                adapter.notifyDataSetChanged();
            }
        });

        // query
        viewModel.queryEvents();

        return view;
    }

}
