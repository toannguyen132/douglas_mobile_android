package com.project.groupproject.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.groupproject.R;
import com.project.groupproject.models.Event;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CreateEventFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    // components
    private Button mButton;
    private DatePickerDialog mDatePicker;

    //firebase
    private DatabaseReference mReference;

    private DatePickerDialog.OnDateSetListener fromListener, toListener;

    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        

        // add sample event
        Event event = getEvent();
        createEvent(event);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
        void onFragmentInteraction(Uri uri);
    }

    /**
     *
     * @return
     */
    private Event getEvent() {
        Event e = new Event();
        e.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        e.name = "sample name";
        e.description = "sample description";
        e.location = "Surrey, Canada";
        e.lat = -1.0;
        e.lng = 23.0012;
        e.num_follow = 0;
        e.num_like = 0;
        e.start_date = Calendar.getInstance().getTimeInMillis();
        e.end_date = Calendar.getInstance().getTimeInMillis();
        return e;
    }

    /**
     * Create a event
     * @param event
     */
    private void createEvent(Event event) {
        mReference = FirebaseDatabase.getInstance().getReference().child("events");

        String key = mReference.push().getKey();

        mReference.child(key).setValue(event.toMap());

    }
}
