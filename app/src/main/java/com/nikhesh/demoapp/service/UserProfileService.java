package com.nikhesh.demoapp.service;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.health.UidHealthStats;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.nikhesh.demoapp.cache.UserProfileCache;
import com.nikhesh.demoapp.contant.AppConstants;
import com.nikhesh.demoapp.data.CloudFirestoreHelper;
import com.nikhesh.demoapp.data.CloudStorageHelper;
import com.nikhesh.demoapp.data.LocalStorageHelper;
import com.nikhesh.demoapp.repository.model.UserModel;
import com.nikhesh.demoapp.util.FirebaseProfileHelper;
import com.nikhesh.demoapp.util.Helper;

import java.io.File;

public class UserProfileService {
    private final Context context;
    private final FirebaseUser user;

    public UserProfileService(Context appContext) {
        context = appContext;
        user = UserProfileCache.getFirebaseUser();
    }

    public void reloadProfileImage() {
        if (user == null || !Helper.isUriEmpty(UserProfileCache.getProfileUri())) {
            return;
        }
        Log.d("UserProfileService", "reloadProfileImage: uid" + user.getUid());
        CloudStorageHelper cloudStorageHelper = new CloudStorageHelper(context);
        Uri cloudUri = cloudStorageHelper.getDownloadUriInUser(user.getUid());
        if (!Helper.isUriEmpty(cloudUri)) {
            UserProfileCache.setProfileUri(cloudUri);
        }
        if(UserProfileCache.isLoggedIn()) {
            CloudFirestoreHelper.saveUser(new UserModel(UserProfileCache.getFirebaseUser().getDisplayName(), UserProfileCache.getFirebaseUser().getUid(), UserProfileCache.getFirebaseUser().getEmail(),
                    Helper.isUriEmpty(UserProfileCache.getProfileUri())? null: UserProfileCache.getProfileUri().toString()));
        }
        Log.i("UserProfileService", "reloadProfileImage: url" + " " + UserProfileCache.getProfileUri());
    }

    public void updateProfileImage(byte[] data) {
        CloudStorageHelper cloudStorageHelper = new CloudStorageHelper(context);
        FirebaseProfileHelper firebaseProfileHelper = new FirebaseProfileHelper();
        if (data == null) {
            cloudStorageHelper.deleteFile(user.getUid());
            UserProfileCache.setProfileUri(null);
        } else {
            Uri cloudUri = cloudStorageHelper.uploadDataFile(user.getUid(), data);
            firebaseProfileHelper.updateProfileImage(cloudUri);
            if (Helper.isUriEmpty(cloudUri)) {
                new AlertDialog.Builder(context)
                        .setTitle("Failed to update profile image, please try again.");
            } else {
                UserProfileCache.setProfileUri(cloudUri);
            }
        }
        Log.i("UserProfileService", "updateProfileImage: url" + " " + UserProfileCache.getProfileUri());
    }
}
