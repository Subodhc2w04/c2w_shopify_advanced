package com.c2w.config;

import com.c2w.utils.Keys;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

public class RazorpayConfig {
    private static String API_KEY = Keys.api_key;
    private static String SECRET_API_KEY = Keys.secretAPIKey;
    static RazorpayClient razorpayClient;

    public static RazorpayClient getRazorpayClient() {

        try {
            razorpayClient = new RazorpayClient(API_KEY, SECRET_API_KEY);
        } catch (RazorpayException e) {
            e.printStackTrace();
        }
        return razorpayClient;
    }
}
