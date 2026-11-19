package com.example.gruz.models;

public class Order {
    private String id;
    private String number;
    private String status;
    private String comment;

    public Order(String id, String number, String status, String comment) {
        this.id = id;
        this.number = number;
        this.status = status;
        this.comment = comment;
    }

    public String getId() { return id; }
    public String getNumber() { return number; }
    public String getStatus() { return status; }
    public String getComment() { return comment; }
}
