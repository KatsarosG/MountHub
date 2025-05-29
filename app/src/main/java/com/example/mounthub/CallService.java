package com.example.mounthub;

public class CallService {

    ManageSOS manage_sos;
    String msg;

    String fail;

    public String Call112() {
        msg = "Currently calling 112";
        return msg;
    }

    public String failed(){
        fail = "Call failed";
        return fail;
    }
}
