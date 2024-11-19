package com.example.architecture.models;

public class Requester {

    private String requestId; // Unique ID for the request
    private String recipientId; // Email ID of the recipient
    private String requesterId; // Email ID of the requester
    private String profileImageUrl; // URL of the requester's profile image
    private String status; // Status of the request (e.g., "pending", "accepted", "rejected")
    private long timestamp; // Timestamp for when the request was created

    // Default constructor required for Firebase
    public Requester() { }

    public Requester(String requestId, String recipientId, String requesterId, String profileImageUrl, String status, long timestamp) {
        this.requestId = requestId;
        this.recipientId = recipientId;
        this.requesterId = requesterId;
        this.profileImageUrl = profileImageUrl;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters and Setters

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
