package com.nikhesh.demoapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class LocalStorageHelper {
    private final SharedPreferences sharedPreferences;
    private final Context context;

    public LocalStorageHelper(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
    }

    public void save(String key, String data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (data == null) {
            editor.remove(key);
        } else {
            editor.putString(key, data);
        }
        editor.apply();
    }

    public String getData(String key) {
        return sharedPreferences.getString(key, null);
    }

    public boolean createFile(File file) {
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            return true;
        } catch (IOException e) {
            Log.e("LocalStorageHelper", "Error creating temporary file", e);
        }
        return false;
    }

    public boolean deleteFile(File file) {
        return file.delete();
    }
}
