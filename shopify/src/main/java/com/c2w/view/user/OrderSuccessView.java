package com.c2w.view.user;

import com.c2w.view.authentication.AuthPage;
import com.c2w.model.user.OrderUser;
import com.c2w.view.user.ChatbotView;
import com.c2w.view.user.NavbarView;
import com.c2w.view.user.ToastView;
import com.c2w.view.user.Theme;
import com.c2w.view.user.UIHelper;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;


public class OrderSuccessView extends ScrollPane {
    private Scene orderSuccessScene;

    public OrderSuccessView(OrderUser order) {

        
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: " + Theme.COLOR_BG_PAGE + "; -fx-border-color: transparent;");

        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(40, 20, 50, 20));

        VBox card = new VBox(16);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(560);
        card.setPadding(new Insets(36, 36, 36, 36));
        card.setStyle("-fx-background-color: " + Theme.COLOR_BG_CARD + "; -fx-background-radius: 12px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 12px;");
        card.setEffect(Theme.floatingShadow());

        // Animated Success Checkmark Badge
        StackPane checkBadge = new StackPane();
        checkBadge.setMinSize(72, 72);
        checkBadge.setMaxSize(72, 72);
        checkBadge.setStyle("-fx-background-color: " + Theme.COLOR_SUCCESS + "; -fx-background-radius: 36px;");

        Label checkIcon = new Label("✓");
        checkIcon.setFont(Theme.font(36, FontWeight.BOLD));
        checkIcon.setTextFill(Color.WHITE);
        checkBadge.getChildren().add(checkIcon);

        ScaleTransition pop = new ScaleTransition(Duration.millis(350), checkBadge);
        pop.setFromX(0.4);
        pop.setFromY(0.4);
        pop.setToX(1.0);
        pop.setToY(1.0);
        pop.play();

        Label congratsTitle = new Label("Order Placed Successfully!");
        congratsTitle.setFont(Theme.font(22, FontWeight.BOLD));
        congratsTitle.setTextFill(Color.web(Theme.COLOR_DARK));

        String orderId = order != null ? order.getOrderId() : "ODR-984210";
        String arrivalDate = order != null ? order.getExpectedDelivery() : "3 Business Days";

        Label orderIdLbl = new Label("Order ID: " + orderId);
        orderIdLbl.setFont(Theme.font(14, FontWeight.BOLD));
        orderIdLbl.setTextFill(Color.web(Theme.COLOR_PRIMARY));

        Label deliveryEst = new Label("Estimated Delivery: " + arrivalDate);
        deliveryEst.setFont(Theme.font(13));
        deliveryEst.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));

        Label infoLbl = new Label("We've sent an order confirmation with live tracking link to your registered email and mobile.");
        infoLbl.setFont(Theme.font(12));
        infoLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
        infoLbl.setWrapText(true);
        infoLbl.setMaxWidth(420);
        infoLbl.setAlignment(Pos.CENTER);

        HBox actions = new HBox(14);
        actions.setAlignment(Pos.CENTER);

   

        Button shopMoreBtn = UIHelper.createOutlineButton("CONTINUE SHOPPING");
        shopMoreBtn.setOnAction(e -> {
            MainUser userHome = new MainUser();
            AuthPage.myStage.setScene(userHome.getUserHomePageScene(() -> {
                // LoginView login = new LoginView();
                AuthPage.myStage.setScene(new AuthPage().showLoginPage());
            }));
        });

        actions.getChildren().addAll(shopMoreBtn);

        card.getChildren().addAll(checkBadge, congratsTitle, orderIdLbl, deliveryEst, infoLbl, new Region() {{ setPrefHeight(8); }}, actions);
        container.getChildren().add(card);
        setContent(container);
    }

    public Scene getOrderSuccessViewScene(Runnable backAction) {
        if (orderSuccessScene == null) {
            BorderPane root = new BorderPane();
            NavbarView navbar = new NavbarView();
            root.setTop(navbar);
            root.setCenter(this);

            StackPane overlayContainer = new StackPane(root);
            ToastView toast = new ToastView();
            ChatbotView chatbot = new ChatbotView();
            overlayContainer.getChildren().addAll(toast, chatbot);

            orderSuccessScene = new Scene(overlayContainer, 1550,850);
            if (getClass().getResource("/styles.css") != null) {
                orderSuccessScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            }
        }
        return orderSuccessScene;
    }
}
