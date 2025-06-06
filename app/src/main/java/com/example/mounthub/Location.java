package com.example.mounthub;

public class Location {
    private int id;
    private String name;
    private Coordinate coordinates;
    private String locationType;
    private String description;
    private String info; // optional field from first version
    private float distance; // used for sorting by proximity

    // Constructors
    public Location(int id, String name, String locationType) {
        this.id = id;
        this.name = name;
        this.locationType = locationType;
    }

    public Location(int id, String name, Coordinate coordinates, String locationType) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.locationType = locationType;
    }

    public Location(int id, String name, String locationType, String description, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.locationType = locationType;
        this.description = description;
        this.coordinates = new Coordinate(latitude, longitude);
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Coordinate getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinate coordinates) { this.coordinates = coordinates; }

    public String getLocationType() { return locationType; }
    public void setLocationType(String locationType) { this.locationType = locationType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }

    public float getDistance() { return distance; }
    public void setDistance(float distance) { this.distance = distance; }

    // Utility: Haversine formula to calculate distance between two coordinates
    public static float distanceBetween(double userLat, double userLon, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371000; // meters

        double dLat = Math.toRadians(lat2 - userLat);
        double dLon = Math.toRadians(lon2 - userLon);

        double lat1 = Math.toRadians(userLat);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (float) (EARTH_RADIUS * c);
    }

}
