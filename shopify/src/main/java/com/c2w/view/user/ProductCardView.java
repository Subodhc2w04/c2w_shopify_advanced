package com.c2w.view.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.c2w.controller.user.CartController;
import com.c2w.controller.user.ProductController;
import com.c2w.controller.user.WishlistController;
import com.c2w.model.user.ProductsforUser;
import com.c2w.model.user.WishlistModel;
import com.c2w.view.authentication.AuthPage;
import com.c2w.view.user.Theme;
import com.c2w.view.user.UIHelper;
import com.c2w.view.user.ProductDetailsView;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;


public class ProductCardView extends VBox {
    private final ProductsforUser product;
    private final Button wishlistBtn;
    private boolean loaded = false;
    private final List<ProductsforUser> wishlistProducts = new ArrayList<>();

    public ProductCardView(ProductsforUser product) {
        this.product = product;

        setPrefWidth(225);
        setMaxWidth(235);
        setStyle("-fx-background-color: " + Theme.COLOR_BG_CARD + "; " +
                "-fx-background-radius: 8px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; " +
                "-fx-border-radius: 8px; " +
                "-fx-border-width: 1px;");
        setEffect(Theme.cardShadow());
        setCursor(Cursor.HAND);

        // --- 1. IMAGE CONTAINER ---
        StackPane imgContainer = new StackPane();
        imgContainer.setPrefSize(225, 205);
        imgContainer.setMinSize(225, 205);
        imgContainer.setMaxSize(235, 205);

        

        String prodName = product.getProductName() != null ? product.getProductName() : "Product";
        String prodCat = product.getProductCategory() != null ? product.getProductCategory() : "Fashion";

        ImageView imgView = UIHelper.createImageView(product.getProductImage(), 225, 205);
        imgView.setFitWidth(225);
        imgView.setFitHeight(205);
        imgView.setPreserveRatio(false);
        imgView.setSmooth(true);

        Rectangle clip = new Rectangle(225, 205);
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        imgView.setClip(clip);

        // Discount Tag (Top-Left)
        if (product.getDiscountValue() > 0) {
            Label discountBadge = UIHelper.createBadge((int) product.getDiscountValue() + "% OFF", Theme.COLOR_PRIMARY,
                    "#FFFFFF");
            StackPane.setAlignment(discountBadge, Pos.TOP_LEFT);
            StackPane.setMargin(discountBadge, new Insets(10, 0, 0, 10));
            imgContainer.getChildren().add(discountBadge);
        }

        // Wishlist Heart Button (Top-Right)
        wishlistBtn = new Button();
        wishlistBtn.setCursor(Cursor.HAND);
        wishlistBtn.setTooltip(new Tooltip("Add to Wishlist"));
        updateWishlistIcon();

        wishlistBtn.setOnAction(e -> {
            e.consume();
            WishlistController.getInstance().toggle(product);
            updateWishlistIcon();

            ScaleTransition pulse = new ScaleTransition(Duration.millis(140), wishlistBtn);
            pulse.setFromX(1.0);
            pulse.setFromY(1.0);
            pulse.setToX(1.35);
            pulse.setToY(1.35);
            pulse.setAutoReverse(true);
            pulse.setCycleCount(2);
            pulse.play();
        });

        StackPane.setAlignment(wishlistBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(wishlistBtn, new Insets(8, 8, 0, 0));

        // Rating Pill (Bottom-Left)
        HBox ratingPill = UIHelper.createRatingPill(product.getRating(), product.getRatingCount());
        StackPane.setAlignment(ratingPill, Pos.BOTTOM_LEFT);
        StackPane.setMargin(ratingPill, new Insets(0, 0, 8, 8));

        imgContainer.getChildren().addAll(imgView, wishlistBtn);

        // --- 2. DETAILS SECTION ---
        VBox infoBox = new VBox(6);
        infoBox.setPadding(new Insets(10, 12, 12, 12));

        Label nameLbl = new Label(prodName);
        nameLbl.setFont(Theme.font(12, FontWeight.BOLD));
        nameLbl.setTextFill(Color.web(Theme.COLOR_DARK));
        nameLbl.setMaxWidth(205);
        nameLbl.setTextOverrun(javafx.scene.control.OverrunStyle.ELLIPSIS);
        nameLbl.setWrapText(false);

        HBox priceRow = UIHelper.createPriceRow(product.getProductPrice(), product.getOriginalPrice(),
                product.getDiscountValue());

        Button addCartBtn = new Button("ADD TO CART");
        addCartBtn.setFont(Theme.font(11, FontWeight.BOLD));
        addCartBtn.setCursor(Cursor.HAND);
        addCartBtn.setMaxWidth(Double.MAX_VALUE);
        addCartBtn.setPadding(new Insets(7, 0, 7, 0));
        addCartBtn.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY_LIGHT + "; " +
                "-fx-text-fill: " + Theme.COLOR_PRIMARY + "; " +
                "-fx-border-color: " + Theme.COLOR_PRIMARY + "; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 4px; " +
                "-fx-background-radius: 4px;");

        addCartBtn.setOnMouseEntered(ev -> addCartBtn.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY
                + "; -fx-text-fill: white; -fx-background-radius: 4px;"));
        addCartBtn.setOnMouseExited(ev -> addCartBtn.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY_LIGHT
                + "; -fx-text-fill: " + Theme.COLOR_PRIMARY + "; -fx-border-color: " + Theme.COLOR_PRIMARY
                + "; -fx-border-radius: 4px; -fx-background-radius: 4px;"));

        addCartBtn.setOnAction(e -> {
            e.consume();
            CartController.getInstance().addToCart(product, 1);
        });

        VBox.setMargin(addCartBtn, new Insets(4, 0, 0, 0));
        infoBox.getChildren().addAll(nameLbl, priceRow, addCartBtn, ratingPill);

        getChildren().addAll(imgContainer, infoBox);

        // Hover Effect
        setOnMouseEntered(e -> {
            setEffect(Theme.cardHoverShadow());
            ScaleTransition st = new ScaleTransition(Duration.millis(140), this);
            st.setToX(1.025);
            st.setToY(1.025);
            st.play();
        });

        setOnMouseExited(e -> {
            setEffect(Theme.cardShadow());
            ScaleTransition st = new ScaleTransition(Duration.millis(140), this);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        // Click to view product details
        setOnMouseClicked(e -> {
            ProductDetailsView details = new ProductDetailsView(product);
            Scene currentScene = AuthPage.myStage != null ? AuthPage.myStage.getScene() : null;
            if (AuthPage.myStage != null) {
                AuthPage.myStage.setScene(details.getProductDetailsScene(() -> {
                    if (currentScene != null) {
                        AuthPage.myStage.setScene(currentScene);
                    }
                }));
            }
        });

        WishlistModel.getInstance().addListener(this::updateWishlistIcon);
    }

    private void updateWishlistIcon() {
        if (product == null || product.getProductId() == null)
            return;
        boolean wishlisted = WishlistModel.getInstance().isWishlisted(product.getProductId());
        System.out.println("liked product ID:"+product.getProductId());
        if (wishlisted) {
            wishlistBtn.setText("♥");
            wishlistBtn.setStyle("-fx-background-color: rgba(30,30,38,0.85); " +
                    "-fx-background-radius: 15px; " +
                    "-fx-text-fill: " + Theme.COLOR_PRIMARY + "; " +
                    "-fx-font-size: 15px; -fx-font-weight: bold; " +
                    "-fx-min-width: 30px; -fx-min-height: 30px; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 4, 0, 0, 1);");

           loadFromFirebase();
        } else {
            wishlistBtn.setText("♡");
            wishlistBtn.setStyle("-fx-background-color: rgba(30,30,38,0.7); " +
                    "-fx-background-radius: 15px; " +
                    "-fx-text-fill: " + Theme.COLOR_SECONDARY_TEXT + "; " +
                    "-fx-font-size: 15px; -fx-font-weight: bold; " +
                    "-fx-min-width: 30px; -fx-min-height: 30px; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 4, 0, 0, 1);");
        }
    }

    public synchronized void loadFromFirebase() {
    try {
        List<ProductsforUser> liked = WishlistModel.getInstance().getWishlistedProducts();
        System.out.println("============wish:" + liked.size());

        wishlistProducts.clear();
        wishlistProducts.addAll(liked);

        loaded = true;
        System.out.println("loaded-wishlist :" + loaded);
        System.out.println("wishlist products:" + wishlistProducts.size());
    } catch (Exception e) {
        System.err.println("Failed to load wishlist products: " + e.getMessage());
    }
}

    public synchronized void refresh() {
        loadFromFirebase();
    }
    public List<ProductsforUser> getAll() {
        if (!loaded) {
            loadFromFirebase();
        }
        return Collections.unmodifiableList(wishlistProducts);
    }
}
