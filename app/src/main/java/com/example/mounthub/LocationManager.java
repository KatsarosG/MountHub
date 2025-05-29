package com.example.mounthub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LocationManager extends AppCompatActivity {
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        int id = getIntent().getIntExtra("numberValue", 0);
        DatabaseManager db = new DatabaseManager(this);
        Location loc = db.getLocationDetails(id);

        TextView name = findViewById(R.id.location_name);
        TextView type = findViewById(R.id.location_type);
        TextView coords = findViewById(R.id.location_coords);

        name.setText(loc.getName());
        type.setText(loc.getLocationType());
        coords.setText("Lat: " + loc.getCoordinates().getLatitude() +
                ", Lon: " + loc.getCoordinates().getLongitude());
    }*/
}

