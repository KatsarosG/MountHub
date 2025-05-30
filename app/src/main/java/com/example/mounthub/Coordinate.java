package com.example.mounthub;

public class Coordinate {
	private double latitude;
	private double longitude;
	// Static coordinates used globally
	public static double currentLatitude = 0.0;
	public static double currentLongitude = 0.0;


	public Coordinate(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() { return latitude; }
	public double getLongitude() { return longitude; }

	public void setLatitude(double latitude) { this.latitude = latitude; }
	public void setLongitude(double longitude) { this.longitude = longitude; }
}
