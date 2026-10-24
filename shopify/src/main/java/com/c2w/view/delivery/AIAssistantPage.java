package com.c2w.view.delivery;

import com.c2w.controller.delivery.DeliveryAIAssistantController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AIAssistantPage {

    private Scene aiAssistantPageScene;
    private final DeliveryAIAssistantController aiController = new DeliveryAIAssistantController();

    public Scene getAIAssistantPageScene(Runnable backAction) {

        // ------------------ SIDEBAR ------------------
        VBox sidebar = DeliverySidebar.getSidebar("AI Assistant", backAction);

        // ------------------ MAIN CONTENT ------------------
        Button backBtn = new Button("← Back");
        backBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-font-size: 13px; -fx-cursor: hand; -fx-border-color: #334155; -fx-border-radius: 6px; -fx-padding: 6 12;");
        backBtn.setOnAction(e -> backAction.run());

        Label pageTitle = new Label("AI Delivery Assistant");
        pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

        Label pageSubtitle = new Label(
                "Your smart assistant for delivery support, order troubleshooting, live routing, and partner guidance.");
        pageSubtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

        VBox titleBox = new VBox(4, pageTitle, pageSubtitle);

        HBox topBar = new HBox(16, backBtn, titleBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 16, 0));

        // Chat Container
        VBox chatLog = new VBox(14);
        chatLog.setPadding(new Insets(16));

        // Initial Assistant Welcome Bubble
        Label aiWelcomeLabel = new Label("🤖 C2W Delivery Assistant");
        aiWelcomeLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #a78bfa;");

        Label aiWelcomeMsg = new Label(
                "Hello Partner! I am your C2W Delivery AI Assistant.\n\nAsk me anything about your active orders, delivery workflow, live GPS tracking, wallet payouts, or handling customer issues.");
        aiWelcomeMsg.setWrapText(true);
        aiWelcomeMsg.setStyle("-fx-font-size: 13px; -fx-text-fill: #f1f5f9;");

        VBox initialAiBubble = new VBox(4, aiWelcomeLabel, aiWelcomeMsg);
        initialAiBubble.setPadding(new Insets(12, 16, 12, 16));
        initialAiBubble.setStyle(
                "-fx-background-color: #2e1065; -fx-background-radius: 12px 12px 12px 2px; -fx-border-color: #581c87; -fx-border-radius: 12px 12px 12px 2px;");

        HBox initialAiRow = new HBox(initialAiBubble);
        initialAiRow.setAlignment(Pos.CENTER_LEFT);
        chatLog.getChildren().add(initialAiRow);

        ScrollPane chatScroll = new ScrollPane(chatLog);
        chatScroll.setFitToWidth(true);
        chatScroll.setPrefHeight(420);
        chatScroll.setStyle(
                "-fx-background: #0f172a; -fx-background-color: #0f172a; -fx-border-color: #334155; -fx-border-radius: 12px;");

        // Quick Suggestion Chips for Delivery Partners
        Button chip1 = new Button("How to complete delivery?");
        Button chip2 = new Button("When do payouts credit?");
  

        String chipStyle = "-fx-background-color: #1e293b; -fx-text-fill: #cbd5e1; -fx-font-size: 11px; -fx-background-radius: 16px; -fx-border-color: #334155; -fx-border-radius: 16px; -fx-cursor: hand; -fx-padding: 6 12;";
        chip1.setStyle(chipStyle);
        chip2.setStyle(chipStyle);
      

        HBox chipsRow = new HBox(8, chip1,chip2);

        // Input Field and Send Button
        TextField askInput = new TextField();
        askInput.setPromptText("Ask anything about your deliveries, routes, or payouts...");
        askInput.setStyle(
                "-fx-background-color: #0f172a; -fx-text-fill: #ffffff; -fx-prompt-text-fill: #64748b; -fx-border-color: #334155; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 10 14; -fx-font-size: 14px;");
        HBox.setHgrow(askInput, Priority.ALWAYS);

        Button sendBtn = new Button("Send ➤");
        sendBtn.setStyle(
                "-fx-background-color: #7c3aed; -fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 24; -fx-background-radius: 8px; -fx-cursor: hand;");

        HBox inputRow = new HBox(12, askInput, sendBtn);
        inputRow.setAlignment(Pos.CENTER);

        // Synchronous send handler
        Runnable onSend = () -> {
            String userText = askInput.getText();
            if (userText != null && !userText.trim().isEmpty()) {
                String cleanInput = userText.trim();
                askInput.clear();

                // 1. Add User message bubble
                Label userLbl = new Label("You (Partner)");
                userLbl.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #93c5fd;");
                Label userMsg = new Label(cleanInput);
                userMsg.setWrapText(true);
                userMsg.setStyle("-fx-font-size: 13px; -fx-text-fill: #ffffff;");
                VBox userBubble = new VBox(4, userLbl, userMsg);
                userBubble.setPadding(new Insets(10, 16, 10, 16));
                userBubble.setStyle("-fx-background-color: #1e3a8a; -fx-background-radius: 12px 12px 2px 12px;");
                HBox userRow = new HBox(userBubble);
                userRow.setAlignment(Pos.CENTER_RIGHT);
                chatLog.getChildren().add(userRow);

                // 2. Synchronously call Groq AI Controller
                String aiReply = aiController.processMessage(cleanInput);

                // 3. Add AI message bubble
                Label aiLbl = new Label("C2W Delivery Assistant");
                aiLbl.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #a78bfa;");
                Label aiMsg = new Label(aiReply);
                aiMsg.setWrapText(true);
                aiMsg.setStyle("-fx-font-size: 13px; -fx-text-fill: #f1f5f9;");
                VBox aiBubble = new VBox(4, aiLbl, aiMsg);
                aiBubble.setPadding(new Insets(12, 16, 12, 16));
                aiBubble.setStyle(
                        "-fx-background-color: #2e1065; -fx-background-radius: 12px 12px 12px 2px; -fx-border-color: #581c87; -fx-border-radius: 12px 12px 12px 2px;");
                HBox aiRow = new HBox(aiBubble);
                aiRow.setAlignment(Pos.CENTER_LEFT);
                chatLog.getChildren().add(aiRow);

                // Scroll to bottom
                chatScroll.setVvalue(1.0);
            }
        };

        sendBtn.setOnAction(e -> onSend.run());
        askInput.setOnAction(e -> onSend.run());

        chip1.setOnAction(e -> {
            askInput.setText("How do I complete my active delivery?");
            onSend.run();
        });
        chip2.setOnAction(e -> {
            askInput.setText("When do payouts credit?");
            onSend.run();
        });
       

        VBox chatContainer = new VBox(14, chatScroll, chipsRow, inputRow);
        chatContainer.setPadding(new Insets(20));
        chatContainer.setStyle(
                "-fx-background-color: #1e293b; -fx-background-radius: 14px; -fx-border-color: #334155; -fx-border-radius: 14px;");

        VBox mainContent = new VBox(20, topBar, chatContainer);
        mainContent.setPadding(new Insets(24, 32, 24, 32));

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background: #0b111e; -fx-background-color: #0b111e; -fx-border-color: transparent;");

        HBox rootLayout = new HBox(sidebar, scrollPane);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        rootLayout.setStyle("-fx-background-color: #0b111e;");

        aiAssistantPageScene = new Scene(rootLayout, 1550, 850);
        return aiAssistantPageScene;
    }
}
