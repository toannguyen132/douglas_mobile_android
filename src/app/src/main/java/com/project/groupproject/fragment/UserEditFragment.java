package com.project.groupproject.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.project.groupproject.R;
import com.project.groupproject.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnUserEditFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserEditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_USER = "user";

    private User user;
    private OnUserEditFragmentInteractionListener mListener;

    private EditText inputFirstName, inputLastName;

    public UserEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment UserEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserEditFragment newInstance(User user) {
        UserEditFragment fragment = new UserEditFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User)getArguments().getSerializable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_edit, container, false);

        // init component
        inputFirstName = rootView.findViewById(R.id.input_first_name);
        inputLastName = rootView.findViewById(R.id.input_last_name);


        // set current user info to edit text
        inputFirstName.setText(user.firstname);
        inputLastName.setText(user.lastname);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserEditFragmentInteractionListener) {
            mListener = (OnUserEditFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserEditFragmentInteractionListener");
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
    public interface OnUserEditFragmentInteractionListener {
        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
    }
}
