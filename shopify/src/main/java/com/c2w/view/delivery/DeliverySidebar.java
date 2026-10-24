package com.c2w.view.delivery;

import com.c2w.view.authentication.AuthPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import com.c2w.controller.delivery.DeliveryPersonController;

public class DeliverySidebar {

    private static final String ACTIVE_STYLE = "-fx-background-color: #2563eb; -fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 16; -fx-background-radius: 8px; -fx-cursor: hand;";
    private static final String INACTIVE_STYLE = "-fx-background-color: transparent; -fx-text-fill: #cbd5e1; -fx-font-size: 13px; -fx-padding: 10 16; -fx-background-radius: 8px; -fx-cursor: hand;";

    public static VBox getSidebar(String activeTab, Runnable backAction) {
        DeliveryPersonController deliveryController = new DeliveryPersonController();
        // String profileStatus = deliveryController.getDeliveryData(AuthPage.email,
        // "profileCompleted");
        // Brand Header
        Label brandIcon = new Label("🛍️");
        brandIcon.setStyle("-fx-font-size: 22px; -fx-text-fill: #3b82f6;");

        Label brandTitle = new Label("Shopify");
        brandTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

        HBox brandHeader = new HBox(8, brandIcon, brandTitle);
        brandHeader.setAlignment(Pos.CENTER_LEFT);

        Label brandSub = new Label("Delivery Person");
        brandSub.setStyle("-fx-font-size: 11px; -fx-text-fill: #94a3b8;");

        VBox brandBox = new VBox(4, brandHeader, brandSub);
        brandBox.setPadding(new Insets(0, 0, 20, 0));
        brandBox.setStyle("-fx-border-color: #1e293b; -fx-border-width: 0 0 1 0;");

        // Navigation Buttons
        Button navDashboardBtn = createNavButton("Dashboard", "Dashboard".equalsIgnoreCase(activeTab));
        Button navAssignedBtn = createNavButton("Orders",
                "Orders".equalsIgnoreCase(activeTab));
        Button navCurrentBtn = createNavButton("Current Delivery",
                "Current Delivery".equalsIgnoreCase(activeTab) || "Current".equalsIgnoreCase(activeTab));
        Button navWalletBtn = createNavButton("Wallet", "Wallet".equalsIgnoreCase(activeTab));
        Button navHistoryBtn = createNavButton("Order History",
                "Order History".equalsIgnoreCase(activeTab) || "History".equalsIgnoreCase(activeTab));
        Button navAIBtn = createNavButton("AI Assistant",
                "AI Assistant".equalsIgnoreCase(activeTab) || "AI".equalsIgnoreCase(activeTab));
        Button navProfileBtn = createNavButton("Profile", "Profile".equalsIgnoreCase(activeTab));

        VBox navItemsBox = new VBox(6, navDashboardBtn, navAssignedBtn, navCurrentBtn, navWalletBtn,
                navHistoryBtn, navProfileBtn);

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        Button logoutBtn = new Button("⏻  Logout");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setStyle(
                "-fx-background-color: #334155; -fx-text-fill: #f87171; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 16; -fx-background-radius: 8px; -fx-cursor: hand;");

        // Navigation Actions
        navDashboardBtn.setOnAction(e -> {
            String profileStatus = deliveryController.getDeliveryData(AuthPage.email, "profileCompleted");

            if ( profileStatus.equals("true")) {
                DeliveryDashboardPage dashboardPage = new DeliveryDashboardPage();
                AuthPage.myStage.setScene(dashboardPage.getDashboardPageScene(backAction));
            } else {
                DeliveryAlert.showWarning(
                        "Profile Incomplete",
                        "Complete Your Profile First",
                        "Please complete your profile before accessing this page.");
            }
        });

        navAssignedBtn.setOnAction(e -> {
            String profileStatus = deliveryController.getDeliveryData(AuthPage.email, "profileCompleted");

            if ( profileStatus.equals("true")) {
                DispatchedOrdersPage dispatchedOrdersPage = new DispatchedOrdersPage();
                AuthPage.myStage.setScene(dispatchedOrdersPage.getAssignedOrdersPageScene(backAction));
            } else {
                DeliveryAlert.showWarning(
                        "Profile Incomplete",
                        "Complete Your Profile First",
                        "Please complete your profile before accessing this page.");
            }
        });

        navCurrentBtn.setOnAction(e -> {
            String profileStatus = deliveryController.getDeliveryData(AuthPage.email, "profileCompleted");

            if ( profileStatus.equals("true")) {
                CurrentDeliveryPage currentDeliveryPage = new CurrentDeliveryPage();
                AuthPage.myStage.setScene(currentDeliveryPage.getCurrentDeliveryPageScene(backAction));
            } else {
                DeliveryAlert.showWarning(
                        "Profile Incomplete",
                        "Complete Your Profile First",
                        "Please complete your profile before accessing this page.");
            }
        });

        navWalletBtn.setOnAction(e -> {
            String profileStatus = deliveryController.getDeliveryData(AuthPage.email, "profileCompleted");
            if (profileStatus.equals("true")) {
                WalletPage walletPage = new WalletPage();
                AuthPage.myStage.setScene(walletPage.getWalletPageScene(backAction));
            } else {
                DeliveryAlert.showWarning(
                        "Profile Incomplete",
                        "Complete Your Profile First",
                        "Please complete your profile before accessing this page.");
            }
        });

        navHistoryBtn.setOnAction(e -> {
            String profileStatus = deliveryController.getDeliveryData(AuthPage.email, "profileCompleted");

            if ( profileStatus.equals("true")) {
                OrderHistoryPage orderHistoryPage = new OrderHistoryPage();
                AuthPage.myStage.setScene(orderHistoryPage.getOrderHistoryPageScene(backAction));
            } else {
                DeliveryAlert.showWarning(
                        "Profile Incomplete",
                        "Complete Your Profile First",
                        "Please complete your profile before accessing this page.");
            }
        });

        navAIBtn.setOnAction(e -> {
            String profileStatus = deliveryController.getDeliveryData(AuthPage.email, "profileCompleted");

            if ( profileStatus.equals("true")) {
                AIAssistantPage aiAssistantPage = new AIAssistantPage();
                AuthPage.myStage.setScene(aiAssistantPage.getAIAssistantPageScene(backAction));
            } else {
                DeliveryAlert.showWarning(
                        "Profile Incomplete",
                        "Complete Your Profile First",
                        "Please complete your profile before accessing this page.");
            }
        });

        navProfileBtn.setOnAction(e -> {
            if (!"Profile".equalsIgnoreCase(activeTab)) {
                ProfilePage profilePage = new ProfilePage();
                AuthPage.myStage.setScene(profilePage.getProfilePageScene(backAction));
            }
        });

        // Sidebar container
        VBox sidebar = new VBox(16, brandBox, navItemsBox, sidebarSpacer, logoutBtn);
        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(250);
        sidebar.setPadding(new Insets(24, 18, 24, 18));
        sidebar.setStyle("-fx-background-color: #0f172a; -fx-border-color: #1e293b; -fx-border-width: 0 1 0 0;");

        return sidebar;
    }

    private static Button createNavButton(String text, boolean isActive) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(isActive ? ACTIVE_STYLE : INACTIVE_STYLE);
        return btn;
    }
}
