package com.c2w.view.delivery;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class HomePage {

    private Scene homePageScene;

    public Scene getHomePageScene(Runnable backAction) {

        // Top Navigation Bar
        Label brandIcon = new Label("⚡");
        brandIcon.setStyle("-fx-font-size: 24px; -fx-text-fill: #3b82f6;");

        Label brandTitle = new Label("SHOPPING DELIVERY");
        brandTitle.setStyle(
                "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-letter-spacing: 1px;");

        HBox brandBox = new HBox(10, brandIcon, brandTitle);
        brandBox.setAlignment(Pos.CENTER_LEFT);

        Region navSpacer = new Region();
        HBox.setHgrow(navSpacer, Priority.ALWAYS);

        Button navLoginBtn = new Button("Login to Portal");
        navLoginBtn.setStyle(
                "-fx-background-color: #2563eb; -fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 8px; -fx-cursor: hand;");

        HBox topNavBar = new HBox(20, brandBox, navSpacer, navLoginBtn);
        topNavBar.setAlignment(Pos.CENTER);
        topNavBar.setPadding(new Insets(20, 50, 20, 50));
        topNavBar.setStyle("-fx-background-color: #0f172a; -fx-border-color: #1e293b; -fx-border-width: 0 0 1 0;");

        // Hero Section
        Label badgeLabel = new Label("DELIVERY PARTNER NETWORK");
        badgeLabel.setStyle(
                "-fx-background-color: #1e1b4b; -fx-text-fill: #818cf8; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 20px; -fx-border-color: #4338ca; -fx-border-radius: 20px;");

        Label heroTitle = new Label("SHOPPING DELIVERY");
        heroTitle.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

        Label heroSubtitle = new Label("Delivery Partner Portal");
        heroSubtitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #38bdf8;");

        Label heroDescription = new Label(
                "Manage assigned orders, navigate live delivery routes, track earnings, and deliver smiles across the city seamlessly.");
        heroDescription.setWrapText(true);
        heroDescription.setMaxWidth(650);
        heroDescription.setAlignment(Pos.CENTER);
        heroDescription.setStyle("-fx-font-size: 15px; -fx-text-fill: #94a3b8; -fx-text-alignment: center;");

        Button mainLoginBtn = new Button("🚀 Partner Login");
        mainLoginBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #2563eb, #7c3aed); -fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 14 36; -fx-background-radius: 10px; -fx-cursor: hand;");

        // Feature Highlights Cards
        Label card1Icon = new Label("📦");
        card1Icon.setStyle("-fx-font-size: 28px;");
        Label card1Title = new Label("Smart Order Assignment");
        card1Title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        Label card1Desc = new Label("Receive optimized orders tailored for quick pickups and fast drop-offs.");
        card1Desc.setWrapText(true);
        card1Desc.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");
        VBox card1 = new VBox(10, card1Icon, card1Title, card1Desc);
        card1.setPrefWidth(280);
        card1.setPadding(new Insets(20));
        card1.setStyle(
                "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px;");

        Label card2Icon = new Label("📍");
        card2Icon.setStyle("-fx-font-size: 28px;");
        Label card2Title = new Label("Live Route Tracking");
        card2Title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        Label card2Desc = new Label("Real-time GPS status coordinates with interactive delivery state controls.");
        card2Desc.setWrapText(true);
        card2Desc.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");
        VBox card2 = new VBox(10, card2Icon, card2Title, card2Desc);
        card2.setPrefWidth(280);
        card2.setPadding(new Insets(20));
        card2.setStyle(
                "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px;");

        Label card3Icon = new Label("💰");
        card3Icon.setStyle("-fx-font-size: 28px;");
        Label card3Title = new Label("Instant Wallet Payouts");
        card3Title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        Label card3Desc = new Label("Transparent earnings breakdown with automated per-order settlements.");
        card3Desc.setWrapText(true);
        card3Desc.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");
        VBox card3 = new VBox(10, card3Icon, card3Title, card3Desc);
        card3.setPrefWidth(280);
        card3.setPadding(new Insets(20));
        card3.setStyle(
                "-fx-background-color: #1e293b; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px;");

        HBox featuresBox = new HBox(24, card1, card2, card3);
        featuresBox.setAlignment(Pos.CENTER);

        VBox heroContent = new VBox(18, badgeLabel, heroTitle, heroSubtitle, heroDescription, mainLoginBtn,
                featuresBox);
        heroContent.setAlignment(Pos.CENTER);
        heroContent.setPadding(new Insets(40, 50, 40, 50));

        // Footer
        Label footerText = new Label("Shopping Delivery Partner Portal © 2026 • JavaFX 17 Prototype");
        footerText.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");
        VBox footerBox = new VBox(footerText);
        footerBox.setAlignment(Pos.CENTER);
        footerBox.setPadding(new Insets(15));
        footerBox.setStyle("-fx-background-color: #0b111e;");

        VBox rootLayout = new VBox(topNavBar, heroContent, footerBox);
        rootLayout.setStyle("-fx-background-color: #0b111e;");
        VBox.setVgrow(heroContent, Priority.ALWAYS);

        homePageScene = new Scene(rootLayout, 1550, 850);

        // Actions
        // LoginPage loginPage = new LoginPage();
        // navLoginBtn.setOnAction(e -> {
        // AuthPage.myStage.setScene(loginPage.getLoginPageScene(() ->
        // AuthPage.myStage.setScene(homePageScene)));
        // });
        // mainLoginBtn.setOnAction(e -> {
        // AuthPage.myStage.setScene(loginPage.getLoginPageScene(() ->
        // AuthPage.myStage.setScene(homePageScene)));
        // });
        return homePageScene;
    }
}
