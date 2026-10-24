package com.c2w.model.delivery;

import com.google.cloud.Timestamp;

public class DeliveryPerson {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String vehicleType;
    private String vehicleNumber;
    private String address;
    private String profileCompleted;
    private String approvalStatus;
    private Timestamp createdAt;
    private String notificationType;
    private double walletBalance;
    private int totalOrders;

    public DeliveryPerson() {
    }

    public DeliveryPerson(String id, String name, String email, String phone, String vehicleType, String vehicleNumber,
            String address, String profileCompleted, String approvalStatus, Timestamp createdAt,
            String notificationType, double walletBalance) {
        this(id, name, email, phone, vehicleType, vehicleNumber, address, profileCompleted, approvalStatus, createdAt,
                notificationType, walletBalance, 0);
    }

    public DeliveryPerson(String id, String name, String email, String phone, String vehicleType, String vehicleNumber,
            String address, String profileCompleted, String approvalStatus, Timestamp createdAt,
            String notificationType, double walletBalance, int totalOrders) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.address = address;
        this.profileCompleted = profileCompleted;
        this.approvalStatus = approvalStatus;
        this.createdAt = createdAt;
        this.notificationType = notificationType;
        this.walletBalance = walletBalance;
        this.totalOrders = totalOrders;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getNotiType() {
        return notificationType;
    }

    public void setNotiType(String notiType) {
        this.notificationType = notiType;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String isProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(String profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    public String getApproveStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
