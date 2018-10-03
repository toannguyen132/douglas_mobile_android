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

    UserEditFragment userEditFragment;
    UserInfoFragment userInfoFragment;

    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // init firebase
        mAuth = FirebaseAuth.getInstance();

        // get current user
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            // init database reference
            mUserReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(mUser.getUid());

            // get current user
            getCurrentUser();

        } else {
            // TODO: redirect to user
        }
    }

    /**
     * UserInfoFragment interface: Implement event click edit button
     */
    @Override
    public void onEditButtonClicked() {
        switchEditMode();
    }

    /**
     * UserEditFragment interface: implement user save info
     */
    @Override
    public void onSaveUserInfo(User updatedUser) {
        updateCurrentUserInfo(updatedUser);
    }

    /**
     *
     */
    private void switchEditMode() {
        userEditFragment = UserEditFragment.newInstance(currentUser);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.user_info_container, userEditFragment).commit();
        isEditMode = true;
    }

    private void switchViewMode() {
        // prepare fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // update
        userInfoFragment = UserInfoFragment.newInstance( currentUser );
        fragmentTransaction.add(R.id.user_info_container, userInfoFragment);
        isEditMode = false;
    }

    /**
     * get Current user
     */
    private void getCurrentUser(){
        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);

                // for the first time loading, add fragment
                if (isEditMode){
                    // enable the button
                    userEditFragment.setLoading(false);
                }
                switchViewMode();
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
    private void updateCurrentUserInfo(User updatedUser) {
        // disable button
        userEditFragment.setLoading(true);

        mUserReference.setValue(updatedUser);
    }
}
