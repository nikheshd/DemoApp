package com.nikhesh.demoapp.ui.signin;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nikhesh.demoapp.MainActivity;
import com.nikhesh.demoapp.R;
import com.nikhesh.demoapp.service.UserProfileService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SignInFragment extends Fragment {

    private final ActivityResultLauncher<Intent> signInLauncher;

    public SignInFragment() {
        System.out.println("Inside SignInFragment");
        signInLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                this::onSignInResult
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        // Set up sign-in button
        Button signInButton = view.findViewById(R.id.btn_google_sign_in);
        signInButton.setOnClickListener(v -> launchLoginFlow());

        return view;
    }

    private void launchLoginFlow() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // Navigate to MainActivity after successful sign-in
            if (user != null) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish(); // Close the login activity
            }
        } else {
            // Sign in failed. Handle the error.
            // ...
            new AlertDialog.Builder(requireContext())
                    .setTitle("Sign in failed, please try again.");
        }
    }
}
