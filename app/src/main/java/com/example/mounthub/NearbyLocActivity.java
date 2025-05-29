package com.example.mounthub;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NearbyLocActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    private DatabaseManager dbManager;

    // Example user coordinates (can be replaced with real GPS later)
    private final double userLat = 38.285;
    private final double userLon = 21.785;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_loc);

        recyclerView = findViewById(R.id.nearby_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbManager = new DatabaseManager(this);
        displayNearbyLocations();
    }

    private void displayNearbyLocations() {
        // Fetch locations from database
        List<com.example.mounthub.Location> locationList = dbManager.fetchAllLocations();

        // Calculate distance for each location
        for (com.example.mounthub.Location loc : locationList) {
            float[] result = new float[1];
            com.example.mounthub.Location.distanceBetween(
                    userLat, userLon,
                    loc.getCoordinates().getLatitude(),
                    loc.getCoordinates().getLongitude(),
                    result
            );

            loc.setDistance(result[0]);

        }

        // Sort by distance (nearest first)
        Collections.sort(locationList, Comparator.comparingDouble(com.example.mounthub.Location::getDistance));

        // Attach sorted data to adapter
        adapter = new LocationAdapter(locationList, this);
        recyclerView.setAdapter(adapter);
    }
}
