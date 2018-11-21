package com.project.groupproject.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.project.groupproject.R;
import com.project.groupproject.models.User;
import com.project.groupproject.viewmodals.AuthUserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnUserEditFragmentListener} interface
 * to handle interaction events.
 * Use the {@link UserEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserEditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_USER = "user";

    private User user;
    private OnUserEditFragmentListener mListener;

    private EditText inputFirstName, inputLastName;
    private Button btnSave;
    private AuthUserViewModel userViewModal;

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
        userViewModal = ViewModelProviders.of(getActivity()).get(AuthUserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_edit, container, false);

        // init component
        inputFirstName = rootView.findViewById(R.id.input_first_name);
        inputLastName = rootView.findViewById(R.id.input_last_name);

        // track data
        userViewModal.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                // set current user info to edit text
                inputFirstName.setText(user.firstname);
                inputLastName.setText(user.lastname);
            }
        });

        // button event
        btnSave = rootView.findViewById(R.id.btn_save_profile);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });

        return rootView;
    }

    public void setLoading(boolean isLoading) {
        if (isLoading){
            btnSave.setEnabled(false);
        } else {
            btnSave.setEnabled(true);
        }
    }

    /**
     * On save button click event
     */
    public void onSaveButtonClicked(){
        if (mListener != null) {
            // load updated user info into new user object
            User newUser = userViewModal.getUser().getValue();

            newUser.firstname = inputFirstName.getText().toString();
            newUser.lastname = inputLastName.getText().toString();

            userViewModal.saveCurrentUser(newUser);

            // trigger event
            mListener.onSaveUserInfo(newUser);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserEditFragmentListener) {
            mListener = (OnUserEditFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserEditFragmentListener");
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
    public interface OnUserEditFragmentListener {
        // TODO: Update argument type and name

        void onSaveUserInfo(User user);
    }
}
