package com.example.architecture.models;


public class TeamMember extends User {
    private String role;
    private String profileImageUrl;

    // Default constructor
    public TeamMember() {
        super();
    }

    // Constructor with all fields, including inherited ones
    public TeamMember(String name, String phone, String email, String password, String role, String profileImageUrl) {
        super(name, phone, email, password); // Call User constructor to set inherited fields
        this.role = role;
        this.profileImageUrl = profileImageUrl;
    }

    // Getters and Setters for additional fields
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
