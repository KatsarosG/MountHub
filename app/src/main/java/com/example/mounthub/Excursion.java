package com.example.mounthub;

import java.util.Date;
import java.util.List;

public class Excursion {
	private final int ID;
	private final Trail TRAIL;
	private Date date;
	private List<User> participants;
	private String info;
	
	// Constructor
	public Excursion(int ID, Trail TRAIL, Date date, List<User> participants, String info) {
		this.ID = ID;
		this.TRAIL = TRAIL;
		this.date = date;
		this.participants = participants;
		this.info = info;
	}

	public int getID() {
		return ID;
	}

	public Trail getTRAIL() {
		return TRAIL;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<User> getParticipants() {
		return participants;
	}

	public void setParticipants(List<User> participants) {
		this.participants = participants;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
