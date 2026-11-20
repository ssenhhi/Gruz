package com.example.gruz.models;

public class OrderCreateRequest {
    private String number;
    private String status;
    private String comment;
    private String user;
    private String cityFrom;
    private String cityTo;
    private String streetFrom;
    private String streetTo;

    public OrderCreateRequest(String number, String status, String comment, String user,
                              String cityFrom, String cityTo, String streetFrom, String streetTo) {
        this.number = number;
        this.status = status;
        this.comment = comment;
        this.user = user;
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.streetFrom = streetFrom;
        this.streetTo = streetTo;
    }
}
