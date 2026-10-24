package com.c2w.model.admin;

import com.google.cloud.Timestamp;

public class DeliverySignUpNotifications {
    private String address;
    private String approveStatus;
    private String email;
    private String id;
    private String name;
    private String phone;
    private String profileCompleted;
    private String vehicleNumber;
    private String vehicleType;
    private String notiType;
    public Timestamp createdAt;

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getNotiType() {
        return notiType;
    }

    public void setNotiType(String notiType) {
        this.notiType = notiType;
    }

    public DeliverySignUpNotifications() {
    }

    public DeliverySignUpNotifications(String address, String approveStatus,
            String email, String id, String name,
            String phone, String profileCompleted,
            String vehicleNumber, String vehicleType,String notiType) {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String isProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(String profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
