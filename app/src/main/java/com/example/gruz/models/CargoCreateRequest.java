package com.example.gruz.models;

public class CargoCreateRequest {
    private String order;
    private String cargo_type;
    private double weight;
    private double volume;
    private int length;
    private int width;
    private int height;
    public CargoCreateRequest(String order, String cargo_type, double weight, double volume, int length, int width, int height) {
        this.order = order;
        this.cargo_type = cargo_type;
        this.weight = weight;
        this.volume = volume;
        this.length = length;
        this.width = width;
        this.height = height;
    }
}
