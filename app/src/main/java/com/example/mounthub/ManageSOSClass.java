package com.example.mounthub;

import android.content.Context;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class ManageSOSClass {

    private Map map;
    private MyLocationNewOverlay locationNewOverlay;

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

}

