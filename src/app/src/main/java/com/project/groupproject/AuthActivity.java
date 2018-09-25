package com.project.groupproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.project.groupproject.fragment.LoginFragment;
import com.project.groupproject.fragment.RegisterFragment;


public class AuthActivity extends AppCompatActivity
    implements RegisterFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener {


    FragmentManager fragmentManager;
    LoginFragment loginFragment;
    RegisterFragment registerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // add login fragment
//        LoginFragment loginFragment = new LoginFragment();
//        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.add(R.id.fragment_container, loginFragment).commit();

        // login fragment
        loginFragment = LoginFragment.getInstance();
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, loginFragment).commit();


        // register fragment
        registerFragment = RegisterFragment.getInstance();
//        fragmentTransaction.add(R.id.fragment_container, registerFragment).commit();

    }

    /**
     * trigger this method after register successfully
     * @param user
     */
    @Override
    public void onRegisterSuccess(FirebaseUser user) {
        Toast.makeText(this, "Register successfully.",
                Toast.LENGTH_SHORT).show();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, loginFragment).commit();
    }

    /**
     * trigger this method after loggin successfully
     * @param user
     */
    @Override
    public void onLoginSuccess(FirebaseUser user) {
        Toast.makeText(this, "You have been login as " + user.getEmail(),
                Toast.LENGTH_SHORT).show();
        // after login success, move to user profile activity
        Intent i = new Intent(AuthActivity.this, UserProfile.class);
        startActivity(i);
    }
}
