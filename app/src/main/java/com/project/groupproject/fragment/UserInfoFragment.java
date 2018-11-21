package com.project.groupproject.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.project.groupproject.R;
import com.project.groupproject.UserEventsListActivity;
import com.project.groupproject.adapters.ViewPageAdapter;
import com.project.groupproject.models.Event;
import com.project.groupproject.models.User;
import com.project.groupproject.viewmodals.AuthUserViewModel;
import com.project.groupproject.viewmodals.EventsListViewModel;

import java.util.List;

import javax.annotation.Nullable;

/**
 */
public class UserInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER = "user";

    // TODO: Rename and change types of parameters
    private User mUser;

    private OnUserInfoFragmentListener mListener;
    private AuthUserViewModel viewModel;
    private EventsListViewModel createdViewModel;
    private EventsListViewModel likedViewModel;

    // view
    private TextView textName;
    private TextView textEmail;

    private ViewPager viewPager;

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
        viewModel = ViewModelProviders.of(getActivity()).get(AuthUserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        View view = inflater.inflate(R.layout.fragment_user_info2, container, false);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.htab_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("LIKED EVENTS"));
        tabLayout.addTab(tabLayout.newTab().setText("CREATED EVENTS"));

        viewPager = view.findViewById(R.id.htab_viewpager);
        ViewPageAdapter adapter = new ViewPageAdapter(getFragmentManager());
        adapter.AddFragment(new UserAboutFragment(), "About");
        adapter.AddFragment(new EventCreatedFragment(), "Created");
        adapter.AddFragment(new EventLikedFragment(), "Liked");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

//        //Toolbar
//        Toolbar toolbar = view.findViewById(R.id.htab_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null){
//            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        actionBar.hide();

        //
//        textEmail = view.findViewById(R.id.text_email);
        textName = view.findViewById(R.id.text_name);
//
        // event click
//        (view.findViewById(R.id.user_info_edit_button)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchView();
//            }
//        });
//
//        // logout
//        (view.findViewById(R.id.user_info_logout)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logout();
//            }
//        });
//
//        // my events
//        (view.findViewById(R.id.user_info_events_btn)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToMyEvent();
//            }
//        });
//
        // enable loading
        final ProgressBar loadingBar = getActivity().findViewById(R.id.loading_bar);
        loadingBar.setVisibility(View.VISIBLE);
//
//        // track user data
        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                mUser = user;
//                textEmail.setText(user.email);
                textName.setText(user.firstname + " " + user.lastname);
                loadingBar.setVisibility(View.GONE);

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

    /**
     * Firebase Auth sign out and return to auth activity
     */
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
    }

    /**
     * Open new Activity displaying my events
     */
    private void goToMyEvent() {
        Intent intent = new Intent(getContext(), UserEventsListActivity.class);
        intent.putExtra("uid", mUser.getId());
        startActivity(intent);
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onCreated(uri);
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
