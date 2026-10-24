package com.c2w.view.user;

import com.c2w.controller.user.CartController;
import com.c2w.controller.user.WishlistController;
import com.c2w.model.user.AddToCart;
import com.c2w.model.user.ProductRepository;
import com.c2w.model.user.ProductsforUser;
import com.c2w.view.authentication.AuthPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;

import java.util.List;

public class CartView extends ScrollPane {

    private final VBox container;
    private Scene cartScene;
    private Button backBtn;
    private final CartController cartController = CartController.getInstance();

    public CartView() {
        setFitToWidth(true);
        setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-background: " + Theme.COLOR_BG_PAGE + "; " +
                        "-fx-border-color: transparent;");

        container = new VBox(20);
        container.setPadding(new Insets(24, 36, 48, 36));
        container.setMaxWidth(1240);
        container.setAlignment(Pos.TOP_CENTER);

        StackPane centerWrapper = new StackPane(container);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        centerWrapper.setStyle("-fx-background-color: transparent;");

        setContent(centerWrapper);

        // Fetch from Firebase once if in-memory cart is empty
        if (AddToCart.getInstance().getItems().isEmpty()) {
            cartController.loadCartFromFirebase();
        }

        renderCart();

        AddToCart.getInstance().addListener(this::renderCart);
    }

    private void renderCart() {
        container.getChildren().clear();

        // Get reactive in-memory items (single source of truth for the UI)
        List<AddToCart> items = AddToCart.getInstance().getItems();

        System.out.println("//////////////////////// cart items size: " + items.size());

        if (items.isEmpty()) {
            EmptyStateView empty = new EmptyStateView(
                    "🛒",
                    "Hey, Your Bag Feels So Light!",
                    "There is nothing in your bag. Let's add some fashion items to make you smile.",
                    "EXPLORE PRODUCTS",
                    () -> {
                        CatalogView cat = new CatalogView("All");
                        AuthPage.myStage.setScene(
                                cat.getCatalogViewScene(
                                        () -> AuthPage.myStage.setScene(cartScene)));
                    });

            container.getChildren().add(empty);
            return;
        }

        HBox split = new HBox(32);
        split.setAlignment(Pos.TOP_CENTER);

        // =========================================================
        // LEFT SIDE - CART ITEMS
        // =========================================================
        VBox leftList = new VBox(14);
        leftList.setPrefWidth(680);
        HBox.setHgrow(leftList, Priority.ALWAYS);

        backBtn = UIHelper.createGhostButton("← Back");

        Label headerLbl = new Label(
                "SHOPPING BAG (" + AddToCart.getInstance().getItemCount() + " ITEMS)");
        headerLbl.setFont(Theme.font(16, FontWeight.BOLD));
        headerLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        leftList.getChildren().addAll(backBtn, headerLbl);

        for (AddToCart item : items) {
            leftList.getChildren().add(createCartItemCard(item));
        }

        // =========================================================
        // RIGHT SIDE - PRICE SUMMARY
        // =========================================================
        VBox rightSummary = new VBox(16);
        rightSummary.setPrefWidth(380);
        rightSummary.setMinWidth(360);
        rightSummary.setPadding(new Insets(20));
        rightSummary.setStyle(
                "-fx-background-color: " + Theme.COLOR_BG_SECTION + "; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-border-color: " + Theme.COLOR_BORDER + "; " +
                        "-fx-border-radius: 10px;");
        rightSummary.setEffect(Theme.cardShadow());

        // =========================================================
        // COUPON SECTION
        // =========================================================
        VBox couponBox = new VBox(6);
        Label couponTitle = new Label("COUPONS & OFFERS");
        couponTitle.setFont(Theme.font(12, FontWeight.BOLD));

        HBox couponInputRow = new HBox(8);
        TextField couponField = UIHelper.createTextField("Enter coupon code (e.g. C2W_Cloths)");
        HBox.setHgrow(couponField, Priority.ALWAYS);

        Button applyBtn = UIHelper.createPrimaryButton("APPLY");
        applyBtn.setOnAction(e -> {
            String enteredCode = couponField.getText() != null ? couponField.getText().trim() : "";
            cartController.applyCoupon(enteredCode);
        });

        couponInputRow.getChildren().addAll(couponField, applyBtn);

        String activeCoupon = AddToCart.getInstance().getAppliedCoupon();
        double activeDiscount = AddToCart.getInstance().getCouponDiscount();
        String discountType = AddToCart.getInstance().getAppliedCouponType();
        double discountValue = AddToCart.getInstance().getAppliedCouponValue();

        if (activeCoupon != null && !activeCoupon.isEmpty()) {
            HBox appliedRow = new HBox(8);
            appliedRow.setAlignment(Pos.CENTER_LEFT);

            String discountDesc;
            if (discountType != null && (discountType.equalsIgnoreCase("Percentage") || discountType.contains("%"))) {
                discountDesc = ((int) discountValue) + "% OFF: -" + UIHelper.formatCurrency(activeDiscount);
            } else {
                discountDesc = "-" + UIHelper.formatCurrency(activeDiscount) + " OFF";
            }

            Label activeCouponLbl = new Label("✓ Code '" + activeCoupon + "' applied (" + discountDesc + ")");
            activeCouponLbl.setFont(Theme.font(11.5, FontWeight.BOLD));
            activeCouponLbl.setTextFill(Color.web(Theme.COLOR_SUCCESS));

            Hyperlink removeLink = new Hyperlink("Remove");
            removeLink.setFont(Theme.font(11.5, FontWeight.BOLD));
            removeLink.setStyle("-fx-text-fill: #e53935; -fx-underline: true; -fx-padding: 0;");
            removeLink.setOnAction(ev -> cartController.removeCoupon());

            appliedRow.getChildren().addAll(activeCouponLbl, removeLink);
            couponBox.getChildren().addAll(couponTitle, couponInputRow, appliedRow);
        } else {
            String hint = getAvailableCouponHint(items);
            if (hint != null && !hint.isEmpty()) {
                Label hintLbl = new Label("💡 " + hint);
                hintLbl.setFont(Theme.font(11, FontWeight.NORMAL));
                hintLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
                hintLbl.setCursor(Cursor.HAND);
                couponBox.getChildren().addAll(couponTitle, couponInputRow, hintLbl);
            } else {
                couponBox.getChildren().addAll(couponTitle, couponInputRow);
            }
        }

        // =========================================================
        // PRICE DETAILS
        // =========================================================
        Label priceTitle = new Label("PRICE DETAILS (" + AddToCart.getInstance().getItemCount() + " Items)");
        priceTitle.setFont(Theme.font(13, FontWeight.BOLD));
        priceTitle.setTextFill(Color.web(Theme.COLOR_DARK));

        VBox rows = new VBox(10);
        rows.getChildren().addAll(
                createSummaryRow("Total MRP", UIHelper.formatCurrency(AddToCart.getInstance().getTotalMRP()), false),
                createSummaryRow("Discount on MRP",
                        "- " + UIHelper.formatCurrency(AddToCart.getInstance().getProductDiscount()), true),
                createSummaryRow("Coupon Discount",
                        AddToCart.getInstance().getCouponDiscount() > 0
                                ? "- " + UIHelper.formatCurrency(AddToCart.getInstance().getCouponDiscount())
                                : "₹0",
                        true)

        );

        // =========================================================
        // TOTAL
        // =========================================================
        HBox totalRow = new HBox();
        totalRow.setAlignment(Pos.CENTER_LEFT);
        totalRow.setPadding(new Insets(10, 0, 0, 0));

        Label totalLbl = new Label("Total Amount");
        totalLbl.setFont(Theme.font(15, FontWeight.BOLD));
        totalLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label totalAmtLbl = new Label(UIHelper.formatCurrency(AddToCart.getInstance().getFinalPayable()));
        totalAmtLbl.setFont(Theme.font(18, FontWeight.BOLD));
        totalAmtLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        totalRow.getChildren().addAll(totalLbl, sp, totalAmtLbl);

        // =========================================================
        // PLACE ORDER BUTTON
        // =========================================================
        Button placeOrderBtn = UIHelper.createPrimaryButton("PLACE ORDER");
        placeOrderBtn.setFont(Theme.font(14, FontWeight.BOLD));
        placeOrderBtn.setMaxWidth(Double.MAX_VALUE);
        placeOrderBtn.setPrefHeight(46);
        placeOrderBtn.setOnAction(e -> {
            CheckoutView checkoutView = new CheckoutView();
            AuthPage.myStage.setScene(
                    checkoutView.getCheckoutViewScene(
                            () -> AuthPage.myStage.setScene(cartScene)));
        });

        rightSummary.getChildren().addAll(
                couponBox,
                UIHelper.createSeparator(),
                priceTitle,
                rows,
                UIHelper.createSeparator(),
                totalRow,
                placeOrderBtn);

        split.getChildren().addAll(leftList, rightSummary);
        container.getChildren().add(split);
    }

    /**
     * Creates one cart item card with reactive quantity adjustment controls.
     */
    private HBox createCartItemCard(AddToCart item) {
        HBox card = new HBox(16);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(14));
        card.setStyle(
                "-fx-background-color: " + Theme.COLOR_BG_CARD + "; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-border-color: " + Theme.COLOR_BORDER + "; " +
                        "-fx-border-radius: 8px;");
        card.setEffect(Theme.subtleShadow());

        // PRODUCT IMAGE
        ImageView img = UIHelper.createImageView(item.getProductImg(), 90, 105);
        Rectangle clip = new Rectangle(90, 105);
        clip.setArcWidth(8);
        clip.setArcHeight(8);
        img.setClip(clip);

        // PRODUCT DETAILS
        VBox details = new VBox(6);
        HBox.setHgrow(details, Priority.ALWAYS);

        Label name = new Label(item.getProductName());
        name.setFont(Theme.font(12));
        name.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));

     
        int qty = item.getQuantity();

        HBox priceRow = UIHelper.createPriceRow(
        item.getProductPrice() * qty,         
        item.getProductOrignalPrice() * qty,   
        item.getDiscountValue());           

        // QUANTITY CONTROLS
        HBox actionsRow = new HBox(12);
        actionsRow.setAlignment(Pos.CENTER_LEFT);

        HBox qtyCtrl = new HBox(6);
        qtyCtrl.setAlignment(Pos.CENTER_LEFT);

        // MINUS BUTTON
        Button minusBtn = new Button("-");
        minusBtn.setFont(Theme.font(12, FontWeight.BOLD));
        minusBtn.setPrefSize(26, 26);
        minusBtn.setStyle(
                "-fx-background-color: " + Theme.COLOR_BG_ELEVATED + "; " +
                        "-fx-background-radius: 4px; " +
                        "-fx-cursor: hand; " +
                        "-fx-text-fill: " + Theme.COLOR_DARK + ";");

        // QUANTITY LABEL
        Label qtyLbl = new Label(String.valueOf(item.getQuantity()));
        qtyLbl.setFont(Theme.font(12, FontWeight.BOLD));
        qtyLbl.setMinWidth(20);
        qtyLbl.setAlignment(Pos.CENTER);

        // PLUS BUTTON
        Button plusBtn = new Button("+");
        plusBtn.setFont(Theme.font(12, FontWeight.BOLD));
        plusBtn.setPrefSize(26, 26);
        plusBtn.setStyle(
                "-fx-background-color: " + Theme.COLOR_BG_ELEVATED + "; " +
                        "-fx-background-radius: 4px; " +
                        "-fx-cursor: hand; " +
                        "-fx-text-fill: " + Theme.COLOR_DARK + ";");

        // MINUS ACTION (updates UI and replaces qty in Firebase)
        minusBtn.setOnAction(e -> {
            int currentQty = item.getQuantity();
            int newQty = currentQty - 1;
            System.out.println(
                    "Minus clicked: " + item.getProductName() + " | current=" + currentQty + " -> new=" + newQty);
            cartController.updateQuantity(item, newQty);
        });

        // PLUS ACTION (updates UI and replaces qty in Firebase)
        plusBtn.setOnAction(e -> {
            int currentQty = item.getQuantity();
            int newQty = currentQty + 1;
            System.out.println(
                    "Plus clicked: " + item.getProductName() + " | current=" + currentQty + " -> new=" + newQty);
            cartController.updateQuantity(item, newQty);
        });

        qtyCtrl.getChildren().addAll(minusBtn, qtyLbl, plusBtn);

        Hyperlink removeLink = new Hyperlink("Remove");
        removeLink.setFont(Theme.font(11.5));
        removeLink.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
        removeLink.setOnAction(e -> cartController.removeItem(item));

        Hyperlink wishlistLink = new Hyperlink("Move to Wishlist");
        wishlistLink.setFont(Theme.font(11.5));
        wishlistLink.setTextFill(Color.web(Theme.COLOR_PRIMARY));
        wishlistLink.setOnAction(e -> {
            WishlistController.getInstance().toggle(item.getProduct());
            cartController.removeItem(item);
        });

        Label separator = new Label("|");
        separator.setTextFill(Color.web(Theme.COLOR_BORDER));

        actionsRow.getChildren().addAll(qtyCtrl, separator, removeLink, wishlistLink);

        details.getChildren().addAll(name, priceRow, actionsRow);
        card.getChildren().addAll(img, details);

        return card;
    }

    private HBox createSummaryRow(String label, String value, boolean isHighlight) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);

        Label lbl = new Label(label);
        lbl.setFont(Theme.font(12.5));
        lbl.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label valLbl = new Label(value);
        valLbl.setFont(Theme.font(12.5, FontWeight.BOLD));
        valLbl.setTextFill(
                isHighlight ? Color.web(Theme.COLOR_SUCCESS) : Color.web(Theme.COLOR_DARK));

        row.getChildren().addAll(lbl, sp, valLbl);
        return row;
    }

    public Scene getCartViewScene(Runnable backAction) {
        if (backBtn != null && backAction != null) {
            backBtn.setOnAction(e -> backAction.run());
        }

        BorderPane rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + Theme.COLOR_BG_PAGE + ";");
        rootLayout.setTop(new NavbarView());
        rootLayout.setCenter(this);

        StackPane root = new StackPane(rootLayout, new ToastView(), new ChatbotView());
        cartScene = new Scene(root, 1550, 850);
        cartScene.setFill(Theme.PAINT_BG_PAGE);

        return cartScene;
    }

    public Scene getCartPageScene(Runnable backAction) {
        return getCartViewScene(backAction);
    }

    private String getAvailableCouponHint(List<AddToCart> items) {
        if (items == null)
            return null;

        for (AddToCart item : items) {
            ProductsforUser prod = item.getProduct();
            if (prod == null && item.getProductId() != null) {
                prod = ProductRepository.getInstance().getById(item.getProductId()).orElse(null);
            }

            if (prod != null
                    && prod.getCouponCode() != null
                    && !prod.getCouponCode().trim().isEmpty()
                    && prod.isCouponStatus()) {

                String type = prod.getDiscountType();
                double val = prod.getDiscountValue();

                if (val > 0) {
                    if (type != null && type.equalsIgnoreCase("Percentage")) {
                        return "Use code '" + prod.getCouponCode() + "' for " + (int) val + "% off on "
                                + item.getProductName();
                    } else {
                        return "Use code '" + prod.getCouponCode() + "' for " + UIHelper.formatCurrency(val)
                                + " off on " + item.getProductName();
                    }
                }
            }
        }

        return null;
    }
}