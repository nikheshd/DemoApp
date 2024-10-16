package com.nikhesh.demoapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class Helper {
    private final Context context;

    public Helper(Context context) {
        this.context = context;
    }

    public static void finish(Task<?> task) {
        try {
            Tasks.await(task);
        } catch (Exception e) {
            Log.e("Helper.await", Objects.requireNonNull(e.getMessage()));
        }
    }

    public static Uri convertBitmapToUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", null);
        return Uri.parse(path);
    }

    public static boolean isUriEmpty(Uri uri) {
        return uri == null || Uri.EMPTY.equals(uri);
    }

    public Bitmap getBitmapFromUri(Uri uri) {
        if (isUriEmpty(uri))    return null;
        try{
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (Exception e) {
            Log.e("Helper.getBitmapFromUri", Objects.requireNonNull(e.getMessage()));
        }
        return null;
    }

    public byte[] compressImage(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int quality = 10; // Adjust quality (0-100), lower is more compression
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
