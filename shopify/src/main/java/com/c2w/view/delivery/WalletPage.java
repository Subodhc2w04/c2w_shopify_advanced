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
import com.c2w.model.delivery.WalletTransaction;
import com.c2w.view.authentication.AuthPage;

public class WalletPage {

    private Scene walletPageScene;
    private OrderController orderController = new OrderController();

    public Scene getWalletPageScene(Runnable backAction) {

        // ------------------ SIDEBAR ------------------
        VBox sidebar = DeliverySidebar.getSidebar("Wallet", backAction);

        // Fetch Real-time Balance & Transactions
        double currentBalance = orderController.getWalletBalance(AuthPage.email);
        List<WalletTransaction> list = orderController.getTransactions(AuthPage.email);

        int completedCount = list.size();
        int totalEarnings = 0;
        for (WalletTransaction t : list) {
            totalEarnings += t.getAmount();
        }

        // ------------------ MAIN CONTENT ------------------
        Button backBtn = new Button("← Back");
        backBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-font-size: 13px; -fx-cursor: hand; -fx-border-color: #334155; -fx-border-radius: 6px; -fx-padding: 6 12;");
        backBtn.setOnAction(e -> backAction.run());

        Label pageTitle = new Label("My Wallet");
        pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

        Label pageSubtitle = new Label("Manage delivery payouts, weekly incentives, and bank transfer settlements.");
        pageSubtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

        VBox titleBox = new VBox(4, pageTitle, pageSubtitle);

        HBox topBar = new HBox(16, backBtn, titleBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 16, 0));

        // Primary Balance Card + Quick Stats
        Label balanceTitle = new Label("Total Available Balance");
        balanceTitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

        Label balanceAmount = new Label(String.format("₹%.2f", currentBalance));
        balanceAmount.setStyle("-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: #34d399;");

      
        Region heroSpacer = new Region();
        HBox.setHgrow(heroSpacer, Priority.ALWAYS);

        HBox mainBalanceRow = new HBox(new VBox(4, balanceTitle, balanceAmount), heroSpacer);
        mainBalanceRow.setAlignment(Pos.CENTER);
        mainBalanceRow.setPadding(new Insets(20));
        mainBalanceRow.setStyle(
                "-fx-background-color: linear-gradient(to right, #0f2e24, #1e293b); -fx-background-radius: 14px; -fx-border-color: #059669; -fx-border-radius: 14px;");

        // Period Breakdown Cards
        // Card 1: Completed Deliveries
        Label stat1Title = new Label("Delivered Orders");
        stat1Title.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
        Label stat1Value = new Label(String.valueOf(completedCount));
        stat1Value.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        Label stat1Sub = new Label(completedCount + " completed deliveries");
        stat1Sub.setStyle("-fx-font-size: 11px; -fx-text-fill: #38bdf8;");
        VBox stat1Card = new VBox(6, stat1Title, stat1Value, stat1Sub);
        stat1Card.setPadding(new Insets(16));
        stat1Card.setStyle(
                "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px;");
        HBox.setHgrow(stat1Card, Priority.ALWAYS);

        // Card 2: Total Earnings
        Label stat2Title = new Label("Total Earnings");
        stat2Title.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
        Label stat2Value = new Label("₹" + totalEarnings);
        stat2Value.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        Label stat2Sub = new Label("Earned from deliveries");
        stat2Sub.setStyle("-fx-font-size: 11px; -fx-text-fill: #818cf8;");
        VBox stat2Card = new VBox(6, stat2Title, stat2Value, stat2Sub);
        stat2Card.setPadding(new Insets(16));
        stat2Card.setStyle(
                "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px;");
        HBox.setHgrow(stat2Card, Priority.ALWAYS);

        // Card 3: Avg per delivery
        Label stat3Title = new Label("Avg Per Delivery");
        stat3Title.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
        Label stat3Value = new Label(completedCount > 0 ? "₹" + (totalEarnings / completedCount) : "₹0");
        stat3Value.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        Label stat3Sub = new Label("Delivery charge average");
        stat3Sub.setStyle("-fx-font-size: 11px; -fx-text-fill: #a78bfa;");
        VBox stat3Card = new VBox(6, stat3Title, stat3Value, stat3Sub);
        stat3Card.setPadding(new Insets(16));
        stat3Card.setStyle(
                "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px;");
        HBox.setHgrow(stat3Card, Priority.ALWAYS);

        HBox breakdownGrid = new HBox(16, stat1Card, stat2Card, stat3Card);

        // Recent Transactions Section
        Label transSectionTitle = new Label("Recent Transactions");
        transSectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

        VBox transListBox = new VBox(12);

        if (list.isEmpty()) {
            Label noTransLabel = new Label("No delivery transactions yet.");
            noTransLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b; -fx-padding: 16;");
            transListBox.getChildren().add(noTransLabel);
        } else {
            for (WalletTransaction t : list) {
                Label iconLabel = new Label("💰");
                iconLabel.setStyle("-fx-font-size: 18px;");

                Label ordLabel = new Label("Order Payout: #" + t.getOrderId());
                ordLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                Label typeLabel = new Label(t.getType() + "Date : "+t.getDate());
                typeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

                VBox infoBox = new VBox(2, ordLabel, typeLabel);

                Region sp = new Region();
                HBox.setHgrow(sp, Priority.ALWAYS);

                Label dateLabel = new Label(t.getDate());
                dateLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

                Label amtLabel = new Label("+₹" + t.getAmount());
                amtLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #34d399;");

                VBox amtBox = new VBox(2, amtLabel, dateLabel);
                amtBox.setAlignment(Pos.CENTER_RIGHT);

                HBox transItem = new HBox(16, iconLabel, infoBox, sp, amtBox);
                transItem.setAlignment(Pos.CENTER);
                transItem.setPadding(new Insets(14, 18, 14, 18));
                transItem.setStyle(
                        "-fx-background-color: #1e293b; -fx-background-radius: 10px; -fx-border-color: #334155; -fx-border-radius: 10px;");

                transListBox.getChildren().add(transItem);
            }
        }

        VBox mainContent = new VBox(20, topBar, mainBalanceRow, breakdownGrid, transSectionTitle, transListBox);
        mainContent.setPadding(new Insets(24, 32, 24, 32));

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #0b111e; -fx-background-color: #0b111e; -fx-border-color: transparent;");

        HBox rootLayout = new HBox(sidebar, scrollPane);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        rootLayout.setStyle("-fx-background-color: #0b111e;");

        walletPageScene = new Scene(rootLayout, 1550, 850);

        return walletPageScene;
    }
}
