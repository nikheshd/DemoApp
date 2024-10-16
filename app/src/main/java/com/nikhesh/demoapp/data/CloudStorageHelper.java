package com.nikhesh.demoapp.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nikhesh.demoapp.contant.AppConstants;
import com.nikhesh.demoapp.util.Helper;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class CloudStorageHelper {
    private final Context context;
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final StorageReference storageRef = storage.getReference();
    private static final StorageReference appRef = storageRef.child("demoapp");
    private static final StorageReference fileRef = appRef.child("user");

    public CloudStorageHelper(Context context) {
        this.context = context;
    }

    public boolean downloadFromUser(String key, File file) {
        StorageReference ref = fileRef.child(key).child(AppConstants.CloudFileName.PROFILE_IMAGE);
        AtomicBoolean status = new AtomicBoolean(false);
        Helper.finish(ref.getFile(file)
                .addOnSuccessListener(taskSnapshot -> status.set(true)));
        Log.d("CloudStorageHelper", "getInUser: " + key + " " + status.get());
        return status.get();
    }

    public Uri updateInUser(String key, Uri uri) {
        AtomicBoolean status = new AtomicBoolean(false);
        Log.i("CloudStorageHelper", "updateInUser: " + key + " " + uri);
        StorageReference ref = fileRef.child(key).child(AppConstants.CloudFileName.PROFILE_IMAGE);
        if (Helper.isUriEmpty(uri)) {
            Helper.finish(ref.delete()
                    .addOnSuccessListener(unused -> status.set(true)));
        } else {
            Helper.finish(ref.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> status.set(true)));
        }
        if (!Helper.isUriEmpty(uri) && status.get()) {
            return getDownloadUriInUser(key);
        }
        return null;
    }

    public Uri getDownloadUriInUser(String key) {
        final Uri[] uri = new Uri[1];
        StorageReference ref = fileRef.child(key).child(AppConstants.CloudFileName.PROFILE_IMAGE);
        Helper.finish(ref.getDownloadUrl()
                .addOnSuccessListener(u -> uri[0] = u));
        Log.d("CloudStorageHelper", "getDownloadUriInUser: " + key + " " + uri[0]);
        return uri[0];
    }

    public void deleteFile(String key) {
        StorageReference ref = fileRef.child(key).child(AppConstants.CloudFileName.PROFILE_IMAGE);
        Helper.finish(ref.delete());
    }

    public Uri uploadDataFile(String key, byte[] data) {
        AtomicBoolean status = new AtomicBoolean(false);
        StorageReference ref = fileRef.child(key).child(AppConstants.CloudFileName.PROFILE_IMAGE);
        Helper.finish(ref.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> status.set(true)));
        if (status.get()) {
            return getDownloadUriInUser(key);
        }
        return null;
    }
}
