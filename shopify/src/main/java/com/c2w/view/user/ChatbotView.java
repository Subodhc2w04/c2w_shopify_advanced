package com.c2w.view.user;

import com.c2w.controller.user.ChatbotController;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class ChatbotView extends StackPane {
    private final VBox chatWindow;
    private final VBox messageList;
    private final ScrollPane scrollPane;
    private final TextField inputField;
    private final Button fabButton;
    private boolean isOpen = false;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    public ChatbotView() {
        setAlignment(Pos.BOTTOM_RIGHT);
        setPickOnBounds(false);
        setPadding(new Insets(0, 24, 24, 0));

        chatWindow = new VBox();
        chatWindow.setPrefSize(350, 480);
        chatWindow.setMinSize(350, 480);
        chatWindow.setMaxSize(350, 480);
        chatWindow.setStyle("-fx-background-color: " + Theme.COLOR_BG_CARD + "; " +
                "-fx-background-radius: 14px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; " +
                "-fx-border-radius: 14px; " +
                "-fx-border-width: 1px;");
        chatWindow.setEffect(Theme.floatingShadow());
        chatWindow.setVisible(false);
        chatWindow.setManaged(false);

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-background-color: linear-gradient(to right, #1A1A22, #252530); " +
                "-fx-background-radius: 14px 14px 0 0;");

        Circle onlineDot = new Circle(4, Color.web(Theme.COLOR_SUCCESS));

        VBox titleBox = new VBox(2);
        Label titleLbl = new Label("C2W Style Assistant");
        titleLbl.setFont(Theme.font(13, FontWeight.BOLD));
        titleLbl.setTextFill(Color.WHITE);

        Label statusLbl = new Label("AI Fashion Stylist • Online");
        statusLbl.setFont(Theme.font(10.5));
        statusLbl.setTextFill(Color.web("#94969F"));
        titleBox.getChildren().addAll(titleLbl, statusLbl);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        Button closeBtn = new Button("✕");
        closeBtn.setFont(Theme.font(12, FontWeight.BOLD));
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> toggleChat());

        header.getChildren().addAll(onlineDot, titleBox, headerSpacer, closeBtn);

        // Messages List
        messageList = new VBox(10);
        messageList.setPadding(new Insets(14, 14, 14, 14));

        scrollPane = new ScrollPane(messageList);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: " + Theme.COLOR_BG_CARD
                + "; -fx-border-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Prompt Chips
        ScrollPane chipsScroll = new ScrollPane();
        chipsScroll.setFitToHeight(true);
        chipsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chipsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chipsScroll.setStyle(
                "-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        HBox chipsBox = new HBox(8);
        chipsBox.setPadding(new Insets(6, 12, 10, 12));
        chipsBox.getChildren().addAll(
                createChip("Where is my order?", "Where is my order?"),
                createChip("My Cart", "Show my cart"),
                createChip("My Wishlist", "Show my wishlist"),
                createChip("Suggest party outfit", "Suggest an outfit for a party"),
                createChip("Show footwear", "Show me footwear"),
                createChip("Clothing", "Show clothing collection"),
                createChip("Return policy?", "What is your return policy?"),
                createChip("My Profile", "Open my profile"),
                createChip("All Categories", "Show all categories"));

        chipsScroll.setContent(chipsBox);

        // Input Area
        HBox inputBar = new HBox(8);
        inputBar.setAlignment(Pos.CENTER_LEFT);
        inputBar.setPadding(new Insets(10, 12, 12, 12));
        inputBar.setStyle("-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-width: 1px 0 0 0;");

        inputField = new TextField();
        inputField.setPromptText("Ask style questions or track orders...");
        inputField.setFont(Theme.font(12));
        inputField.setStyle(
                "-fx-background-color: #1A1A22; -fx-text-fill: white; -fx-background-radius: 18px; -fx-padding: 8 14 8 14; -fx-border-color: "
                        + Theme.COLOR_BORDER + "; -fx-border-radius: 18px;");
        HBox.setHgrow(inputField, Priority.ALWAYS);

        Button sendBtn = new Button("➤");
        sendBtn.setFont(Theme.font(13, FontWeight.BOLD));
        sendBtn.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY
                + "; -fx-text-fill: white; -fx-background-radius: 16px; -fx-min-width: 32px; -fx-min-height: 32px; -fx-cursor: hand;");
        sendBtn.setOnAction(e -> sendMessage());
        inputField.setOnAction(e -> sendMessage());

        inputBar.getChildren().addAll(inputField, sendBtn);

        chatWindow.getChildren().addAll(header, scrollPane, chipsScroll, inputBar);

        // Welcome greeting with quick navigation actions
        addBotMessage(
                "Hello! I'm your C2W Style Assistant ✨ How can I help you curate your perfect wardrobe or track your order today?"
        // List.of(
        // new ChatbotController.NavAction("📦 Where is my order?", () ->
        // ChatbotController.getInstance().navigateToOrders()),
        // new ChatbotController.NavAction("🛒 View Cart", () ->
        // ChatbotController.getInstance().navigateToCart()),
        // new ChatbotController.NavAction("🛍️ Browse All", () ->
        // ChatbotController.getInstance().navigateToCatalog("All"))
        );

        // Floating Action Button
        fabButton = new Button("💬");
        fabButton.setFont(Theme.font(18));
        fabButton.setCursor(Cursor.HAND);
        fabButton.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY + "; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 28px; " +
                "-fx-min-width: 56px; -fx-min-height: 56px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(255,62,108,0.4), 12, 0, 0, 4);");

        fabButton.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), fabButton);
            st.setToX(1.08);
            st.setToY(1.08);
            st.play();
        });
        fabButton.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), fabButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        fabButton.setOnAction(e -> toggleChat());

        getChildren().addAll(chatWindow, fabButton);
        StackPane.setMargin(chatWindow, new Insets(0, 0, 70, 0));
    }

    private Button createChip(String label, String prompt) {
        Button chip = new Button(label);
        chip.setFont(Theme.font(10.5, FontWeight.BOLD));
        chip.setCursor(Cursor.HAND);
        chip.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY_LIGHT + "; " +
                "-fx-text-fill: " + Theme.COLOR_PRIMARY + "; " +
                "-fx-background-radius: 12px; -fx-padding: 4 10 4 10; " +
                "-fx-border-color: " + Theme.COLOR_PRIMARY + "; -fx-border-radius: 12px; -fx-border-width: 0.5px;");
        chip.setOnAction(e -> {
            inputField.setText(prompt);
            sendMessage();
        });
        return chip;
    }

    public void toggleChat() {
        isOpen = !isOpen;
        if (isOpen) {
            chatWindow.setVisible(true);
            chatWindow.setManaged(true);
            chatWindow.setOpacity(0.0);
            chatWindow.setScaleX(0.85);
            chatWindow.setScaleY(0.85);

            FadeTransition ft = new FadeTransition(Duration.millis(180), chatWindow);
            ft.setToValue(1.0);

            ScaleTransition st = new ScaleTransition(Duration.millis(180), chatWindow);
            st.setToX(1.0);
            st.setToY(1.0);

            ft.play();
            st.play();
            fabButton.setText("✕");
            inputField.requestFocus();
        } else {
            chatWindow.setVisible(false);
            chatWindow.setManaged(false);
            fabButton.setText("💬");
        }
    }

    private void sendMessage() {
        String text = inputField.getText();
        if (text == null || text.trim().isEmpty())
            return;
        inputField.clear();

        addUserMessage(text.trim());

        Label typingLbl = new Label("C2W is typing...");
        typingLbl.setFont(Theme.font(10.5));
        typingLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
        messageList.getChildren().add(typingLbl);
        scrollToBottom();

        PauseTransition pause = new PauseTransition(Duration.millis(500));
        pause.setOnFinished(e -> {
            ChatbotController.getInstance()
                    .process(text.trim())
                    .thenAccept(reply -> {
                        Platform.runLater(() -> {
                            messageList.getChildren().remove(typingLbl);
                            if (reply != null) {
                                if (reply.actions != null && !reply.actions.isEmpty()) {
                                    addBotMessage(reply.message, reply.actions);
                                } else if (reply.actionText != null && reply.action != null) {
                                    addBotMessage(reply.message, reply.actionText, reply.action);
                                } else {
                                    addBotMessage(reply.message);
                                }
                            }
                        });
                    });
        });
        pause.play();
    }

    private void addUserMessage(String message) {
        VBox bubble = new VBox(3);
        bubble.setAlignment(Pos.CENTER_RIGHT);

        Label msgLbl = new Label(message);
        msgLbl.setFont(Theme.font(12));
        msgLbl.setTextFill(Color.WHITE);
        msgLbl.setWrapText(true);
        msgLbl.setMaxWidth(230);
        msgLbl.setPadding(new Insets(8, 12, 8, 12));
        msgLbl.setStyle(
                "-fx-background-color: " + Theme.COLOR_PRIMARY + "; -fx-background-radius: 14px 14px 2px 14px;");

        Label timeLbl = new Label(LocalTime.now().format(timeFormatter));
        timeLbl.setFont(Theme.font(9));
        timeLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

        bubble.getChildren().addAll(msgLbl, timeLbl);
        messageList.getChildren().add(bubble);
        scrollToBottom();
    }

    public void addBotMessage(String message) {
        addBotMessage(message, (List<ChatbotController.NavAction>) null);
    }

    public void addBotMessage(String message, String actionButtonText, Runnable action) {
        List<ChatbotController.NavAction> list = null;
        if (actionButtonText != null && action != null) {
            list = List.of(new ChatbotController.NavAction(actionButtonText, action));
        }
        addBotMessage(message, list);
    }

    public void addBotMessage(String message, List<ChatbotController.NavAction> actions) {
        VBox bubble = new VBox(4);
        bubble.setAlignment(Pos.CENTER_LEFT);

        VBox content = new VBox(8);
        content.setMaxWidth(260);
        content.setPadding(new Insets(10, 12, 10, 12));
        content.setStyle("-fx-background-color: " + Theme.COLOR_BG_ELEVATED + "; " +
                "-fx-background-radius: 14px 14px 14px 2px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 14px 14px 14px 2px;");

        Label msgLbl = new Label(message != null ? message : "");
        msgLbl.setFont(Theme.font(12));
        msgLbl.setTextFill(Color.web(Theme.COLOR_DARK));
        msgLbl.setWrapText(true);
        content.getChildren().add(msgLbl);

        if (actions != null && !actions.isEmpty()) {
            VBox buttonsBox = new VBox(6);
            buttonsBox.setPadding(new Insets(4, 0, 0, 0));

            for (ChatbotController.NavAction nav : actions) {
                if (nav == null || nav.text == null || nav.action == null)
                    continue;

                Button actBtn = new Button(nav.text);
                actBtn.setFont(Theme.font(11, FontWeight.BOLD));
                actBtn.setMaxWidth(Double.MAX_VALUE);
                actBtn.setAlignment(Pos.CENTER);
                actBtn.setCursor(Cursor.HAND);
                actBtn.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY
                        + "; -fx-text-fill: white; -fx-background-radius: 8px; -fx-padding: 7 12 7 12;");

                actBtn.setOnMouseEntered(e -> actBtn.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY_HOVER
                        + "; -fx-text-fill: white; -fx-background-radius: 8px; -fx-padding: 7 12 7 12;"));
                actBtn.setOnMouseExited(e -> actBtn.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY
                        + "; -fx-text-fill: white; -fx-background-radius: 8px; -fx-padding: 7 12 7 12;"));

                actBtn.setOnAction(e -> {
                    toggleChat();
                    nav.action.run();
                });
                buttonsBox.getChildren().add(actBtn);
            }
            content.getChildren().add(buttonsBox);
        }

        Label timeLbl = new Label(LocalTime.now().format(timeFormatter));
        timeLbl.setFont(Theme.font(9));
        timeLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

        bubble.getChildren().addAll(content, timeLbl);
        messageList.getChildren().add(bubble);
        scrollToBottom();
    }

    private void scrollToBottom() {
        PauseTransition pt = new PauseTransition(Duration.millis(50));
        pt.setOnFinished(e -> scrollPane.setVvalue(1.0));
        pt.play();
    }
}
