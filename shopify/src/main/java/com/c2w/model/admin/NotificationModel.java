package com.c2w.model.admin;

import com.google.cloud.Timestamp;

public class NotificationModel {
    String email;
    String message;
    String notiType;
    String orderId;
    String role;
    String status;
    Timestamp timestamp;

    public NotificationModel(String email,String message, String notiType, String orderId, String role, String status,
            Timestamp timestamp) {
                this.email=email;
        this.message = message;
        this.notiType = notiType;
        this.orderId = orderId;
        this.role = role;
        this.status = status;
        this.timestamp = timestamp;
    }

    public NotificationModel(){

    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotiType() {
        return notiType;
    }

    public void setNotiType(String notiType) {
        this.notiType = notiType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    
}
