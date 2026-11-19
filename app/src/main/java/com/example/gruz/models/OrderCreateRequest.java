package com.example.gruz.models;

public class OrderCreateRequest {
    private String number;
    private String status;
    private String comment;
    private String user;

    public OrderCreateRequest(String number, String status, String comment, String user) {
        this.number = number;
        this.status = status;
        this.comment = comment;
        this.user = user;
    }
}
