package com.example.mounthub;

import java.util.List;

public class Mountain extends Location{

    private List<Village> villages;
    private List<Trail> trails;
    private List<Refuge> refuges;
    private String info;
    public Mountain(int ID, String name, String locationType) {
        super(ID, name, "location");
    }
}
