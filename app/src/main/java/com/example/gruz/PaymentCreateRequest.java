package com.example.gruz;

public class PaymentCreateRequest {
    private String order; // ID заказа
    private String method; // "Карта", "Наличные", "Безналичный расчет"
    private int preliminary_cost; // Цена
    private String payment_status; // "Оплачено" или "Не оплачено"

    public PaymentCreateRequest(String order, String method, int preliminary_cost, String payment_status) {
        this.order = order;
        this.method = method;
        this.preliminary_cost = preliminary_cost;
        this.payment_status = payment_status;
    }
}
