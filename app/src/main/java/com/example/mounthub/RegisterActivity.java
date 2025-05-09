package com.example.mounthub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    Button registerBtn;
    EditText username, email, password, info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.register);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        info = findViewById(R.id.info);
    }

    public void onRegisterClick(View view) {
        User user;
        try  {
            user = new User(-1, username.getText().toString(), email.getText().toString(), password.getText().toString(), info.getText().toString());
            DatabaseManager databaseManager = new DatabaseManager(RegisterActivity.this);
            databaseManager.addUser(user);
        } catch (Exception e) {
            Log.d("Register", "Error: " + e.getMessage());
            return;
        }


        // save user in session
        UserSessionManager userSessionManager = new UserSessionManager(RegisterActivity.this);
        userSessionManager.saveUser(user);

        // go to main activity
        Intent intentObj = new Intent(this, MainActivity.class);
        startActivity(intentObj);
        finish();
    }
}
