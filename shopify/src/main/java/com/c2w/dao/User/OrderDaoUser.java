package com.c2w.dao.User;

import com.c2w.config.FirebaseInitialize;
import com.c2w.model.user.Address;
import com.c2w.model.user.AddToCart;
import com.c2w.model.user.OrderUser;
import com.c2w.view.authentication.AuthPage;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.c2w.view.user.CheckoutView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderDaoUser {

    private Firestore db = FirebaseInitialize.getFirestore();

    

    public void addOrder(OrderUser order) {
        System.out.println("-----------------CODURL:"+order.getCodUrl());
                System.out.println("-----------------CODURL:"+CheckoutView.codUrl);

        try {
            String email = (order.getEmail() != null && !order.getEmail().trim().isEmpty())
                    ? order.getEmail().trim()
                    : (AuthPage.email != null ? AuthPage.email.trim() : "");

            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("Email", email);
            orderMap.put("orderId", order.getOrderId());
            orderMap.put("Pin", order.getPin());
            orderMap.put("orderDate", order.getOrderDate());
            orderMap.put("expectedDelivery", order.getExpectedDelivery());
            orderMap.put("discount", order.getDiscount());
            orderMap.put("deliveryCharges", order.getDeliveryCharges());
            orderMap.put("finalAmount", order.getFinalAmount());
            orderMap.put("Amount", order.getFinalAmount());
            orderMap.put("paymentMethod", order.getPaymentMethod());
            orderMap.put("paymentStatus", order.getPaymentStatus());
            orderMap.put("status", order.getStatus().toUpperCase());
            
                orderMap.put("CodURL",order.getCodUrl());
            
            

            if (order.getShippingAddress() != null) {
                Map<String, Object> addrMap = new HashMap<>();
                addrMap.put("fullName", order.getShippingAddress().getFullName());
                addrMap.put("mobile", order.getShippingAddress().getMobile());
                addrMap.put("street", order.getShippingAddress().getStreet());
                addrMap.put("city", order.getShippingAddress().getCity());
                addrMap.put("state", order.getShippingAddress().getState());
                addrMap.put("pinCode", order.getShippingAddress().getPinCode());
                addrMap.put("addressType", order.getShippingAddress().getAddressType());
                addrMap.put("isDefault", order.getShippingAddress().isDefault());
                orderMap.put("shippingAddress", addrMap);
            }

            List<Map<String, Object>> itemsList = new ArrayList<>();
            if (order.getItems() != null) {
                for (AddToCart item : order.getItems()) {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("productId", item.getProductId());
                    itemMap.put("productName", item.getProductName());
                    itemMap.put("productPrice", item.getProductPrice());
                    itemMap.put("productImg", item.getProductImg());
                    itemMap.put("productOrignalPrice", item.getProductOrignalPrice());
                    itemMap.put("quantity", item.getQuantity());
                    itemMap.put("discountValue", item.getDiscountValue());
                    itemsList.add(itemMap);
                }
            }
            orderMap.put("items", itemsList);

            db.collection("Orders")
                    .document(order.getOrderId())
                    .set(orderMap)
                    .get();

            System.out.println("===================Order Data Inserted: " + order.getOrderId() + "=====================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<OrderUser> getOrders(String email) {
        List<OrderUser> orders = new ArrayList<>();
        try {
            String targetEmail = (email != null && !email.trim().isEmpty())
                    ? email.trim()
                    : (AuthPage.email != null ? AuthPage.email.trim() : "");

            if (targetEmail.isEmpty()) {
                System.out.println("OrderDaoUser.getOrders: email is empty, returning empty list");
                return orders;
            }

            Set<String> seenIds = new HashSet<>();

            // 1. Check with "Email"
            var docs1 = db.collection("Orders")
                    .whereEqualTo("Email", targetEmail)
                    .get()
                    .get()
                    .getDocuments();

            for (DocumentSnapshot doc : docs1) {
                OrderUser o = mapDocumentToOrder(doc);
                if (o != null && seenIds.add(o.getOrderId())) {
                    orders.add(o);
                }
            }

            // 2. Check with "email"
            var docs2 = db.collection("Orders")
                    .whereEqualTo("email", targetEmail)
                    .get()
                    .get()
                    .getDocuments();

            for (DocumentSnapshot doc : docs2) {
                OrderUser o = mapDocumentToOrder(doc);
                if (o != null && seenIds.add(o.getOrderId())) {
                    orders.add(o);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("============== Fetch orders for: " + email + " -> count: " + orders.size() + " ==============");
        return orders;
    }

    private OrderUser mapDocumentToOrder(DocumentSnapshot doc) {
        if (doc == null || !doc.exists()) return null;
        try {
            String email = doc.getString("Email");
            if (email == null) email = doc.getString("email");

            String orderId = doc.getString("orderId");
            if (orderId == null) orderId = doc.getId();

            Long pin = doc.getLong("Pin");

            if (pin == null) {
                pin = 0L;
            }
                    

            String orderDate = doc.getString("orderDate");
            String expectedDelivery = doc.getString("expectedDelivery");

            double totalMRP = getDoubleValue(doc.get("totalMRP"));
            double discount = getDoubleValue(doc.get("discount"));
            double deliveryCharges = getDoubleValue(doc.get("deliveryCharges"));
            double finalAmount = getDoubleValue(doc.get("finalAmount"));
            if (finalAmount == 0.0) finalAmount = getDoubleValue(doc.get("Amount"));

            String paymentMethod = doc.getString("paymentMethod");
            String paymentStatus = doc.getString("paymentStatus");
            String CodUrl=doc.getString("CodURL");
            String status = doc.getString("status");
            if (status == null) status = "Confirmed";

            List<AddToCart> items = new ArrayList<>();
            Object itemsObj = doc.get("items");
            if (itemsObj instanceof List<?>) {
                for (Object itemObj : (List<?>) itemsObj) {
                    if (itemObj instanceof Map<?, ?>) {
                        Map<?, ?> map = (Map<?, ?>) itemObj;
                        String pid = map.get("productId") != null ? map.get("productId").toString() : "";
                        String pName = map.get("productName") != null ? map.get("productName").toString() : "";
                        double pPrice = getDoubleValue(map.get("productPrice"));
                        String pImg = map.get("productImg") != null ? map.get("productImg").toString() : "";
                        double origPrice = getDoubleValue(map.get("productOrignalPrice"));
                        int qty = getIntValue(map.get("quantity"));
                        double discVal = getDoubleValue(map.get("discountValue"));

                        AddToCart cartItem = new AddToCart(pName, pid, pImg, pPrice, origPrice, qty > 0 ? qty : 1);
                        cartItem.setDiscountValue(discVal);
                        items.add(cartItem);
                    }
                }
            }

            Address address = null;
            Object addrObj = doc.get("shippingAddress");
            if (addrObj instanceof Map<?, ?>) {
                Map<?, ?> am = (Map<?, ?>) addrObj;
                String fullName = am.get("fullName") != null ? am.get("fullName").toString() : "";
                String mobile = am.get("mobile") != null ? am.get("mobile").toString() : "";
                String street = am.get("street") != null ? am.get("street").toString() : "";
                String city = am.get("city") != null ? am.get("city").toString() : "";
                String state = am.get("state") != null ? am.get("state").toString() : "";
                String pinCode = am.get("pinCode") != null ? am.get("pinCode").toString() : "";
                String addressType = am.get("addressType") != null ? am.get("addressType").toString() : "Home";
                boolean isDefault = Boolean.TRUE.equals(am.get("isDefault"));

                address = new Address(fullName, mobile, street, city, state, pinCode, addressType, isDefault);
            }

            return new OrderUser(email, orderId,pin, orderDate, items, discount, deliveryCharges, finalAmount,
                    paymentMethod, paymentStatus, status, address, expectedDelivery,CodUrl);
        } catch (Exception e) {
            System.err.println("Error mapping order document: " + doc.getId());
            e.printStackTrace();
            return null;
        }
    }

    private double getDoubleValue(Object val) {
        if (val instanceof Number) {
            return ((Number) val).doubleValue();
        } else if (val != null) {
            try {
                return Double.parseDouble(val.toString());
            } catch (Exception ignored) {}
        }
        return 0.0;
    }

    private int getIntValue(Object val) {
        if (val instanceof Number) {
            return ((Number) val).intValue();
        } else if (val != null) {
            try {
                return Integer.parseInt(val.toString());
            } catch (Exception ignored) {}
        }
        return 1;
    }

    public void updateReviewRatings(
            String productId,
            Map<String, Object> dataMap) {

        try {

            DocumentSnapshot document = db.collection("Products").document(productId).get().get();

            List<Map<String, Object>> ratingList = (List<Map<String, Object>>) document.get("reviews");

            if (ratingList == null) {

                ratingList = new ArrayList<>();
            }

            ratingList.add(dataMap);

            db.collection("Products").document(productId).update("reviews",ratingList);

            System.out.println("Review added successfully");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> getReviewsData(String productId) {
        List<Map<String, Object>> reviews = new ArrayList<>();
        try {
            DocumentSnapshot document = db.collection("Products").document(productId).get().get();
            List<Map<String, Object>> ratingList = (List<Map<String, Object>>) document.get("reviews");
            if (ratingList != null) {
                reviews.addAll(ratingList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
