package com.example.mounthub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    Button registerBtn;
    EditText username, email, password, passwordConfirm, info;
    TextView errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.register);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.confirm_password);
        info = findViewById(R.id.info);
        errorMsg = findViewById(R.id.error_message);
    }

    public void onRegisterClick(View view) {
        String username = this.username.getText().toString();
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        String passwordConfirm = this.passwordConfirm.getText().toString();
        String info = this.info.getText().toString();

        // validation
        boolean hasErrors = false;

        // clear previous messages
        errorMsg.setText("");

        if (username.isEmpty()) {
            errorMsg.append("Username cannot be empty\n");
            hasErrors = true;
        }

        if (email.isEmpty()) {
            errorMsg.append("Email cannot be empty\n");
            hasErrors = true;
        }

        if (password.isEmpty()) {
            errorMsg.append("Password cannot be empty\n");
            hasErrors = true;
        }

        if (passwordConfirm.isEmpty()) {
            errorMsg.append("Confirm password cannot be empty\n");
            hasErrors = true;
        }

        if (info.isEmpty()) {
            errorMsg.append("Info cannot be empty\n");
            hasErrors = true;
        }

        if (!password.equals(passwordConfirm)) {
            errorMsg.append("Passwords have to match\n");
            hasErrors = true;
        }

        if (hasErrors) {
            return; // Don't proceed with login if there are empty fields
        }

        // user creation
        User user = new User(-1, username, email, password, info);
        DatabaseManager databaseManager = new DatabaseManager(RegisterActivity.this);
        long userId = databaseManager.addUser(user);

        if (userId == 0) {
            errorMsg.append("Error inserting user");
            return;
        }

        if (userId == -1) {
            errorMsg.append("Username already exists");
            return;
        }

        if (userId == -2) {
            errorMsg.append("Email already exists");
            return;
        }

        user.setId((int) userId);

        // save user in session
        UserSessionManager userSessionManager = new UserSessionManager(RegisterActivity.this);
        userSessionManager.saveUser(user);

        // go to main activity
        Intent intentObj = new Intent(this, MainActivity.class);
        startActivity(intentObj);
        finish();
    }
}
