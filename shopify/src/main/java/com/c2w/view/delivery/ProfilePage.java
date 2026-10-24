package com.c2w.view.delivery;

import com.c2w.view.authentication.AuthPage;

import com.c2w.controller.delivery.DeliveryPersonController;
import com.c2w.model.delivery.DeliveryPerson;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProfilePage {

        private Scene profilePageScene;
        private DeliveryPersonController loginController = new DeliveryPersonController();

        public Scene getProfilePageScene(Runnable backAction) {

                DeliveryPerson person = loginController.getAllDeliveryPersonsData(AuthPage.email);

                VBox sidebar = DeliverySidebar.getSidebar("Profile", backAction);

                Button backBtn = new Button("← Back");
                backBtn.setStyle(
                                "-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-font-size: 13px; -fx-cursor: hand; -fx-border-color: #334155; -fx-border-radius: 6px; -fx-padding: 6 12;");
                backBtn.setOnAction(e -> backAction.run());

                Label pageTitle = new Label("My Profile");
                pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                Label pageSubtitle = new Label(
                                "Manage personal credentials, vehicle registration, and active delivery zones.");
                pageSubtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

                VBox titleBox = new VBox(4, pageTitle, pageSubtitle);

                HBox topBar = new HBox(16, backBtn, titleBox);
                topBar.setAlignment(Pos.CENTER_LEFT);
                topBar.setPadding(new Insets(0, 0, 16, 0));

                Label avatarPlaceholder = new Label("👤");
                avatarPlaceholder.setStyle(
                                "-fx-font-size: 40px; -fx-background-color: #0f172a; -fx-padding: 20; -fx-background-radius: 50px; -fx-border-color: #3b82f6; -fx-border-radius: 50px;");

                Label nameLabel = new Label(person.getName());
                nameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

                Label idBadge = new Label("Partner ID: " + person.getId());
                idBadge.setStyle(
                                "-fx-background-color: #064e3b; -fx-text-fill: #34d399; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 12; -fx-background-radius: 12px;");

                VBox avatarBox = new VBox(8, avatarPlaceholder, nameLabel, idBadge);
                avatarBox.setAlignment(Pos.CENTER);
                avatarBox.setPadding(new Insets(20));
                avatarBox.setStyle(
                                "-fx-background-color: #1e293b; -fx-background-radius: 14px; -fx-border-color: #334155; -fx-border-radius: 14px;");

                Label emailLbl = new Label("Email Address");
                emailLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

                TextField emailField = new TextField(person.getEmail());
                emailField.setEditable(false);
                emailField.setFocusTraversable(false);
                emailField.setStyle(
                                "-fx-background-color: #0f172a; -fx-text-fill: #94a3b8; -fx-border-color: #334155; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 8 12;");

                Label phoneLbl = new Label("Phone Number");
                phoneLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

                TextField phoneField = new TextField(person.getPhone());
                phoneField.setEditable(false);
                phoneField.setFocusTraversable(false);
                phoneField.setStyle(
                                "-fx-background-color: #0f172a; -fx-text-fill: #ffffff; -fx-border-color: #334155; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 8 12;");

                Label vehicleTypeLbl = new Label("Vehicle Type");
                vehicleTypeLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

                ComboBox<String> vehicleTypeCombo = new ComboBox<>();
                vehicleTypeCombo.getItems().addAll(
                                "Bike",
                                "Scooter",
                                "Electric Scooter",
                                "Car",
                                "Van");
                vehicleTypeCombo.setValue(person.getVehicleType());
                vehicleTypeCombo.setMaxWidth(Double.MAX_VALUE);
                vehicleTypeCombo.setDisable(false);
                vehicleTypeCombo.setMouseTransparent(true);
                vehicleTypeCombo.setStyle(
                                "-fx-background-color: #0f172a;" +
                                                "-fx-border-color: #334155;" +
                                                "-fx-border-radius: 8px;" +
                                                "-fx-background-radius: 8px;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-opacity: 1.0;");
                vehicleTypeCombo.setButtonCell(new ListCell<String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);

                                setText(empty ? null : item);

                                setTextFill(javafx.scene.paint.Color.WHITE);
                                setStyle(
                                                "-fx-background-color: #0f172a;" +
                                                                "-fx-text-fill: white;" +
                                                                "-fx-font-size: 13px;");
                        }
                });

                vehicleTypeCombo.setCellFactory(lv -> new ListCell<String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty || item == null) {
                                        setText(null);
                                        setStyle("-fx-background-color: #0f172a;-fx-text-fill: #ffffff");
                                } else {
                                        setText(item);
                                        setStyle("-fx-text-fill: #ffffff; -fx-background-color: #0f172a; -fx-font-size: 13px; -fx-padding: 8 12;");
                                }
                        }
                });

                Label vehicleNumLbl = new Label("Vehicle Number");
                vehicleNumLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

                TextField vehicleNumField = new TextField(person.getVehicleNumber());
                vehicleNumField.setPromptText("MH 12 ZY 1212");
                vehicleNumField.setEditable(false);
                vehicleNumField.setFocusTraversable(false);
                vehicleNumField.setStyle(
                                "-fx-background-color: #0f172a; -fx-text-fill: #ffffff; -fx-prompt-text-fill: #64748b; -fx-border-color: #334155; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 8 12;");

                Label addressLbl = new Label("Address");
                addressLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

                TextField addressField = new TextField(person.getAddress());
                addressField.setEditable(false);
                addressField.setFocusTraversable(false);
                addressField.setStyle(
                                "-fx-background-color: #0f172a; -fx-text-fill: #ffffff; -fx-border-color: #334155; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 8 12;");

                Button editBtn = new Button("Edit Profile");
                editBtn.setStyle(
                                "-fx-background-color: #334155; -fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-padding: 8 18; -fx-background-radius: 6px; -fx-cursor: hand;");

                Button saveBtn = new Button("Save Changes");
                saveBtn.setStyle(
                                "-fx-background-color: #2563eb; -fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 22; -fx-background-radius: 6px; -fx-cursor: hand;");

                Button cancelBtn = new Button("Cancel");
                cancelBtn.setStyle(
                                "-fx-background-color: #475569; -fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-padding: 8 18; -fx-background-radius: 6px; -fx-cursor: hand;");

                Label statusSaved = new Label("");
                statusSaved.setStyle("-fx-font-size: 13px; -fx-text-fill: #34d399;");

                HBox btnRow = new HBox(12, editBtn, saveBtn, cancelBtn, statusSaved);
                btnRow.setAlignment(Pos.CENTER_LEFT);

                saveBtn.setVisible(false);
                cancelBtn.setVisible(false);

                editBtn.setOnAction(e -> {

                        phoneField.setEditable(true);
                        phoneField.setFocusTraversable(true);

                      vehicleTypeCombo.setMouseTransparent(false);

                        vehicleNumField.setEditable(true);
                        vehicleNumField.setFocusTraversable(true);

                        addressField.setEditable(true);
                        addressField.setFocusTraversable(true);

                        editBtn.setVisible(false);
                        saveBtn.setVisible(true);
                        cancelBtn.setVisible(true);

                        statusSaved.setText("");
                        statusSaved.setStyle("-fx-font-size: 13px; -fx-text-fill: #eaeef4;");

                        phoneField.requestFocus();
                });

                cancelBtn.setOnAction(e -> {

                        phoneField.setText(person.getPhone());
                        vehicleTypeCombo.setValue(person.getVehicleType());
                        vehicleNumField.setText(person.getVehicleNumber());
                        addressField.setText(person.getAddress());

                        phoneField.setEditable(false);
                        phoneField.setFocusTraversable(false);

                       vehicleTypeCombo.setMouseTransparent(true);

                        vehicleNumField.setEditable(false);
                        vehicleNumField.setFocusTraversable(false);

                        addressField.setEditable(false);
                        addressField.setFocusTraversable(false);

                        editBtn.setVisible(true);
                        saveBtn.setVisible(false);
                        cancelBtn.setVisible(false);

                        statusSaved.setText("");
                });

                saveBtn.setOnAction(e -> {

                        String phone = phoneField.getText().trim();
                        String vehicleType = vehicleTypeCombo.getValue();
                        String vehicleNumber = vehicleNumField.getText().trim();
                        String address = addressField.getText().trim();

                        if (phone.isEmpty()) {
                                DeliveryAlert.showWarning("Validation Error", "Phone Required",
                                                "Phone number is required.");
                                phoneField.requestFocus();
                                return;
                        }

                        if (!phone.matches("\\d{10}")) {
                                DeliveryAlert.showWarning("Validation Error", "Invalid Phone",
                                                "Phone number must contain exactly 10 digits.");
                                phoneField.requestFocus();
                                return;
                        }

                        if (vehicleType == null || vehicleType.trim().isEmpty()) {
                                DeliveryAlert.showWarning("Validation Error", "Vehicle Required",
                                                "Please select a vehicle type.");
                                vehicleTypeCombo.requestFocus();
                                return;
                        }

                        if (vehicleNumber.isEmpty()) {
                                DeliveryAlert.showWarning("Validation Error", "Vehicle Number Required",
                                                "Vehicle number is required.");
                                vehicleNumField.requestFocus();
                                return;
                        }

                        String formattedVehicleNum = vehicleNumber.toUpperCase().replaceAll("\\s+", " ").trim();
                        if (!formattedVehicleNum.matches("^[A-Z]{2}\\s?[0-9]{1,2}\\s?[A-Z]{1,3}\\s?[0-9]{4}$")) {
                                DeliveryAlert.showWarning("Validation Error", "Invalid Vehicle Number",
                                                "Please enter a valid vehicle number.\nExample format: MH 12 ZY 1212");
                                vehicleNumField.requestFocus();
                                return;
                        }

                        if (address.isEmpty()) {
                                DeliveryAlert.showWarning("Validation Error", "Address Required",
                                                "Address is required.");
                                addressField.requestFocus();
                                return;
                        }

                        loginController.updateDeliveryPerson(
                                        phone,
                                        vehicleType,
                                        formattedVehicleNum,
                                        address,
                                        "true");

                        vehicleNumField.setText(formattedVehicleNum);

                        statusSaved.setText("Profile saved successfully!");
                        statusSaved.setStyle("-fx-font-size: 13px; -fx-text-fill: #34d399;");

                        phoneField.setEditable(false);
                        phoneField.setFocusTraversable(false);

                      vehicleTypeCombo.setMouseTransparent(true);

                        vehicleNumField.setEditable(false);
                        vehicleNumField.setFocusTraversable(false);

                        addressField.setEditable(false);
                        addressField.setFocusTraversable(false);

                        editBtn.setVisible(true);
                        saveBtn.setVisible(false);
                        cancelBtn.setVisible(false);
                });

                VBox formCard = new VBox(
                                12,
                                new VBox(4, emailLbl, emailField),
                                new VBox(4, phoneLbl, phoneField),
                                new VBox(4, vehicleTypeLbl, vehicleTypeCombo),
                                new VBox(4, vehicleNumLbl, vehicleNumField),
                                new VBox(4, addressLbl, addressField),
                                btnRow);

                formCard.setPadding(new Insets(24));
                formCard.setStyle(
                                "-fx-background-color: #1e293b; -fx-background-radius: 14px; -fx-border-color: #334155; -fx-border-radius: 14px;");

                HBox.setHgrow(formCard, Priority.ALWAYS);

                HBox profileGrid = new HBox(24, avatarBox, formCard);

                VBox mainContent = new VBox(20, topBar, profileGrid);
                mainContent.setPadding(new Insets(24, 32, 24, 32));

                ScrollPane scrollPane = new ScrollPane(mainContent);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle(
                                "-fx-background: #0b111e; -fx-background-color: #0b111e; -fx-border-color: transparent;");

                HBox rootLayout = new HBox(sidebar, scrollPane);
                HBox.setHgrow(scrollPane, Priority.ALWAYS);
                rootLayout.setStyle("-fx-background-color: #0b111e;");

                profilePageScene = new Scene(rootLayout, 1550, 850);

                return profilePageScene;
        }
}