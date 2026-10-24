package com.c2w.view.user;

import com.c2w.controller.user.OrderControllerUser;
import com.c2w.model.user.AddToCart;
import com.c2w.model.user.OrderUser;
import com.c2w.view.authentication.AuthPage;
import com.c2w.view.user.ChatbotView;
import com.c2w.view.user.EmptyStateView;
import com.c2w.view.user.NavbarView;
import com.c2w.view.user.ToastView;
import com.c2w.view.user.Theme;
import com.c2w.view.user.UIHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

import java.util.List;


public class OrderHistoryView {
    private final ScrollPane scrollPane = new ScrollPane();
    private Scene orderHistoryScene;
    private Button backBtn;

    boolean flag = false;

    public OrderHistoryView() {
        scrollPane.setFitToWidth(true);   // makes the wrapper stretch to window width
    scrollPane.setStyle("-fx-background-color: transparent; -fx-background: " + Theme.COLOR_BG_PAGE
            + "; -fx-border-color: transparent;");

    VBox container = new VBox(20);
    container.setPadding(new Insets(24, 36, 48, 36));
    container.setMaxWidth(1080);
    container.setAlignment(Pos.TOP_CENTER);

    StackPane centerWrapper = new StackPane(container);
    centerWrapper.setAlignment(Pos.TOP_CENTER);          // this centers the container
    centerWrapper.setStyle("-fx-background-color: transparent;");

    scrollPane.setContent(centerWrapper);  
 
        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);

        backBtn = UIHelper.createGhostButton("← Back");

        Label title = new Label("MY ORDERS");
        title.setFont(Theme.font(20, FontWeight.BOLD));
        title.setTextFill(Color.web(Theme.COLOR_DARK));

        OrderControllerUser orderController = new OrderControllerUser();
        List<OrderUser> orders = orderController.getOrders();

        System.out.println("===========getting orders :" + orders.size());

        Label count = new Label("(" + orders.size() + " Orders)");

        count.setFont(Theme.font(14));
        count.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

        header.getChildren().addAll(backBtn, title, count);
        container.getChildren().add(header);

        if (orders.isEmpty()) {
            EmptyStateView empty = new EmptyStateView(
                    "📦",
                    "No Orders Placed Yet",
                    "You haven't placed any orders yet. Once you shop, your order details and delivery status will appear here.",
                    "EXPLORE PRODUCTS",
                    () -> {
                        CatalogView cat = new CatalogView("All");
                        AuthPage.myStage
                                .setScene(cat.getCatalogViewScene(() -> AuthPage.myStage.setScene(orderHistoryScene)));
                    });
            container.getChildren().add(empty);
        } else {
            for (OrderUser o : orders) {
                container.getChildren().add(createOrderCard(o));
            }
        }

       
    }

    private VBox createOrderCard(OrderUser order) {

        if (order.getStatus().equals("OUT_FOR_DELIVERY")) {

            flag = true;
        }
        VBox card = new VBox(14);
        card.setPadding(new Insets(18, 22, 18, 22));
        card.setStyle("-fx-background-color: " + Theme.COLOR_BG_CARD + "; -fx-background-radius: 8px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 8px;");
        card.setEffect(Theme.subtleShadow());

        // Header Row: Order ID, Date, Status Badge
        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);

        VBox idGroup = new VBox(2);
        Label idLbl = new Label("Order #" + order.getOrderId());
        idLbl.setFont(Theme.font(13.5, FontWeight.BOLD));
        idLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        Label dateLbl = new Label("Placed on " + order.getOrderDate());
        dateLbl.setFont(Theme.font(11.5));
        dateLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
        idGroup.getChildren().addAll(idLbl, dateLbl);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Status Badge
        String badgeBg = Theme.COLOR_SUCCESS_LIGHT;
        String badgeText = Theme.COLOR_SUCCESS;
        System.out.println("--------------------status-------------" + order.getStatus());

        if ("ORDER PLACED".equalsIgnoreCase(order.getStatus())) {

            badgeBg = "#adcaef";
            badgeText = "#1c6ae9";

        } else if ("ORDER READY FOR DISPATCH".equalsIgnoreCase(order.getStatus())) {

            badgeBg = "#a89be6";
            badgeText = "#784eda";

        } else if ("ACCEPTED".equalsIgnoreCase(order.getStatus())) {

            badgeBg = "#5571cc";
            badgeText = "#090c9d";

        } else if ("PICKED_UP".equalsIgnoreCase(order.getStatus())) {

            badgeBg = "#ebd681";
            badgeText = "#936009";

        } else if ("OUT_FOR_DELIVERY".equalsIgnoreCase(order.getStatus())) {

            badgeBg = "#95d9c9";
            badgeText = "#093c36";

        } else if ("DELIVERED".equalsIgnoreCase(order.getStatus())) {

            badgeBg = "#76be6d";
            badgeText = "#063a12";

        }

        Label statusBadge = UIHelper.createBadge("● " + order.getStatus().toUpperCase(), badgeBg, badgeText);

        topRow.getChildren().addAll(idGroup, spacer, statusBadge);

        // Items Summary
        VBox itemsBox = new VBox(10);
        for (AddToCart item : order.getItems()) {
            HBox itemLine = new HBox(12);
            itemLine.setAlignment(Pos.CENTER_LEFT);

        

            VBox itemInfo = new VBox(2);
            Label itemName = new Label(item.getProductName() != null ? item.getProductName() : "Product");
            itemName.setFont(Theme.font(13, FontWeight.SEMI_BOLD));
            itemName.setTextFill(Color.web(Theme.COLOR_DARK));

            Label itemQty = new Label(
                    "Qty: " + item.getQuantity() + " × " + UIHelper.formatCurrency(item.getProductPrice()));
            itemQty.setFont(Theme.font(11.5));
            itemQty.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
            itemInfo.getChildren().addAll(itemName, itemQty);

            Region sp2 = new Region();
            HBox.setHgrow(sp2, Priority.ALWAYS);

            Label itemPrice = new Label(UIHelper.formatCurrency(item.getTotalPrice()));
            itemPrice.setFont(Theme.font(13, FontWeight.BOLD));
            itemPrice.setTextFill(Color.web(Theme.COLOR_DARK));

            itemLine.getChildren().addAll(itemInfo, sp2, itemPrice);
            itemsBox.getChildren().add(itemLine);
        }

        // Footer Actions & Total
        HBox footer = new HBox(14);
        footer.setAlignment(Pos.CENTER_LEFT);

        VBox totalBox = new VBox(2);
        Label totalTitle = new Label("Total Paid (" + order.getPaymentMethod() + ")");
        totalTitle.setFont(Theme.font(11));
        totalTitle.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

        Label totalAmount = new Label(UIHelper.formatCurrency(order.getFinalAmount()));
        totalAmount.setFont(Theme.font(15, FontWeight.BOLD));
        totalAmount.setTextFill(Color.web(Theme.COLOR_DARK));

        Button review=new Button("Reviews & Ratings");
        review.setAlignment(Pos.TOP_RIGHT);
        review.setOnAction(e->{
            Runnable backAction = () -> AuthPage.myStage.setScene(orderHistoryScene);
            AuthPage.myStage.setScene(new OrderedProductsPage().getOrderPageView(order.getItems(), backAction));
        });
        review.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 6px 12px;");
        Region spacer1 = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        totalBox.getChildren().addAll(totalTitle, totalAmount);

        HBox hb=new HBox(10,totalBox,spacer1,review);
        
        Region footSpacer = new Region();
        HBox.setHgrow(footSpacer, Priority.ALWAYS);

        //////////////////// Pin//////////////////

        VBox pinBox = new VBox(2);
        Label pinTitle = new Label("Pin for Delivery:");
        pinTitle.setFont(Theme.font(11));
        pinTitle.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

        Label pin = new Label("" + order.getPin());
        pin.setFont(Theme.font(15, FontWeight.BOLD));
        pin.setTextFill(Color.web(Theme.COLOR_DARK));
        pinBox.getChildren().addAll(pinTitle, pin);

        

        footer.getChildren().addAll(hb, footSpacer);

        if (flag) {

            card.getChildren().addAll(topRow, UIHelper.createSeparator(), itemsBox, UIHelper.createSeparator(), pinBox,
                    UIHelper.createSeparator(), footer);
        } else {

            card.getChildren().addAll(topRow, UIHelper.createSeparator(), itemsBox, UIHelper.createSeparator(), footer);
        }
        return card;
    }

    public Scene getOrderHistoryViewScene(Runnable backAction) {
    if (backBtn != null && backAction != null) {
        backBtn.setOnAction(e -> backAction.run());
    }
    BorderPane rootLayout = new BorderPane();
    rootLayout.setStyle("-fx-background-color: " + Theme.COLOR_BG_PAGE + ";");
    rootLayout.setTop(new NavbarView());
    rootLayout.setCenter(scrollPane);                    // was: this
    StackPane root = new StackPane(rootLayout, new ToastView(), new ChatbotView());
    orderHistoryScene = new Scene(root, 1550, 850);
    orderHistoryScene.setFill(Theme.PAINT_BG_PAGE);
    return orderHistoryScene;
}

    public Scene getOrderPageScene(Runnable backAction) {
        return getOrderHistoryViewScene(backAction);
    }
}
