package com.c2w.view.delivery;

import com.c2w.view.authentication.AuthPage;
import com.c2w.controller.delivery.DeliveryPersonController;
import com.c2w.controller.delivery.OrderController;
import com.c2w.model.delivery.Order;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class OrderDetailsPage {

        private Scene orderDetailsPageScene;
        private OrderController orderController = new OrderController();
        private DeliveryPersonController deliveryPersonController = new DeliveryPersonController();

        public Scene getOrderDetailsPageScene(Order order, Runnable backAction) {

                final Order activeOrder = order;

                // ------------------ TOP NAVIGATION BAR ------------------
                Button backBtn = new Button("← Back");
                backBtn.setStyle(
                                "-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-font-size: 13px; -fx-cursor: hand; -fx-border-color: #334155; -fx-border-radius: 6px; -fx-padding: 6 14;");
                backBtn.setOnAction(e -> backAction.run());

                Label pageTitle = new Label("Order Details & Decision");
                pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                Region topSpacer = new Region();
                HBox.setHgrow(topSpacer, Priority.ALWAYS);

                HBox topBar = new HBox(16, backBtn, pageTitle, topSpacer);
                topBar.setAlignment(Pos.CENTER_LEFT);
                topBar.setPadding(new Insets(20, 40, 10, 40));

                if (activeOrder == null) {
                        Label noOrder = new Label("No order selected.");
                        noOrder.setStyle("-fx-font-size: 16px; -fx-text-fill: #f87171;");
                        VBox box = new VBox(20, topBar, noOrder);
                        box.setStyle("-fx-background-color: #0b111e;");
                        box.setPadding(new Insets(20));
                        return new Scene(box, 1550, 850);
                }

                // ------------------ ORDER HEADER CARD ------------------
                Label orderIdLabel = new Label(
                                "Order #" + (activeOrder.getOrderId() != null ? activeOrder.getOrderId() : "N/A"));
                orderIdLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                Label statusBadge = new Label(
                                activeOrder.getStatus() != null ? activeOrder.getStatus() : "Ready for Dispatch");
                statusBadge.setStyle(
                                "-fx-background-color: #1e3a8a; -fx-text-fill: #60a5fa; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 6 16; -fx-background-radius: 14px; -fx-border-color: #3b82f6; -fx-border-radius: 14px;");

                Region headerSpacer = new Region();
                HBox.setHgrow(headerSpacer, Priority.ALWAYS);

                HBox orderHeader = new HBox(orderIdLabel, headerSpacer, statusBadge);
                orderHeader.setAlignment(Pos.CENTER);

                // ------------------ QUICK METRICS SUMMARY ROW ------------------
                VBox custBox = createMetricBox("Customer",
                                activeOrder.getCustomerName() != null ? activeOrder.getCustomerName() : "N/A",
                                "#ffffff");
                VBox itemsBox = createMetricBox("Total Items", activeOrder.getItemCount() + " Items", "#ffffff");
                VBox amountBox = createMetricBox("Order Value", "₹" + activeOrder.getAmount(), "#34d399");
                VBox payoutBox = createMetricBox("Delivery Payout", activeOrder.getDeliveryCharges(), "#a78bfa");

                HBox summaryRow = new HBox(32, custBox, itemsBox, amountBox, payoutBox);
                summaryRow.setPadding(new Insets(12, 16, 12, 16));
                summaryRow.setStyle(
                                "-fx-background-color: #0f172a; -fx-background-radius: 10px; -fx-border-color: #334155; -fx-border-radius: 10px;");

                // ------------------ PICKUP & DELIVERY INFO CARDS ------------------

                // Customer Drop Location Card
                Label deliveryHeading = new Label("📍 Delivery Destination (Customer)");
                deliveryHeading.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34d399;");

                Label deliveryAddress = new Label(
                                activeOrder.getDeliveryAddress() != null ? activeOrder.getDeliveryAddress()
                                                : "Address not available");
                deliveryAddress.setStyle("-fx-font-size: 13px; -fx-text-fill: #ffffff; -fx-font-weight: bold;");
                deliveryAddress.setWrapText(true);

                Label deliveryNotes = new Label("• Expected Delivery: "
                                + (activeOrder.getExpectedDate() != null ? activeOrder.getExpectedDate()
                                                : "Same Day Delivery"));
                deliveryNotes.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

                VBox deliveryCard = new VBox(6, deliveryHeading, deliveryAddress, deliveryNotes);
                deliveryCard.setPadding(new Insets(16));
                deliveryCard.setStyle(
                                "-fx-background-color: #0f172a; -fx-background-radius: 10px; -fx-border-color: #334155; -fx-border-radius: 10px;");
                HBox.setHgrow(deliveryCard, Priority.ALWAYS);

                HBox locationsRow = new HBox(16, deliveryCard);

                // ------------------ PAYMENT & META DETAILS ------------------
                Label payHeading = new Label("Payment & Order Details");
                payHeading.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #f59e0b;");

                String payMode = activeOrder.getPaymentMode() != null ? activeOrder.getPaymentMode()
                                : "Prepaid / Online";
                Label payModeLabel = new Label("Payment Method: " + payMode);
                payModeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #cbd5e1;");

                String orderTime = activeOrder.getOrderTime() != null ? " | Placed on: " + activeOrder.getOrderTime()
                                : "";
                Label metaDetailsLabel = new Label("Payment Status: " + activeOrder.getPaymentStatus() + orderTime);
                metaDetailsLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

                VBox paymentCard = new VBox(6, payHeading, payModeLabel, metaDetailsLabel);
                paymentCard.setPadding(new Insets(16));
                paymentCard.setStyle(
                                "-fx-background-color: #0f172a; -fx-background-radius: 10px; -fx-border-color: #334155; -fx-border-radius: 10px;");

                // ------------------ ACTION CONTROLS & DECISION BOX ------------------
                Label actionStatusLabel = new Label(
                                "Decision required: Review the details above and accept or reject this delivery.");
                actionStatusLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

                Button acceptBtn = new Button("Accept Order");
                acceptBtn.setStyle(
                                "-fx-background-color: #10b981; -fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 28; -fx-background-radius: 8px; -fx-cursor: hand;");

                Button rejectBtn = new Button("❌ Reject Order");
                rejectBtn.setStyle(
                                "-fx-background-color: #334155; -fx-text-fill: #f87171; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 28; -fx-background-radius: 8px; -fx-cursor: hand;");

                Button proceedDeliveryBtn = new Button("Proceed to Current Delivery →");
                proceedDeliveryBtn.setVisible(false);
                proceedDeliveryBtn.setStyle(
                                "-fx-background-color: #2563eb; -fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 28; -fx-background-radius: 8px; -fx-cursor: hand;");

                HBox actionButtons = new HBox(16, acceptBtn, rejectBtn, proceedDeliveryBtn);
                actionButtons.setAlignment(Pos.CENTER_LEFT);

                // Action Handlers
                CurrentDeliveryPage currentDeliveryPage = new CurrentDeliveryPage();

                acceptBtn.setOnAction(e -> {

                        // First check whether delivery person already
                        // has an accepted order
                        boolean hasCurrentOrder = orderController.hasCurrentOrder(AuthPage.email);

                        if (hasCurrentOrder) {
                                DeliveryAlert.showWarning(
                                                "Current Order Pending",
                                                "Complete Your Current Order First",
                                                "You already have an accepted order. Please complete your current delivery before accepting another order.");
                                return;
                        }

                        // No current order, so accept this order
                        activeOrder.setStatus("ACCEPTED");

                        // orderController.setCurrentActiveDelivery(activeOrder);

                        statusBadge.setText("ACCEPTED");

                        statusBadge.setStyle(
                                        "-fx-background-color: #064e3b; " +
                                                        "-fx-text-fill: #34d399; " +
                                                        "-fx-font-size: 13px; " +
                                                        "-fx-font-weight: bold; " +
                                                        "-fx-padding: 6 16; " +
                                                        "-fx-background-radius: 14px; " +
                                                        "-fx-border-color: #059669; " +
                                                        "-fx-border-radius: 14px;");

                        actionStatusLabel.setText(
                                        "Order Accepted! Proceed to Current Delivery to start pickup.");

                        actionStatusLabel.setStyle(
                                        "-fx-font-size: 13px; " +
                                                        "-fx-font-weight: bold; " +
                                                        "-fx-text-fill: #34d399;");

                        acceptBtn.setDisable(true);
                        rejectBtn.setDisable(true);

                        proceedDeliveryBtn.setVisible(true);

                        orderController.updateAcceptedOrder(
                                        activeOrder.getOrderId(),
                                        AuthPage.email,
                                        "ACCEPTED");

                        String dpName = deliveryPersonController.getDeliveryData(AuthPage.email, "name");
                        if (dpName == null || dpName.isEmpty()) {
                                dpName = AuthPage.email;
                        }
                        orderController.sendNotification(
                                        activeOrder.getOrderId(),
                                        AuthPage.email,
                                        dpName,
                                        "ACCEPTED",
                                        dpName + " accepted Order #" + activeOrder.getOrderId());
                });

                rejectBtn.setOnAction(e -> {
                        orderController.rejectOrder(AuthPage.email, activeOrder.getOrderId());
                        backAction.run();
                });

                proceedDeliveryBtn.setOnAction(e -> {
                        AuthPage.myStage.setScene(currentDeliveryPage.getCurrentDeliveryPageScene(
                                        () -> AuthPage.myStage.setScene(orderDetailsPageScene)));
                });

                VBox actionContainer = new VBox(10, actionStatusLabel, actionButtons);
                actionContainer.setPadding(new Insets(14, 0, 0, 0));

                // ------------------ MAIN CARD ASSEMBLY ------------------
                VBox card = new VBox(20, orderHeader, summaryRow, locationsRow, paymentCard, actionContainer);
                card.setPadding(new Insets(28));
                card.setMaxWidth(850);
                card.setStyle(
                                "-fx-background-color: #1e293b; -fx-background-radius: 14px; -fx-border-color: #334155; -fx-border-radius: 14px;");

                VBox contentBox = new VBox(card);
                contentBox.setAlignment(Pos.TOP_CENTER);
                contentBox.setPadding(new Insets(10, 40, 40, 40));

                VBox rootLayout = new VBox(10, topBar, contentBox);
                rootLayout.setStyle("-fx-background-color: #0b111e;");

                ScrollPane scrollPane = new ScrollPane(rootLayout);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle(
                                "-fx-background: #0b111e; -fx-background-color: #0b111e; -fx-border-color: transparent;");

                orderDetailsPageScene = new Scene(scrollPane, 1550, 850);
                return orderDetailsPageScene;
        }

        private VBox createMetricBox(String title, Object value, String colorHex) {
                Label titleLabel = new Label(title);
                titleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

                Label valueLabel = new Label(value.toString());
                valueLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + colorHex + ";");

                return new VBox(4, titleLabel, valueLabel);
        }
}
