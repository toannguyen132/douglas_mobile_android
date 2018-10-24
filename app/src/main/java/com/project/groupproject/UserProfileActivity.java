package com.project.groupproject;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.groupproject.fragment.UserEditFragment;
import com.project.groupproject.fragment.UserInfoFragment;
import com.project.groupproject.models.User;
import com.project.groupproject.viewmodals.AuthUserViewModal;

public class UserProfileActivity extends AppCompatActivity implements
        UserInfoFragment.OnUserInfoFragmentListener,
        UserEditFragment.OnUserEditFragmentListener {

    FirebaseAuth mAuth;
    DocumentReference documentReference;

    FirebaseUser mUser;
    User currentUser;

    UserEditFragment userEditFragment;
    UserInfoFragment userInfoFragment;

    boolean isEditMode = false;

    AuthUserViewModal viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        viewModel = ViewModelProviders.of(this).get(AuthUserViewModal.class);

        // init firebase
        mAuth = FirebaseAuth.getInstance();

        // get current user
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            documentReference = FirebaseFirestore.getInstance().collection("users").document(mUser.getUid());
            // get current user
            getCurrentUser();

            switchViewMode();

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
        fragmentTransaction.replace(R.id.user_info_container, userInfoFragment).commit();
        isEditMode = false;
    }

    /**
     * get Current user
     */
    private void getCurrentUser(){

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        currentUser = document.toObject(User.class);
                        // pass user to view model
                        viewModel.setUser(currentUser);
                    } else {
                        Log.d("Event", "Current user is empty");
                    }

                    // for the first time loading, add fragment
                    if (isEditMode){
                        // enable the button
                        userEditFragment.setLoading(false);
                    }

                } else {
                    Log.d("Event", "Cannot get current user");
                }
            }
        });
    }

    /**
     *
     */
    private void updateCurrentUserInfo(final User updatedUser) {
        // disable button
        userEditFragment.setLoading(true);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("users").document(mUser.getUid()).set(updatedUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        currentUser = updatedUser;
                        viewModel.setUser(currentUser);
                        switchViewMode();
                    }
                });
    }
}
