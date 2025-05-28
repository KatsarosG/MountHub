package com.example.mounthub;

import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class ContactService {

    private ManageSOSClass manage_sos;
    private MyLocationNewOverlay location;

    private String msg;
    public void sharewithContact(){
        ManageSOSClass manage_sos = new ManageSOSClass();
        location = manage_sos.shareLocationwithEmergencyContact();
        System.out.println("Currently Sharing your location with your chosen contact");
    }

    public void call112(){
        msg = manage_sos.initiateCall();
        System.out.println(msg);
    }
}
