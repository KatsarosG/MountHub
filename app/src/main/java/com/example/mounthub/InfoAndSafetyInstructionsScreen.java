package com.example.mounthub;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InfoAndSafetyInstructionsScreen extends AppCompatActivity {

    private TextView contactMessageTextView;
    private TextView callMessageTextView;
    ContactService contact_service;
    CallService call_service;

    String call_message;
    String contact_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sos);
        Toast.makeText(this, "Activity Loaded", Toast.LENGTH_SHORT).show();

        contactMessageTextView = findViewById(R.id.contactMessageTextView);
        callMessageTextView = findViewById(R.id.callMessageTextView);

        // Call your display method
        displayInfo();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void displayInfo(){

        ContactService contact_service = new ContactService();
        contact_message = contact_service.sharewithContact();


        CallService call_service = new CallService();
        call_message = call_service.Call112();

        Toast.makeText(this, contact_message, Toast.LENGTH_LONG).show();
        Toast.makeText(this, call_message, Toast.LENGTH_LONG).show();

        contactMessageTextView.setText(contact_message);
        callMessageTextView.setText(call_message);

    }
}