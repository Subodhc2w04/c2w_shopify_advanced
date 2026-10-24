package com.c2w.view.delivery;

import com.c2w.controller.delivery.DeliveryPersonController;
import com.c2w.controller.delivery.OrderController;
import com.c2w.model.delivery.Order;
import com.c2w.view.authentication.AuthPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.util.Optional;

public class CurrentDeliveryPage {

        private Scene currentDeliveryPageScene;
        private OrderController orderController = new OrderController();
        private DeliveryPersonController deliveryPersonController = new DeliveryPersonController();

        public Scene getCurrentDeliveryPageScene(Runnable backAction) {

                Order activeDelivery = orderController.getCurrentOrder(AuthPage.email);

                // ------------------ SIDEBAR ------------------
                VBox sidebar = DeliverySidebar.getSidebar("Current Delivery", backAction);

                // ------------------ MAIN CONTENT ------------------
                Button backBtn = new Button("← Back");
                backBtn.setStyle(
                                "-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-font-size: 13px; -fx-cursor: hand; -fx-border-color: #334155; -fx-border-radius: 6px; -fx-padding: 6 12;");
                backBtn.setOnAction(e -> backAction.run());

                Label pageTitle = new Label("Current Delivery");
                pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                HBox topBar = new HBox(16, backBtn, pageTitle);
                topBar.setAlignment(Pos.CENTER_LEFT);
                topBar.setPadding(new Insets(0, 0, 16, 0));

                VBox contentArea;

                // Handle Empty State: No active order or already delivered
                if (activeDelivery == null || "DELIVERED".equalsIgnoreCase(activeDelivery.getStatus())) {
                        Label emptyIcon = new Label("🚚");
                        emptyIcon.setStyle("-fx-font-size: 48px;");

                        Label emptyTitle = new Label("No Active Delivery in Progress");
                        emptyTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                        Label emptyDesc = new Label(
                                        "You have no ongoing deliveries right now. Check available orders to accept your next delivery.");
                        emptyDesc.setStyle("-fx-font-size: 14px; -fx-text-fill: #94a3b8; -fx-text-alignment: center;");
                        emptyDesc.setWrapText(true);

                        Button browseOrdersBtn = new Button("📦 Browse Available Orders");
                        browseOrdersBtn.setStyle(
                                        "-fx-background-color: #2563eb; -fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 28; -fx-background-radius: 8px; -fx-cursor: hand;");
                        browseOrdersBtn.setOnAction(e -> {
                                DispatchedOrdersPage dispatchedOrdersPage = new DispatchedOrdersPage();
                                AuthPage.myStage.setScene(dispatchedOrdersPage.getAssignedOrdersPageScene(backAction));
                        });

                        VBox emptyCard = new VBox(16, emptyIcon, emptyTitle, emptyDesc, browseOrdersBtn);
                        emptyCard.setAlignment(Pos.CENTER);
                        emptyCard.setPadding(new Insets(60, 40, 60, 40));
                        emptyCard.setStyle(
                                        "-fx-background-color: #1e293b; -fx-background-radius: 14px; -fx-border-color: #334155; -fx-border-radius: 14px;");

                        contentArea = new VBox(20, topBar, emptyCard);
                } else {
                        final Order order = activeDelivery;
                        String initialStatus = order.getStatus() != null ? order.getStatus().toUpperCase() : "ACCEPTED";

                        // Delivery Status Stepper Visual
                        Label step1 = new Label("1. ACCEPTED");
                        step1.setStyle("-fx-background-color: #1e3a8a; -fx-text-fill: #93c5fd; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 20px;");

                        Label stepArrow1 = new Label("→");
                        stepArrow1.setStyle("-fx-text-fill: #64748b;");

                        Label step2 = new Label("2. PICKED UP");
                        step2.setStyle("-fx-background-color: #334155; -fx-text-fill: #94a3b8; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 20px;");

                        Label stepArrow2 = new Label("→");
                        stepArrow2.setStyle("-fx-text-fill: #64748b;");

                        Label step3 = new Label("3. OUT FOR DELIVERY");
                        step3.setStyle("-fx-background-color: #334155; -fx-text-fill: #94a3b8; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 20px;");

                        Label stepArrow3 = new Label("→");
                        stepArrow3.setStyle("-fx-text-fill: #64748b;");

                        Label step4 = new Label("4. DELIVERED");
                        step4.setStyle("-fx-background-color: #334155; -fx-text-fill: #94a3b8; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 20px;");

                        HBox stepperBox = new HBox(10, step1, stepArrow1, step2, stepArrow2, step3, stepArrow3, step4);
                        stepperBox.setAlignment(Pos.CENTER);
                        stepperBox.setPadding(new Insets(14));
                        stepperBox.setStyle(
                                        "-fx-background-color: #0f172a; -fx-background-radius: 10px; -fx-border-color: #334155; -fx-border-radius: 10px;");

                        // Main Delivery Info Card
                        Label cardOrderId = new Label("Active Delivery: #"
                                        + (order.getOrderId() != null ? order.getOrderId() : "N/A"));
                        cardOrderId.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                        Label currentStatusBadge = new Label(initialStatus);
                        currentStatusBadge.setStyle(
                                        "-fx-background-color: #1e3a8a; -fx-text-fill: #60a5fa; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 4 14; -fx-background-radius: 12px;");

                        Region spHeader = new Region();
                        HBox.setHgrow(spHeader, Priority.ALWAYS);

                        HBox cardHeader = new HBox(cardOrderId, spHeader, currentStatusBadge);
                        cardHeader.setAlignment(Pos.CENTER);

                        // Info Details
                        String custPhone = order.getCustomerPhone() != null ? " (" + order.getCustomerPhone() + ")"
                                        : "";
                        Label custLabel = new Label("Customer: "
                                        + (order.getCustomerName() != null ? order.getCustomerName() : "N/A")
                                        + custPhone);
                        custLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #cbd5e1;");

                        Label deliveryLocLabel = new Label("📍 Delivery Location: "
                                        + (order.getDeliveryAddress() != null ? order.getDeliveryAddress() : "Pune"));
                        deliveryLocLabel.setStyle(
                                        "-fx-font-size: 13px; -fx-text-fill: #34d399; -fx-font-weight: bold;");

                        Label orderSummaryLabel = new Label("Items: " + order.getItemCount() + "  |  Bill Amount: ₹"
                                        + order.getAmount() + "  |  Payment Status: "
                                        + (order.getPaymentStatus() != null ? order.getPaymentStatus() : "Prepaid"));
                        orderSummaryLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

                        VBox infoBox = new VBox(8, custLabel, deliveryLocLabel, orderSummaryLabel);
                        infoBox.setPadding(new Insets(10, 0, 10, 0));

                        // Interactive Status Transition Buttons
                        Button confirmPickupBtn = new Button("1. Confirm Pickup");
                        confirmPickupBtn.setStyle(
                                        "-fx-background-color: #2563eb; -fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 18; -fx-background-radius: 8px; -fx-cursor: hand;");

                        Button startDeliveryBtn = new Button("2. Start Delivery");
                        startDeliveryBtn.setDisable(true);
                        startDeliveryBtn.setStyle(
                                        "-fx-background-color: #7c3aed; -fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 18; -fx-background-radius: 8px; -fx-cursor: hand;");

                        Button markDeliveredBtn = new Button("3. Mark as Delivered");
                        markDeliveredBtn.setDisable(true);
                        markDeliveredBtn.setStyle(
                                        "-fx-background-color: #10b981; -fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 18; -fx-background-radius: 8px; -fx-cursor: hand;");

                        Button trackOrderBtn = new Button("🗺️ Track Order Map");
                        trackOrderBtn.setStyle(
                                        "-fx-background-color: #0284c7; -fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 18; -fx-background-radius: 8px; -fx-cursor: hand;");

                        Button finishDeliveryBtn = new Button("✅ Finish & Return to Orders");
                        finishDeliveryBtn.setVisible(false);
                        finishDeliveryBtn.setStyle(
                                        "-fx-background-color: #059669; -fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 18; -fx-background-radius: 8px; -fx-cursor: hand;");

                        HBox actionsRow = new HBox(14, confirmPickupBtn, startDeliveryBtn, markDeliveredBtn,
                                        trackOrderBtn, finishDeliveryBtn);
                        actionsRow.setAlignment(Pos.CENTER_LEFT);

                        Label feedbackLabel = new Label(
                                        "Action needed: Arrive at merchant store and confirm order pickup.");
                        feedbackLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #60a5fa;");

                        if ("PICKED_UP".equalsIgnoreCase(initialStatus)) {
                                step2.setStyle("-fx-background-color: #3730a3; -fx-text-fill: #a5b4fc; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 20px;");
                                confirmPickupBtn.setDisable(true);
                                startDeliveryBtn.setDisable(false);
                                feedbackLabel.setText("Parcel collected! Click 'Start Delivery' to begin navigation.");
                        } else if ("OUT_FOR_DELIVERY".equalsIgnoreCase(initialStatus)) {
                                step2.setStyle("-fx-background-color: #3730a3; -fx-text-fill: #a5b4fc; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 20px;");
                                step3.setStyle("-fx-background-color: #78350f; -fx-text-fill: #fbbf24; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 20px;");
                                confirmPickupBtn.setDisable(true);
                                startDeliveryBtn.setDisable(true);
                                markDeliveredBtn.setDisable(false);
                                feedbackLabel.setText(
                                                "En route to customer address! Deliver parcel and mark completed.");
                        }

                        // Action Handlers
                        confirmPickupBtn.setOnAction(e -> {
                                order.setStatus("PICKED_UP");
                                orderController.updateDeliveryOrderStatus("PICKED_UP", order.getOrderId());
                                currentStatusBadge.setText("PICKED_UP");
                                currentStatusBadge.setStyle(
                                                "-fx-background-color: #3730a3; -fx-text-fill: #a5b4fc; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 4 14; -fx-background-radius: 12px;");

                                step2.setStyle("-fx-background-color: #3730a3; -fx-text-fill: #a5b4fc; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 20px;");
                                confirmPickupBtn.setDisable(true);
                                startDeliveryBtn.setDisable(false);
                                feedbackLabel.setText(
                                                "Parcel collected! Click 'Start Delivery' to begin GPS navigation to customer.");
                                feedbackLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #a5b4fc;");

                                String dpName = deliveryPersonController.getDeliveryData(AuthPage.email, "name");
                                if (dpName == null || dpName.isEmpty())
                                        dpName = AuthPage.email;
                                orderController.sendNotification(
                                                order.getOrderId(),
                                                AuthPage.email,
                                                dpName,
                                                "PICKED_UP",
                                                dpName + " picked up Order #" + order.getOrderId());
                        });

                        startDeliveryBtn.setOnAction(e -> {
                                order.setStatus("OUT_FOR_DELIVERY");
                                orderController.updateDeliveryOrderStatus("OUT_FOR_DELIVERY", order.getOrderId());
                                currentStatusBadge.setText("OUT_FOR_DELIVERY");
                                currentStatusBadge.setStyle(
                                                "-fx-background-color: #78350f; -fx-text-fill: #fbbf24; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 4 14; -fx-background-radius: 12px;");

                                step3.setStyle("-fx-background-color: #78350f; -fx-text-fill: #fbbf24; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 20px;");
                                startDeliveryBtn.setDisable(true);
                                markDeliveredBtn.setDisable(false);
                                feedbackLabel.setText(
                                                "You are en route! Deliver parcel to customer address and mark completed.");
                                feedbackLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #fbbf24;");

                                String dpName = deliveryPersonController.getDeliveryData(AuthPage.email, "name");
                                if (dpName == null || dpName.isEmpty())
                                        dpName = AuthPage.email;
                                orderController.sendNotification(
                                                order.getOrderId(),
                                                AuthPage.email,
                                                dpName,
                                                "OUT_FOR_DELIVERY",
                                                dpName + " is out for delivery with Order #" + order.getOrderId());
                        });

                        markDeliveredBtn.setOnAction(e -> {
                                String paymentStatus = orderController.getOrderData(order.getOrderId(), "paymentStatus");
                                if (paymentStatus == null || paymentStatus.trim().isEmpty()) {
                                        paymentStatus = order.getPaymentStatus();
                                }

                                boolean isPendingCod = paymentStatus != null && (
                                                paymentStatus.equalsIgnoreCase("Pending COD") ||
                                                paymentStatus.equalsIgnoreCase("PENDING (COD)") ||
                                                paymentStatus.toUpperCase().contains("PENDING"));

                                if (isPendingCod) {
                                        showCodPaymentDialog(order, currentStatusBadge, step4, markDeliveredBtn,
                                                        finishDeliveryBtn, feedbackLabel, orderSummaryLabel);
                                } else {
                                        showPinVerificationDialog(order, currentStatusBadge, step4, markDeliveredBtn,
                                                        finishDeliveryBtn, feedbackLabel);
                                }
                        });

                        finishDeliveryBtn.setOnAction(e -> {
                                // orderController.clearCurrentActiveDelivery();
                                DispatchedOrdersPage dispatchedOrdersPage = new DispatchedOrdersPage();
                                AuthPage.myStage.setScene(dispatchedOrdersPage.getAssignedOrdersPageScene(backAction));
                        });

                        trackOrderBtn.setOnAction(e -> {
                                TrackingPage trackingPage = new TrackingPage();
                                AuthPage.myStage.setScene(trackingPage.getTrackingPageScene(() -> {
                                        AuthPage.myStage.setScene(getCurrentDeliveryPageScene(backAction));
                                }));
                        });

                        VBox deliveryCard = new VBox(16, cardHeader, infoBox, actionsRow, feedbackLabel);
                        deliveryCard.setPadding(new Insets(24));
                        deliveryCard.setStyle(
                                        "-fx-background-color: #1e293b; -fx-background-radius: 14px; -fx-border-color: #334155; -fx-border-radius: 14px;");

                        contentArea = new VBox(20, topBar, stepperBox, deliveryCard);
                }

                contentArea.setPadding(new Insets(24, 32, 24, 32));

                ScrollPane scrollPane = new ScrollPane(contentArea);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle(
                                "-fx-background: #0b111e; -fx-background-color: #0b111e; -fx-border-color: transparent;");

                HBox rootLayout = new HBox(sidebar, scrollPane);
                HBox.setHgrow(scrollPane, Priority.ALWAYS);
                rootLayout.setStyle("-fx-background-color: #0b111e;");

                currentDeliveryPageScene = new Scene(rootLayout, 1550, 850);
                return currentDeliveryPageScene;
        }

        private void showCodPaymentDialog(Order order, Label currentStatusBadge, Label step4, Button markDeliveredBtn,
                        Button finishDeliveryBtn, Label feedbackLabel, Label orderSummaryLabel) {

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("COD Payment Collection");
                dialog.setHeaderText("Cash on Delivery Payment");

                String codUrl = orderController.getOrderData(order.getOrderId(), "CodURL");
                if (codUrl == null || codUrl.trim().isEmpty()) {
                        codUrl = order.getCodURL();
                }

                Label headerTitle = new Label("Collect COD Payment");
                headerTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                Label orderInfo = new Label("Order #" + order.getOrderId() + "  |  Customer: "
                                + (order.getCustomerName() != null ? order.getCustomerName() : "Customer"));
                orderInfo.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

                Label amountLabel = new Label("Amount to Collect: ₹" + order.getAmount());
                amountLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #38bdf8;");

                ImageView qrView = new ImageView();
                qrView.setFitWidth(220);
                qrView.setFitHeight(220);
                qrView.setPreserveRatio(true);

                if (codUrl != null && !codUrl.trim().isEmpty()) {
                        try {
                                Image qrImage = new Image(codUrl, true);
                                qrView.setImage(qrImage);
                        } catch (Exception ex) {
                                System.err.println("Failed to load QR image from CodURL: " + ex.getMessage());
                        }
                }

                VBox qrBox = new VBox(qrView);
                qrBox.setAlignment(Pos.CENTER);
                qrBox.setPadding(new Insets(10));
                qrBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10px;");

                Label helpText = new Label("Ask the customer to scan the QR code to pay via UPI or collect Cash.\n"
                                + "After receiving payment, click 'Confirm Payment Received'.");
                helpText.setStyle("-fx-font-size: 12px; -fx-text-fill: #cbd5e1; -fx-text-alignment: center;");
                helpText.setWrapText(true);

                VBox content = new VBox(12, headerTitle, orderInfo, amountLabel, qrBox, helpText);
                content.setAlignment(Pos.CENTER);
                content.setPadding(new Insets(14, 20, 14, 20));
                content.setStyle("-fx-background-color: #0f172a; -fx-background-radius: 14px;");

                dialog.getDialogPane().setContent(content);

                ButtonType confirmBtnType = new ButtonType("✓ Confirm Payment Received", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelBtnType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                dialog.getDialogPane().getButtonTypes().addAll(cancelBtnType, confirmBtnType);

                dialog.getDialogPane().setStyle(
                                "-fx-background-color: #0f172a;"
                                                + "-fx-border-color: #334155;"
                                                + "-fx-border-width: 1.5px;"
                                                + "-fx-border-radius: 14px;"
                                                + "-fx-background-radius: 14px;"
                                                + "-fx-padding: 16px;"
                                                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 16, 0, 0, 6);");

                Button confirmBtn = (Button) dialog.getDialogPane().lookupButton(confirmBtnType);
                confirmBtn.setStyle(
                                "-fx-background-color: #10b981; -fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 8px; -fx-cursor: hand;");

                Button cancelBtn = (Button) dialog.getDialogPane().lookupButton(cancelBtnType);
                cancelBtn.setStyle(
                                "-fx-background-color: #334155; -fx-text-fill: #cbd5e1; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 18; -fx-background-radius: 8px; -fx-cursor: hand;");

                dialog.setResultConverter(button -> {
                        if (button == confirmBtnType) {
                                orderController.updatePaymentStatus(order.getOrderId(), "PAID");
                                order.setPaymentStatus("PAID");
                                if (orderSummaryLabel != null) {
                                        orderSummaryLabel.setText("Items: " + order.getItemCount()
                                                        + "  |  Bill Amount: ₹" + order.getAmount()
                                                        + "  |  Payment Status: PAID");
                                }
                                return button;
                        }
                        return null;
                });

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == confirmBtnType) {
                        showPinVerificationDialog(order, currentStatusBadge, step4, markDeliveredBtn,
                                        finishDeliveryBtn, feedbackLabel);
                }
        }

        private void showPinVerificationDialog(Order order, Label currentStatusBadge, Label step4,
                        Button markDeliveredBtn, Button finishDeliveryBtn, Label feedbackLabel) {

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Delivery PIN Verification");
                dialog.setHeaderText("Confirm Delivery");

                Label description = new Label(
                                "Ask the customer for the delivery PIN\n"
                                                + "and enter it below to confirm delivery.");

                description.setStyle(
                                "-fx-font-size: 13px;"
                                                + "-fx-text-fill: #94a3b8;"
                                                + "-fx-text-alignment: center;");

                Label orderLabel = new Label("Order: #" + order.getOrderId());
                orderLabel.setStyle(
                                "-fx-font-size: 13px;"
                                                + "-fx-font-weight: bold;"
                                                + "-fx-text-fill: #60a5fa;");

                Label pinLabel = new Label("Delivery PIN");
                pinLabel.setStyle(
                                "-fx-font-size: 13px;"
                                                + "-fx-font-weight: bold;"
                                                + "-fx-text-fill: #cbd5e1;");

                TextField pinField = new TextField();
                pinField.setPromptText("Enter customer's PIN");
                pinField.setMaxWidth(220);
                pinField.setStyle(
                                "-fx-background-color: #0f172a;"
                                                + "-fx-text-fill: #ffffff;"
                                                + "-fx-prompt-text-fill: #64748b;"
                                                + "-fx-font-size: 16px;"
                                                + "-fx-alignment: center;"
                                                + "-fx-border-color: #334155;"
                                                + "-fx-border-radius: 8px;"
                                                + "-fx-background-radius: 8px;"
                                                + "-fx-padding: 10px;");

                VBox popupContent = new VBox(12, description, orderLabel, pinLabel, pinField);
                popupContent.setAlignment(Pos.CENTER);
                popupContent.setPadding(new Insets(10, 20, 10, 20));
                popupContent.setStyle("-fx-background-color: #0f172a; -fx-background-radius: 14px;");

                dialog.getDialogPane().setContent(popupContent);

                ButtonType verifyButton = new ButtonType("✓ Verify PIN", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                dialog.getDialogPane().getButtonTypes().addAll(cancelButton, verifyButton);

                dialog.getDialogPane().setStyle(
                                "-fx-background-color: #0f172a;"
                                                + "-fx-border-color: #334155;"
                                                + "-fx-border-width: 1.5px;"
                                                + "-fx-border-radius: 14px;"
                                                + "-fx-background-radius: 14px;"
                                                + "-fx-padding: 16px;"
                                                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 16, 0, 0, 6);");

                Button verifyBtn = (Button) dialog.getDialogPane().lookupButton(verifyButton);
                verifyBtn.setStyle(
                                "-fx-background-color: #2563eb;"
                                                + "-fx-text-fill: #ffffff;"
                                                + "-fx-font-size: 13px;"
                                                + "-fx-font-weight: bold;"
                                                + "-fx-padding: 8 20;"
                                                + "-fx-background-radius: 8px;"
                                                + "-fx-cursor: hand;");

                Button cancelBtn = (Button) dialog.getDialogPane().lookupButton(cancelButton);
                cancelBtn.setStyle(
                                "-fx-background-color: #334155;"
                                                + "-fx-text-fill: #cbd5e1;"
                                                + "-fx-font-size: 13px;"
                                                + "-fx-font-weight: bold;"
                                                + "-fx-padding: 8 18;"
                                                + "-fx-background-radius: 8px;"
                                                + "-fx-cursor: hand;");

                dialog.setResultConverter(button -> {
                        if (button == verifyButton) {
                                if (pinField.getText().trim().isEmpty()) {
                                        DeliveryAlert.showError("Invalid PIN", "PIN Verification Failed",
                                                        "Please enter the delivery PIN.");
                                        return null;
                                }

                                try {
                                        Long enteredPin = Long.parseLong(pinField.getText().trim());
                                        boolean pinMatched = orderController.checkPin(order.getOrderId(), enteredPin);

                                        if (!pinMatched) {
                                                DeliveryAlert.showError("Invalid PIN", "PIN Verification Failed",
                                                                "Invalid PIN. Ask the customer to provide the correct PIN.");
                                                pinField.clear();
                                                pinField.requestFocus();
                                                return null;
                                        }

                                        // ---------------- DELIVERED ----------------
                                        order.setStatus("DELIVERED");
                                        orderController.updateDeliveryOrderStatus("DELIVERED", order.getOrderId());

                                        currentStatusBadge.setText("DELIVERED");
                                        currentStatusBadge.setStyle(
                                                        "-fx-background-color: #064e3b;"
                                                                        + "-fx-text-fill: #34d399;"
                                                                        + "-fx-font-size: 12px;"
                                                                        + "-fx-font-weight: bold;"
                                                                        + "-fx-padding: 4 14;"
                                                                        + "-fx-background-radius: 12px;");

                                        step4.setStyle(
                                                        "-fx-background-color: #064e3b;"
                                                                        + "-fx-text-fill: #34d399;"
                                                                        + "-fx-font-size: 11px;"
                                                                        + "-fx-font-weight: bold;"
                                                                        + "-fx-padding: 6 14;"
                                                                        + "-fx-background-radius: 20px;");

                                        markDeliveredBtn.setDisable(true);
                                        finishDeliveryBtn.setVisible(true);

                                        feedbackLabel.setText("✅ Delivery completed successfully! PIN verified.");
                                        feedbackLabel.setStyle(
                                                        "-fx-font-size: 13px;"
                                                                        + "-fx-font-weight: bold;"
                                                                        + "-fx-text-fill: #34d399;");

                                        // Add delivery earnings
                                        orderController.addMoney(AuthPage.email, order.getDeliveryCharges());

                                        // Increment completed orders
                                        deliveryPersonController.incrementTotalOrders(AuthPage.email);

                                        // Send notification
                                        String dpName = deliveryPersonController.getDeliveryData(AuthPage.email, "name");
                                        if (dpName == null || dpName.isEmpty()) {
                                                dpName = AuthPage.email;
                                        }

                                        orderController.sendNotification(
                                                        order.getOrderId(),
                                                        AuthPage.email,
                                                        dpName,
                                                        "DELIVERED",
                                                        dpName + " delivered Order #" + order.getOrderId());

                                        return button;

                                } catch (NumberFormatException ex) {
                                        DeliveryAlert.showError("Invalid PIN", "PIN Verification Failed",
                                                        "PIN must contain numbers only.");
                                        return null;
                                }
                        }
                        return button;
                });

                dialog.showAndWait();
        }
}
