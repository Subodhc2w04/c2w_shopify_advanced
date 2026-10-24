package com.c2w.view.delivery;

import com.c2w.controller.delivery.OrderController;
import com.c2w.model.delivery.Order;
import com.c2w.model.delivery.RouteInfo;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TrackingPage {

        private Scene trackingPageScene;
        private OrderController orderController = new OrderController();

        public Scene getTrackingPageScene(Runnable backAction) {

                Order activeDelivery = orderController.getCurrentOrder(AuthPage.email);

                VBox sidebar = DeliverySidebar.getSidebar("Tracking", backAction);

                Button backBtn = new Button("← Back");
                backBtn.setStyle(
                                "-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-font-size: 13px; -fx-cursor: hand; -fx-border-color: #334155; -fx-border-radius: 6px; -fx-padding: 6 12;");
                backBtn.setOnAction(e -> backAction.run());

                Label pageTitle = new Label("Live Route Tracking & Navigation");
                pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                HBox topBar = new HBox(16, backBtn, pageTitle);
                topBar.setAlignment(Pos.CENTER_LEFT);
                topBar.setPadding(new Insets(0, 0, 16, 0));

                VBox contentArea;

                if (activeDelivery == null) {
                        Label emptyIcon = new Label("📍");
                        emptyIcon.setStyle("-fx-font-size: 48px;");

                        Label emptyTitle = new Label("No Active Route to Track");
                        emptyTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                        Label emptyDesc = new Label(
                                        "Accept an order from Assigned Orders to view dynamic GPS navigation, route tracking line, and real-time distance.");
                        emptyDesc.setStyle("-fx-font-size: 14px; -fx-text-fill: #94a3b8; -fx-text-alignment: center;");

                        Button browseOrdersBtn = new Button("📦 View Assigned Orders");
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

                        RouteInfo routeInfo = orderController.getRouteForOrder(order);

                        Label ordLabel = new Label(
                                        "Order #" + (order.getOrderId() != null ? order.getOrderId() : "N/A"));
                        ordLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                        Label statusLabel = new Label(
                                        "● " + (order.getStatus() != null ? order.getStatus() : "ACTIVE"));
                        statusLabel.setStyle(
                                        "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #60a5fa; -fx-background-color: rgba(96,165,250,0.15); -fx-padding: 4 10; -fx-background-radius: 6px;");

                        Label originLabel = new Label("🛵 " + routeInfo.getOrigin().getName());
                        originLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #93c5fd; -fx-font-weight: bold;");

                        Label destLabel = new Label("📍 "
                                        + (order.getDeliveryAddress() != null ? order.getDeliveryAddress() : "Pune"));
                        destLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #34d399; -fx-font-weight: bold;");

                        Label distBadge = new Label("📏 " + routeInfo.getFormattedDistance());
                        distBadge.setStyle(
                                        "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #fbbf24; -fx-background-color: rgba(251,191,36,0.15); -fx-padding: 4 10; -fx-background-radius: 6px;");

                        Label etaBadge = new Label("⏱️ ETA: " + routeInfo.getFormattedEta());
                        etaBadge.setStyle(
                                        "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #a78bfa; -fx-background-color: rgba(167,139,250,0.15); -fx-padding: 4 10; -fx-background-radius: 6px;");

                        Region sp = new Region();
                        HBox.setHgrow(sp, Priority.ALWAYS);

                        HBox telemetryBox = new HBox(16, ordLabel, statusLabel, originLabel, destLabel, sp, distBadge,
                                        etaBadge);
                        telemetryBox.setAlignment(Pos.CENTER_LEFT);
                        telemetryBox.setPadding(new Insets(14, 20, 14, 20));
                        telemetryBox.setStyle(
                                        "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px;");

                        // Dynamic Map Container
                        StackPane mapContainer = new StackPane();
                        mapContainer.setPrefHeight(520);

                        Region gluonMap = DeliveryMapView.createGluonMapNode(routeInfo, 520);
                        mapContainer.getChildren().add(gluonMap);

                        // Order Quick Info Card
                        HBox orderInfoCard = createOrderDetailsCard(order, routeInfo);

                        contentArea = new VBox(20, topBar, telemetryBox, mapContainer, orderInfoCard);
                }

                contentArea.setPadding(new Insets(24, 32, 24, 32));

                ScrollPane scrollPane = new ScrollPane(contentArea);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle(
                                "-fx-background: #0b111e; -fx-background-color: #0b111e; -fx-border-color: transparent;");

                HBox rootLayout = new HBox(sidebar, scrollPane);
                HBox.setHgrow(scrollPane, Priority.ALWAYS);
                rootLayout.setStyle("-fx-background-color: #0b111e;");

                trackingPageScene = new Scene(rootLayout, 1550, 850);
                return trackingPageScene;
        }

        private HBox createOrderDetailsCard(Order order, RouteInfo routeInfo) {
                VBox col1 = new VBox(4);
                Label cName = new Label(
                                "Customer: " + (order.getCustomerName() != null ? order.getCustomerName() : "N/A"));
                cName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
                Label cPhone = new Label(
                                "Phone: " + (order.getCustomerPhone() != null ? order.getCustomerPhone() : "N/A"));
                cPhone.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
                col1.getChildren().addAll(cName, cPhone);

                VBox col2 = new VBox(4);
                Label pay = new Label("Payment: " + (order.getPaymentMode() != null ? order.getPaymentMode() : "COD"));
                pay.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
                Label status = new Label(
                                "Status: " + (order.getPaymentStatus() != null ? order.getPaymentStatus() : "Pending"));
                status.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
                col2.getChildren().addAll(pay, status);

                VBox col3 = new VBox(4);
                Label amt = new Label("Amount: ₹" + order.getAmount());
                amt.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #34d399;");
                Label items = new Label("Items: " + order.getItemCount() + " items");
                items.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
                col3.getChildren().addAll(amt, items);

                VBox col4 = new VBox(4);
                Label routeHdr = new Label("Route Distance & ETA");
                routeHdr.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #38bdf8;");
                Label routeDetail = new Label(routeInfo.getFormattedDistance() + " • " + routeInfo.getFormattedEta());
                routeDetail.setStyle("-fx-font-size: 12px; -fx-text-fill: #cbd5e1;");
                col4.getChildren().addAll(routeHdr, routeDetail);

                Region s1 = new Region();
                Region s2 = new Region();
                Region s3 = new Region();
                HBox.setHgrow(s1, Priority.ALWAYS);
                HBox.setHgrow(s2, Priority.ALWAYS);
                HBox.setHgrow(s3, Priority.ALWAYS);

                HBox card = new HBox(20, col1, s1, col2, s2, col3, s3, col4);
                card.setAlignment(Pos.CENTER_LEFT);
                card.setPadding(new Insets(16, 24, 16, 24));
                card.setStyle(
                                "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px;");
                return card;
        }
}
