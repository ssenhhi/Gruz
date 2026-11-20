package com.example.gruz.models;

public class OrderResponse {
    private String id;
    private String number;
    private String status;
    private String comment;
    private String cityFrom;
    private String cityTo;
    private String streetFrom;
    private String streetTo;

    public String getId() { return id; }
    public String getNumber() { return number; }
    public String getStatus() { return status; }
    public String getComment() { return comment; }
    public String getCityFrom() { return cityFrom; }
    public String getCityTo() { return cityTo; }
    public String getStreetFrom() { return streetFrom; }
    public String getStreetTo() { return streetTo; }
}
