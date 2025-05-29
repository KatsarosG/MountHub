package com.example.mounthub;

public class Location {
    private int id;
    private String name;
    private Coordinate coordinates;
    private String locationType;
    private String description;
    private float distance;

    public Location(int id, String name, String locationType) {
        this.id = id;
        this.name = name;
        this.locationType = locationType;
    }

    public Location(int id, String name, String locationType, String description, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.locationType = locationType;
        this.coordinates = new Coordinate(latitude, longitude);
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Coordinate getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinate coordinates) { this.coordinates = coordinates; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public float getDistance() { return distance; }
    public void setDistance(float distance) { this.distance = distance; }

    public String getLocationType() { return locationType; }
    public void setLocationType(String locationType) { this.locationType = locationType; }

    // Utility method for calculating distance between two coordinates
    public static void distanceBetween(double userLat, double userLon, double latitude, double longitude, float[] result) {
        final int EARTH_RADIUS = 6371000; // meters

        double dLat = Math.toRadians(latitude - userLat);
        double dLon = Math.toRadians(longitude - userLon);

        double lat1 = Math.toRadians(userLat);
        double lat2 = Math.toRadians(latitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        float distance = (float) (EARTH_RADIUS * c);

        if (result != null && result.length > 0) {
            result[0] = distance;
        }
    }
}
