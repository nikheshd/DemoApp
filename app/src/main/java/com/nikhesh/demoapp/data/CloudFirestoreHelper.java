package com.nikhesh.demoapp.data;

import com.google.firebase.firestore.FirebaseFirestore;
import com.nikhesh.demoapp.repository.model.UserModel;

public class CloudFirestoreHelper {
    public static void saveUser(UserModel user) {
        if (user == null || user.getUid() == null)   return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(user);
    }
}
