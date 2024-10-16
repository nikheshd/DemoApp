package com.nikhesh.demoapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nikhesh.demoapp.cache.UserProfileCache;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Hi " + UserProfileCache.getFirebaseUser().getDisplayName() + "!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}