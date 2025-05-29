package com.example.mounthub;


public class NearbyBroadcastService {


    public String alert;

    public void NearbyBroadcastServvice(){
        ManageSOS manage_sos = new ManageSOS();

        alert = manage_sos.sendAlertToNearbyDevices();

    }

}
