package com.c2w.view.user;

import com.c2w.model.user.AddToCart;
import com.c2w.model.user.SessionModel;
import com.c2w.model.user.WishlistModel;
import com.c2w.view.authentication.AuthPage;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * Top sticky navigation bar view.
 */
public class NavbarView extends HBox {
    private final TextField searchField;
    private final Button searchClearBtn;
    private final Label cartBadgeLbl;
    private final Label wishlistBadgeLbl;
    private final StackPane cartBadgeContainer;
    private final StackPane wishlistBadgeContainer;

    public NavbarView() {
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0, 32, 0, 32));
        setPrefHeight(68);
        setMinHeight(68);
        setMaxHeight(68);
        setStyle("-fx-background-color: " + Theme.COLOR_BG_SECTION + "; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; " +
                "-fx-border-width: 0 0 1px 0;");
        setEffect(Theme.navbarShadow());

        // --- 1. BRAND LOGO ---
        HBox logoBox = new HBox(6);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.setCursor(Cursor.HAND);

        Label logoIcon = new Label("✦");
        logoIcon.setFont(Theme.font(22, FontWeight.BOLD));
        logoIcon.setTextFill(Color.web(Theme.COLOR_PRIMARY));

        VBox textGroup = new VBox(-3);
        textGroup.setAlignment(Pos.CENTER_LEFT);

        Label logoMain = new Label("C2W");
        logoMain.setFont(Theme.font(20, FontWeight.BOLD));
        logoMain.setTextFill(Color.WHITE);

        Label logoSub = new Label("FASHION");
        logoSub.setFont(Theme.font(8.5, FontWeight.BOLD));
        logoSub.setTextFill(Color.web(Theme.COLOR_PRIMARY));
        logoSub.setStyle("-fx-letter-spacing: 2px;");

        textGroup.getChildren().addAll(logoMain, logoSub);
        logoBox.getChildren().addAll(logoIcon, textGroup);

        logoBox.setOnMouseClicked(e -> navigateTo(new MainUser().getUserHomePageScene(() -> AuthPage.myStage.setScene(new AuthPage().showLoginPage()))));

        // --- 2. NAV LINKS ---
        HBox navLinks = new HBox(18);
        navLinks.setAlignment(Pos.CENTER_LEFT);
        navLinks.setPadding(new Insets(0, 0, 0, 24));

        navLinks.getChildren().addAll(
                createNavLink("HOME", () -> navigateTo(new MainUser().getUserHomePageScene(() -> AuthPage.myStage.setScene(new AuthPage().showLoginPage())))),
                createNavLink("CLOTHING", () -> navigateTo(new CatalogView("Clothing").getCatalogViewScene(homeBackAction()))),
                createNavLink("ACCESSORIES", () -> navigateTo(new CatalogView("Accessories").getCatalogViewScene(homeBackAction()))),
                createNavLink("SHOES", () -> navigateTo(new CatalogView("Shoes").getCatalogViewScene(homeBackAction()))),
                createNavLink("ELECTRONICS", () -> navigateTo(new CatalogView("Electronics").getCatalogViewScene(homeBackAction()))),
                createNavLink("BOOK", () -> navigateTo(new CatalogView("Book").getCatalogViewScene(homeBackAction())))
        );

        // --- 3. LIVE SEARCH BAR ---
        HBox searchContainer = new HBox(8);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(0, 12, 0, 14));
        searchContainer.setPrefHeight(40);
        searchContainer.setPrefWidth(320);
        searchContainer.setMaxWidth(380);
        searchContainer.setStyle("-fx-background-color: " + Theme.COLOR_BG_INPUT + "; -fx-background-radius: 6px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-width: 1px; -fx-border-radius: 6px;");

        Label searchIcon = new Label("🔍");
        searchIcon.setFont(Theme.font(13));
        searchIcon.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

        searchField = new TextField();
        searchField.setPromptText("Search for products, brands and more...");
        searchField.setFont(Theme.font(12.5));
        searchField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; " +
                "-fx-text-fill: " + Theme.COLOR_DARK + "; -fx-prompt-text-fill: " + Theme.COLOR_MUTED_TEXT + ";");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchClearBtn = new Button("✕");
        searchClearBtn.setFont(Theme.font(10, FontWeight.BOLD));
        searchClearBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + Theme.COLOR_MUTED_TEXT + "; -fx-cursor: hand;");
        searchClearBtn.setVisible(false);

        searchClearBtn.setOnAction(e -> {
            searchField.clear();
            searchClearBtn.setVisible(false);
            navigateTo(new CatalogView("All").getCatalogViewScene(homeBackAction()));
        });

        searchField.textProperty().addListener((obs, oldV, newV) -> {
            searchClearBtn.setVisible(newV != null && !newV.trim().isEmpty());
        });

        searchField.setOnAction(e -> {
            String query = searchField.getText();
            if (query != null && !query.trim().isEmpty()) {
                navigateTo(new CatalogView(query.trim()).getCatalogViewScene(homeBackAction()));
            }
        });

        searchContainer.getChildren().addAll(searchIcon, searchField, searchClearBtn);

        // --- 4. RIGHT ACTIONS ---
        HBox rightActions = new HBox(14);
        rightActions.setAlignment(Pos.CENTER_RIGHT);

        StackPane wishlistBtn = createActionIcon("♡", "Wishlist",
                () -> navigateTo(new WishlistView().getWishlistViewScene(homeBackAction())));
        wishlistBadgeLbl = new Label("0");
        wishlistBadgeLbl.setFont(Theme.font(9.5, FontWeight.BOLD));
        wishlistBadgeLbl.setTextFill(Color.WHITE);

        wishlistBadgeContainer = new StackPane(wishlistBadgeLbl);
        wishlistBadgeContainer.setMinSize(16, 16);
        wishlistBadgeContainer.setMaxSize(16, 16);
        wishlistBadgeContainer
                .setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY + "; -fx-background-radius: 8px;");
        StackPane.setAlignment(wishlistBadgeContainer, Pos.TOP_RIGHT);
        StackPane.setMargin(wishlistBadgeContainer, new Insets(-3, -3, 0, 0));
        wishlistBtn.getChildren().add(wishlistBadgeContainer);

        StackPane cartBtn = createActionIcon("🛒", "Bag",
                () -> navigateTo(new CartView().getCartViewScene(homeBackAction())));
        cartBadgeLbl = new Label("0");
        cartBadgeLbl.setFont(Theme.font(9.5, FontWeight.BOLD));
        cartBadgeLbl.setTextFill(Color.WHITE);

        cartBadgeContainer = new StackPane(cartBadgeLbl);
        cartBadgeContainer.setMinSize(16, 16);
        cartBadgeContainer.setMaxSize(16, 16);
        cartBadgeContainer.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY + "; -fx-background-radius: 8px;");
        StackPane.setAlignment(cartBadgeContainer, Pos.TOP_RIGHT);
        StackPane.setMargin(cartBadgeContainer, new Insets(-3, -3, 0, 0));
        cartBtn.getChildren().add(cartBadgeContainer);

        StackPane ordersBtn = createActionIcon("📦", "Orders",
                () -> navigateTo(new OrderHistoryView().getOrderHistoryViewScene(homeBackAction())));

        HBox profileBtn = new HBox(6);
        profileBtn.setAlignment(Pos.CENTER);
        profileBtn.setCursor(Cursor.HAND);
        profileBtn.setPadding(new Insets(4, 10, 4, 10));
        profileBtn.setStyle("-fx-background-color: " + Theme.COLOR_BG_ELEVATED + "; -fx-background-radius: 20px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 20px;");

        Label avatar = new Label("👤");
        avatar.setFont(Theme.font(13));
        avatar.setTextFill(Color.WHITE);

        Label userName = new Label("Profile");
        userName.setFont(Theme.font(11.5, FontWeight.BOLD));
        userName.setTextFill(Color.web(Theme.COLOR_DARK));

        profileBtn.getChildren().addAll(avatar, userName);
        profileBtn.setOnMouseClicked(e -> {
            System.out.println("++++++++++++++++++++");
            navigateTo(new ProfileView().getProfileViewScene(homeBackAction()));
        }
        );

        rightActions.getChildren().addAll(wishlistBtn, cartBtn, ordersBtn, profileBtn);

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        Region spacer2 = new Region();
        spacer2.setPrefWidth(20);

        getChildren().addAll(logoBox, navLinks, spacer1, searchContainer, spacer2, rightActions);

        // Reactive Listeners
        WishlistModel.getInstance().addListener(this::refreshWishlistBadge);
        AddToCart.getInstance().addListener(this::refreshCartBadge);
        SessionModel.getInstance().addListener(() -> {
            if (SessionModel.getInstance().isLoggedIn()) {
                String first = SessionModel.getInstance().getCurrentUser().getFullName().split(" ")[0];
                userName.setText(first);
            } else {
                userName.setText("Profile");
            }
        });

        refreshWishlistBadge();
        refreshCartBadge();
    }

    private Button createNavLink(String title, Runnable action) {
        Button btn = new Button(title);
        btn.setFont(Theme.font(12, FontWeight.BOLD));
        btn.setCursor(Cursor.HAND);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Theme.COLOR_SECONDARY_TEXT
                + "; -fx-padding: 6 0 6 0;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: "
                + Theme.COLOR_PRIMARY + "; -fx-padding: 6 0 6 0;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: "
                + Theme.COLOR_SECONDARY_TEXT + "; -fx-padding: 6 0 6 0;"));
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private Button createSpecialNavLink(String title, Runnable action) {
        Button btn = new Button(title);
        btn.setFont(Theme.font(11.5, FontWeight.BOLD));
        btn.setCursor(Cursor.HAND);
        btn.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY_LIGHT + "; " +
                "-fx-text-fill: " + Theme.COLOR_PRIMARY + "; " +
                "-fx-background-radius: 12px; " +
                "-fx-padding: 4 10 4 10;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY
                + "; -fx-text-fill: white; -fx-background-radius: 12px; -fx-padding: 4 10 4 10;"));
        btn.setOnMouseExited(
                e -> btn.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY_LIGHT + "; -fx-text-fill: "
                        + Theme.COLOR_PRIMARY + "; -fx-background-radius: 12px; -fx-padding: 4 10 4 10;"));
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private StackPane createActionIcon(String symbol, String tooltipText, Runnable action) {
        StackPane container = new StackPane();
        container.setCursor(Cursor.HAND);
        container.setPrefSize(42, 42);
        container.setMinSize(42, 42);

        VBox iconGroup = new VBox(0);
        iconGroup.setAlignment(Pos.CENTER);

        Label iconLbl = new Label(symbol);
        iconLbl.setFont(Theme.font(16));
        iconLbl.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));

        Label textLbl = new Label(tooltipText);
        textLbl.setFont(Theme.font(9.5, FontWeight.BOLD));
        textLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

        iconGroup.getChildren().addAll(iconLbl, textLbl);
        container.getChildren().add(iconGroup);

        container.setOnMouseEntered(e -> {
            iconLbl.setTextFill(Color.web(Theme.COLOR_PRIMARY));
            textLbl.setTextFill(Color.web(Theme.COLOR_PRIMARY));
        });
        container.setOnMouseExited(e -> {
            iconLbl.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));
            textLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
        });

        container.setOnMouseClicked(e -> action.run());
        return container;
    }

    private void refreshWishlistBadge() {
        int count = WishlistModel.getInstance().getCount();
        System.out.println("----wishlist count:"+count);
        wishlistBadgeLbl.setText(String.valueOf(count));
        wishlistBadgeContainer.setVisible(count > 0);
        popBadge(wishlistBadgeContainer);
    }

    private void refreshCartBadge() {
        int count = AddToCart.getInstance().getItemCount();
        cartBadgeLbl.setText(String.valueOf(count));
        cartBadgeContainer.setVisible(count > 0);
        popBadge(cartBadgeContainer);
    }

    private void popBadge(Node badge) {
        ScaleTransition st = new ScaleTransition(Duration.millis(120), badge);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.35);
        st.setToY(1.35);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    private void navigateTo(Scene scene) {
        if (scene != null && AuthPage.myStage != null) {
            AuthPage.myStage.setScene(scene);
        }
    }

    private Runnable homeBackAction() {
        return () -> navigateTo(new MainUser().getUserHomePageScene(() -> AuthPage.myStage.setScene(new AuthPage().showLoginPage())));
    }
}
