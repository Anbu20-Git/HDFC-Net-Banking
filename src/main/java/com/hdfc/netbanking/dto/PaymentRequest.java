package com.hdfc.netbanking.dto;

public class PaymentRequest {

    private Double amount;
    private String currency;
    private String order_id;

    public PaymentRequest() {
    }

    public PaymentRequest(
            Double amount,
            String currency,
            String order_id) {

        this.amount = amount;
        this.currency = currency;
        this.order_id = order_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}