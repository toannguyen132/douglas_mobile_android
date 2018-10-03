package com.project.groupproject.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.groupproject.R;
import com.project.groupproject.models.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {

    static final String TAG = "register";
    private FirebaseAuth mAuth;
    private OnFragmentInteractionListener mListener;
    private Context context;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputPasswordConfirm;

    public RegisterFragment() {
        // Required empty public constructor
    }

    static public RegisterFragment getInstance(){
        return new RegisterFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_register, container, false);

        // init fỉebase auth instance
        mAuth = FirebaseAuth.getInstance();

        // assign components
        context = fragmentView.getContext();
        inputEmail = fragmentView.findViewById(R.id.register_email);
        inputPassword = fragmentView.findViewById(R.id.register_password);
        inputPasswordConfirm = fragmentView.findViewById(R.id.register_password_confirm);

        // add click listener to button
        Button btn = fragmentView.findViewById(R.id.register_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        fragmentView.findViewById(R.id.text_login)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchLoginView();
                    }
                });

        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserEditFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onRegisterSuccess(FirebaseUser user);
        void onSwitchLogin();
    }


    private String getUsername(){
        return inputEmail.getText().toString();
    }

    private String getPassword(){
        return inputPassword.getText().toString();
    }

    private void switchLoginView() {
        mListener.onSwitchLogin();
    }

    /**
     * perform action register new user
     * new user will be save to firebase
     */
    private void register() {
        String email = getUsername();
        String password = getPassword();

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        // add new User account
                        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                        User newUser = new User();
                        newUser.firstname = "";
                        newUser.lastname = "";
                        newUser.email = user.getEmail();
                        dataref.setValue(newUser);

                        // trigger event on register success
                        if (mListener != null) {
                            mListener.onRegisterSuccess(user);
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });
    }
}
