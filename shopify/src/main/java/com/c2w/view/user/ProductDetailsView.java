package com.c2w.view.user;

import com.c2w.controller.user.CartController;
import com.c2w.controller.user.WishlistController;
import com.c2w.model.user.AddToCart;
import com.c2w.model.user.OrderUser;
import com.c2w.model.user.ProductsforUser;
import com.c2w.model.user.ProductRepository;
import com.c2w.view.authentication.AuthPage;
import com.c2w.view.user.Theme;
import com.c2w.view.user.UIHelper;
import com.c2w.view.user.ChatbotView;
import com.c2w.view.user.NavbarView;
import com.c2w.view.user.ProductCardView;
import com.c2w.view.user.ToastView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDetailsView extends ScrollPane {
    private int quantity = 1;
    private Scene productDetailsScene;
    private Button backBtn;

    List<OrderUser> orders = new ArrayList<>();
    
    public ProductDetailsView() {
        this(null);
    }

    public ProductDetailsView(ProductsforUser product) {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: " + Theme.COLOR_BG_PAGE
                + "; -fx-border-color: transparent;");

        if (product == null) {
            List<ProductsforUser> all = ProductRepository.getInstance().getAll();
            if (!all.isEmpty()) {
                product = all.get(0);
            } else {
                product = new ProductsforUser();
            }
        }

        final ProductsforUser currentProd = product;

        VBox content = new VBox(32);
        content.setPadding(new Insets(24, 40, 48, 40));
        content.setMaxWidth(1280);
        content.setAlignment(Pos.TOP_CENTER);

        // Breadcrumb
        HBox breadcrumb = new HBox(8);
        breadcrumb.setAlignment(Pos.CENTER_LEFT);

        backBtn = UIHelper.createGhostButton("← Back");

        Hyperlink homeLink = new Hyperlink("Home");
        homeLink.setFont(Theme.font(12));
        homeLink.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
        homeLink.setOnAction(e -> {
            MainUser home = new MainUser();
            AuthPage.myStage.setScene(home.getUserHomePageScene(() -> AuthPage.myStage.setScene(new AuthPage().showLoginPage())));
        });

        Label sep1 = new Label("/");
        sep1.setFont(Theme.font(12));
        sep1.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

        String categoryName = product.getProductCategory() != null ? product.getProductCategory() : "All";
        Hyperlink catLink = new Hyperlink(categoryName);
        catLink.setFont(Theme.font(12));
        catLink.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
        catLink.setOnAction(e -> {
            CatalogView cat = new CatalogView(categoryName);
            AuthPage.myStage.setScene(cat.getCatalogViewScene(() -> AuthPage.myStage.setScene(productDetailsScene)));
        });

        Label sep2 = new Label("/");
        sep2.setFont(Theme.font(12));
        sep2.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

        String prodName = product.getProductName() != null ? product.getProductName() : "Product Details";
        String prodId = product.getProductId() != null ? product.getProductId() : "Product ID";
        Label prodNameBreadcrumb = new Label(prodName);
        prodNameBreadcrumb.setFont(Theme.font(12, FontWeight.BOLD));
        prodNameBreadcrumb.setTextFill(Color.web(Theme.COLOR_DARK));

        breadcrumb.getChildren().addAll(backBtn, homeLink, sep1, catLink, sep2, prodNameBreadcrumb);

        // Main 2-Column Split: Image on Left, Details on Right
        HBox split = new HBox(36);
        split.setAlignment(Pos.TOP_CENTER);

        // --- LEFT COLUMN: IMAGE GALLERY ---
        VBox leftGallery = new VBox(12);
        leftGallery.setAlignment(Pos.TOP_CENTER);
        leftGallery.setPrefWidth(420);
        leftGallery.setMinWidth(420);

        ImageView mainImg = UIHelper.createImageView(product.getProductImage(), 420, 460);
        mainImg.setFitWidth(420);
        mainImg.setFitHeight(460);
        mainImg.setPreserveRatio(false);

        Rectangle mainClip = new Rectangle(420, 460);
        mainClip.setArcWidth(16);
        mainClip.setArcHeight(16);
        mainImg.setClip(mainClip);

     
        leftGallery.getChildren().addAll(mainImg);

        // --- RIGHT COLUMN: PRODUCT INFORMATION ---
        VBox rightDetails = new VBox(16);
        rightDetails.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(rightDetails, Priority.ALWAYS);

        Label nameLbl = new Label(prodName);
        nameLbl.setFont(Theme.font(18, FontWeight.BOLD));
        nameLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        HBox ratingRow = UIHelper.createRatingPill(product.getRating(), product.getRatingCount());
        
        // Pricing Box
        HBox priceBox = new HBox(12);
        priceBox.setAlignment(Pos.CENTER_LEFT);

        Label finalPriceLbl = new Label(UIHelper.formatCurrency(product.getProductPrice()));
        finalPriceLbl.setFont(Theme.font(24, FontWeight.BOLD));
        finalPriceLbl.setTextFill(Color.web(Theme.COLOR_DARK));
         priceBox.getChildren().addAll(finalPriceLbl);

       

        Label taxLbl = new Label("inclusive of all taxes");
        taxLbl.setFont(Theme.font(11));
        taxLbl.setTextFill(Color.web(Theme.COLOR_SUCCESS));

        // Description Box
        if (product.getProductDescription() != null && !product.getProductDescription().trim().isEmpty()) {
            Label descLbl = new Label(product.getProductDescription());
            descLbl.setFont(Theme.font(13));
            descLbl.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));
            descLbl.setWrapText(true);
            descLbl.setMaxWidth(600);
            rightDetails.getChildren().add(descLbl);
        }

        // Quantity Picker
        HBox qtyRow = new HBox(10);
        qtyRow.setAlignment(Pos.CENTER_LEFT);
        Label qtyLbl = new Label("QUANTITY:");
        qtyLbl.setFont(Theme.font(12, FontWeight.BOLD));
        qtyLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        Button minusBtn = new Button("-");
        minusBtn.setFont(Theme.font(14, FontWeight.BOLD));
        minusBtn.setPrefSize(32, 32);
        minusBtn.setStyle("-fx-background-color: " + Theme.COLOR_BG_ELEVATED + "; -fx-text-fill: " + Theme.COLOR_DARK
                + "; -fx-background-radius: 4px; -fx-cursor: hand;");

        Label qtyCountLbl = new Label("1");
        qtyCountLbl.setFont(Theme.font(14, FontWeight.BOLD));
        qtyCountLbl.setTextFill(Color.web(Theme.COLOR_DARK));
        qtyCountLbl.setMinWidth(24);
        qtyCountLbl.setAlignment(Pos.CENTER);

        Button plusBtn = new Button("+");
        plusBtn.setFont(Theme.font(14, FontWeight.BOLD));
        plusBtn.setPrefSize(32, 32);
        plusBtn.setStyle("-fx-background-color: " + Theme.COLOR_BG_ELEVATED + "; -fx-text-fill: " + Theme.COLOR_DARK
                + "; -fx-background-radius: 4px; -fx-cursor: hand;");

        minusBtn.setOnAction(e -> {
            if (quantity > 1) {
                quantity--;
                qtyCountLbl.setText(String.valueOf(quantity));
            }
        });
        plusBtn.setOnAction(e -> {
            quantity++;
            qtyCountLbl.setText(String.valueOf(quantity));
        });

        qtyRow.getChildren().addAll(qtyLbl, minusBtn, qtyCountLbl, plusBtn);

        // Action Buttons Row
        HBox actionRow = new HBox(12);
        actionRow.setAlignment(Pos.CENTER_LEFT);

        Button addCartBtn = UIHelper.createPrimaryButton("ADD TO BAG 🛍️");
        addCartBtn.setPrefHeight(44);
        addCartBtn.setOnAction(e -> {
            CartController.getInstance().addToCart(currentProd, quantity);
        });

        Button buyNowBtn = UIHelper.createDarkButton("BUY NOW ⚡");
        buyNowBtn.setPrefHeight(44);
        buyNowBtn.setOnAction(e -> {
            AddToCart.getInstance().addItem(currentProd, quantity);
            CheckoutView checkoutView = new CheckoutView();
            AuthPage.myStage.setScene(checkoutView.getCheckoutViewScene(() -> AuthPage.myStage.setScene(productDetailsScene)));
        });

        Button wishlistBtn = UIHelper.createOutlineButton("WISHLIST ♡");
        wishlistBtn.setPrefHeight(44);
        wishlistBtn.setOnAction(e -> {
            WishlistController.getInstance().toggle(currentProd);
        });

        actionRow.getChildren().addAll(addCartBtn, buyNowBtn, wishlistBtn);

        // Specifications Box
        VBox specsBox = new VBox(8);
        Label specsTitle = new Label("PRODUCT SPECIFICATIONS");
        specsTitle.setFont(Theme.font(13, FontWeight.BOLD));
        specsTitle.setTextFill(Color.web(Theme.COLOR_DARK));

        GridPane specGrid = new GridPane();
        specGrid.setHgap(24);
        specGrid.setVgap(8);
        int row = 0;
        for (Map.Entry<String, String> entry : product.getSpecifications().entrySet()) {
            Label kLbl = new Label(entry.getKey());
            kLbl.setFont(Theme.font(12));
            kLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

            Label vLbl = new Label(entry.getValue());
            vLbl.setFont(Theme.font(12, FontWeight.BOLD));
            vLbl.setTextFill(Color.web(Theme.COLOR_DARK));

            specGrid.add(kLbl, 0, row);
            specGrid.add(vLbl, 1, row);
            row++;
        }
        specsBox.getChildren().addAll(specsTitle, specGrid);

        rightDetails.getChildren().addAll(
                nameLbl, ratingRow, priceBox, taxLbl,
                UIHelper.createSeparator(),
                qtyRow, actionRow
                );

        split.getChildren().addAll(leftGallery, rightDetails);


        content.getChildren().addAll(breadcrumb, split);

        StackPane centerWrapper = new StackPane(content);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        centerWrapper.setStyle("-fx-background-color: transparent;");
        setContent(centerWrapper);
    }

    public Scene getProductDetailsViewScene(Runnable backAction) {
        if (backBtn != null && backAction != null) {
            backBtn.setOnAction(e -> backAction.run());
        }
        BorderPane rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + Theme.COLOR_BG_PAGE + ";");
        rootLayout.setTop(new NavbarView());
        rootLayout.setCenter(this);
        StackPane root = new StackPane(rootLayout, new ToastView(), new ChatbotView());
        productDetailsScene = new Scene(root, 1550,850);
        productDetailsScene.setFill(Theme.PAINT_BG_PAGE);
        return productDetailsScene;
    }

    public Scene getProductDetailsScene(Runnable backAction) {
        return getProductDetailsViewScene(backAction);
    }
}
