package com.project.groupproject.viewmodals;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.groupproject.models.User;

public class AuthUserViewModal extends ViewModel {
    public static final String NAME = "users";
    private CollectionReference collection = FirebaseFirestore.getInstance().collection(NAME);

    private final MutableLiveData<User> user = new MutableLiveData<>();

    public void setUser(User user) {
        this.user.setValue(user);
    }

    public LiveData<User> getUser() {
        return user;
    }

    /**
     * public functions
     */

    public void fetchUser(String uid) {
        collection.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                User user = document.toObject(User.class);
                user.setId(document.getId());
                setUser(user);
            }
        });
    }

    public void fetchCurrentUser() {
        this.fetchUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public void saveUser(String uid, User user){
        final User newUser = user;
        collection.document(uid).set(user);
    }

    public void saveCurrentUser(User user){
        User currentUser = this.user.getValue();
        saveUser(currentUser.getId(), user);
    }


}
