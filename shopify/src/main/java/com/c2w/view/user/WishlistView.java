package com.c2w.view.user;

import com.c2w.view.authentication.AuthPage;
import com.c2w.controller.user.CartController;
import com.c2w.controller.user.WishlistController;
import com.c2w.model.user.ProductsforUser;
import com.c2w.model.user.WishlistModel;
import com.c2w.view.user.ChatbotView;
import com.c2w.view.user.EmptyStateView;
import com.c2w.view.user.NavbarView;
import com.c2w.view.user.ToastView;
import com.c2w.view.user.Theme;
import com.c2w.view.user.UIHelper;
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

import java.util.List;

public class WishlistView extends ScrollPane {
    private final VBox container;
    private Scene wishlistScene;

    public WishlistView() {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: " + Theme.COLOR_BG_PAGE + "; -fx-border-color: transparent;");

        container = new VBox(20);
        container.setPadding(new Insets(24, 36, 48, 36));
        container.setMaxWidth(1280);
        container.setAlignment(Pos.TOP_CENTER);

        setContent(container);
        renderWishlist();

        WishlistModel.getInstance().addListener(this::renderWishlist);
    }

    private void renderWishlist() {
        container.getChildren().clear();

        List<ProductsforUser> items = WishlistModel.getInstance().getWishlistedProducts();

        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLbl = new Label("MY WISHLIST");
        titleLbl.setFont(Theme.font(20, FontWeight.BOLD));
        titleLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        Label countLbl = new Label("(" + items.size() + " Items)");
        countLbl.setFont(Theme.font(14));
        countLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

        header.getChildren().addAll(titleLbl, countLbl);
        container.getChildren().add(header);

        if (items.isEmpty()) {
            EmptyStateView emptyView = new EmptyStateView(
                    "♡",
                    "Your Wishlist is Empty",
                    "Your wishlist is waiting for something special. Explore our catalog and tap the heart on styles you love!",
                    "START SHOPPING",
                    () -> {
                        CatalogView catalog = new CatalogView("All");
                        AuthPage.myStage.setScene(catalog.getCatalogViewScene(() -> AuthPage.myStage.setScene(getWishlistViewScene(null))));
                    }
            );
            container.getChildren().add(emptyView);
            return;
        }

        FlowPane grid = new FlowPane(16, 20);
        grid.setAlignment(Pos.TOP_LEFT);

        for (ProductsforUser product : items) {
            grid.getChildren().add(createWishlistCard(product));
        }

        container.getChildren().add(grid);
    }

    private VBox createWishlistCard(ProductsforUser product) {
        VBox card = new VBox(8);
        card.setPrefWidth(225);
        card.setMaxWidth(230);
        card.setStyle("-fx-background-color: " + Theme.COLOR_BG_CARD + "; -fx-background-radius: 8px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 8px;");
        card.setEffect(Theme.cardShadow());

        StackPane imgBox = new StackPane();
        ImageView img = UIHelper.createImageView(product.getProductImage(), 225, 200);
        img.setFitWidth(225);
        img.setFitHeight(200);
        Rectangle clip = new Rectangle(225, 200);
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        img.setClip(clip);

        Button removeBtn = new Button("❌");
        removeBtn.setFont(Theme.font(11, FontWeight.BOLD));
        removeBtn.setTextFill(Color.RED);;
        removeBtn.setCursor(Cursor.HAND);
        removeBtn.setStyle("-fx-background-color: #FF3E6C; -fx-background-radius: 12px; -fx-min-width: 24px; -fx-min-height: 24px; -fx-text-fill: " + Theme.COLOR_DARK + ";");
        removeBtn.setOnAction(e -> WishlistController.getInstance().remove(product.getProductId()));

        StackPane.setAlignment(removeBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(removeBtn, new Insets(8, 8, 0, 0));

        imgBox.getChildren().addAll(img, removeBtn);

        VBox details = new VBox(4);
        details.setPadding(new Insets(8, 12, 12, 12));

        Label name = new Label(product.getProductName());
        name.setFont(Theme.font(11.5));
        name.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));

        HBox priceRow = UIHelper.createPriceRow(product.getProductPrice(), product.getOriginalPrice(), product.getDiscountValue());

        Button moveCartBtn = UIHelper.createPrimaryButton("MOVE TO BAG");
        moveCartBtn.setMaxWidth(Double.MAX_VALUE);
        moveCartBtn.setPadding(new Insets(8, 0, 8, 0));
        moveCartBtn.setOnAction(e -> {
            CartController.getInstance().addToCart(product, 1);
            WishlistController.getInstance().remove(product.getProductId());
        });

        details.getChildren().addAll( name, priceRow, new Region() {{ setPrefHeight(4); }}, moveCartBtn);
        card.getChildren().addAll(imgBox, details);

        card.setOnMouseClicked(e -> {
            if (e.getTarget() != removeBtn && e.getTarget() != moveCartBtn) {
                Scene currentScene = AuthPage.myStage.getScene();
                ProductDetailsView detailsView = new ProductDetailsView(product);
                AuthPage.myStage.setScene(detailsView.getProductDetailsScene(() -> AuthPage.myStage.setScene(currentScene)));
            }
        });

        return card;
    }

    public Scene getWishlistViewScene(Runnable backAction) {
        if (wishlistScene == null) {
            BorderPane root = new BorderPane();
            NavbarView navbar = new NavbarView();
            root.setTop(navbar);
            root.setCenter(this);

            StackPane overlayContainer = new StackPane(root);
            ToastView toast = new ToastView();
            ChatbotView chatbot = new ChatbotView();
            overlayContainer.getChildren().addAll(toast, chatbot);

            wishlistScene = new Scene(overlayContainer, 1550,850);

        }
        return wishlistScene;
    }
}
