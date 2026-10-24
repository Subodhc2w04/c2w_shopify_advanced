package com.c2w.model.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.c2w.view.authentication.AuthPage;

public class OrderRepository {
    private static OrderRepository instance;
    private final List<OrderUser> orders = new ArrayList<>();
    private final List<Runnable> changeListeners = new ArrayList<>();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private OrderRepository() {
        // seedSampleOrders();
    }

    public static synchronized OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    public void addListener(Runnable listener) {
        if (listener != null && !changeListeners.contains(listener)) {
            changeListeners.add(listener);
        }
    }

    private void notifyListeners() {
        for (Runnable r : changeListeners) {
            try {
                r.run();
            } catch (Exception ignored) {
            }
        }
    }



    public List<OrderUser> getAll() {
        return Collections.unmodifiableList(orders);
    }

    public Optional<OrderUser> getById(String id) {
        return orders.stream().filter(o -> o.getOrderId().equalsIgnoreCase(id)).findFirst();
    }

    public OrderUser createOrder(
        List<AddToCart> cartItems,
         Address address, String paymentMethod,
            double totalMRP, double discount, double delivery, double finalAmount,String codUrl) {
        String orderId = "ODR-" + (100000 + new Random().nextInt(900000));
        long pin = 100000 + new Random().nextInt(900000);
        String today = LocalDate.now().format(dateFormatter);
        String expected = LocalDate.now().plusDays(3).format(dateFormatter);

        // List<TrackingStage> stages = List.of(
        //         new TrackingStage("Order Placed", "Your order has been placed successfully", today + ", Just now",
        //                 true, true));

        String paymentStatus = paymentMethod.toLowerCase().contains("cash") ? "PENDING (COD)" : "PAID";

        OrderUser newOrder = new OrderUser(AuthPage.email,orderId,pin, today, 
                 new ArrayList<>(cartItems),
                 discount, delivery, finalAmount,
                paymentMethod, paymentStatus, "Order Placed",
                address, expected,codUrl);


        orders.add(0, newOrder);
        notifyListeners();
        return newOrder;
    }
}
