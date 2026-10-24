package com.c2w.view.delivery;

import com.c2w.view.authentication.AuthPage;
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

import java.util.List;

import com.c2w.controller.delivery.OrderController;
import com.c2w.model.delivery.Order;

public class DispatchedOrdersPage {

        private Scene assignedOrdersPageScene;
        private OrderController orderController = new OrderController();

        public Scene getAssignedOrdersPageScene(Runnable backAction) {
                List<Order> assignedList = orderController.getAllOrders();

                // ------------------ SIDEBAR ------------------
                VBox sidebar = DeliverySidebar.getSidebar("Orders", backAction);

                // ------------------ MAIN CONTENT ------------------
                Button backBtn = new Button("← Back");
                backBtn.setStyle(
                                "-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-font-size: 13px; -fx-cursor: hand; -fx-border-color: #334155; -fx-border-radius: 6px; -fx-padding: 6 12;");
                backBtn.setOnAction(e -> backAction.run());

                Label pageTitle = new Label("All Orders");
                pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                Label pageSubtitle = new Label(
                                "Review orders newly assigned by the dispatch system. Accept or reject based on capacity.");
                pageSubtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

                VBox titleBox = new VBox(4, pageTitle, pageSubtitle);

                HBox topBar = new HBox(16, backBtn, titleBox);
                topBar.setAlignment(Pos.CENTER_LEFT);
                topBar.setPadding(new Insets(0, 0, 16, 0));
                HBox contentArea;

                if (assignedList.isEmpty()) {

                        Label emptyIcon = new Label("🚚");
                        emptyIcon.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 48px;");

                        Label emptyTitle = new Label("No Orders Available");
                        emptyTitle.setStyle(
                                        "-fx-font-size: 20px; " +
                                                        "-fx-font-weight: bold; " +
                                                        "-fx-text-fill: #ffffff;");

                        Label emptyDesc = new Label(
                                        "There are no orders available. Check back soon.");
                        emptyDesc.setStyle(
                                        "-fx-font-size: 14px; " +
                                                        "-fx-text-fill: #94a3b8; " +
                                                        "-fx-text-alignment: center;");
                        emptyDesc.setWrapText(true);

                        VBox emptyCard = new VBox(16, emptyIcon, emptyTitle, emptyDesc);
                        emptyCard.setAlignment(Pos.CENTER);
                        emptyCard.setPrefWidth(400);
                        emptyCard.setPrefHeight(500);
                        emptyCard.setPadding(new Insets(60, 40, 60, 40));
                        emptyCard.setStyle(
                                        "-fx-background-color: #1e293b; " +
                                                        "-fx-background-radius: 14px; " +
                                                        "-fx-border-color: #334155; " +
                                                        "-fx-border-radius: 14px;");

                        // Container for main area
                        VBox mainContent = new VBox(20);

                        mainContent.getChildren().add(topBar);

                        // This HBox takes all remaining space
                        HBox emptyArea = new HBox(emptyCard);
                        emptyArea.setAlignment(Pos.CENTER);

                        mainContent.getChildren().add(emptyArea);

                        mainContent.setPadding(new Insets(24, 32, 24, 32));

                        // Main area should take remaining width
                        HBox.setHgrow(mainContent, Priority.ALWAYS);

                        contentArea = new HBox(sidebar, mainContent);

                        contentArea.setStyle("-fx-background-color: #0b111e;");

                        assignedOrdersPageScene = new Scene(contentArea, 1550, 850);
                } else {
                        System.out.println("In else-------------------------");
                        // Order Cards List

                        VBox cardsContainer = new VBox(16);

                        OrderDetailsPage orderDetailsPage = new OrderDetailsPage();

                        for (Order order : assignedList) {
                                Label ordIdLabel = new Label("Order #" + order.getOrderId());
                                ordIdLabel.setStyle(
                                                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                                Label ordStatusBadge = new Label(order.getStatus());
                                ordStatusBadge.setStyle(
                                                "-fx-background-color: #1e3a8a; -fx-text-fill: #60a5fa; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 12; -fx-background-radius: 12px;");

                                Region spacer = new Region();
                                HBox.setHgrow(spacer, Priority.ALWAYS);

                                HBox cardHeader = new HBox(ordIdLabel, spacer, ordStatusBadge);
                                cardHeader.setAlignment(Pos.CENTER);

                                // Customer info
                                Label custLabel = new Label("Customer: " + order.getCustomerName());
                                custLabel.setStyle(
                                                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #cbd5e1;");

                                Label itemsLabel = new Label("Items: " + order.getItemCount());
                                itemsLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

                                Label amountLabel = new Label("Amount: ₹" + order.getAmount());
                                amountLabel.setStyle(
                                                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34d399;");

                                HBox infoRow = new HBox(24, custLabel, itemsLabel, amountLabel);

                                Label deliveryLabel = new Label("Delivery Address: " + order.getDeliveryAddress());
                                deliveryLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #cbd5e1;");
                                Label paymentStatus = new Label("Payment Status: " + order.getPaymentStatus());
                                paymentStatus.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

                                VBox addressBox = new VBox(6, deliveryLabel);
                                addressBox.setPadding(new Insets(6, 0, 6, 0));

                                VBox payemtnStatusBox = new VBox(6, paymentStatus);

                                // View Details Button
                                Button viewDetailsBtn = new Button("View Details →");
                                viewDetailsBtn.setStyle(
                                                "-fx-background-color: #2563eb; -fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 6px; -fx-cursor: hand;");
                                viewDetailsBtn.setOnAction(e -> {
                                        AuthPage.myStage.setScene(orderDetailsPage.getOrderDetailsPageScene(
                                                        order,
                                                        () -> {
                                                                DispatchedOrdersPage freshPage = new DispatchedOrdersPage();
                                                                AuthPage.myStage.setScene(freshPage.getAssignedOrdersPageScene(backAction));
                                                        }));
                                });

                                HBox cardFooter = new HBox(viewDetailsBtn);
                                cardFooter.setAlignment(Pos.CENTER_RIGHT);

                                VBox card = new VBox(12, cardHeader, infoRow, addressBox, payemtnStatusBox, cardFooter);
                                card.setPadding(new Insets(20));
                                card.setStyle(
                                                "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px;");

                                cardsContainer.getChildren().add(card);
                        }

                        VBox mainContent = new VBox(20, topBar, cardsContainer);
                        mainContent.setPadding(new Insets(24, 32, 24, 32));

                        ScrollPane scrollPane = new ScrollPane(mainContent);
                        scrollPane.setFitToWidth(true);
                        scrollPane.setStyle(
                                        "-fx-background: #0b111e; -fx-background-color: #0b111e; -fx-border-color: transparent;");

                        contentArea = new HBox(sidebar, scrollPane);
                        HBox.setHgrow(scrollPane, Priority.ALWAYS);
                        contentArea.setStyle("-fx-background-color: #0b111e;");

                        assignedOrdersPageScene = new Scene(contentArea, 1550, 850);

                }
                return assignedOrdersPageScene;
        }
}
