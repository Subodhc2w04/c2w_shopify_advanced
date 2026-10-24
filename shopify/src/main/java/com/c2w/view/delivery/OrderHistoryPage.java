package com.c2w.view.delivery;

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
import com.c2w.view.authentication.AuthPage;

public class OrderHistoryPage {

        private Scene orderHistoryPageScene;
        private OrderController orderController = new OrderController();

        public Scene getOrderHistoryPageScene(Runnable backAction) {

                // ------------------ SIDEBAR ------------------
                VBox sidebar = DeliverySidebar.getSidebar("Order History", backAction);

                // ------------------ MAIN CONTENT ------------------
                Button backBtn = new Button("← Back");
                backBtn.setStyle(
                                "-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-font-size: 13px; -fx-cursor: hand; -fx-border-color: #334155; -fx-border-radius: 6px; -fx-padding: 6 12;");
                backBtn.setOnAction(e -> backAction.run());

                Label pageTitle = new Label("Order History");
                pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                Label pageSubtitle = new Label("Past deliveries completed, rejected or cancelled across all shifts.");
                pageSubtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

                VBox titleBox = new VBox(4, pageTitle, pageSubtitle);

                HBox topBar = new HBox(16, backBtn, titleBox);
                topBar.setAlignment(Pos.CENTER_LEFT);
                topBar.setPadding(new Insets(0, 0, 16, 0));

                // Filter Buttons Bar
                Button filterAllBtn = new Button("All");
                filterAllBtn.setStyle(
                                "-fx-background-color: #2563eb; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-padding: 8 18; -fx-background-radius: 20px; -fx-cursor: hand;");

                HBox filterBar = new HBox(10, filterAllBtn);
                filterBar.setAlignment(Pos.CENTER_LEFT);

                VBox cardsContainer = new VBox(12);

                Runnable renderAll = () -> {
                        cardsContainer.getChildren().clear();
                        List<Order> list = orderController.getDeliveredOrders(AuthPage.email);
                        for (Order o : list) {
                                Label idLabel = new Label("Order #" + o.getOrderId());
                                idLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                                Label cust = new Label("Customer: " + o.getCustomerName()  + " Date :"
                                                + o.getOrderTime());
                                cust.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

                                VBox leftBox = new VBox(4, idLabel, cust);

                                Region sp = new Region();
                                HBox.setHgrow(sp, Priority.ALWAYS);

                                Label statusBadge = new Label(o.getStatus());
                                if ("DELIVERED".equalsIgnoreCase(o.getStatus())) {
                                        statusBadge.setStyle(
                                                        "-fx-background-color: #064e3b; -fx-text-fill: #34d399; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 12; -fx-background-radius: 12px;");
                                } else if ("REJECTED".equalsIgnoreCase(o.getStatus())) {
                                        statusBadge.setStyle(
                                                        "-fx-background-color: #7f1d1d; -fx-text-fill: #f87171; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 12; -fx-background-radius: 12px;");
                                } else if ("CANCELLED".equalsIgnoreCase(o.getStatus())) {
                                        statusBadge.setStyle(
                                                        "-fx-background-color: #334155; -fx-text-fill: #94a3b8; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 12; -fx-background-radius: 12px;");
                                } else {
                                        statusBadge.setStyle(
                                                        "-fx-background-color: #1e3a8a; -fx-text-fill: #60a5fa; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 12; -fx-background-radius: 12px;");
                                }

                                Label earn = new Label("DELIVERED".equalsIgnoreCase(o.getStatus())
                                                ? "+₹" + o.getDeliveryCharges() + " Payout"
                                                : "₹0 Payout");
                                earn.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: "
                                                + ("DELIVERED".equalsIgnoreCase(o.getStatus()) ? "#34d399" : "#64748b")
                                                + ";");

                                VBox rightBox = new VBox(6, statusBadge, earn);
                                rightBox.setAlignment(Pos.CENTER_RIGHT);

                                HBox card = new HBox(16, leftBox, sp, rightBox);
                                card.setAlignment(Pos.CENTER);
                                card.setPadding(new Insets(16));
                                card.setStyle(
                                                "-fx-background-color: #1e293b; -fx-background-radius: 10px; -fx-border-color: #334155; -fx-border-radius: 10px;");

                                cardsContainer.getChildren().add(card);
                        }
                };

                renderAll.run();

                // Filters Action
                filterAllBtn.setOnAction(e -> {
                        filterAllBtn.setStyle(
                                        "-fx-background-color: #2563eb; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-padding: 8 18; -fx-background-radius: 20px; -fx-cursor: hand;");

                });

                VBox mainContent = new VBox(20, topBar, filterBar, cardsContainer);
                mainContent.setPadding(new Insets(24, 32, 24, 32));

                ScrollPane scrollPane = new ScrollPane(mainContent);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle(
                                "-fx-background: #0b111e; -fx-background-color: #0b111e; -fx-border-color: transparent;");

                HBox rootLayout = new HBox(sidebar, scrollPane);
                HBox.setHgrow(scrollPane, Priority.ALWAYS);
                rootLayout.setStyle("-fx-background-color: #0b111e;");

                orderHistoryPageScene = new Scene(rootLayout, 1550, 850);

                return orderHistoryPageScene;
        }
}
