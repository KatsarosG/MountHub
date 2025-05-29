package com.example.mounthub;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AlertWindow extends Dialog {

    public AlertWindow(Context context) {
        super(context);

        // Inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.alert_window, null);

        // Set the content view
        setContentView(dialogView);

        // Initialize views
        Button btnCancel = dialogView.findViewById(R.id.okay_btn);

        // confirmation
        btnCancel.setOnClickListener(v -> {
            this.dismiss();
        });
    }

    public void showAlert() {
        this.show();
    }
}