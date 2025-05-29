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


        List<com.example.mounthub.Location> locationList = dbManager.fetchAllLocations();
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

        Location loc1 = new Location(1, "Refuge A", "Refuge");
        loc1.setCoordinates(new Coordinate(38.279, 21.776));
        locationList.add(loc1);

        Location loc2 = new Location(2, "Spring B", "Water Source");
        loc2.setCoordinates(new Coordinate(38.288, 21.789));
        locationList.add(loc2);

        Location loc3 = new Location(3, "Hill C", "Mountain");
        loc3.setCoordinates(new Coordinate(38.283, 21.795));
        locationList.add(loc3);

        Location loc4 = new Location(4, "Camp D", "Shelter");
        loc4.setCoordinates(new Coordinate(38.292, 21.781));
        locationList.add(loc4);

        Location loc5 = new Location(5, "Village E", "Village");
        loc5.setCoordinates(new Coordinate(38.280, 21.788));
        locationList.add(loc5);

        Location loc6 = new Location(6, "Pond F", "Water Source");
        loc6.setCoordinates(new Coordinate(38.287, 21.774));
        locationList.add(loc6);

        Location loc7 = new Location(7, "Peak G", "Mountain");
        loc7.setCoordinates(new Coordinate(38.282, 21.785));
        locationList.add(loc7);

        Location loc8 = new Location(8, "Shelter H", "Shelter");
        loc8.setCoordinates(new Coordinate(38.290, 21.792));
        locationList.add(loc8);

        Location loc9 = new Location(9, "Refuge I", "Refuge");
        loc9.setCoordinates(new Coordinate(38.278, 21.779));
        locationList.add(loc9);

        Location loc10 = new Location(10, "Spring J", "Water Source");
        loc10.setCoordinates(new Coordinate(38.289, 21.784));
        locationList.add(loc10);

        Location loc11 = new Location(11, "Mountain K", "Mountain");
        loc11.setCoordinates(new Coordinate(38.285, 21.780));
        locationList.add(loc11);

        Location loc12 = new Location(12, "Village L", "Village");
        loc12.setCoordinates(new Coordinate(38.281, 21.789));
        locationList.add(loc12);

        Location loc13 = new Location(13, "Shelter M", "Shelter");
        loc13.setCoordinates(new Coordinate(38.284, 21.776));
        locationList.add(loc13);

        Location loc14 = new Location(14, "Pond N", "Water Source");
        loc14.setCoordinates(new Coordinate(38.286, 21.787));
        locationList.add(loc14);

        Location loc15 = new Location(15, "Refuge O", "Refuge");
        loc15.setCoordinates(new Coordinate(38.280, 21.782));
        locationList.add(loc15);

        Location loc16 = new Location(16, "Village P", "Village");
        loc16.setCoordinates(new Coordinate(38.291, 21.779));
        locationList.add(loc16);

        Location loc17 = new Location(17, "Peak Q", "Mountain");
        loc17.setCoordinates(new Coordinate(38.283, 21.790));
        locationList.add(loc17);

        Location loc18 = new Location(18, "Shelter R", "Shelter");
        loc18.setCoordinates(new Coordinate(38.279, 21.783));
        locationList.add(loc18);

        Location loc19 = new Location(19, "Spring S", "Water Source");
        loc19.setCoordinates(new Coordinate(38.288, 21.785));
        locationList.add(loc19);

        Location loc20 = new Location(20, "Village T", "Village");
        loc20.setCoordinates(new Coordinate(38.282, 21.788));
        locationList.add(loc20);

        Location loc21 = new Location(21, "Refuge U", "Refuge");
        loc21.setCoordinates(new Coordinate(38.280, 21.777));
        locationList.add(loc21);

        for (Location loc : locationList) {
            float[] result = new float[1];
            Location.distanceBetween(
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

