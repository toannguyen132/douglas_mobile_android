package com.project.groupproject.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.project.groupproject.MainActivity;
import com.project.groupproject.MapsActivity;
import com.project.groupproject.R;
import com.project.groupproject.SingleEvent;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    Button mButton;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_login, container, false);
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);


        // init button and assign event listener
        mButton = rootView.findViewById(R.id.button_login2);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                register();
                Intent i = new Intent(rootView.getContext(), MapsActivity.class);
                Toast.makeText(rootView.getContext(), "test", Toast.LENGTH_LONG).show();
                startActivity(i);
            }
        });


        return rootView;
    }

}
