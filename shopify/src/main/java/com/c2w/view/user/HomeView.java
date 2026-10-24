package com.c2w.view.user;

import com.c2w.model.user.ProductsforUser;
import com.c2w.model.user.ProductRepository;
import com.c2w.view.authentication.AuthPage;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.List;

public class HomeView extends ScrollPane {
    private Scene homeViewScene;

    public HomeView() {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: " + Theme.COLOR_BG_PAGE
                + "; -fx-border-color: transparent;");

        VBox content = new VBox(26);
        content.setPadding(new Insets(20, 36, 48, 36));
        content.setMaxWidth(1300);
        content.setAlignment(Pos.TOP_CENTER);

        // --- 1. HERO PROMOTIONAL BANNER ---
        StackPane heroPane = new StackPane();
        heroPane.setPrefHeight(250);
        heroPane.setMinHeight(250);
        heroPane.setMaxHeight(250);
        heroPane.setStyle("-fx-background-radius: 14px;");

        Rectangle heroClip = new Rectangle(1220, 250);
        heroClip.setArcWidth(20);
        heroClip.setArcHeight(20);
        heroPane.setClip(heroClip);

        ImageView heroImg = UIHelper.createImageView("banners/hero_home.png", 1220, 250);
        heroImg.setFitWidth(1220);
        heroImg.setFitHeight(250);

        VBox heroTextBox = new VBox(10);
        heroTextBox.setAlignment(Pos.CENTER_LEFT);
        heroTextBox.setPadding(new Insets(26, 48, 26, 48));

        Label promoTag = new Label("CURATED FOR YOU • NEW ARRIVALS");
        promoTag.setFont(Theme.font(11.5, FontWeight.BOLD));
        promoTag.setTextFill(Color.RED);

        Label headline = new Label("Festive Season Lookbook");
        headline.setFont(Theme.font(30, FontWeight.BOLD));
        // headline.setTextFill(Color.MAGENTA);

        Label subheadline = new Label(
                "Explore curated fashion essentials crafted to define your everyday elegance.");
        subheadline.setFont(Theme.font(13.5));
        // subheadline.setTextFill(Color.PINK);

        HBox heroCtaBox = new HBox(14);
        heroCtaBox.setAlignment(Pos.CENTER_LEFT);
        heroCtaBox.setPadding(new Insets(6, 0, 0, 0));

        Button shopNowBtn = UIHelper.createPrimaryButton("EXPLORE COLLECTION →");
        shopNowBtn.setOnAction(e -> {
            CatalogView cat = new CatalogView("All");
            AuthPage.myStage.setScene(cat.getCatalogViewScene(() -> AuthPage.myStage.setScene(homeViewScene)));
        });

        heroCtaBox.getChildren().add(shopNowBtn);
        heroTextBox.getChildren().addAll( heroCtaBox);
        heroPane.getChildren().addAll(heroImg, heroTextBox);

        // --- 2. TOP CATEGORIES STRIP ---
        HBox catHeader = UIHelper.createSectionHeader("TOP CATEGORIES", "Browse collections crafted for every mood",
                "ALL CATEGORIES >",
                () -> {
                    CategoriesView catView = new CategoriesView();
                    AuthPage.myStage
                            .setScene(catView.getCategoriesViewScene(() -> AuthPage.myStage.setScene(homeViewScene)));
                });

        HBox catStrip = new HBox(14);
        catStrip.setAlignment(Pos.CENTER);

        String[] categories = { "Clothing", "Accessories", "Shoes", "Electronics", "Book" };
        for (String cat : categories) {
            catStrip.getChildren().add(createCategoryPillCard(cat));
        }

        // --- 3. TRENDING PRODUCTS ---
        HBox trendingHeader = UIHelper.createSectionHeader("TRENDING PRODUCTS", "Bestsellers and highly-rated pieces",
                "VIEW ALL >",
                () -> {
                    CatalogView cat = new CatalogView("All");
                    AuthPage.myStage.setScene(cat.getCatalogViewScene(() -> AuthPage.myStage.setScene(homeViewScene)));
                });

        HBox trendingCards = new HBox(16);
        trendingCards.setAlignment(Pos.CENTER_LEFT);

        List<ProductsforUser> trending = ProductRepository.getInstance().getTrending();
        System.out.println("===========trending size===========" + trending.size());
        for (ProductsforUser p : trending) {
            trendingCards.getChildren().add(new ProductCardView(p));
        }

        ScrollPane trendingScroll = new ScrollPane(trendingCards);
        trendingScroll.setFitToHeight(true);
        trendingScroll.setVbarPolicy(ScrollBarPolicy.NEVER);
        trendingScroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        trendingScroll.setStyle(
                "-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        // --- 4. RECOMMENDED FOR YOU ---
        HBox recHeader = UIHelper.createSectionHeader("RECOMMENDED FOR YOU",
                "Selected specifically based on your taste profile", "DISCOVER MORE >",
                () -> {
                    CatalogView cat = new CatalogView("All");
                    AuthPage.myStage.setScene(cat.getCatalogViewScene(() -> AuthPage.myStage.setScene(homeViewScene)));
                });

        HBox recCards = new HBox(16);
        recCards.setAlignment(Pos.CENTER_LEFT);

        List<ProductsforUser> recommended = ProductRepository.getInstance().getRecommended();
        for (ProductsforUser p : recommended) {
            recCards.getChildren().add(new ProductCardView(p));
        }

        ScrollPane recScroll = new ScrollPane(recCards);
        recScroll.setFitToHeight(true);
        recScroll.setVbarPolicy(ScrollBarPolicy.NEVER);
        recScroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        recScroll.setStyle(
                "-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        // --- 5. SPECIAL OFFERS & BANK DEALS ---
        // HBox bankDeals = new HBox(16);
        // bankDeals.setAlignment(Pos.CENTER);
        // bankDeals.getChildren().addAll(
        // createDealCard("🏦 HDFC Bank Instant 10% Off",
        // "Get 10% instant discount up to ₹1,000 on HDFC Credit Cards.",
        // Theme.COLOR_BG_CARD,
        // Theme.COLOR_GOLD),
        // createDealCard("⚡ First Order: Code FIRSTLOOK10",
        // "Enjoy flat 10% off on your inaugural order with free express shipping.",
        // Theme.COLOR_BG_CARD,
        // Theme.COLOR_PRIMARY),
        // createDealCard("💳 UPI Cashback Bonanza",
        // "Get ₹150 assured cashback on all Google Pay / PhonePe transactions.",
        // Theme.COLOR_BG_CARD,
        // Theme.COLOR_SUCCESS));

        content.getChildren().addAll(heroPane, catHeader, catStrip, trendingHeader, trendingScroll, recHeader,
                recScroll);

        StackPane centerWrapper = new StackPane(content);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        centerWrapper.setStyle("-fx-background-color: transparent;");
        setContent(centerWrapper);
    }

    private VBox createCategoryPillCard(String categoryName) {
        VBox pill = new VBox(6);
        pill.setAlignment(Pos.CENTER);
        pill.setCursor(Cursor.HAND);
        pill.setPrefWidth(180);
        pill.setPadding(new Insets(10));
        pill.setStyle("-fx-background-color: " + Theme.COLOR_BG_CARD + "; -fx-background-radius: 10px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 10px;");

        ImageView img = UIHelper.createImageView("categories/" + categoryName.toLowerCase() + ".png", 160, 110);
        img.setFitWidth(160);
        img.setFitHeight(110);
        img.setPreserveRatio(false);

        Rectangle clip = new Rectangle(160, 110);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        img.setClip(clip);

        Label lbl = new Label(categoryName.toUpperCase());
        lbl.setFont(Theme.font(12, FontWeight.BOLD));
        lbl.setTextFill(Color.web(Theme.COLOR_DARK));

        pill.getChildren().addAll(img, lbl);

        pill.setOnMouseEntered(e -> {
            pill.setStyle("-fx-background-color: " + Theme.COLOR_BG_ELEVATED + "; -fx-background-radius: 10px; " +
                    "-fx-border-color: " + Theme.COLOR_PRIMARY + "; -fx-border-radius: 10px;");
            ScaleTransition st = new ScaleTransition(Duration.millis(120), pill);
            st.setToX(1.03);
            st.setToY(1.03);
            st.play();
        });

        pill.setOnMouseExited(e -> {
            pill.setStyle("-fx-background-color: " + Theme.COLOR_BG_CARD + "; -fx-background-radius: 10px; " +
                    "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 10px;");
            ScaleTransition st = new ScaleTransition(Duration.millis(120), pill);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        pill.setOnMouseClicked(e -> {
            CatalogView cat = new CatalogView(categoryName);
            AuthPage.myStage.setScene(cat.getCatalogViewScene(() -> AuthPage.myStage.setScene(homeViewScene)));
        });
        return pill;
    }

    private VBox createDealCard(String title, String desc, String bgHex, String accentHex) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setPrefWidth(380);
        card.setStyle("-fx-background-color: " + bgHex + "; -fx-background-radius: 8px; " +
                "-fx-border-color: " + accentHex + "; -fx-border-width: 1px;");

        Label titleLbl = new Label(title);
        titleLbl.setFont(Theme.font(13, FontWeight.BOLD));
        titleLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        Label descLbl = new Label(desc);
        descLbl.setFont(Theme.font(11.5));
        descLbl.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));
        descLbl.setWrapText(true);

        card.getChildren().addAll(titleLbl, descLbl);
        return card;
    }

    public Scene getHomeViewScene(Runnable backAction) {
        BorderPane rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + Theme.COLOR_BG_PAGE + ";");
        rootLayout.setTop(new NavbarView());
        rootLayout.setCenter(this);
        StackPane root = new StackPane(rootLayout, new ToastView(), new ChatbotView());
        homeViewScene = new Scene(root, 1550, 850);
        homeViewScene.setFill(Theme.PAINT_BG_PAGE);
        return homeViewScene;
    }

    public Scene getUserHomePageScene(Runnable backAction) {
        return getHomeViewScene(backAction);
    }
}