package com.example.mounthub;

import android.content.Context;

import com.example.mounthub.ui.home.HomeFragment;

import org.osmdroid.views.overlay.Marker;

public class LocationHandler {

    AddLocationPopupWindow addLocationPopupWindow;
    AskLocInfoPopupWindow askLocInfoPopupWindow;
    DatabaseManager databaseManager;

    public void startAddLoc(Context ctx, HomeFragment homeFragment) {
        addLocationPopupWindow = new AddLocationPopupWindow(ctx, homeFragment);
        addLocationPopupWindow.askForPin();
    }

    public void insertPin(Context ctx, Marker pin, HomeFragment homeFragment) {
        askLocInfoPopupWindow = new AskLocInfoPopupWindow(ctx, pin, homeFragment);

        // hide the add location popup
        addLocationPopupWindow.dismiss();

        askLocInfoPopupWindow.askForInfo();
    }

    public boolean validateLoc(Context ctx, String name, String locationType, Coordinate coordinates) {
        databaseManager = new DatabaseManager(ctx);

        return !databaseManager.isLocDuplicate(
                name,
                locationType,
                coordinates,
                ctx
        );
    }

    public void insertInfo() {
        databaseManager.QueryLocInfo();
    }
}
