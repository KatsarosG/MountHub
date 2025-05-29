package com.example.mounthub;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class ManageSOS {

    private Map map;
    private MyLocationNewOverlay locationNewOverlay;

    private static final int SOS_END_DELAY_MS = 10000; // we will change it based on how much time we want.


    public void getLocation(Context context, MapView mapView) {
        map = new Map(context, mapView);
        locationNewOverlay = map.returnLocation();
    }

    public MyLocationNewOverlay shareLocationwithEmergencyContact(){
        return locationNewOverlay;
    }

    public String initiateCall(){
        String call_112;
        call_112 = "Calling 112";
        return call_112;
    }

    public String sendAlertToNearbyDevices(){
        String alertDevices;
        alertDevices = "Just sent a danger alert to nearby devices";
        return alertDevices;
    }

    public void cancelCall(Context context) {
        Toast.makeText(context, "Call was cancelled", Toast.LENGTH_SHORT).show();
    }

    public void stopSharingLocationwithEmergencyContact(Context context) {
        Toast.makeText(context, "Stopped sharing with emergency contacts", Toast.LENGTH_SHORT).show();
    }

    public void endSOSNotification(Context context) {
        new Handler().postDelayed(() -> {
            // Show toast messages
            Toast.makeText(context, "Call was cancelled", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "Stopped sharing with emergency contacts", Toast.LENGTH_SHORT).show();

            // Go back to MainActivity
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }, SOS_END_DELAY_MS);
    }

}

