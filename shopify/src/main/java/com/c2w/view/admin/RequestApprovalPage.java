package com.c2w.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.c2w.controller.admin.DeliverySignUpController;
import com.c2w.model.admin.DeliverySignUpNotifications;
import com.c2w.model.admin.NotificationModel;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class RequestApprovalPage {

    // ========================================================
    // SHOW APPROVAL REQUESTS PAGE
    // ========================================================

    public VBox getApprovalRequestsPage() {

        VBox mainContent = new VBox(20);

        mainContent.setPadding(
                new Insets(25)
        );

        mainContent.setStyle(
                "-fx-background-color: #0B1017;"
        );

        // ========================================================
        // TITLE
        // ========================================================

        Label title = new Label(
                "Approval Requests"
        );

        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #F5F7FA;"
        );

        Label subtitle = new Label(
                "Review and manage requests received from users and delivery persons."
        );

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #8A9BB0;"
        );


        // ========================================================
        // RADIO BUTTONS
        // ========================================================

        // RadioButton userRadio =
        //         new RadioButton("User");

        RadioButton deliveryRadio =
                new RadioButton("Delivery Person");

        //userRadio.setStyle(
        //         "-fx-font-size: 14px;" +
        //         "-fx-text-fill: #F5F7FA;"
        // );

        deliveryRadio.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #F5F7FA;"
        );

        //ToggleGroup group =
                //new ToggleGroup();

        //userRadio.setToggleGroup(group);
        //deliveryRadio.setToggleGroup(group);

        // User selected by default

        //deliveryRadio.setSelected(true);

        HBox selectionBox =
                new HBox(25);

        selectionBox.setAlignment(
                Pos.CENTER_LEFT
        );

        selectionBox.getChildren().addAll(
                //userRadio,
               // deliveryRadio
        );


        // ========================================================
        // USER APPROVAL REQUEST LIST
        // ========================================================

        // ListView<DeliverySignUpNotifications>
        //         userApprovalListView =
        //         createApprovalRequestListView(
        //                 "User"
        //         );


        // ========================================================
        // DELIVERY APPROVAL REQUEST LIST
        // ========================================================

        ListView<DeliverySignUpNotifications>
                deliveryApprovalListView =
                createApprovalRequestListView(
                        "delivery"
                );


        // ========================================================
        // HISTORY / REQUEST CONTAINER
        // ========================================================

        VBox requestContainer =
                new VBox(10);

        requestContainer.getChildren().add(
                deliveryApprovalListView
        );

        VBox.setVgrow(
                requestContainer,
                Priority.ALWAYS
        );


        // ========================================================
        // RADIO BUTTON ACTIONS
        // ========================================================

        // userRadio.setOnAction(e -> {

        //     requestContainer.getChildren().clear();

        //     requestContainer.getChildren().add(
        //             userApprovalListView
        //     );
        // });


        deliveryRadio.setOnAction(e -> {

            requestContainer.getChildren().clear();

            requestContainer.getChildren().add(
                    deliveryApprovalListView
            );
        });


        // ========================================================
        // ADD EVERYTHING
        // ========================================================

        mainContent.getChildren().addAll(
                title,
                subtitle,
                selectionBox,
                requestContainer
        );


        // ========================================================
        // SCROLL PANE
        // ========================================================

        ScrollPane scrollPane =
                new ScrollPane(
                        mainContent
                );

        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setStyle(
                "-fx-background-color: #0B1017;" +
                "-fx-background: #0B1017;"
        );


        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        VBox page =
                new VBox();

        page.getChildren().add(
                scrollPane
        );

        VBox.setVgrow(
                mainContent,
                Priority.ALWAYS
        );

        mainContent.setFillWidth(true);

        return page;
    }


    // ========================================================
    // CREATE APPROVAL REQUEST LIST VIEW
    // ========================================================

    private ListView<DeliverySignUpNotifications>
    createApprovalRequestListView(
            String userType
    ) {

        List<DeliverySignUpNotifications>
                notifications =
                new ArrayList<>();


        // ========================================================
        // FETCH REQUESTS
        // ========================================================

        if (userType.equals("User")) {

            System.out.println(
                    "Fetching user approval requests..."
            );

        } else if (userType.equals("delivery")) {

            DeliverySignUpController controller =
                    new DeliverySignUpController();

            notifications =
                    controller.goToDeliverySignUpNotifications(
                            userType
                    );
        }


        // ========================================================
        // CREATE LIST VIEW
        // ========================================================

        ListView<DeliverySignUpNotifications>
                listView;


        if (notifications.isEmpty()) {

            listView =
                    new ListView<>();

        } else {

            listView =
                    new ListView<>(
                            FXCollections.observableArrayList(
                                    notifications
                            )
                    );
        }


        listView.setPrefHeight(500);

        listView.setStyle(
                "-fx-background-color: #0B1017;" +
                "-fx-control-inner-background: #0B1017;" +
                "-fx-border-color: #273442;"
        );


        // ========================================================
        // PLACEHOLDER
        // ========================================================

        Label placeholder =
                new Label(
                        "No approval requests available."
                );

        placeholder.setStyle(
                "-fx-text-fill: #8A9BB0;" +
                "-fx-font-size: 13px;"
        );

        listView.setPlaceholder(
                placeholder
        );


        // ========================================================
        // LIST CELL
        // ========================================================

        listView.setCellFactory(
                list -> new ListCell<>() {

            @Override
            protected void updateItem(
                    DeliverySignUpNotifications request,
                    boolean empty
            ) {

                super.updateItem(
                        request,
                        empty
                );


                if (empty || request == null) {

                    setGraphic(null);

                    setStyle(
                            "-fx-background-color: #0B1017;"
                    );

                    return;
                }


                // =================================================
                // MAIN CARD
                // =================================================

                HBox card =
                        new HBox(15);

                card.setPadding(
                        new Insets(15)
                );

                card.setAlignment(
                        Pos.CENTER_LEFT
                );

                card.setStyle(
                        "-fx-background-color: #151C25;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #273442;" +
                        "-fx-border-radius: 8;"
                );


                // =================================================
                // REQUEST INFORMATION
                // =================================================

                VBox information =
                        new VBox(7);


                // NAME

                Label nameLabel =
                        new Label(
                                request.getName()
                        );

                nameLabel.setStyle(
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #36CFC9;"
                );


                // EMAIL

                Label emailLabel =
                        new Label(
                                "Email: " +
                                request.getEmail()
                        );

                emailLabel.setStyle(
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #F5F7FA;"
                );


                // REQUEST TYPE

                Label requestLabel =
                        new Label(
                                "Request: " +
                                request.getNotiType()
                        );

                requestLabel.setStyle(
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #F5F7FA;"
                );


                // DATE

                Label dateLabel =
                        new Label(
                                "Received: " +
                                request.getCreatedAt()
                        );

                dateLabel.setStyle(
                        "-fx-font-size: 12px;" +
                        "-fx-text-fill: #8A9BB0;"
                );


                information.getChildren().addAll(
                        nameLabel,
                        emailLabel,
                        requestLabel,
                        dateLabel
                );


                // =================================================
                // SPACER
                // =================================================

                javafx.scene.layout.Region spacer =
                        new javafx.scene.layout.Region();

                HBox.setHgrow(
                        spacer,
                        Priority.ALWAYS
                );


                // =================================================
                // ACCEPT BUTTON
                // =================================================

                Button acceptButton =
                        new Button("Accept");

                acceptButton.setPadding(
                        new Insets(
                                8,
                                16,
                                8,
                                16
                        )
                );

                acceptButton.setStyle(
                        "-fx-background-color: #36CFC9;" +
                        "-fx-text-fill: #0B1017;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;"
                );


                // =================================================
                // REJECT BUTTON
                // =================================================

                Button rejectButton =
                        new Button("Reject");

                rejectButton.setPadding(
                        new Insets(
                                8,
                                16,
                                8,
                                16
                        )
                );

                rejectButton.setStyle(
                        "-fx-background-color: #7A2E35;" +
                        "-fx-text-fill: #FFFFFF;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;"
                );


                // =================================================
                // BUTTON CONTAINER
                // =================================================

                HBox buttons =
                        new HBox(8);

                buttons.setAlignment(
                        Pos.CENTER_RIGHT
                );

                buttons.getChildren().addAll(
                        acceptButton,
                        rejectButton
                );


                // =================================================
                // ACCEPT ACTION
                // =================================================

                acceptButton.setOnAction(
                        event -> {

                    System.out.println(
                            "Approved : " +
                            request.getId()
                    );


                    DeliverySignUpController
                            deliverySignUpController =
                            new DeliverySignUpController();


                    deliverySignUpController
                            .updateApprovalStatus(
                                    "delivery",
                                    request.getId(),
                                    "Approved"
                            );
                        NotificationModel noti=new NotificationModel();
                        noti.setEmail(request.getEmail());
                        noti.setMessage("Request accepted");
                        noti.setNotiType("REQUEST");
                        noti.setOrderId("NA");
                        noti.setRole("deliveryPerson");
                        noti.setStatus("Approved");
                        noti.setTimestamp(request.getCreatedAt());
                        deliverySignUpController.addNotification(noti);


                    request.setApproveStatus(
                            "Approved"
                    );


                    acceptButton.setDisable(
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
                            Color.web("#36CFC9")
                    );

                    statusLabel.setStyle(
                            "-fx-font-weight: bold;"
                    );


                    buttons.getChildren().clear();

                    buttons.getChildren().add(
                            statusLabel
                    );
                });


                // =================================================
                // REJECT ACTION
                // =================================================

                rejectButton.setOnAction(
                        event -> {

                    System.out.println(
                            "Rejected : " +
                            request.getId()
                    );


                    DeliverySignUpController
                            deliverySignUpController =
                            new DeliverySignUpController();


                    deliverySignUpController
                            .updateApprovalStatus(
                                    "delivery",
                                    request.getId(),
                                    "Rejected"
                            );


                    request.setApproveStatus(
                            "Rejected"
                    );


                    acceptButton.setDisable(
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
                            Color.web("#E05A67")
                    );

                    statusLabel.setStyle(
                            "-fx-font-weight: bold;"
                    );


                    buttons.getChildren().clear();

                    buttons.getChildren().add(
                            statusLabel
                    );
                });


                // =================================================
                // ADD CARD CONTENT
                // =================================================

                card.getChildren().addAll(
                        information,
                        spacer,
                        buttons
                );


                setGraphic(card);
            }
        });


        return listView;
    }
}