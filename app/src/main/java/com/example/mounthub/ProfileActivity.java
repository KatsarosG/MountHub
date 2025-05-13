package com.example.mounthub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    Button logoutBtn, deleteBtn;
    TextView username;
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // set username
        username = findViewById(R.id.username);
        userSessionManager = new UserSessionManager(ProfileActivity.this);
        User user = userSessionManager.getCurrentUser();
        username.setText(user.getUsername());

        // logout
        logoutBtn = findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // clear user session
                userSessionManager.clearUser();

                // go to login activity
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // clear session
                userSessionManager.clearUser();

                // delete user from database
                DatabaseManager dbManager = new DatabaseManager(ProfileActivity.this);
                dbManager.deleteUser(user);

                // go to login activity
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}