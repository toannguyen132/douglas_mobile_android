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
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

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

        eventsView = view.findViewById(R.id.listView);

        eventsList = new ArrayList<>();

        //pass results to listViewAdapter class
        adapter = new ListEventsAdapter(getContext(), eventsList);

        //bind the adapter to the listview
        eventsView.setAdapter(adapter);

        // query
        viewModel = ViewModelProviders.of(getActivity()).get(EventsListViewModel.class);

        //Toolbar
        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.drawable.header_bg);

        //Input_search
        final TextView inputSearch = view.findViewById(R.id.input_search);
        inputSearch.setAllCaps(true);

        //loading bar
        final ProgressBar loadingBar = getActivity().findViewById(R.id.loading_bar);
        loadingBar.setVisibility(View.VISIBLE);

        // watch the event data
        final LiveData<List<Event>> eventList = viewModel.getEventList();
        eventList.observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                eventsList.clear();
                eventsList.addAll(events);
                adapter.notifyDataSetChanged();
                loadingBar.setVisibility(View.GONE);
            }
        });

        // query
        viewModel.queryEvents();

        return view;
    }

    public void applyFilter(String s) {
        viewModel.filterEvent(s);
    }

}
