package com.example.architecture.models;

import java.util.List;

public class Project {
    private String id; // Unique identifier for each project

    private String name;
    private String description;
    private String location;
    private String completionDate;
    private double budgetEstimate;
    private String projectType;
    private List<String> teamMembers;
    private List<String> images; // List of image URLs or file paths
    private String contactEmail; // Optional contact email for inquiries

    // Constructor
    public Project(String id, String name, String description, String location, String completionDate,
                   double budgetEstimate, String projectType, List<String> teamMembers,
                   List<String> images, String contactEmail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.completionDate = completionDate;
        this.budgetEstimate = budgetEstimate;
        this.projectType = projectType;
        this.teamMembers = teamMembers;
        this.images = images;
        this.contactEmail = contactEmail;
    }

    public Project(){}

    // Getters
    public String getId() {
        return id;
    }
    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public double getBudgetEstimate() {
        return budgetEstimate;
    }

    public String getProjectType() {
        return projectType;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }

    public List<String> getImages() {
        return images;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    // Method to format team members for display
    public String getFormattedTeamMembers() {
        return String.join(", ", teamMembers);
    }
}