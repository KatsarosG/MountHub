package com.example.mounthub;

public class ActivityLocation extends Location {
    private String info;
    public ActivityLocation(int ID,String name,String locationType) {
        super(ID, name,"ActivityLocation");
        this.info = info;
    }

    public String getInfo(){
        return info;
    }

}
