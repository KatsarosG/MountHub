package com.example.mounthub;
import java.sql.*;
import com.example.mounthub.Coordinate;

public class Location {
    private final int ID;
    private String name;
    private Coordinate coordinates;
    private String locationType;
    private String description;
    private String info;

    public Location(int ID,String name, Coordinate coordinates, String locationType) {
        this.ID = ID;
        this.name = name;
        this.coordinates = coordinates;
        this.locationType = locationType;
    }
    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public Coordinate getCoordinates() { return coordinates; }

    public String getDescription(String desc){
        return description;
    }//the name and description for the location will be asked in the add location popup window that will communicate with the location class

    public static void queryLocInfo(String name, String locationType) {
    }
    public static void queryInsertLocInfo(String name, String locationType) {

    } //normally the return type should be location and not void but the body is currently empty because the database is not ready+input variable connection
}
