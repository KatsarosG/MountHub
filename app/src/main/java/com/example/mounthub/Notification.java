package com.example.mounthub;

public class Notification {
    private String message;
    private boolean seen;

    public Notification(String message) {
        this.message = message;
        this.seen = false;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSeen() {
        return seen;
    }

    public void markAsSeen() {
        this.seen = true;
    }
}