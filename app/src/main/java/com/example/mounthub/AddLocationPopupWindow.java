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

public class AddLocationPopupWindow extends Dialog {

    public AddLocationPopupWindow(Context context) {
        super(context);

        // Inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.add_location_popup_window, null);

        // Set the content view
        setContentView(dialogView);

        // Initialize views
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Remove backdrop and make background transparent
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);

        // Make dialog non-focusable so activity behind remains interactive
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        // Position dialog at bottom
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(params);

        // X Button
        btnCancel.setOnClickListener(v -> {
            this.dismiss();
        });

    }

    public void askForPin() {
        this.show();
    }
}