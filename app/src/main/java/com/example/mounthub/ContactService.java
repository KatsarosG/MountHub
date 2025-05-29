package com.example.mounthub;

import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class ContactService{

    private ManageSOS manage_sos;
    private MyLocationNewOverlay location;

    private String msg;
    public String sharewithContact(){
        ManageSOS manage_sos = new ManageSOS();
        location = manage_sos.shareLocationwithEmergencyContact();
        msg = "Currently Sharing your location with your chosen contact";
        return msg;
    }


}