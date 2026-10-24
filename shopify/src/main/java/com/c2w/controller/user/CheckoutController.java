package com.c2w.controller.user;

import com.c2w.model.user.Address;
import com.c2w.model.user.AddToCart;
import com.c2w.model.user.OrderUser;
import com.c2w.model.user.OrderRepository;
import com.c2w.view.authentication.AuthPage;
import com.c2w.view.user.ToastView;
import com.c2w.view.user.CheckoutView;
import com.c2w.view.user.MainUser;
import com.c2w.view.user.OrderSuccessView;

import java.util.ArrayList;
import java.util.List;


public class CheckoutController {
    private static CheckoutController instance;

    private CheckoutController() {}

    public static synchronized CheckoutController getInstance() {
        if (instance == null) {
            instance = new CheckoutController();
        }
        return instance;
    }

    public OrderUser processOrder(String fullName, String mobile, String street, String city, String state, String pinCode, String paymentMethod) {
        if (fullName == null || fullName.trim().isEmpty()) {
            ToastView.showInfoToast("Required Field", "Please enter recipient name.");
            return null;
        }
        if (mobile == null || mobile.trim().length() < 10) {
            ToastView.showInfoToast("Invalid Mobile", "Please enter a valid 10-digit mobile number.");
            return null;
        }
        if (street == null || street.trim().isEmpty() || city == null || pinCode == null || pinCode.trim().length() < 6) {
            ToastView.showInfoToast("Address Incomplete", "Please complete all address details with valid PIN.");
            return null;
        }

        AddToCart cart = AddToCart.getInstance();
        if (cart.getItems().isEmpty()) {
            ToastView.showInfoToast("Cart is Empty", "Add items to your bag before checking out.");
            return null;
        }

        Address address = new Address(fullName.trim(), mobile.trim(), street.trim(), city.trim(), state != null ? state.trim() : "Karnataka", pinCode.trim(), "Home", true);
        List<AddToCart> orderedItems = new ArrayList<>(cart.getItems());

       
        OrderUser order = OrderRepository.getInstance().createOrder(
                orderedItems,
                address,
                paymentMethod != null ? paymentMethod : "UPI",
                cart.getTotalMRP(),
                cart.getProductDiscount() + cart.getCouponDiscount(),
                cart.getDeliveryCharges(),
                cart.getFinalPayable(),
                CheckoutView.codUrl
                
        );

        // Persist order via the user-side order flow (OrderControllerUser -> OrderDaoUser -> Firestore)
        new OrderControllerUser().addOrder(
                AuthPage.email,
                order.getOrderId(),
                order.getPin(),
                order.getOrderDate(),
                order.getItems(),
                order.getTotalMRP(),
                order.getDiscount(),
                order.getDeliveryCharges(),
                order.getFinalAmount(),
                order.getPaymentMethod(),
                order.getPaymentStatus(),
                order.getStatus(),
                order.getShippingAddress(),
                order.getExpectedDelivery(),
                order.getCodUrl()
        );

        // Clear shopping cart both from in-memory UI and Firebase storage
        CartController.getInstance().clearCart();

        // Navigate to Order Success screen
        OrderSuccessView successView = new OrderSuccessView(order);
        AuthPage.myStage.setScene(successView.getOrderSuccessViewScene(() -> {
            MainUser home = new MainUser();
            AuthPage.myStage.setScene(home.getUserHomePageScene(() -> AuthPage.myStage.setScene(new AuthPage().showLoginPage())));
        }));
        ToastView.showToast("Order Placed Successfully! 🎉", "Order ID: " + order.getOrderId());

        return order;
    }
}
