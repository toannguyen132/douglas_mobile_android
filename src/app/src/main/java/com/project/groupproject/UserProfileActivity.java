package com.project.groupproject;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.groupproject.fragment.UserEditFragment;
import com.project.groupproject.fragment.UserInfoFragment;
import com.project.groupproject.models.User;

public class UserProfileActivity extends AppCompatActivity implements
        UserInfoFragment.OnFragmentInteractionListener,
        UserEditFragment.OnUserEditFragmentInteractionListener {

    FirebaseAuth mAuth;
    DatabaseReference mUserReference;

    FirebaseUser mUser;
    User currentUser;

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
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            getCurrentUser();

            UserInfoFragment fragment = UserInfoFragment.newInstance("Email", mUser.getEmail());

            fragmentTransaction.add(R.id.user_info_container, fragment);
            fragmentTransaction.commit();

        } else {
            // TODO: redirect to user
        }
    }

    /**
     * Implement event click edit button
     */
    @Override
    public void onEditButtonClicked() {
        switchEditMode();
    }

    /**
     *
     */
    private void switchEditMode() {
        UserEditFragment editFragment = UserEditFragment.newInstance(currentUser);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.user_info_container, editFragment).commit();
    }

    private void switchViewMode() {

    }

    /**
     * get Current user
     */
    private void getCurrentUser(){
        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mUser.getUid());
        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserProfileActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *
     */
    private void updateCurrentUserInfo() {

    }
}
