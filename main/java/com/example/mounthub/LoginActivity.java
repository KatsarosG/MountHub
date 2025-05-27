package com.example.mounthub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    Button btnRegister, btnSignIn;
    EditText username, password;
    TextView errorMsg;
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if user is already logged in
        userSessionManager = new UserSessionManager(LoginActivity.this);
        User user = userSessionManager.getCurrentUser();

        if (user != null) {
            // go to map
            Intent intentObj = new Intent(this, MainActivity.class);
            startActivity(intentObj);
            finish();
        }

        setContentView(R.layout.activity_login);

        btnRegister = findViewById(R.id.register_activity);
        btnSignIn = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        errorMsg = findViewById(R.id.error_message);
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
//        username = "test";
//        password = "test";

        // validation
        boolean hasErrors = false;

        // clear previous messages
        errorMsg.setText("");

        if (username.isEmpty()) {
            errorMsg.append("Username is empty\n");
            hasErrors = true;
        }

        if (password.isEmpty()) {
            errorMsg.append("Password is empty\n");
            hasErrors = true;
        }

        if (hasErrors) {
            return; // Don't proceed with login if there are empty fields
        }

        // user search
        DatabaseManager databaseManager = new DatabaseManager(LoginActivity.this);
        User user = databaseManager.fetchUser(username);

        // user not found
        if (user == null) {
            errorMsg.append("User not found. Ensure username is right");
            return;
        }

        // password is incorrect
//        Log.d("Login", "Password: " + user.getPassword());
        if (!user.getPassword().equals(password)) {
            errorMsg.append("Incorrect password");
            return;
        }

        // save user in session
        userSessionManager.saveUser(user);

        // go to main activity
        Intent intentObj = new Intent(this, MainActivity.class);
        startActivity(intentObj);
        finish();
    }
}
