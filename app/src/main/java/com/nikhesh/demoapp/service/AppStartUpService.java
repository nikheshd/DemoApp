package com.nikhesh.demoapp.service;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.nikhesh.demoapp.cache.UserProfileCache;

import java.util.concurrent.CompletableFuture;

public class AppStartUpService {
    private final Context context;

    public AppStartUpService(Context context) {
        this.context = context;
        FirebaseApp.initializeApp(context);
        UserProfileCache.reload();
        CompletableFuture.runAsync(() -> new UserProfileService(context).reloadProfileImage());
    }
}
