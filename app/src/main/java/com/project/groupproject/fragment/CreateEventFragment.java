package com.project.groupproject.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.project.groupproject.R;
import com.project.groupproject.lib.EventFactory;
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
    private EditText fromDate;
    private EditText toDate;
    private EditText inputName;
    private EditText inputDesc;
    private EditText inputLocation;

    //firebase
    private DatabaseReference mReference;

    private DatePickerDialog datepicker;
    private DatePickerDialog.OnDateSetListener fromListener, toListener;

    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        //
        fromDate = view.findViewById(R.id.input_event_start_date);
        toDate = view.findViewById(R.id.input_event_end_date);
        mButton = view.findViewById(R.id.btn_create_event);
        inputName = view.findViewById(R.id.input_event_name);
        inputDesc = view.findViewById(R.id.input_event_desc);
        inputLocation = view.findViewById(R.id.input_event_location);

        // datepicker
        datepicker = new DatePickerDialog(view.getContext());

        // set listener
        fromListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fromDate.setText(year + "/" + month + "/" + dayOfMonth);
            }
        };
        toListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                toDate.setText(year + "/" + month + "/" + dayOfMonth);
            }
        };
        fromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Toast.makeText(view.getContext(), "test", Toast.LENGTH_SHORT);
                    datepicker.setOnDateSetListener(fromListener);
                    datepicker.show();
                }
            }
        });
        toDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datepicker.setOnDateSetListener(toListener);
                    datepicker.show();
                }
            }
        });

        //
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event e = getEvent();
                EventFactory.createEvent(e, new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Event", "add event sucessfully");
                    }
                });
            }
        });

        // add sample event
//        Event event = getEvent();
//        createEvent(event);

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
        e.name = inputName.getText().toString();
        e.description = inputDesc.getText().toString();
        e.location = inputLocation.getText().toString();
        e.lat = -1.0;
        e.lng = 23.0012;
        e.num_follow = 0;
        e.num_like = 0;
        e.start_date = Calendar.getInstance().getTimeInMillis();
        e.end_date = Calendar.getInstance().getTimeInMillis();
        return e;
    }
}
