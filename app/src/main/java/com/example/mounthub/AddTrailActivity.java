package com.example.mounthub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class AddTrailActivity extends AppCompatActivity {

    EditText nameInput, durationInput, difficultyInput, infoInput;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trail);

        Intent intent = getIntent();
        ArrayList<GeoPoint> route = intent.getParcelableArrayListExtra("route");

        nameInput = findViewById(R.id.name);
        durationInput = findViewById(R.id.duration);
        difficultyInput = findViewById(R.id.difficulty);
        infoInput = findViewById(R.id.info);

        submitBtn = findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                int duration = Integer.parseInt(durationInput.getText().toString());
                String difficulty = difficultyInput.getText().toString();
                String info = infoInput.getText().toString();

                ArrayList<Coordinate> coordinates = new ArrayList<>();

                // parse list of geopoints to list of coordinates
                for (GeoPoint point : route) {
                    Coordinate coordinate = new Coordinate(point.getLatitude(), point.getLongitude());
                    coordinates.add(coordinate);
                }

//                Trail trail = new Trail(-1, name, coordinates, duration, Trail.Difficulty.EASY, info);

                Trail trail = new Trail(
                        -1,
                        "Appalachian Trail",
                        List.of(new Coordinate(38.246684768604155, 21.737499370110385), new Coordinate(38.24630040417543, 21.734617569821097)),
                        Trail.Difficulty.EASY,
                        "Info"
                );

                // Add trail to database
                DatabaseManager databaseManager = new DatabaseManager(AddTrailActivity.this);
                databaseManager.addTrail(trail);

                Toast.makeText(AddTrailActivity.this, "Trail added", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
