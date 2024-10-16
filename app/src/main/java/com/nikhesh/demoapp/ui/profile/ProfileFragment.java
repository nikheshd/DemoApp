package com.nikhesh.demoapp.ui.profile;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.nikhesh.demoapp.MainActivity;
import com.nikhesh.demoapp.R;
import com.nikhesh.demoapp.cache.UserProfileCache;
import com.nikhesh.demoapp.databinding.FragmentProfilev1Binding;
import com.nikhesh.demoapp.service.UserProfileService;
import com.nikhesh.demoapp.util.Helper;

import java.util.concurrent.CompletableFuture;


public class ProfileFragment extends Fragment {
    private FragmentProfilev1Binding binding;
    private ImageView profileImageView;

    // Declare ActivityResultLauncher for gallery
    private ActivityResultLauncher<Intent> galleryLauncher;

    // Declare ActivityResultLauncher for camera
    private ActivityResultLauncher<Intent> cameraLauncher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        initializeLaunchers();

        binding = FragmentProfilev1Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView settingsTextView = root.findViewById(R.id.user_name);
        settingsTextView.setText(UserProfileCache.getFirebaseUser().getDisplayName());

        TextView emailTextView = root.findViewById(R.id.email);
        emailTextView.setText(UserProfileCache.getFirebaseUser().getEmail());

        profileImageView = root.findViewById(R.id.profile_image);
        setProfileImageView();

        LinearLayout signoutLayout = root.findViewById(R.id.signout_layout);
        signoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call your sign-out logic here, for example:
                signOutUser();
            }
        });

        profileImageView.setOnClickListener(v -> {
            showImageOptions();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void signOutUser() {
        AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener(task -> {
                    UserProfileCache.reload();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish(); // Close the signout activity
                });
    }

    private void initializeLaunchers() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Uri selectedImage = data.getData();
                    getActivity().getContentResolver().takePersistableUriPermission(selectedImage, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    Helper helper = new Helper(requireContext());
                    updateProfileImage(helper.compressImage(helper.getBitmapFromUri(selectedImage)));
                }
            }
        });
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                Helper helper = new Helper(requireContext());
                updateProfileImage(helper.compressImage(photo));
            }
        });
    }

    private void showImageOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        CharSequence[] options;
        if (Helper.isUriEmpty(UserProfileCache.getProfileUri())) {
            options = new CharSequence[]{"Select from Gallery", "Capture from Camera"};
        } else {
            options = new CharSequence[]{"Select from Gallery", "Capture from Camera","Remove profile image"};
        }
        builder.setTitle("Change profile image")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0 :
                            openGallery();
                            break;
                        case 1:
                            openCamera();
                            break;
                        case 2:
                            removeProfileImage();
                            break;
                    }
                })
                .show();
    }

    private void removeProfileImage() {
        updateProfileImage(null);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent); // Use the gallery launcher
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent); // Use the camera launcher
    }

    private void updateProfileImage(byte[] photo) {
        UserProfileService userProfileService = new UserProfileService(requireContext());
        CompletableFuture.runAsync(() -> userProfileService.updateProfileImage(photo));
        profileImageView.setImageResource(R.drawable.ic_profile_black_24dp);
        setProfileImageView();
    }

    private void setProfileImageView() {
        profileImageView.setImageResource(R.drawable.ic_profile_black_24dp);
        if (!Helper.isUriEmpty(UserProfileCache.getProfileUri())) {
            Glide.with(this)
                    .load(UserProfileCache.getProfileUri())
                    .into(profileImageView);
        }
    }
}
