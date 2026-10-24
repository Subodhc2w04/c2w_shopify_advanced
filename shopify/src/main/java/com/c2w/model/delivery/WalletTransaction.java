package com.c2w.model.delivery;



public class WalletTransaction {
    private String orderId;
    private int amount;
    private String date;
    private String type;

    public WalletTransaction(String orderId, int amount, String date, String type) {
        this.orderId = orderId;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
