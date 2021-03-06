package com.project.groupproject.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.project.groupproject.R;
import com.project.groupproject.adapters.ListEventsAdapter;
import com.project.groupproject.models.Event;
import com.project.groupproject.viewmodals.AuthUserViewModel;
import com.project.groupproject.viewmodals.EventsListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventLikedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventLikedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventLikedFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    ListView listView;
    ListEventsAdapter adapter;
    AuthUserViewModel viewmodel;
    Context context;
    List<Event> events;

    public EventLikedFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EventLikedFragment newInstance(String param1, String param2) {
        EventLikedFragment fragment = new EventLikedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewmodel = ViewModelProviders.of(getActivity()).get(AuthUserViewModel.class);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_liked, container, false);

        listView = view.findViewById(R.id.list);
        events = new ArrayList<>();
        adapter = new ListEventsAdapter(context, events);
        listView.setAdapter(adapter);

        viewmodel.getLikedEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> likedEvents) {
                events.clear();
                events.addAll(likedEvents);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
    }
}
