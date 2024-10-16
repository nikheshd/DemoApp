package com.nikhesh.demoapp;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.nikhesh.demoapp.cache.UserProfileCache;
import com.nikhesh.demoapp.databinding.ActivityMainBinding;
import com.nikhesh.demoapp.service.AppStartUpService;
import com.nikhesh.demoapp.ui.dashboard.DashboardFragment;
import com.nikhesh.demoapp.ui.home.HomeFragment;
import com.nikhesh.demoapp.ui.profile.ProfileFragment;
import com.nikhesh.demoapp.ui.signin.SignInFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Inside MainActivity");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        new AppStartUpService(getApplicationContext());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!UserProfileCache.isLoggedIn()) {
            loadFragment(new SignInFragment());
        } else {
            loadFragment(new HomeFragment());
            BottomNavigationView bottomNav = findViewById(R.id.nav_view);
            bottomNav.setOnItemSelectedListener(navListener);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private final BottomNavigationView.OnItemSelectedListener navListener = item -> {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_home) {
            loadFragment(new HomeFragment());
        } else if (itemId == R.id.navigation_dashboard) {
            loadFragment(new DashboardFragment());
        } else if (itemId == R.id.navigation_profile) {
            loadFragment(new ProfileFragment());
        }
        return true;
    };

}