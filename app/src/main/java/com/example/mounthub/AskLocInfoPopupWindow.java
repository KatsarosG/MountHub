package com.example.mounthub;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.osmdroid.views.overlay.Marker;

public class AskLocInfoPopupWindow extends Dialog {

    public AskLocInfoPopupWindow(Context context, Marker locationPin) {
        super(context);

        // Inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.ask_loc_info_popup_window, null);

        // Set the content view
        setContentView(dialogView);

        // Initialize views
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);
        EditText nameInput = dialogView.findViewById(R.id.name);
        EditText descriptionInput = dialogView.findViewById(R.id.description);
        EditText additionalInfoInput = dialogView.findViewById(R.id.additionalInfo);
        EditText locationTypeInput = dialogView.findViewById(R.id.location_type);

        // Remove backdrop and make background transparent
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);


        // Position dialog at bottom
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(params);

        // Set button click listeners
        btnSubmit.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String description = descriptionInput.getText().toString();
            String locationType = locationTypeInput.getText().toString();
            String additionalInfo = additionalInfoInput.getText().toString();
            Coordinate coordinates = new Coordinate(
                    locationPin.getPosition().getLatitude(),
                    locationPin.getPosition().getLongitude()
            );

            LocationHandler locationHandler = new LocationHandler();
            if (!locationHandler.validateLoc(this.getContext(), name, locationType, coordinates)) {
                return;
            }

            locationHandler.insertInfo();

            DatabaseManager databaseManager = new DatabaseManager(this.getContext());
            databaseManager.addLocation(name, description, additionalInfo, coordinates);

            this.dismiss();
        });

    }

    public void askForInfo() {
        this.show();
    }
}