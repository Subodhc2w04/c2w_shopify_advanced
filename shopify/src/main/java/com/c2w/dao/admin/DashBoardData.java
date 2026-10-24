package com.c2w.dao.admin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.c2w.config.FirebaseInitialize;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class DashBoardData {
    Firestore db = FirebaseInitialize.getFirestore();

    public Map<String, Object> getCardsData() {
        double revenue = 0;
        int ordersCount = 0;
        int customerCount = 0;
        int productCount = 0;
        try {
            QuerySnapshot snap = db.collection("Orders").whereEqualTo("status", "DELIVERED").get().get();
            for (DocumentSnapshot doc : snap.getDocuments()) {
                revenue += doc.getDouble("finalAmount");
            }
            ordersCount = db.collection("Orders").get().get().size();
            customerCount = db.collection("Users").get().get().size();
            productCount = db.collection("Products").get().get().size();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Map.of("Revenue", revenue, "OrdersCount", ordersCount, "ProductsCount", productCount, "CustomerCount",
                customerCount);
    }

    public Map<String, Integer> getOrderStatusData() {

        int totalOrderedStatus = 0;
        int totalReadyForDispatchStatus = 0;
        int totalAcceptedStatus = 0;
        int totalPickUpStatus = 0;
        int totalOutForDeliveryStatus = 0;
        int totalDeliveredStatus = 0;

        try {

            QuerySnapshot snapshot = db.collection("Orders").get().get();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {

                String status = doc.getString("status");

                if ("ORDER_PLACED".equals(status)) {
                    totalOrderedStatus++;
                } else if ("ORDER_READY_FOR_DISPATCH".equals(status)) {
                    totalReadyForDispatchStatus++;
                } else if ("ACCEPTED".equals(status)) {
                    totalAcceptedStatus++;
                } else if ("PICKED_UP".equals(status)) {
                    totalPickUpStatus++;
                } else if ("ORDER_FOR_DELIVERY".equals(status)) {
                    totalOutForDeliveryStatus++;
                } else if ("DELIVERED".equals(status)) {
                    totalDeliveredStatus++;
                }
            }

        } catch (InterruptedException | ExecutionException e) {

            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        return Map.of(
                "TotalOrderedStatus", totalOrderedStatus,
                "TotalReadyForDispatchStatus", totalReadyForDispatchStatus,
                "TotalAcceptedStatus", totalAcceptedStatus,
                "TotalPickUpStatus", totalPickUpStatus,
                "TotalOutForDeliveryStatus", totalOutForDeliveryStatus,
                "TotalDeliveredStatus", totalDeliveredStatus);
    }

    public Map<String, Integer> getProductCategoryData() {

        int totalElectronics = 0;
        int totalClothing = 0;
        int totalBooks = 0;
        int totalShoes = 0;
        int totalAccessories = 0;

        try {

            QuerySnapshot snapshot = db.collection("Products").get().get();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {

                String category = doc.getString("productCategory");

                if ("Electronics".equals(category)) {
                    totalElectronics++;
                } else if ("Clothing".equals(category)) {
                    totalClothing++;
                } else if ("Books".equals(category)) {
                    totalBooks++;
                } else if ("Shoes".equals(category)) {
                    totalShoes++;
                } else if ("Accessories".equals(category)) {
                    totalAccessories++;
                }
            }

        } catch (InterruptedException | ExecutionException e) {

            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        return Map.of(
                "Electronics", totalElectronics,
                "Clothing", totalClothing,
                "Books", totalBooks,
                "Shoes", totalShoes,
                "Accessories", totalAccessories);
    }

    public Map<String, Integer> getTopSellingProducts() {

        Map<String, Integer> productSales = new HashMap<>();

        try {

            ApiFuture<QuerySnapshot> future = db.collection("Orders").get();

            QuerySnapshot snapshot = future.get();

            // Calculate total quantity sold for each product
            for (QueryDocumentSnapshot document : snapshot.getDocuments()) {

                List<Map<String, Object>> items = (List<Map<String, Object>>) (List<?>) document.get("items");

                if (items == null) {
                    continue;
                }

                for (Map<String, Object> item : items) {

                    String productName = (String) item.get("productName");

                    Object quantityObject = item.get("quantity");

                    if (productName == null || quantityObject == null) {
                        continue;
                    }

                    int quantity = ((Number) quantityObject).intValue();

                    int oldQuantity = productSales.getOrDefault(productName, 0);

                    productSales.put(
                            productName,
                            oldQuantity + quantity);
                }
            }

            // Convert Map entries into a List
            List<Map.Entry<String, Integer>> productList = new ArrayList<>(productSales.entrySet());

            // Sort the list by quantity - highest first
            productList.sort(
                    new Comparator<Map.Entry<String, Integer>>() {

                        @Override
                        public int compare(Map.Entry<String, Integer> entry1,Map.Entry<String, Integer> entry2) {

                            return Integer.compare(entry2.getValue(),entry1.getValue());
                        }
                    });

            // Create sorted Map
            Map<String, Integer> sortedProducts = new LinkedHashMap<>();

            for (Map.Entry<String, Integer> entry : productList) {

                sortedProducts.put(
                        entry.getKey(),
                        entry.getValue());
            }
            System.out.println("SortedProducts - "+sortedProducts);
            return sortedProducts;

        } catch (Exception e) {

            e.printStackTrace();

            return new LinkedHashMap<>();
        }
    }

    public Map<String, Integer> getOrderGrowth() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "dd MMM yyyy",
                Locale.ENGLISH);

        LocalDate today = LocalDate.now();

        LocalDate startDate = today.minusDays(6);

        Map<LocalDate, Integer> orderCount = new LinkedHashMap<>();

        // Create all 7 dates with initial value 0
        for (int i = 0; i < 7; i++) {

            LocalDate date = startDate.plusDays(i);

            orderCount.put(date, 0);
        }

        try {

            ApiFuture<QuerySnapshot> future = db.collection("Orders").get();

            QuerySnapshot snapshot = future.get();

            for (QueryDocumentSnapshot document : snapshot.getDocuments()) {

                String orderDateString = document.getString("orderDate");

                if (orderDateString == null) {
                    continue;
                }

                try {

                    LocalDate orderDate = LocalDate.parse(
                            orderDateString,
                            formatter);

                    if (!orderDate.isBefore(startDate)
                            && !orderDate.isAfter(today)) {

                        orderCount.put(
                                orderDate,
                                orderCount.get(orderDate) + 1);
                    }

                } catch (DateTimeParseException e) {

                    System.out.println(
                            "Invalid order date: "
                                    + orderDateString);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        // Convert LocalDate to String
        Map<String, Integer> result = new LinkedHashMap<>();

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM");

        for (Map.Entry<LocalDate, Integer> entry : orderCount.entrySet()) {

            String date = entry.getKey()
                    .format(outputFormatter);

            result.put(
                    date,
                    entry.getValue());
        }
        System.out.print("Result - "+result);
        return result;
    }
}
