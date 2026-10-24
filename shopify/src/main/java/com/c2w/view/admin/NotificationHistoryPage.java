package com.c2w.view.admin;

import java.util.List;

import com.c2w.controller.admin.NotificationController;
import com.c2w.model.admin.NotificationModel;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class NotificationHistoryPage {

        public VBox getNotificationHistoryPage() {

                // ========================================================
                // MAIN CONTENT
                // ========================================================

                VBox mainContent = new VBox(20);

                mainContent.setPadding(
                                new Insets(25));

                mainContent.setStyle(
                                "-fx-background-color: #0B1017;");

                // ========================================================
                // TITLE
                // ========================================================

                Label title = new Label(
                                "Notification History");

                title.setStyle(
                                "-fx-font-size: 26px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #F5F7FA;");

                // ========================================================
                // SUBTITLE
                // ========================================================

                Label subtitle = new Label(
                                "View notification history for users and delivery persons.");

                subtitle.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #8A9BB0;");

                // ========================================================
                // RADIO BUTTONS
                // ========================================================

                RadioButton userRadio = new RadioButton("User");

                RadioButton deliveryRadio = new RadioButton("Delivery Person");

                userRadio.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #F5F7FA;");

                deliveryRadio.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #F5F7FA;");

                ToggleGroup group = new ToggleGroup();

                userRadio.setToggleGroup(group);
                deliveryRadio.setToggleGroup(group);

                // Default
                userRadio.setSelected(true);

                HBox selectionBox = new HBox(25);

                selectionBox.setAlignment(
                                Pos.CENTER_LEFT);

                selectionBox.getChildren().addAll(
                                userRadio,
                                deliveryRadio);

                // ========================================================
                // CONTROLLER
                // ========================================================

                NotificationController notificationController = new NotificationController();

                // ========================================================
                // FETCH BOTH TYPES OF NOTIFICATIONS
                // ========================================================

                List<List<NotificationModel>> mergedList = notificationController.gotoDeliveryNotifications();

                // Delivery Person notifications
                List<NotificationModel> deliveryNotifications = mergedList.get(0);

                // User / Customer notifications
                List<NotificationModel> userNotifications = mergedList.get(1);

                // ========================================================
                // USER LISTVIEW
                // ========================================================

                ListView<NotificationModel> userHistoryListView = new ListView<>();

                userHistoryListView.setPlaceholder(
                                createPlaceholder(
                                                "No user notifications available."));

                userHistoryListView.setStyle(
                                "-fx-background-color: #0B1017;" +
                                                "-fx-control-inner-background: #0B1017;" +
                                                "-fx-border-color: #273442;");

                userHistoryListView.setItems(
                                FXCollections.observableArrayList(
                                                userNotifications));

                // ========================================================
                // DELIVERY LISTVIEW
                // ========================================================

                ListView<NotificationModel> deliveryHistoryListView = new ListView<>();

                deliveryHistoryListView.setPlaceholder(
                                createPlaceholder(
                                                "No delivery person notifications available."));

                deliveryHistoryListView.setStyle(
                                "-fx-background-color: #0B1017;" +
                                                "-fx-control-inner-background: #0B1017;" +
                                                "-fx-border-color: #273442;");

                deliveryHistoryListView.setItems(
                                FXCollections.observableArrayList(
                                                deliveryNotifications));

                // ========================================================
                // USER CELL FACTORY
                // ========================================================

                userHistoryListView.setCellFactory(
                                list -> new ListCell<NotificationModel>() {

                                        @Override
                                        protected void updateItem(
                                                        NotificationModel notification,
                                                        boolean empty) {

                                                super.updateItem(
                                                                notification,
                                                                empty);

                                                if (empty || notification == null) {

                                                        setGraphic(null);
                                                        setText(null);

                                                        return;
                                                }

                                                VBox card = createNotificationCard(
                                                                notification);

                                                setGraphic(card);
                                        }
                                });

                // ========================================================
                // DELIVERY CELL FACTORY
                // ========================================================

                deliveryHistoryListView.setCellFactory(
                                list -> new ListCell<NotificationModel>() {

                                        @Override
                                        protected void updateItem(
                                                        NotificationModel notification,
                                                        boolean empty) {

                                                super.updateItem(
                                                                notification,
                                                                empty);

                                                if (empty || notification == null) {

                                                        setGraphic(null);
                                                        setText(null);

                                                        return;
                                                }

                                                VBox card = createNotificationCard(
                                                                notification);

                                                setGraphic(card);
                                        }
                                });

                // ========================================================
                // HISTORY CONTAINER
                // ========================================================

                VBox historyContainer = new VBox(10);

                // Default = User
                historyContainer.getChildren().add(
                                userHistoryListView);

                VBox.setVgrow(
                                userHistoryListView,
                                Priority.ALWAYS);

                VBox.setVgrow(
                                deliveryHistoryListView,
                                Priority.ALWAYS);

                VBox.setVgrow(
                                historyContainer,
                                Priority.ALWAYS);

                // ========================================================
                // USER RADIO
                // ========================================================

                userRadio.setOnAction(e -> {

                        historyContainer.getChildren().clear();

                        historyContainer.getChildren().add(
                                        userHistoryListView);
                });

                // ========================================================
                // DELIVERY RADIO
                // ========================================================

                deliveryRadio.setOnAction(e -> {

                        historyContainer.getChildren().clear();

                        historyContainer.getChildren().add(
                                        deliveryHistoryListView);
                });

                // ========================================================
                // ADD TO MAIN CONTENT
                // ========================================================

                mainContent.getChildren().addAll(
                                title,
                                subtitle,
                                selectionBox,
                                historyContainer);

                VBox.setVgrow(
                                historyContainer,
                                Priority.ALWAYS);

                mainContent.setFillWidth(true);

                VBox page = new VBox();

                page.getChildren().add(
                                mainContent);

                VBox.setVgrow(
                                mainContent,
                                Priority.ALWAYS);

                return page;
        }

        // ============================================================
        // CREATE NOTIFICATION CARD
        // ============================================================

        private VBox createNotificationCard(
                        NotificationModel notification) {

                VBox card = new VBox(8);

                card.setPadding(
                                new Insets(15));

                card.setStyle(
                                "-fx-background-color: #151C25;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-border-color: #00B8E6;" +
                                                "-fx-border-width: 2;" +
                                                "-fx-border-radius: 8;");

                // ========================================================
                // EMAIL
                // ========================================================

                Label emailLabel = new Label(
                                "Email: "
                                                + notification.getEmail());

                emailLabel.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #F5F7FA;");

                // ========================================================
                // NOTIFICATION TYPE
                // ========================================================

                Label typeLabel = new Label(
                                "Notification: "
                                                + notification.getNotiType());

                typeLabel.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #F5F7FA;");

                // ========================================================
                // DATE
                // ========================================================

                Label dateLabel = new Label(
                                "Date: "
                                                + notification.getTimestamp());

                dateLabel.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #8A9BB0;");

                // ========================================================
                // REQUEST NOTIFICATION
                // ========================================================

                if ("REQUEST".equalsIgnoreCase(
                                notification.getNotiType())) {

                        Label statusLabel = new Label(
                                        "Status: "
                                                        + notification.getStatus());

                        statusLabel.setStyle(
                                        "-fx-font-size: 13px;" +
                                                        "-fx-text-fill: #36CFC9;");

                        card.getChildren().addAll(
                                        emailLabel,
                                        typeLabel,
                                        statusLabel,
                                        dateLabel);
                }

                // ========================================================
                // READ ONLY NOTIFICATION
                // ========================================================

                else if ("ReadOnly".equalsIgnoreCase(
                                notification.getNotiType())) {

                        Label messageLabel = new Label(
                                        notification.getMessage());

                        messageLabel.setStyle(
                                        "-fx-font-size: 14px;" +
                                                        "-fx-text-fill: #F5F7FA;");

                        messageLabel.setWrapText(true);

                        Label orderLabel = new Label(
                                        "Order ID: "
                                                        + notification.getOrderId());

                        orderLabel.setStyle(
                                        "-fx-font-size: 13px;" +
                                                        "-fx-text-fill: #F5F7FA;");

                        card.getChildren().addAll(
                                        emailLabel,
                                        messageLabel,
                                        orderLabel,
                                        dateLabel);
                }

                // ========================================================
                // OTHER NOTIFICATION TYPES
                // ========================================================

                else {

                        Label messageLabel = new Label(
                                        notification.getMessage());

                        messageLabel.setStyle(
                                        "-fx-font-size: 14px;" +
                                                        "-fx-text-fill: #F5F7FA;");

                        messageLabel.setWrapText(true);

                        card.getChildren().addAll(
                                        emailLabel,
                                        typeLabel,
                                        messageLabel,
                                        dateLabel);
                }

                return card;
        }

        // ============================================================
        // PLACEHOLDER
        // ============================================================

        private Label createPlaceholder(
                        String text) {

                Label label = new Label(text);

                label.setTextFill(
                                Color.web("#8A9BB0"));

                label.setStyle(
                                "-fx-font-size: 14px;");

                return label;
        }
}