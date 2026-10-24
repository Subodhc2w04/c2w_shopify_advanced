package com.c2w.view.admin;


import java.util.List;

import com.c2w.controller.admin.DeliverySignUpController;
import com.c2w.controller.admin.NotificationController;
import com.c2w.model.admin.DeliverySignUpNotifications;
import com.c2w.model.admin.NotificationModel;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class NotificationPage {

    public VBox show() {
        System.out.println("NotificationPage.show() called");
    // =========================
    // Main Container
    // =========================

    VBox mainContainer = new VBox(15);

    mainContainer.setPadding(new Insets(20));

    mainContainer.setStyle(
            "-fx-background-color: #F5F7FA;"
    );


    // =========================
    // Header
    // =========================

    Label title = new Label("Notifications");

    title.setFont(
            Font.font("Arial", 26)
    );

    title.setTextFill(
            Color.web("#222222")
    );


    Label subtitle = new Label(
            "View your notifications and manage your requests"
    );

    subtitle.setFont(
            Font.font("Arial", 14)
    );

    subtitle.setTextFill(
            Color.web("#666666")
    );


    VBox header = new VBox(5);

    header.getChildren().addAll(
            title,
            subtitle
    );


    // =========================
    // ListView
    // =========================

    ListView<DeliverySignUpNotifications> notificationListView =
            new ListView<>();


    // notificationListView.setItems(
    //         FXCollections.observableArrayList()
    // );
    List<DeliverySignUpNotifications> notificationList =
            new DeliverySignUpController().goToDeliverySignUpNotifications("delivery");

    List<List<NotificationModel>> mergedList = new NotificationController().gotoDeliveryNotifications();
    List<NotificationModel> deliveryList = new NotificationController().gotoDeliveryNotifications().get(0);
    List<NotificationModel> customerList = new NotificationController().gotoDeliveryNotifications().get(1);

    System.out.println(
            "Total notifications : "
                    + notificationList.size()
    );

    notificationListView.setItems(
            FXCollections.observableArrayList(
                    notificationList
            )
    );


    // =========================
    // Cell Factory
    // =========================

    notificationListView.setCellFactory(
            listView -> new ListCell<DeliverySignUpNotifications>() {
                
                @Override
                protected void updateItem(
                        DeliverySignUpNotifications notification,
                        boolean empty) {

                    super.updateItem(
                            notification,
                            empty
                    );
                    System.out.println("ListCell created for notification: " + getItem());

                    if (empty || notification == null) {

                        setGraphic(null);

                        setText(null);

                        return;
                    }


                    // =========================
                    // Notification Card
                    // =========================

                    VBox card = new VBox(8);

                    card.setPadding(
                            new Insets(15)
                    );


                    card.setStyle(
                            "-fx-background-color: white;" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-color: #E0E0E0;" +
                            "-fx-border-radius: 10;"
                    );


                    // =========================
                    // Title
                    // =========================

                    Label titleLabel =
                            new Label(
                                    notification.getNotiType()
                            );

                    titleLabel.setFont(
                            Font.font("Arial", 16)
                    );

                    titleLabel.setStyle(
                            "-fx-font-weight: bold;"
                    );


                    // =========================
                    // Message
                    // =========================

                    Label messageLabel =
                            new Label(
                                    "New Sign-Up Request from " + notification.getName() 
                            );
                            System.out.println("Notification Name: " + notification.getName());

                    messageLabel.setFont(
                            Font.font("Arial", 14)
                    );

                    messageLabel.setTextFill(
                            Color.web("#555555")
                    );

                    messageLabel.setWrapText(true);


                    // =========================
                    // Date / Time
                    // =========================

                    Label timeLabel =
                            new Label(
                                    notification.getCreatedAt().toString()
                            );

                    timeLabel.setFont(
                            Font.font("Arial", 11)
                    );

                    timeLabel.setTextFill(
                            Color.web("#999999")
                    );


                    card.getChildren().addAll(
                            titleLabel,
                            messageLabel,
                            timeLabel
                    );


                    // =================================================
                    // READ ONLY NOTIFICATION
                    // =================================================

                    if ("ReadOnly".equals(
                            notification.getNotiType())) {

                        Label statusLabel =
                                new Label("Notification");

                        statusLabel.setTextFill(
                                Color.web("#2563EB")
                        );

                        card.getChildren().add(
                                statusLabel
                        );
                    }


                    // =================================================
                    // REQUEST NOTIFICATION
                    // =================================================

                    else if ("Request".equals(
                            notification.getNotiType())) {


                        // =========================
                        // PENDING
                        // =========================

                        if ("Pending".equals(
                                notification.getApproveStatus())) {


                            Button approveButton =
                                    new Button("Approve");

                            approveButton.setStyle(
                                    "-fx-background-color: #22C55E;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-cursor: hand;"
                            );


                            Button rejectButton =
                                    new Button("Reject");

                            rejectButton.setStyle(
                                    "-fx-background-color: #EF4444;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-cursor: hand;"
                            );


                            HBox buttonBox =
                                    new HBox(10);

                            buttonBox.setAlignment(
                                    Pos.CENTER_LEFT
                            );

                            buttonBox.getChildren().addAll(
                                    approveButton,
                                    rejectButton
                            );


                            card.getChildren().add(
                                    buttonBox
                            );


                            // =========================
                            // APPROVE
                            // =========================

                            approveButton.setOnAction(
                                    event -> {

                                        System.out.println(
                                                "Approved : "
                                                        + notification
                                                        .getId()
                                        );
                                        DeliverySignUpController deliverySignUpController = new DeliverySignUpController();
                                        deliverySignUpController.updateApprovalStatus("delivery", notification.getId(), "Approved");


                                        // Firebase update will
                                        // be called here

                                        notification.setApproveStatus(
                                                "Approved"
                                        );


                                        approveButton.setDisable(
                                                true
                                        );

                                        rejectButton.setDisable(
                                                true
                                        );


                                        Label statusLabel =
                                                new Label(
                                                        "✓ Approved"
                                                );

                                        statusLabel.setTextFill(
                                                Color.web("#16A34A")
                                        );

                                        statusLabel.setStyle(
                                                "-fx-font-weight: bold;"
                                        );


                                        card.getChildren().add(
                                                statusLabel
                                        );
                                    }
                            );


                            // =========================
                            // REJECT
                            // =========================

                            rejectButton.setOnAction(
                                    event -> {

                                        System.out.println(
                                                "Rejected : "
                                                        + notification
                                                        .getId()
                                        );
                                        DeliverySignUpController deliverySignUpController = new DeliverySignUpController();
                                        deliverySignUpController.updateApprovalStatus("delivery", notification.getId(), "Rejected");


                                        // Firebase update will
                                        // be called here

                                        notification.setApproveStatus(
                                                "Rejected"
                                        );


                                        approveButton.setDisable(
                                                true
                                        );

                                        rejectButton.setDisable(
                                                true
                                        );


                                        Label statusLabel =
                                                new Label(
                                                        "✕ Rejected"
                                                );

                                        statusLabel.setTextFill(
                                                Color.web("#DC2626")
                                        );

                                        statusLabel.setStyle(
                                                "-fx-font-weight: bold;"
                                        );


                                        card.getChildren().add(
                                                statusLabel
                                        );
                                    }
                            );
                        }


                        // =========================
                        // ALREADY APPROVED
                        // =========================

                        else if ("APPROVED".equals(
                                notification.getApproveStatus())) {

                            Label statusLabel =
                                    new Label(
                                            "✓ Approved"
                                    );

                            statusLabel.setTextFill(
                                    Color.web("#16A34A")
                            );

                            statusLabel.setStyle(
                                    "-fx-font-weight: bold;"
                            );

                            card.getChildren().add(
                                    statusLabel
                            );
                        }


                        // =========================
                        // ALREADY REJECTED
                        // =========================

                        else if ("REJECTED".equals(
                                notification.getApproveStatus())) {

                            Label statusLabel =
                                    new Label(
                                            "✕ Rejected"
                                    );

                            statusLabel.setTextFill(
                                    Color.web("#DC2626")
                            );

                            statusLabel.setStyle(
                                    "-fx-font-weight: bold;"
                            );

                            card.getChildren().add(
                                    statusLabel
                            );
                        }
                    }


                    setGraphic(card);
                }
            }
    );


    // =========================
    // Add ListView
    // =========================
    VBox.setVgrow(
            notificationListView,
            Priority.ALWAYS
    );


    mainContainer.getChildren().addAll(
            header,
            notificationListView
    );


    return mainContainer;
}

    // =====================================================
    // READ ONLY NOTIFICATION
    // =====================================================

   
}