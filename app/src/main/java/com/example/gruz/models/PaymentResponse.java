package com.example.gruz.models;

public class PaymentResponse {
    private int preliminary_cost;
    private String method;
    private String payment_status;

    public int getPreliminaryCost() { return preliminary_cost; }
    public String getMethod() { return method; }
    public String getPaymentStatus() { return payment_status; }
}
