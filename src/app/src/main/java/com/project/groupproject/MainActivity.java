package com.project.groupproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.project.groupproject.fragment.LoginFragment;
import com.project.groupproject.fragment.RegisterFragment;


public class MainActivity extends AppCompatActivity
    implements RegisterFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add login fragment
//        LoginFragment loginFragment = new LoginFragment();
//        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.add(R.id.fragment_container, loginFragment).commit();

        // login fragment
        LoginFragment loginFragment = LoginFragment.getInstance();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.fragment_container, loginFragment).commit();

        // register fragment
        /*
        RegisterFragment registerFragment = RegisterFragment.getInstance();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.fragment_container, registerFragment).commit();
        */

    }

    /**
     * trigger this method after register successfully
     * @param user
     */
    @Override
    public void onRegisterSuccess(FirebaseUser user) {
        Toast.makeText(this, "Register successfully.",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * trigger this method after loggin successfully
     * @param user
     */
    @Override
    public void onLoginSuccess(FirebaseUser user) {
        Toast.makeText(this, "You have been login as " + user.getDisplayName(),
                Toast.LENGTH_SHORT).show();

    }
}
