package com.project.groupproject.fragment;


import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.groupproject.MainActivity;
import com.project.groupproject.R;
import com.project.groupproject.adapters.ListEventsAdapter;
import com.project.groupproject.models.Event;
import com.project.groupproject.viewmodals.EventsListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsListFragment extends Fragment {

    ListView eventsView;
    ArrayList<Event> eventsList;
    ListEventsAdapter adapter;
    EventsListViewModel viewModel;
    EditText inputSearch;
    SwipeRefreshLayout swipeRefreshLayout;
    LocationManager locationManager;

    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double lng = location.getLongitude();
            double lat = location.getLatitude();
            Log.d("request_location", lat + ", " + lng);
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                String city = addresses.get(0).getLocality();
                String postalCode = addresses.get(0).getPostalCode().replace(" ", "");
                Toast.makeText(getActivity(), "city " + city, Toast.LENGTH_SHORT ).show();
                inputSearch.setText(city);
                applyFilter(city);
            } catch (IOException e) {

            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

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
        inputSearch = view.findViewById(R.id.input_search);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);


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
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // get current location
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        getLocation();

        // query
        viewModel.queryEvents();

        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE){
                    Log.d(MainActivity.TAG, "search here");
                    String searchQuery = inputSearch.getText().toString();
                    applyFilter(searchQuery);
                }
                return false;
            }
        });

        // swipe refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingBar.setVisibility(View.VISIBLE);
                viewModel.queryEvents();
            }
        });

        return view;
    }

    public void applyFilter(String s) {
        viewModel.filterEvent(s);
    }


    private void getLocation() {
        // request permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // create listener
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, listener);
        } else {
            // Permission is not granted request
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MainActivity.LOCATION_PERMISSION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MainActivity.LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Granted location permission", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, listener);
        }
    }


}
