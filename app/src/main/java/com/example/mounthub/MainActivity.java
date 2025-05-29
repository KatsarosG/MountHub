package com.example.mounthub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mounthub.databinding.ActivityMainBinding;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Toast;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Button profileBtn;
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

        weatherButton = findViewById(R.id.weather_button);
        recordTrailButton = findViewById(R.id.record_trail_button);
        addLocationButton = findViewById(R.id.add_location_button);
        locationsNearMeButton = findViewById(R.id.locations_near_me_button);
        addTrailWithPinsButton = findViewById(R.id.add_trail_with_pins_button);

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
                Toast.makeText(MainActivity.this, "Record trail clicked", Toast.LENGTH_SHORT).show();
            }
        });

        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Add location clicked", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Add trail with pins clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}