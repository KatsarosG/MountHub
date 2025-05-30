package com.example.mounthub;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NearbyLocActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    private DatabaseManager dbManager;

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

    public void displayNearbyLocations() {
        List<Location> locationList = dbManager.fetchAllLocations();
        /*
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

        }*/

        for (Location loc : locationList) {
            float distance = Location.distanceBetween(
                    userLat, userLon,
                    loc.getCoordinates().getLatitude(),
                    loc.getCoordinates().getLongitude()
            );
            loc.setDistance(distance); // Save for later sorting
        }

        // Now sort by that stored distance
        List<Location> sortedLocList = locationList.stream()
                .sorted(Comparator.comparingDouble(Location::getDistance))
                .collect(Collectors.toList());

        // Attach sorted data to adapter
        adapter = new LocationAdapter(sortedLocList, this);
        recyclerView.setAdapter(adapter);
    }
}

