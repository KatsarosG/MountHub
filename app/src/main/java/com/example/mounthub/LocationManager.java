package com.example.mounthub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LocationManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        int id = getIntent().getIntExtra("numberValue", 0);
        DatabaseManager db = new DatabaseManager(this);

        Intent intent = getIntent();

        String nameStr = intent.getStringExtra("name");
        String typeStr = intent.getStringExtra("type");
        double lat = intent.getDoubleExtra("lat", 0);
        double lon = intent.getDoubleExtra("lon", 0);

        TextView name = findViewById(R.id.location_name);
        TextView type = findViewById(R.id.location_type);
        TextView coords = findViewById(R.id.location_coords);

        name.setText(nameStr);
        type.setText(typeStr);
        coords.setText("Lat: " + lat + ", Lon: " + lon);

    }
}

