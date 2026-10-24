package com.c2w.model.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderUser {

    public OrderUser(){}
    private String email;
    private String orderId;
    private long pin;
    private String orderDate;
    private List<AddToCart> items;
    private double totalMRP;
    private double discount;
    private double deliveryCharges;
    private double Amount;
    private String paymentMethod;
    private String paymentStatus;
    private String status;
    private Address shippingAddress;
    private String expectedDelivery;
    private String codUrl;

    public OrderUser(String email, String orderId, long pin,String orderDate, 
                     List<AddToCart> items,
                      double discount, double deliveryCharges, double Amount,
                     String paymentMethod, String paymentStatus, String status,
                     Address shippingAddress, 
                     String expectedDelivery,String codUrl
) {
        this.email = email;
        this.orderId = orderId;
        this.pin=pin;
        this.orderDate = orderDate;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.totalMRP = totalMRP;
        this.discount = discount;
        this.deliveryCharges = deliveryCharges;
        this.Amount = Amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.expectedDelivery = expectedDelivery;
        this.codUrl=codUrl;
    }

    public String getemail() { return email; }public String getCodUrl() {
    return codUrl;
}

  public void setCodUrl(String codUrl) {
    this.codUrl = codUrl;
  }


    public void setemail(String email) { this.email = email; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public long getPin() {
        return pin;
    }

    public void setPin(long pin) {
        this.pin = pin;
    }

    public List<AddToCart> getItems() { return items != null ? Collections.unmodifiableList(items) : Collections.emptyList(); }
    public void setItems(List<AddToCart> items) { this.items = items != null ? new ArrayList<>(items) : new ArrayList<>(); }

    public double getTotalMRP() { return totalMRP; }
    public void setTotalMRP(double totalMRP) { this.totalMRP = totalMRP; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getDeliveryCharges() { return deliveryCharges; }
    public void setDeliveryCharges(double deliveryCharges) { this.deliveryCharges = deliveryCharges; }

    public double getFinalAmount() { return Amount; }
    public void setFinalAmount(double amount) { this.Amount = amount; }
    public double getAmount() { return Amount; }
    public void setAmount(double amount) { this.Amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Address getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(Address shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getExpectedDelivery() { return expectedDelivery; }
    public void setExpectedDelivery(String expectedDelivery) { this.expectedDelivery = expectedDelivery; }
}
