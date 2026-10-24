package com.c2w.controller.delivery;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.c2w.model.delivery.Order;
import com.c2w.view.authentication.AuthPage;

public class DeliveryAIAssistantController {

    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String GROQ_MODEL = "openai/gpt-oss-20b";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final OrderController orderController = new OrderController();

    public String processMessage(String userMessage) {

        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "How can I help you with your deliveries today?";
        }

        String apiKey = "YOUR_GROQ_API_KEY"; // Replace with your actual Groq API key

        if (apiKey == null || apiKey.isBlank()) {
            return "Groq API key is not configured.";
        }

        // Fetch current active order info if available for context
        String activeOrderContext = "";
        String getDeliveredOrderContext = "";
        String getAllOrdersContext = "";
        try {
            if (AuthPage.email != null && !AuthPage.email.isEmpty()) {
                Order currentOrder = orderController.getCurrentOrder(AuthPage.email);
                List<Order> deliveredOrder = orderController.getDeliveredOrders(AuthPage.email);
                List<Order> allOrders = orderController.getAllOrders();

                if (currentOrder != null || !deliveredOrder.isEmpty()) {
                    activeOrderContext = "CURRENT ACTIVE ORDER DETAILS:\n"
                            + "- Order ID: " + currentOrder.getOrderId() + "\n"
                            + "- Customer: " + currentOrder.getCustomerName() + "\n"
                            + "- Contact Phone: " + currentOrder.getCustomerPhone() + "\n"
                            + "- Delivery Address: " + currentOrder.getDeliveryAddress() + "\n"
                            + "- Order Status: " + currentOrder.getStatus() + "\n"
                            + "- Amount: ₹" + currentOrder.getAmount() + "\n"
                            + "- Payment Mode: " + currentOrder.getPaymentMode() + "\n";

                    getDeliveredOrderContext = "PREVIOUS DELIVERED ORDER DETAILS\n"
                            + deliveredOrder;
                    getAllOrdersContext = "ALL DISPATCH ORDERS\n" + allOrders;

                }
            }
        } catch (Exception ignored) {
        }

       String systemPrompt = """
        You are the C2W Delivery AI Assistant.

        You are built into the C2W Shopping Delivery Partner Portal.
        You understand the complete Delivery side of the application.

        APP KNOWLEDGE:

        Dashboard:
        Shows the delivery partner's current delivery information,
        recent orders, delivery statistics, and important updates.

        Assigned Orders:
        Shows orders that are ready for dispatch.
        A delivery partner can open an order, view its details,
        and accept or reject it.
        A delivery partner cannot accept another order if they
        already have an active accepted delivery.

        Current Delivery:
        Shows the delivery currently accepted by the logged-in
        delivery partner.
        The delivery status follows:
        ACCEPTED -> PICKED_UP -> OUT_FOR_DELIVERY -> DELIVERED.

        Tracking:
        Shows the delivery route from the store hub to the
        customer's delivery address.
        It can display distance and estimated travel information.

        Wallet:
        Shows the delivery partner's earnings, balance, and
        delivery payout transactions.
        Successful deliveries can generate a delivery payout.

        Order History:
        Shows previously completed deliveries.

        Profile:
        Shows the delivery partner's personal and account
        information and profile status.

        GENERAL BEHAVIOR:

        You are not a menu or command-based assistant.
        Have a natural conversation with the delivery partner.

        Use the application knowledge above to explain how
        the application works when the user asks.

        Use the live order and account information provided
        below when answering questions about the current situation.

        If the user asks which order they should accept,
        inspect the available orders and explain the relevant
        information about those orders.

        If the user asks about a specific order, use the
        live order data rather than guessing.

        If there is no suitable order available, clearly say so.

        Never invent order IDs, customers, addresses, statuses,
        amounts, or delivery information.

        If the user's question is unclear, ask a short
        clarification question.

        Keep responses natural, practical, and concise.
        Do not unnecessarily list all application features.

        Only assist with delivery-related topics.
        Dont add hash,stars in response give clean response always 
        """
                + (activeOrderContext.isEmpty() ? "" : "\n" + activeOrderContext)
                + (getDeliveredOrderContext.isEmpty() ? "" : "\n" + getDeliveredOrderContext)
                + (getAllOrdersContext.isEmpty() ? "" : "\n" + getAllOrdersContext);

        try {
            String jsonBody = "{"
                    + "\"model\":\"" + GROQ_MODEL + "\","
                    + "\"messages\":["
                    + "{"
                    + "\"role\":\"system\","
                    + "\"content\":\"" + escapeJson(systemPrompt) + "\""
                    + "},"
                    + "{"
                    + "\"role\":\"user\","
                    + "\"content\":\"" + escapeJson(userMessage.trim()) + "\""
                    + "}"
                    + "],"
                    + "\"temperature\":0.7,"
                    + "\"max_completion_tokens\":250"
                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GROQ_API_URL))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return extractGroqMessage(response.body());
            } else {
                System.out.println("Groq API Error Status: " + response.statusCode());
                System.out.println("Groq API Response: " + response.body());
                return "Sorry, I'm unable to process your request at this moment (Status " + response.statusCode()
                        + ").";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, could not connect to Delivery AI service. Please check your network connection.";
        }
    }

    private String extractGroqMessage(String json) {
        String marker = "\"content\":\"";
        int start = json.indexOf(marker);
        if (start == -1) {
            return "Sorry, could not parse the AI response.";
        }

        start += marker.length();
        StringBuilder result = new StringBuilder();
        boolean escaped = false;

        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);

            if (escaped) {
                switch (c) {
                    case 'n' -> result.append('\n');
                    case 'r' -> result.append('\r');
                    case 't' -> result.append('\t');
                    case '"' -> result.append('"');
                    case '\\' -> result.append('\\');
                    default -> result.append(c);
                }
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                break;
            } else {
                result.append(c);
            }
        }

        return result.toString().trim();
    }

    private String escapeJson(String text) {
        if (text == null)
            return "";
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
