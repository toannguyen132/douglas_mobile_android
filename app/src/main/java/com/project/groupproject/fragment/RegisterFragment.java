package com.project.groupproject.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private TextView textError;
    private ProgressBar loadingBar;

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
        textError = fragmentView.findViewById(R.id.text_error);

        // loading bar
        loadingBar = getActivity().findViewById(R.id.loading_bar);

        // add click listener to button
        Button btn = fragmentView.findViewById(R.id.register_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    register();
                }
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
                    + " must implement OnUserEditFragmentListener");
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

    private String getConfirmPassword(){
        return inputPasswordConfirm.getText().toString();
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


        loadingBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        // add new User account
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        User newUser = new User();
                        newUser.firstname = "";
                        newUser.lastname = "";
                        newUser.email = user.getEmail();
                        db.collection("users").document(user.getUid())
                                .set(newUser);

                        // trigger event on register success
                        if (mListener != null) {
                            mListener.onRegisterSuccess(user);
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        textError.setText(task.getException().getMessage());
                    }

                    loadingBar.setVisibility(View.GONE);
                }
            });
    }

    /**
     *
     */

    /**
     * validate
     * @return
     */
    private boolean validate() {
        boolean isValid = true;

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String passwordConfirm = inputPasswordConfirm.getText().toString();

        inputEmail.setError(null);
        inputPassword.setError(null);
        inputPasswordConfirm.setError(null);

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            isValid = false;
            inputEmail.setError(getString(R.string.error_email_invalid));
        }

        if (password.isEmpty()){
            isValid = false;
            inputPassword.setError(getString(R.string.error_password_blank));
        }

        if (password.length() < 4) {
            isValid = false;
            inputPassword.setError(getString(R.string.error_password_short));
        }

        if (!password.equals(passwordConfirm)) {
            isValid = false;
            inputPasswordConfirm.setError(getString(R.string.error_password_confirm_not_match));
        }

        return isValid;
    }
}
