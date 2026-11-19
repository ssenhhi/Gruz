package com.example.gruz.models;

public class PaymentCreateRequest {
    private String order;
    private String method;
    private int preliminary_cost;
    private String payment_status;

    public PaymentCreateRequest(String order, String method, int preliminary_cost, String payment_status) {
        this.order = order;
        this.method = method;
        this.preliminary_cost = preliminary_cost;
        this.payment_status = payment_status;
    }
}
