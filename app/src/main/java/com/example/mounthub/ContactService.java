package com.example.mounthub;

import android.content.Context;

import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class ContactService{

    private ManageSOSClass manage_sos;
    private MyLocationNewOverlay location;

    private String msg;
    public String sharewithContact(){
        ManageSOSClass manage_sos = new ManageSOSClass();
        location = manage_sos.shareLocationwithEmergencyContact();
        msg = "Currently Sharing your location with your chosen contact";
        return msg;
    }


}
