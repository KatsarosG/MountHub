package com.example.mounthub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;

import com.example.mounthub.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mounthub.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputEditText;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Toast;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Button profileBtn;
    private SearchManage searchManage;

    ImageButton weatherButton;
    ImageButton recordTrailButton;
    ImageButton addLocationButton;
    ImageButton locationsNearMeButton;
    ImageButton addTrailWithPinsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // elements
        profileBtn = findViewById(R.id.profile_btn);
        TextInputEditText searchInput = findViewById(R.id.searchInput);
        searchManage = new SearchManage(this, searchInput);

        weatherButton = findViewById(R.id.weather_button);
        recordTrailButton = findViewById(R.id.record_trail_button);
        addLocationButton = findViewById(R.id.add_location_button);
        locationsNearMeButton = findViewById(R.id.locations_near_me_button);
        addTrailWithPinsButton = findViewById(R.id.add_trail_with_pins_button);

        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use FragmentManager to get HomeFragment
                HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_activity_main)
                        .getChildFragmentManager()
                        .getFragments()
                        .get(0);  // get(0) is safe if HomeFragment is loaded first
                


                if (homeFragment != null) {
                    homeFragment.startAddLocationMode(); // create this method in HomeFragment
                }
            }
        });

        // to profile activity
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherFragment weatherFragment = new WeatherFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.add(R.id.container, weatherFragment, "WeatherFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        recordTrailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use FragmentManager to get HomeFragment
                HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_activity_main)
                        .getChildFragmentManager()
                        .getFragments()
                        .get(0);

                if (homeFragment != null) {
                    homeFragment.recordMode();
                }
            }
        });

        locationsNearMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Locations near me clicked", Toast.LENGTH_SHORT).show();
            }
        });

        addTrailWithPinsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use FragmentManager to get HomeFragment
                HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_activity_main)
                        .getChildFragmentManager()
                        .getFragments()
                        .get(0);  // get(0) is safe if HomeFragment is loaded first



                if (homeFragment != null) {
                    homeFragment.startAddTrailMode(); // create this method in HomeFragment
                }
            }
        });
    }
    public void goToNotifScreen(View v){
        Intent i = new Intent(this, NotificationsActivity.class);
        startActivity(i);
    }

    public void goToSOSScreen(View v){
        Intent i = new Intent(this, InfoAndSafetyInstructionsScreen.class);
        startActivity(i);
    }

}