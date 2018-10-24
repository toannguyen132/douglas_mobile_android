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
import android.widget.TextView;

import com.project.groupproject.R;
import com.project.groupproject.models.User;
import com.project.groupproject.viewmodals.AuthUserViewModal;

/**
 */
public class UserInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER = "user";

    // TODO: Rename and change types of parameters
    private User mUser;

    private OnUserInfoFragmentListener mListener;
    private AuthUserViewModal viewModel;

    // view
    private TextView textFirstName;
    private TextView textLastName;
    private TextView textEmail;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user
     * @return A new instance of fragment UserInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserInfoFragment newInstance(User user) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(AuthUserViewModal.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        //
        textEmail = view.findViewById(R.id.user_email);
        textFirstName = view.findViewById(R.id.user_first_name);
        textLastName = view.findViewById(R.id.user_last_name);

        // event click
        (view.findViewById(R.id.user_info_edit_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchView();
            }
        });

        // track user data
        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                textEmail.setText(user.email);
                textFirstName.setText(user.firstname);
                textLastName.setText(user.lastname);
            }
        });
        viewModel.fetchCurrentUser();

        return view;
    }

    /**
     * Switch to edit view
     */
    public void switchView(){
        if (mListener != null){
            mListener.onEditButtonClicked();
        }
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
        if (context instanceof OnUserInfoFragmentListener) {
            mListener = (OnUserInfoFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserEditFragmentListener");
        }
    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     */
    public interface OnUserInfoFragmentListener {
        // TODO: Update argument type and name
        void onEditButtonClicked();
    }
}
