package com.example.mounthub;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.BreakIterator;

public class InfoAndSafetyInstructionsScreen extends AppCompatActivity {

    private TextView contactMessageTextView;
    private TextView callMessageTextView;

    private TextView devicesMessageTextView;
    ContactService contact_service;
    CallService call_service;

    String call_message;
    String contact_message;
    String devices_message;
    private Button btnBack;

    private Button btnNotInDanger;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sos);


        contactMessageTextView = findViewById(R.id.contactMessageTextView);
        callMessageTextView = findViewById(R.id.callMessageTextView);
        devicesMessageTextView = findViewById(R.id.devicesMessageTextView);

        btnBack = findViewById(R.id.btnBack1);
        ManageSOSClass sosManager = new ManageSOSClass();
        btnBack.setOnClickListener(view -> {
            sosManager.cancelCall(this);
            sosManager.stopSharingLocationwithEmergencyContact(this);

            new Handler().postDelayed(() -> onBackPressed(), 1000); // 1-second delay
        });

        displayInfo();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnNotInDanger = findViewById(R.id.btnNoDanger);
        btnNotInDanger.setOnClickListener(view -> {
            sosManager.cancelCall(this);
            new Handler().postDelayed(() -> onBackPressed(), 1000); // 1-second delay
        });
        }


    public void displayInfo(){

        ContactService contact_service = new ContactService();
        contact_message = contact_service.sharewithContact();


        CallService call_service = new CallService();
        call_message = call_service.Call112();

        ManageSOSClass manage_sos = new ManageSOSClass();
        devices_message = manage_sos.sendAlertToNearbyDevices();

        contactMessageTextView.setText(contact_message);
        callMessageTextView.setText(call_message);

        devicesMessageTextView.setText(devices_message);


    }
}