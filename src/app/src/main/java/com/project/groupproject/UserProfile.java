package com.project.groupproject;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.groupproject.fragment.UserInfoFragment;

public class UserProfile extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // init firebase
        mAuth = FirebaseAuth.getInstance();


        // prepare fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // get current user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            UserInfoFragment name = UserInfoFragment.newInstance("Display Name", user.getDisplayName());
            UserInfoFragment email = UserInfoFragment.newInstance("Email", user.getEmail());

            fragmentTransaction.add(R.id.user_info_container, name);
            fragmentTransaction.add(R.id.user_info_container, email);
            fragmentTransaction.commit();

        } else {
            // TODO: redirect to user
        }
    }
}
