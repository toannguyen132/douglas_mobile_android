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
 * {@link EventCreatedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventCreatedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventCreatedFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    ListView listView;
    ListEventsAdapter adapter;
    AuthUserViewModel viewmodel;
    List<Event> events;
    Context context;

    public EventCreatedFragment() {
        // Required empty public constructor
    }

    public List<Event> getEvents() {
        return events;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventCreatedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventCreatedFragment newInstance(String param1, String param2) {
        EventCreatedFragment fragment = new EventCreatedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewmodel = ViewModelProviders.of(getActivity()).get(AuthUserViewModel.class);
        context = getContext();
        events = new ArrayList<>();
        adapter = new ListEventsAdapter(context, events);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_created, container, false);
        listView = view.findViewById(R.id.list);

        if (listView.getAdapter() == null){
            listView.setAdapter(adapter);

            viewmodel.getCreatedEvents().observe(this, new Observer<List<Event>>() {
                @Override
                public void onChanged(@Nullable List<Event> resultEvents) {
                    events.clear();
                    events.addAll(resultEvents);
                    adapter.notifyDataSetChanged();
                }
            });
        } else {
            events.clear();
            adapter.notifyDataSetChanged();
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        // TODO: Update argument type and name
        // void onFragmentInteraction(Uri uri);
    }
}
