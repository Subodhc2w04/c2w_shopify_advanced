package com.c2w.model.delivery;

import com.google.cloud.Timestamp;

public class DeliveryOrderNotification {
    private String orderId;
    private String deliveryPersonEmail;
    private String deliveryPersonName;
    private String status;
    private String message;
    private Timestamp timestamp;

    public DeliveryOrderNotification() {
    }

    public DeliveryOrderNotification(String orderId, String deliveryPersonEmail, String deliveryPersonName,
            String status, String message, Timestamp timestamp) {
        this.orderId = orderId;
        this.deliveryPersonEmail = deliveryPersonEmail;
        this.deliveryPersonName = deliveryPersonName;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryPersonEmail() {
        return deliveryPersonEmail;
    }

    public void setDeliveryPersonEmail(String deliveryPersonEmail) {
        this.deliveryPersonEmail = deliveryPersonEmail;
    }

    public String getDeliveryPersonName() {
        return deliveryPersonName;
    }

    public void setDeliveryPersonName(String deliveryPersonName) {
        this.deliveryPersonName = deliveryPersonName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
