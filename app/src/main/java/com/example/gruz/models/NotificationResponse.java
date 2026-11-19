package com.example.gruz.models;

public class NotificationResponse {
    private String id;
    private String message;
    private String status;
    private boolean is_read;
    private String sent_at;

    public String getId() { return id; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public boolean isRead() { return is_read; }
    public String getSentAt() { return sent_at; }
}
