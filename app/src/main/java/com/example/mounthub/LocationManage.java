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

        Intent intent = getIntent();
        int locationId = intent.getIntExtra("numberValue", 0);

        DatabaseManager databaseManager = new DatabaseManager(this.getBaseContext());
        databaseManager.getLocationDetails(locationId);
    }
}
