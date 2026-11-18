package com.example.gruz;

public class OrderCreateRequest {
    private String number;
    private String status;
    private String comment;
    private String user; // ID пользователя

    public OrderCreateRequest(String number, String status, String comment, String user) {
        this.number = number;
        this.status = status;
        this.comment = comment;
        this.user = user;
    }
}
