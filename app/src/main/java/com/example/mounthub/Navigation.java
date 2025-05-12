package com.example.mounthub;

public class Navigation {

    private final Trail trail;
    private boolean isFinished;
    private boolean isPosted;
    private Coordinate currentLocation;

    public Navigation(Trail trail, Coordinate startingLocation) {
        this.trail = trail;
        this.currentLocation = startingLocation;
        this.isFinished = false;
        this.isPosted = false;
    }

    public void navigateTrail(Coordinate newLocation) {
        this.currentLocation = newLocation;
        //code used to follow the location that changes according to the users movement-coordinate changes and navigate an existing trail
    }

    public void postTrail(User user) {
        if (!isFinished) {
            display("You must finish the trail before posting it.");
            return;
        }
        if (isPosted) {
            display("Trail already posted to profile.");
            return;
        }
        //code to post the trail
        isPosted = true;
        display("Trail posted to your profile successfully.");
    }

    public void finishTrail() {
        if (!isFinished) {
            isFinished = true;
            display("You have finished the trail: " + trail.getName());

        } else {
            display("Trail was already marked as finished.");
        }
    }

    public void display(String message) {
        // For now, just print. Later: could update UI or logs.
        System.out.println(message);
    }
}


