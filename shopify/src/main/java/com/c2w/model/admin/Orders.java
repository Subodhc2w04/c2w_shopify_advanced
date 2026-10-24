package com.c2w.model.admin;

import java.util.List;

public class Orders {

    private double deliveryCharges;
    private double discount;
    private String expectedDelivery;
    private double finalAmount;
    private List<OrderItem> items;
    private String orderDate;
    private String orderId;
    private String paymentMethod;
    private String paymentStatus;
    private ShippingAddress shippingAddress;
    private String status;
    private double totalMRP;

    private String documentId;

    public Orders() {
    }

    public Orders(
            double deliveryCharges,
            double discount,
            String expectedDelivery,
            double finalAmount,
            List<OrderItem> items,
            String orderDate,
            String orderId,
            String paymentMethod,
            String paymentStatus,
            ShippingAddress shippingAddress,
            String status,
            double totalMRP,
            String documentId) {

        this.deliveryCharges = deliveryCharges;
        this.discount = discount;
        this.expectedDelivery = expectedDelivery;
        this.finalAmount = finalAmount;
        this.items = items;
        this.orderDate = orderDate;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.shippingAddress = shippingAddress;
        this.status = status;
        this.totalMRP = totalMRP;
        this.documentId = documentId;
    }

    public double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(String expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalMRP() {
        return totalMRP;
    }

    public void setTotalMRP(double totalMRP) {
        this.totalMRP = totalMRP;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Override
    public String toString() {
        return "Orders [deliveryCharges=" + deliveryCharges + ", discount=" + discount + ", expectedDelivery="
                + expectedDelivery + ", finalAmount=" + finalAmount + ", items=" + items + ", orderDate=" + orderDate
                + ", orderId=" + orderId + ", paymentMethod=" + paymentMethod + ", paymentStatus=" + paymentStatus
                + ", shippingAddress=" + shippingAddress + ", status=" + status + ", totalMRP=" + totalMRP
                + ", documentId=" + documentId + "]";
    }
}
