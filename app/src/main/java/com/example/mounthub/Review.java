package com.example.mounthub;

import java.util.Date;

public class Review {
	private final int ID;
	private final User USER;
	private final Trail TRAIL;
	private int rating;
	private String comment;
	private Date date;

	// Constructor
	public Review(int ID, User USER, Trail TRAIL, int rating, String comment, Date date) {
		this.ID = ID;
		this.USER = USER;
		this.TRAIL = TRAIL;
		setRating(rating);  // Validate within setter
		this.comment = comment;
		this.date = date;
	}

	// Getters (no setters for ID, USER, TRAIL)
	public int getID() {
		return ID;
	}

	public User getUSER() {
		return USER;
	}

	public Trail getTRAIL() {
		return TRAIL;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		if (rating < 0 || rating > 5) {
			throw new IllegalArgumentException("Rating must be between 0 and 5.");
		}
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
