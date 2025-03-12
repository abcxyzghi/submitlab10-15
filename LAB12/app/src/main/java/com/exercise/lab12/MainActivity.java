package com.exercise.lab12;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.exercise.lab12.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration; // For handling the top app bar
    private ActivityMainBinding binding; // Binding for the UI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding the layout of the activity
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the toolbar for the app
        setSupportActionBar(binding.toolbar);

        // Set up the BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.nav_view); // Defines top-level destinations
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notification).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration); // Link navigation controller with the toolbar
        NavigationUI.setupWithNavController(binding.navView, navController); // Set up BottomNavigationView with navigation controller


        // Update the title of the toolbar based on the selected destination
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination,
                                             @Nullable Bundle arguments) {
                // Use If-Else Check destination id base on R.id." To Set Title
                if (destination.getId() == R.id.navigation_home) {
                    binding.toolbar.setTitle("Home");
                } else if (destination.getId() == R.id.navigation_dashboard) {
                    binding.toolbar.setTitle("Dashboard");
                } else if (destination.getId() == R.id.navigation_notification) {
                    binding.toolbar.setTitle("Notifications");
                } else {
                    binding.toolbar.setTitle("Title");
                }
            }
        });
    }}