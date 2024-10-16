package com.nikhesh.demoapp.util;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.UserProfileChangeRequest;
import com.nikhesh.demoapp.cache.UserProfileCache;

import java.util.concurrent.atomic.AtomicBoolean;

public class FirebaseProfileHelper {
    public boolean updateProfileImage(Uri photoUrl) {
        AtomicBoolean status = new AtomicBoolean(false);
        UserProfileCache.getFirebaseUser().updateProfile(
                        new UserProfileChangeRequest.Builder().setPhotoUri(photoUrl).build())
                .addOnSuccessListener(e -> {
                    status.set(true);
                });
        if (!status.get()) {
            Log.e("FirebaseProfileHelper", "Failed to update profile image");
        }
        UserProfileCache.reload();
        return status.get();
    }
}
