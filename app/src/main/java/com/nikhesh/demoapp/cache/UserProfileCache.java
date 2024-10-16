package com.nikhesh.demoapp.cache;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import lombok.Getter;
import lombok.Setter;

public class UserProfileCache {
    @Setter @Getter
    private static FirebaseUser firebaseUser;
    @Setter @Getter
    private static boolean isLoggedIn;
    @Getter @Setter
    private static Uri profileUri;

    public static void reload() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            isLoggedIn = false;
        } else {
            isLoggedIn = true;
        }
        Log.i("UserProfileCache",  isLoggedIn + " " + profileUri);
    }
}
