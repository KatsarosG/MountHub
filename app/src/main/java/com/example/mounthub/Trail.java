package com.example.mounthub;

import java.time.Duration;
import java.util.List;

public class Trail {

	private String name;
	private final int ID; // read-only
	private List<Coordinate> routeLine;
	private int duration;
	private Difficulty difficulty;
	private String info;
	private List<String> photoPaths;
	private List<Integer> reviews;
	private List<Excursion> excursions;
	private int distance;

	// Enum for difficulty
	public enum Difficulty {
		EASY, MEDIUM, HARD
	}

	// Constructor
	public Trail(int ID, String name, List<Coordinate> routeLine, int duration,
                 Difficulty difficulty, String info, List<String> photoPaths,
                 List<Integer> reviews, List<Excursion> excursions, int distance) {
		this.ID = ID;
		this.name = name;
		this.routeLine = routeLine;
		this.duration = duration;
		this.difficulty = difficulty;
		this.info = info;
		this.photoPaths = photoPaths;
		this.reviews = reviews;
		this.excursions = excursions;
		this.distance = distance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getID() {
		return ID;
	}

	public List<Coordinate> getRouteLine() {
		return routeLine;
	}

	public void setRouteLine(List<Coordinate> routeLine) {
		this.routeLine = routeLine;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List<String> getPhotos() {
		return photoPaths;
	}

	public void setPhotoPaths(List<String> photoPaths) {
		this.photoPaths = photoPaths;
	}

	public List<Integer> getReviews() {
		return reviews;
	}

	public void setReviews(List<Integer> reviews) {
		this.reviews = reviews;
	}

	public List<Excursion> getExcursions() {
		return excursions;
	}

	public void setExcursions(List<Excursion> excursions) {
		this.excursions = excursions;
	}

	public void navigateTrail() {
		//Temp
		System.out.println("Navigating trail: " + name);
	}

	public int getDistance() {
		return distance;
	}
}
