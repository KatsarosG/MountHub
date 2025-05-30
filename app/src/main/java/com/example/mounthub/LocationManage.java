package com.example.mounthub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mounthub.ui.home.HomeFragment;

public class LocationManage extends AppCompatActivity {
    Button registerBtn;
    EditText username, email, password, passwordConfirm, info;
    TextView errorMsg;

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

        DatabaseManager databaseManager = new DatabaseManager(this.getBaseContext());
        databaseManager.getLocationDetails(id);
    }
}
