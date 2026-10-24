package com.c2w.controller.delivery;

import java.util.ArrayList;
import java.util.List;

import com.c2w.dao.delivery.DeliveryOrderDao;
import com.c2w.dao.delivery.DeliveryOrderNotificationDao;
import com.c2w.dao.delivery.DeliveryPersonDao;
import com.c2w.model.delivery.DeliveryOrderNotification;
import com.c2w.model.delivery.Order;
import com.c2w.model.delivery.RouteInfo;
import com.c2w.model.delivery.WalletTransaction;
import com.google.cloud.Timestamp;

public class OrderController {

    private DeliveryOrderDao orderDAO = new DeliveryOrderDao();
    private DeliveryPersonDao deliveryPersonDAO = new DeliveryPersonDao();
    private DeliveryOrderNotificationDao notifDAO = new DeliveryOrderNotificationDao();
    private LocationController locationController = new LocationController();

    private static List<WalletTransaction> transactions = new ArrayList<>();

    // Get all orders from Firestore
    public List<Order> getAllOrders() {

        return orderDAO.getOrderData();
    }

    // Get orders according to status
    public List<Order> getOrdersByStatus(String status) {

        List<Order> orders = orderDAO.getOrderData();

        if ("ALL".equalsIgnoreCase(status)) {
            return orders;
        }

        List<Order> result = new ArrayList<>();

        for (Order order : orders) {

            if (status.equalsIgnoreCase(order.getStatus())) {
                result.add(order);
            }
        }

        return result;
    }

    // Wallet transactions
    public List<WalletTransaction> getTransactions() {

        return transactions;
    }

    // TO UPDATE STATUS OF ORDER FROM CURRENT ORDER PAGE
    public void updateDeliveryOrderStatus(String status, String orderId) {
        orderDAO.updateDeliveryOrderStatus(status, orderId);
    }

    // TO GET SPECIFIC FIELD FROM ORDERS COLLECTION
    public String getOrderData(String orderId, String fieldName) {
        return orderDAO.getOrderData(orderId, fieldName);
    }

    // TO UPDATE PAYMENT STATUS (e.g. PAID)
    public void updatePaymentStatus(String orderId, String paymentStatus) {
        orderDAO.updatePaymentStatus(orderId, paymentStatus);
    }

    // TO ADD EMAIL OF DELIVERY PERSON AFTER ACCEPTED ORDER

    public void updateAcceptedOrder(String orderId, String deliveryPersonEmail, String status) {

        orderDAO.acceptOrder(orderId, deliveryPersonEmail, status);
    }

    // TO GET CURRENT ACCEPTED ORDER
    public Order getCurrentOrder(String email) {

        return orderDAO.currentOrder(email);
    }

    // TO CHECK IF DELIVERY PERSON HAVE ALREADY ACCEPTED ORDER
    public boolean hasCurrentOrder(String email) {

        return orderDAO.hasCurrentOrder(email);
    }

    public void addMoney(String email, double amount) {
        deliveryPersonDAO.addMoney(email, amount);
    }

    public double getWalletBalance(String email) {
        return deliveryPersonDAO.getWalletBalance(email);
    }

    public List<Order> getDeliveredOrders(String email) {
        return orderDAO.getDeliveredOrders(email);
    }

    public List<WalletTransaction> getTransactions(String email) {
        List<Order> deliveredOrders = orderDAO.getDeliveredOrders(email);
        List<WalletTransaction> list = new ArrayList<>();
        for (Order order : deliveredOrders) {
            int charges = (int) order.getDeliveryCharges();
            String date = order.getOrderTime() != null && !order.getOrderTime().isEmpty()
                    ? order.getOrderTime()
                    : (order.getExpectedDate() != null ? order.getExpectedDate() : "Delivered");
            list.add(new WalletTransaction(order.getOrderId(), charges, date, "Delivery Payout"));
        }
        return list;
    }

    public List<Order> getRecentOrders() {
        return orderDAO.getRecentOrders();
    }

    // TO SEND ORDER STATUS NOTIFICATION TO ADMIN
    public void sendNotification(String orderId, String email, String name, String status, String message) {
        DeliveryOrderNotification notif = new DeliveryOrderNotification(
                orderId,
                email,
                name,
                status,
                message,
                Timestamp.now());
        notifDAO.sendNotificationToAdmin(notif);
    }

    public LocationController getLocationController() {
        return locationController;
    }

    public RouteInfo getRouteForOrder(Order order) {
        return locationController.getRouteForOrder(order);
    }

    public RouteInfo getRouteForActiveDelivery(String email) {
        Order currentOrder = getCurrentOrder(email);
        if (currentOrder != null) {
            return locationController.getRouteForOrder(currentOrder);
        }
        return null;
    }

    public void rejectOrder(String email, String orderId) {
        orderDAO.rejectOrder(email, orderId);
    }

    public boolean checkPin(String orderId, Long enteredPin) {

        return orderDAO.checkPin(orderId, enteredPin);
    }
}