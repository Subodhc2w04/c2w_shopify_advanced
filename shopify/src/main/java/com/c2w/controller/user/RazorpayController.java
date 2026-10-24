package com.c2w.controller.user;

import org.json.JSONObject;

import com.c2w.config.RazorpayConfig;
import com.razorpay.Order;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

public class RazorpayController {
    RazorpayClient razorpayClient = RazorpayConfig.getRazorpayClient();

    public String createOrder(double amount, String receipt) {

        try {

            JSONObject orderRequest = new JSONObject();

            int amountInPaise = (int) (amount * 100);

            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", receipt);

            Order order = razorpayClient.orders.create(orderRequest);

            String razorpayOrderId = order.get("id");

            System.out.println(
                    "Razorpay Order ID : " + razorpayOrderId);

            return razorpayOrderId;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    public String getPaymentLink(
            double amount,
            String receipt,
            String razorpayOrderId) {

        try {

            JSONObject paymentLinkRequest = new JSONObject();

            int amountInPaise = (int) (amount * 100);

            paymentLinkRequest.put(
                    "amount",
                    amountInPaise);

            paymentLinkRequest.put(
                    "currency",
                    "INR");

            paymentLinkRequest.put(
                    "reference_id",
                    razorpayOrderId);

            paymentLinkRequest.put(
                    "description",
                    "Payment for Order " + receipt);

            paymentLinkRequest.put(
                    "accept_partial",
                    false);

            paymentLinkRequest.put(
                    "reminder_enable",
                    false);

            PaymentLink paymentLink = razorpayClient.paymentLink.create(
                    paymentLinkRequest);

            String url = paymentLink.get("short_url");

            System.out.println(
                    "Payment Link : " + url);

            return url;

        } catch (RazorpayException e) {

            e.printStackTrace();

            return "";

        }
    }
}
