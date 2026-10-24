package com.c2w.controller.user;

import java.util.List;
import java.util.Map;

import com.c2w.dao.User.OrderDaoUser;
import com.c2w.model.user.Address;
import com.c2w.model.user.AddToCart;
import com.c2w.model.user.OrderUser;
import com.c2w.view.authentication.AuthPage;

public class OrderControllerUser {
    private OrderDaoUser orderDaoUser = new OrderDaoUser();

    public void addOrder(String email, String orderId, long pin,String orderDate,
                         List<AddToCart> items,
                         double totalMRP, double discount, double deliveryCharges, double finalAmount,
                         String paymentMethod, String paymentStatus, String status,
                         Address shippingAddress,
                         String expectedDelivery,String codUrl) {

        String targetEmail = (email != null && !email.trim().isEmpty()) ? email.trim() : AuthPage.email;

        OrderUser orders = new OrderUser(targetEmail, orderId,pin, orderDate,
                items,
                 discount, deliveryCharges, finalAmount,
                paymentMethod, paymentStatus, status,
                shippingAddress,
                expectedDelivery,codUrl);

        orderDaoUser.addOrder(orders);
    }

    public List<OrderUser> getOrders() {
        return getOrders(AuthPage.email);
    }

    public List<OrderUser> getOrders(String email) {
        String targetEmail = (email != null && !email.trim().isEmpty()) ? email.trim() : AuthPage.email;
        return orderDaoUser.getOrders(targetEmail);
    }
    public void getUpdateRatings(String productId,Map<String,Object> dataMap){
        orderDaoUser.updateReviewRatings(productId,dataMap);
    }

    public List<Map<String, Object>> getReviews(String productId) {
        return orderDaoUser.getReviewsData(productId);
    }
}

