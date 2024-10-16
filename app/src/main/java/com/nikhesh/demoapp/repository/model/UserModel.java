package com.nikhesh.demoapp.repository.model;

import android.net.Uri;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String displayName;
    private String uid;
    private String email;
    private String photoUri;
}
