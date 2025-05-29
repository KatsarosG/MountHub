package com.example.mounthub;

public class Location {
    private final int ID;
    private String name;
    private Coordinate coordinates;
    private String locationType;
    private String description;
    private String info;
    private float distance; // <-- Add this

    // Existing constructor
    public Location(int ID, String name, String locationType) {
        this.ID = ID;
        this.name = name;
        this.locationType = locationType;
    }

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

        // Store distance in result array's first element
        if (result != null && result.length > 0) {
            result[0] = distance;
        }
    }

    public String getName() {
        return name;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
