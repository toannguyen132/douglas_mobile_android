package com.project.groupproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
        //Change status bar to bg color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // If user is already login, go to main events
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        // login fragment
        loginFragment = LoginFragment.getInstance();
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, loginFragment).commit();

        // register fragment
        registerFragment = RegisterFragment.getInstance();
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
        Intent i = new Intent(AuthActivity.this, UserProfileActivity.class);
        startActivity(i);
    }

    @Override
    public void onSwitchRegister() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, registerFragment).commit();
    }

    @Override
    public void onSwitchLogin() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, loginFragment).commit();
    }
}
