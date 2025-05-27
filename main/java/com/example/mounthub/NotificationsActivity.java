package com.example.mounthub;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private Button btnBack;
    private ListView listViewNotifications;
    private List<Notification> notifications;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        btnBack = findViewById(R.id.btnBack);
        listViewNotifications = findViewById(R.id.listViewNotifications);

        btnBack.setOnClickListener(view -> onBackPressed());

        displayNotifications();
    }

    private void displayNotifications() {
        DatabaseManager dbManager = new DatabaseManager(this);
        notifications = dbManager.fetchNotifications();  // Now returns List<Notification>

        adapter = new NotificationAdapter(this, notifications);
        listViewNotifications.setAdapter(adapter);

        listViewNotifications.setOnItemClickListener((adapterView, view, position, id) -> {
            Notification notification = notifications.get(position);
            if (!notification.isSeen()) {
                notification.markAsSeen();
                adapter.notifyDataSetChanged();
            }
        });
    }

}