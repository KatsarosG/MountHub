package com.example.mounthub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    Button btnRegister, btnSignIn;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnRegister = findViewById(R.id.register_activity);
        btnSignIn = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    public void onRegisterActivityClick(View view) {
//        Log.d("Login", "Register button clicked");
        Intent intentObj = new Intent(this, RegisterActivity.class);
        startActivity(intentObj);
    }

    public void onSignInClick(View view) {
//        Log.d("Login", "Login button clicked");
//        Log.d("Login", "Login button clicked");

        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        // for testing uncomment this
        username = "test";
        password = "test";

        // validation
        if (username.isEmpty()) {
            Toast.makeText(this, "Username is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseManager databaseManager = new DatabaseManager(LoginActivity.this);
        User user = databaseManager.fetchUser(username);

        // user not found
        if (user == null) {
            Toast.makeText(this, "User not found. Ensure username is right.", Toast.LENGTH_SHORT).show();
            return;
        }

        // password is incorrect
        Log.d("Login", "Password: " + user.getPassword());
        if (!user.getPassword().equals(password)) {
            Toast.makeText(this, "Incorrect password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // save user in session
        UserSessionManager userSessionManager = new UserSessionManager(LoginActivity.this);
        userSessionManager.saveUser(user);

        // go to main activity
        Intent intentObj = new Intent(this, MainActivity.class);
        startActivity(intentObj);
        finish();
    }
}
