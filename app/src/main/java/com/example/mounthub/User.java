package com.example.mounthub;

import androidx.annotation.NonNull;

public class User {
    /*
    username: String
    -id: Int
    -email: String
    -uploadedTrails: List<Trail>
    -completedTrails: List<Trail>
    -friends: List<User>
    -notifications: List<Notification>
    -info: String
     */
    private int id;
    private String username;
    private String email;
    private String password;
    private String info;

    // Constructors, getters, and setters
    public User(int id, String username, String email, String password, String info) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.info = info;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", info='" + info + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
