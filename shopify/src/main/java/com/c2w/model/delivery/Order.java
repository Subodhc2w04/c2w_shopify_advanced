package com.c2w.model.delivery;

public class Order {
    private String orderId;
    private String customerName;
    private String customerPhone;
    private int itemCount;
    private int amount;
    private String deliveryAddress;
    private String status;
    private String paymentMode;
    private String expectedDate;
    private String orderTime;
    private String paymentStatus;
    private double deliveryCharges; 
    private String codURL;

    public Order(String orderId, String customerName, String customerPhone, int itemCount, int amount,
            String deliveryAddress, String status, String paymentMode, String expectedDate, String orderTime,
            String paymentStatus, double deliveryCharges) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.itemCount = itemCount;
        this.amount = amount;

        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.paymentMode = paymentMode;
        this.expectedDate = expectedDate;
        this.orderTime = orderTime;
        this.paymentStatus = paymentStatus;
        this.deliveryCharges = deliveryCharges;
    }

    public Order(String orderId, String customerName, String customerPhone, int itemCount, int amount,
            String deliveryAddress, String status, String paymentMode, String expectedDate, String orderTime,
            String paymentStatus, double deliveryCharges, String codURL) {
        this(orderId, customerName, customerPhone, itemCount, amount, deliveryAddress, status, paymentMode,
                expectedDate, orderTime, paymentStatus, deliveryCharges);
        this.codURL = codURL;
    }

    public String getCodURL() {
        return codURL;
    }

    public void setCodURL(String codURL) {
        this.codURL = codURL;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }
}
