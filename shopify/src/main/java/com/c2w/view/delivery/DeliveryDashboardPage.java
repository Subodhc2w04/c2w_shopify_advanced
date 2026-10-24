package com.c2w.view.delivery;

import com.c2w.view.authentication.AuthPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

import com.c2w.controller.delivery.DeliveryPersonController;
import com.c2w.controller.delivery.OrderController;
import com.c2w.model.delivery.DeliveryPerson;
import com.c2w.model.delivery.Order;

public class DeliveryDashboardPage {

        private Scene dashboardPageScene;
        private OrderController orderController = new OrderController();
        private DeliveryPersonController loginController = new DeliveryPersonController();

        public DeliveryDashboardPage() {
        }

        public void isProfileUpdated(Runnable back) {

                String status = loginController.getDeliveryData(AuthPage.email, "profileCompleted");
                System.out.println("--------In DeliveryDashboardPage profile update status is " + status);

                if (status != null && status.equals("true")) {

                        AuthPage.myStage.setScene(
                                        getDashboardPageScene(back));

                } else {
                        getDashboardPageScene(back);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);

                        alert.setTitle("Complete Your Profile");
                        alert.setHeaderText("Profile Completion Required");
                        alert.setContentText(
                                        "Please complete your profile before accessing the Delivery Dashboard.");

                        ButtonType profileButton = new ButtonType("Complete Profile");
                        alert.getButtonTypes().setAll(profileButton);
                        DeliveryAlert.styleAlert(alert);

                        alert.showAndWait().ifPresent(response -> {

                                if (response == profileButton) {

                                        ProfilePage profilePage = new ProfilePage();

                                        AuthPage.myStage.setScene(
                                                        profilePage.getProfilePageScene(
                                                                        () -> AuthPage.myStage.setScene(
                                                                                        getDashboardPageScene(back))));
                                }
                        });
                }
        }

        public Scene getDashboardPageScene(Runnable backAction) {

                DeliveryPerson person = loginController.getAllDeliveryPersonsData(AuthPage.email);
                String driverName = (person != null && person.getName() != null && !person.getName().trim().isEmpty())
                                ? person.getName().trim()
                                : "Partner";

                // ------------------ SIDEBAR ------------------
                VBox sidebar = DeliverySidebar.getSidebar("Dashboard", backAction);

                // ------------------ MAIN CONTENT ------------------

                // Top Greeting Header
                Label headerTitle = new Label("Delivery Dashboard");
                headerTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                Label welcomeLabel = new Label("Welcome back, " + driverName);
                welcomeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #94a3b8;");

                VBox titleBox = new VBox(4, headerTitle, welcomeLabel);

                boolean hasActive = orderController.hasCurrentOrder(AuthPage.email);
                Order currentActiveOrder = hasActive ? orderController.getCurrentOrder(AuthPage.email) : null;

                Label statusBadge = new Label(hasActive ? "● On Active Delivery" : "● Active on Duty");
                statusBadge.setStyle(hasActive
                                ? "-fx-background-color: #78350f; -fx-text-fill: #fbbf24; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 20px; -fx-border-color: #b45309; -fx-border-radius: 20px;"
                                : "-fx-background-color: #064e3b; -fx-text-fill: #34d399; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 20px; -fx-border-color: #059669; -fx-border-radius: 20px;");

                Region headerSpacer = new Region();
                HBox.setHgrow(headerSpacer, Priority.ALWAYS);

                HBox topHeader = new HBox(titleBox, headerSpacer, statusBadge);
                topHeader.setAlignment(Pos.CENTER);
                topHeader.setPadding(new Insets(0, 0, 20, 0));

                double balance = (person != null) ? person.getWalletBalance()
                                : loginController.getWalletBalance(AuthPage.email);
                int totalCompleted = (person != null) ? person.getTotalOrders()
                                : loginController.getTotalOrders(AuthPage.email);
                List<Order> availableOrders = orderController.getAllOrders();
                int assignedCount = (availableOrders != null) ? availableOrders.size() : 0;

                // ------------------ 4 METRIC / STATISTIC CARDS ------------------

                // 1. Available Orders
                Label stat1Title = new Label("Available Orders");
                stat1Title.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
                Label stat1Value = new Label(String.valueOf(assignedCount));
                stat1Value.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
                Label stat1Sub = new Label("Ready for dispatch");
                stat1Sub.setStyle("-fx-font-size: 11px; -fx-text-fill: #38bdf8;");
                VBox statCard1 = new VBox(6, stat1Title, stat1Value, stat1Sub);
                statCard1.setPadding(new Insets(16));
                statCard1.setPrefWidth(210);
                statCard1.setStyle(
                                "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px;");
                HBox.setHgrow(statCard1, Priority.ALWAYS);

                // 2. Completed
                Label stat2Title = new Label("Completed");
                stat2Title.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
                Label stat2Value = new Label(String.valueOf(totalCompleted));
                stat2Value.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #34d399;");
                Label stat2Sub = new Label("Total delivered orders");
                stat2Sub.setStyle("-fx-font-size: 11px; -fx-text-fill: #10b981;");
                VBox statCard2 = new VBox(6, stat2Title, stat2Value, stat2Sub);
                statCard2.setPadding(new Insets(16));
                statCard2.setPrefWidth(210);
                statCard2.setStyle(
                                "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px;");
                HBox.setHgrow(statCard2, Priority.ALWAYS);

                // 3. Active Delivery
                Label stat3Title = new Label("Active Delivery");
                stat3Title.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
                Label stat3Value = new Label(hasActive ? "1" : "0");
                stat3Value.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #fbbf24;");
                Label stat3Sub = new Label(hasActive ? "Delivery in progress" : "No active task");
                stat3Sub.setStyle("-fx-font-size: 11px; -fx-text-fill: #f59e0b;");
                VBox statCard3 = new VBox(6, stat3Title, stat3Value, stat3Sub);
                statCard3.setPadding(new Insets(16));
                statCard3.setPrefWidth(210);
                statCard3.setStyle(
                                "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px;");
                HBox.setHgrow(statCard3, Priority.ALWAYS);

                // 4. Wallet Balance
                Label stat4Title = new Label("Wallet Balance");
                stat4Title.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
                Label stat4Value = new Label(String.format("₹%.0f", balance));
                stat4Value.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #a78bfa;");
                Label stat4Sub = new Label("Available balance");
                stat4Sub.setStyle("-fx-font-size: 11px; -fx-text-fill: #8b5cf6;");
                VBox statCard4 = new VBox(6, stat4Title, stat4Value, stat4Sub);
                statCard4.setPadding(new Insets(16));
                statCard4.setPrefWidth(210);
                statCard4.setStyle(
                                "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px;");
                HBox.setHgrow(statCard4, Priority.ALWAYS);

                HBox statsGrid = new HBox(16, statCard1, statCard2, statCard3, statCard4);

                // ------------------ DYNAMIC ACTIVE ORDER BANNER (IF ACTIVE) ------------------
                VBox dynamicActiveCard = null;
                if (hasActive && currentActiveOrder != null) {
                        Label activeHdr = new Label("🔥 Active Delivery In Progress");
                        activeHdr.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #fbbf24;");

                        Label activeOrdId = new Label("Order #" + currentActiveOrder.getOrderId() + " • "
                                        + currentActiveOrder.getStatus());
                        activeOrdId.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                        Label activeDest = new Label("📍 Delivering to: " + currentActiveOrder.getDeliveryAddress());
                        activeDest.setStyle("-fx-font-size: 13px; -fx-text-fill: #34d399;");

                        Button trackBtn = new Button("🗺️ Live Route Tracking");
                        trackBtn.setStyle(
                                        "-fx-background-color: #0284c7; -fx-text-fill: #ffffff; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6px; -fx-cursor: hand;");
                        trackBtn.setOnAction(e -> {
                                TrackingPage trackingPage = new TrackingPage();
                                AuthPage.myStage.setScene(trackingPage.getTrackingPageScene(
                                                () -> AuthPage.myStage.setScene(dashboardPageScene)));
                        });

                        Button manageBtn = new Button("🚚 Manage Delivery");
                        manageBtn.setStyle(
                                        "-fx-background-color: #2563eb; -fx-text-fill: #ffffff; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6px; -fx-cursor: hand;");
                        manageBtn.setOnAction(e -> {
                                CurrentDeliveryPage currPage = new CurrentDeliveryPage();
                                AuthPage.myStage.setScene(currPage.getCurrentDeliveryPageScene(
                                                () -> AuthPage.myStage.setScene(dashboardPageScene)));
                        });

                        HBox activeActions = new HBox(10, trackBtn, manageBtn);

                        Region actSp = new Region();
                        HBox.setHgrow(actSp, Priority.ALWAYS);

                        HBox actRow = new HBox(16, new VBox(4, activeHdr, activeOrdId, activeDest), actSp,
                                        activeActions);
                        actRow.setAlignment(Pos.CENTER_LEFT);

                        dynamicActiveCard = new VBox(actRow);
                        dynamicActiveCard.setPadding(new Insets(16, 20, 16, 20));
                        dynamicActiveCard.setStyle(
                                        "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #f59e0b; -fx-border-radius: 12px; -fx-border-width: 1.5px;");
                }

                // ------------------ RECENT DISPATCHED ORDERS ------------------
                Label sectionTitle = new Label("Recent Available Orders");
                sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                Button viewAllBtn = new Button("View All →");
                viewAllBtn.setStyle(
                                "-fx-background-color: transparent; -fx-text-fill: #60a5fa; -fx-font-size: 13px; -fx-cursor: hand; -fx-font-weight: bold;");
                viewAllBtn.setOnAction(e -> {
                        DispatchedOrdersPage dispatchedPage = new DispatchedOrdersPage();
                        AuthPage.myStage.setScene(dispatchedPage.getAssignedOrdersPageScene(
                                        () -> AuthPage.myStage.setScene(dashboardPageScene)));
                });

                Region sectionSpacer = new Region();
                HBox.setHgrow(sectionSpacer, Priority.ALWAYS);

                HBox sectionHeader = new HBox(sectionTitle, sectionSpacer, viewAllBtn);
                sectionHeader.setAlignment(Pos.CENTER);
                sectionHeader.setPadding(new Insets(16, 0, 8, 0));

                VBox ordersListBox = new VBox(12);

                List<Order> recentOrders = orderController.getRecentOrders();

                if (recentOrders == null || recentOrders.isEmpty()) {
                        Label noOrdersLbl = new Label("📦 No orders pending for dispatch right now. Check back soon!");
                        noOrdersLbl.setStyle("-fx-font-size: 14px; -fx-text-fill: #94a3b8;");

                        VBox emptyBox = new VBox(noOrdersLbl);
                        emptyBox.setAlignment(Pos.CENTER);
                        emptyBox.setPadding(new Insets(30));
                        emptyBox.setStyle(
                                        "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px;");
                        ordersListBox.getChildren().add(emptyBox);
                } else {
                        for (Order order : recentOrders) {

                                Label ordId = new Label(order.getOrderId());
                                ordId.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                                Label ordStatus = new Label("READY FOR DISPATCH");
                                ordStatus.setStyle(
                                                "-fx-background-color: #1e3a8a; -fx-text-fill: #60a5fa; " +
                                                                "-fx-font-size: 11px; -fx-font-weight: bold; " +
                                                                "-fx-padding: 4 10; -fx-background-radius: 12px;");

                                Region space = new Region();
                                HBox.setHgrow(space, Priority.ALWAYS);

                                HBox orderHeader = new HBox(ordId, space, ordStatus);

                                Label ordCust = new Label("Customer: "
                                                + (order.getCustomerName() != null ? order.getCustomerName() : "N/A"));
                                ordCust.setStyle("-fx-font-size: 13px; -fx-text-fill: #cbd5e1;");

                                String time = (order.getOrderTime() != null && !order.getOrderTime().isEmpty())
                                                ? order.getOrderTime()
                                                : (order.getExpectedDate() != null ? order.getExpectedDate() : "Today");
                                Label ordItems = new Label("Date: " + time + " • " + order.getItemCount() + " items");
                                ordItems.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

                                Label ordAmt = new Label("₹" + order.getAmount());
                                ordAmt.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34d399;");

                                HBox orderInfo = new HBox(20, ordCust, ordItems, ordAmt);

                                Label ordLoc = new Label(
                                                "📍 " + (order.getDeliveryAddress() != null ? order.getDeliveryAddress()
                                                                : "Pune"));
                                ordLoc.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

                                Button viewDetailsBtn = new Button("View Details");
                                viewDetailsBtn.setStyle(
                                                "-fx-background-color: #2563eb; -fx-text-fill: #ffffff; " +
                                                                "-fx-font-size: 12px; -fx-font-weight: bold; " +
                                                                "-fx-padding: 8 16; -fx-background-radius: 6px; -fx-cursor: hand;");

                                Region bottomSpacer = new Region();
                                HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

                                HBox orderBottom = new HBox(ordLoc, bottomSpacer, viewDetailsBtn);
                                orderBottom.setAlignment(Pos.CENTER);

                                VBox orderCard = new VBox(10, orderHeader, orderInfo, orderBottom);
                                orderCard.setPadding(new Insets(16));
                                orderCard.setStyle(
                                                "-fx-background-color: #1e293b; " +
                                                                "-fx-background-radius: 12px; " +
                                                                "-fx-border-color: #334155; " +
                                                                "-fx-border-radius: 12px;");

                                viewDetailsBtn.setOnAction(e -> {
                                        OrderDetailsPage orderDetailsPage = new OrderDetailsPage();
                                        AuthPage.myStage.setScene(
                                                        orderDetailsPage.getOrderDetailsPageScene(
                                                                        order,
                                                                        () -> AuthPage.myStage
                                                                                        .setScene(dashboardPageScene)));
                                });

                                ordersListBox.getChildren().add(orderCard);
                        }
                }

                // Layout
                VBox mainContent;
                if (dynamicActiveCard != null) {
                        mainContent = new VBox(18, topHeader, statsGrid, dynamicActiveCard, sectionHeader,
                                        ordersListBox);
                } else {
                        mainContent = new VBox(18, topHeader, statsGrid, sectionHeader, ordersListBox);
                }

                mainContent.setPadding(new Insets(24, 32, 24, 32));

                ScrollPane scrollPane = new ScrollPane(mainContent);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle(
                                "-fx-background: #0b111e; -fx-background-color: #0b111e; -fx-border-color: transparent;");

                HBox rootLayout = new HBox(sidebar, scrollPane);
                HBox.setHgrow(scrollPane, Priority.ALWAYS);
                rootLayout.setStyle("-fx-background-color: #0b111e;");

                dashboardPageScene = new Scene(rootLayout, 1550, 850);
                return dashboardPageScene;
        }

}
