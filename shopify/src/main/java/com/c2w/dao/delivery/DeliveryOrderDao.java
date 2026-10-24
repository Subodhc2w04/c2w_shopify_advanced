package com.c2w.dao.delivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.c2w.config.FirebaseInitialize;
import com.c2w.model.delivery.Order;
import com.c2w.view.authentication.AuthPage;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

public class DeliveryOrderDao {
    private Firestore db = FirebaseInitialize.getFirestore();

    // GET ALL ORDERS READY FOR DISPATCH
    public List<Order> getOrderData() {

        List<Order> orderList = new ArrayList<>();

        try {

            QuerySnapshot data = db.collection("Orders").whereEqualTo("status", "ORDER_READY_FOR_DISPATCH").get().get();

            if (!data.isEmpty()) {

                for (DocumentSnapshot orderData : data.getDocuments()) {
                    List<String> rejectedEmails = (List<String>) orderData.get("rejectedBy");
                    if (rejectedEmails == null || !rejectedEmails.contains(AuthPage.email)) {
                        Map<String, Object> shippingAddress = (Map<String, Object>) orderData.get("shippingAddress");

                        String customerName = "";
                        String phone = "";

                        if (shippingAddress != null) {
                            customerName = (String) shippingAddress.get("fullName");
                            phone = (String) shippingAddress.get("mobile");
                        }

                        List<Map<String, Object>> items = (List<Map<String, Object>>) orderData.get("items");

                        int itemCount = 0;

                        if (items != null) {

                            for (Map<String, Object> item : items) {

                                Long quantity = (Long) item.get("quantity");

                                if (quantity != null) {
                                    itemCount += quantity.intValue();
                                }
                            }
                        }

                        int amount = 0;

                        Double finalAmount = orderData.getDouble("finalAmount");

                        if (finalAmount != null) {
                            amount = finalAmount.intValue();
                        }

                        String deliveryAddress = formatShippingAddress(shippingAddress);

                        Order orderObj = new Order(
                                orderData.getString("orderId"),
                                customerName,
                                phone,
                                itemCount,
                                amount,
                                deliveryAddress,
                                orderData.getString("status"),
                                orderData.getString("paymentMethod"),
                                orderData.getString("expectedDelivery"),
                                orderData.getString("orderDate"),
                                orderData.getString("paymentStatus"),
                                orderData.getDouble("deliveryCharges"),
                                orderData.getString("CodURL"));

                        orderList.add(orderObj);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderList;
    }

    // GET ONLY RECENT ORDERS
    public List<Order> getRecentOrders() {
        List<Order> allOrders = getOrderData();
        if (allOrders.size() <= 3) {
            return allOrders;
        }
        return allOrders.subList(0, 3);
    }

    // ONLY FOR STATUS UPDATE
    public void updateDeliveryOrderStatus(String status, String orderId) {
        try {
            db.collection("Orders").document(orderId).update("status", status).get();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // AFTER ACCEPTING ORDER ONCE SEND MAIL OF DELIVERY PERSON AND STATUS
    public void acceptOrder(String orderId, String deliveryPersonEmail, String status) {
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("status", status);
            updates.put("deliveryPersonEmail", deliveryPersonEmail);

            db.collection("Orders").document(orderId).update(updates).get();
            System.out.println("Order #" + orderId + " accepted by " + deliveryPersonEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ACCEPTED ORDER TO SHOW ON CURRENT ORDER PAGE
    public Order currentOrder(String email) {

        try {

            QuerySnapshot data = db.collection("Orders")
                    .whereEqualTo("deliveryPersonEmail", email)
                    .whereIn("status", List.of(
                            "ACCEPTED",
                            "PICKED_UP",
                            "OUT_FOR_DELIVERY"))
                    .get()
                    .get();

            if (!data.isEmpty()) {

                DocumentSnapshot orderData = data.getDocuments().get(0);

                Map<String, Object> shippingAddress = (Map<String, Object>) orderData.get("shippingAddress");

                String customerName = "";
                String phone = "";
                String deliveryAddress = formatShippingAddress(shippingAddress);

                if (shippingAddress != null) {
                    customerName = (String) shippingAddress.get("fullName");
                    phone = (String) shippingAddress.get("mobile");
                }

                List<Map<String, Object>> items = (List<Map<String, Object>>) orderData.get("items");

                int itemCount = 0;

                if (items != null) {

                    for (Map<String, Object> item : items) {

                        Long quantity = (Long) item.get("quantity");

                        if (quantity != null) {
                            itemCount += quantity.intValue();
                        }
                    }
                }

                int amount = 0;

                Double finalAmount = orderData.getDouble("finalAmount");

                if (finalAmount != null) {
                    amount = finalAmount.intValue();
                }

                return new Order(
                        orderData.getString("orderId"),
                        customerName,
                        phone,
                        itemCount,
                        amount,
                        deliveryAddress,
                        orderData.getString("status"),
                        orderData.getString("paymentMethod"),
                        orderData.getString("expectedDelivery"),
                        orderData.getString("orderDate"),
                        orderData.getString("paymentStatus"),
                        orderData.getDouble("deliveryCharges"),
                        orderData.getString("CodURL"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // IF USER HAVE CURRENT ACCEPTED ORDER THEN TO RESTRICT FOR ACCEPTING ANOTHER
    // ORDER
    public boolean hasCurrentOrder(String email) {

        try {

            QuerySnapshot querySnapshot = db.collection("Orders")
                    .whereEqualTo("deliveryPersonEmail", email)
                    .whereIn("status", List.of(
                            "ACCEPTED",
                            "PICKED_UP",
                            "OUT_FOR_DELIVERY"))
                    .get()
                    .get();

            return !querySnapshot.isEmpty();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // GET ALL DELIVERED ORDERS FOR THE DELIVERY PERSON
    public List<Order> getDeliveredOrders(String email) {

        List<Order> orderList = new ArrayList<>();

        try {
            QuerySnapshot data = db.collection("Orders")
                    .whereEqualTo("deliveryPersonEmail", email)
                    .whereEqualTo("status", "DELIVERED")
                    .get()
                    .get();

            if (!data.isEmpty()) {

                for (DocumentSnapshot orderData : data.getDocuments()) {

                    Map<String, Object> shippingAddress = (Map<String, Object>) orderData.get("shippingAddress");

                    String customerName = "";
                    String phone = "";
                    String deliveryAddress = formatShippingAddress(shippingAddress);

                    if (shippingAddress != null) {
                        customerName = (String) shippingAddress.get("fullName");
                        phone = (String) shippingAddress.get("mobile");
                    }

                    List<Map<String, Object>> items = (List<Map<String, Object>>) orderData.get("items");

                    int itemCount = 0;

                    if (items != null) {

                        for (Map<String, Object> item : items) {

                            Long quantity = (Long) item.get("quantity");

                            if (quantity != null) {
                                itemCount += quantity.intValue();
                            }
                        }
                    }

                    int amount = 0;

                    Double finalAmount = orderData.getDouble("finalAmount");

                    if (finalAmount != null) {
                        amount = finalAmount.intValue();
                    }

                    Double charges = orderData.getDouble("deliveryCharges");

                    Order orderObj = new Order(
                            orderData.getString("orderId"),
                            customerName,
                            phone,
                            itemCount,
                            amount,
                            deliveryAddress,
                            orderData.getString("status"),
                            orderData.getString("paymentMethod"),
                            orderData.getString("expectedDelivery"),
                            orderData.getString("orderDate"),
                            orderData.getString("paymentStatus"),
                            charges != null ? charges : 0.0,
                            orderData.getString("CodURL"));

                    orderList.add(orderObj);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderList;
    }

    // TO GET SPECIFIC FIELD FROM ORDERS COLLECTION
    public String getOrderData(String orderId, String fieldName) {
        try {
            DocumentSnapshot doc = db.collection("Orders").document(orderId).get().get();
            if (doc.exists()) {
                return doc.getString(fieldName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // TO UPDATE PAYMENT STATUS
    public void updatePaymentStatus(String orderId, String paymentStatus) {
        try {
            db.collection("Orders").document(orderId).update("paymentStatus", paymentStatus).get();
            System.out.println("Payment status for Order #" + orderId + " updated to " + paymentStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatShippingAddress(Map<String, Object> shippingAddress) {
        if (shippingAddress == null) {
            return "Pune, Maharashtra";
        }

        String street = (String) shippingAddress.get("street");
        String city = (String) shippingAddress.get("city");
        String state = (String) shippingAddress.get("state");
        String pinCode = (String) shippingAddress.get("pinCode");

        List<String> parts = new ArrayList<>();
        if (street != null && !street.trim().isEmpty()) {
            parts.add(street.trim());
        }
        if (city != null && !city.trim().isEmpty()) {
            parts.add(city.trim());
        }
        if (state != null && !state.trim().isEmpty()) {
            parts.add(state.trim());
        }

        String mainAddr = String.join(", ", parts);
        if (pinCode != null && !pinCode.trim().isEmpty()) {
            if (!mainAddr.isEmpty()) {
                mainAddr += " - " + pinCode.trim();
            } else {
                mainAddr = pinCode.trim();
            }
        }

        if (mainAddr.trim().isEmpty()) {
            String addressType = (String) shippingAddress.get("addressType");
            if (addressType != null && !addressType.trim().isEmpty()) {
                return addressType.trim();
            }
            return "Pune, Maharashtra";
        }

        return mainAddr;
    }

    // WHEN DELIVERY PERSON REJECTS ORDER
    public void rejectOrder(String email, String orderId) {
        try {
            db.collection("Orders").document(orderId).update("rejectedBy", FieldValue.arrayUnion(email)).get();
            System.out.println("Order #" + orderId + " rejected by " + email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TO CHECK PIN BEFORE MARK AS DELIVERED
    public boolean checkPin(String orderId, Long enteredPin) {

        try {

            DocumentSnapshot orderData = db
                    .collection("Orders")
                    .document(orderId)
                    .get()
                    .get();

            Long     firebasePin = orderData.getLong("Pin");
            System.out.println("USER PIN IS -" + firebasePin + " YOUR PIN IS - " + enteredPin + " FOR " + orderId);

            if (firebasePin == null) {
                return false;
            }

            return firebasePin.equals(enteredPin);

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

}
