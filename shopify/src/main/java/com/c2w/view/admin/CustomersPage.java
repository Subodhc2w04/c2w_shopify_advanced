package com.c2w.view.admin;

import java.util.List;

import com.c2w.controller.admin.CustomerController;
import com.c2w.model.admin.Address;
import com.c2w.model.admin.Customers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomersPage {

    private TableView<Customers> tableView;
    private ObservableList<Customers> customerList;

    private CustomerController customerController;
    private BorderPane root;

    public VBox getTotalCustomersView(BorderPane root) {
        this.root=root;

        customerController = new CustomerController();

        // ==============================
        // MAIN CONTAINER
        // ==============================

        VBox mainContainer = new VBox(20);

        mainContainer.setPadding(new Insets(25));

        // ==============================
        // TITLE
        // ==============================

        Label title = new Label("Customers");

        title.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;"+"-fx-text-fill: white;"
        );

        Label subtitle = new Label(
                "Manage and view all registered customers"
        );

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #777777;"
        );

        VBox titleBox = new VBox(5);

        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        // ==============================
        // SEARCH + TOTAL
        // ==============================

        TextField searchField = new TextField();

        searchField.setPromptText("Search customers...");

        searchField.setPrefWidth(350);

        searchField.setStyle(
                "-fx-background-color: #151C25;" +
                "-fx-text-fill: #F5F7FA;" +
                "-fx-prompt-text-fill: #66778A;" +
                "-fx-border-color: #273442;" +
                "-fx-border-radius: 6px;" +
                "-fx-background-radius: 6px;" +
                "-fx-padding: 11px;" +
                "-fx-font-size: 14px;"
        );

        Label totalLabel = new Label();

        totalLabel.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;"+"-fx-text-fill: white;"
        );

        HBox searchBox = new HBox(20);

        searchBox.setAlignment(Pos.CENTER_LEFT);

        searchBox.getChildren().addAll(
                searchField,
                totalLabel
        );

        // ==============================
        // TABLE
        // ==============================

        tableView = new TableView<>();

        tableView.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );
        tableView.setStyle(
                "-fx-background-color: #151C25;" +
                "-fx-control-inner-background: #151C25;" +
                "-fx-table-cell-border-color: #273442;"
        );

        tableView.setPrefHeight(500);

        // ==============================
        // NAME COLUMN
        // ==============================

        TableColumn<Customers, String> nameColumn =
                new TableColumn<>("Name");

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("fullName")
        );

        // ==============================
        // EMAIL COLUMN
        // ==============================

        TableColumn<Customers, String> emailColumn =
                new TableColumn<>("Email");

        emailColumn.setCellValueFactory(
                new PropertyValueFactory<>("email")
        );

        // ==============================
        // MOBILE COLUMN
        // ==============================

        TableColumn<Customers, String> mobileColumn =
                new TableColumn<>("Mobile");

        mobileColumn.setCellValueFactory(
                new PropertyValueFactory<>("mobile")
        );

        // ==============================
        // GENDER COLUMN
        // ==============================

        TableColumn<Customers, String> genderColumn =
                new TableColumn<>("Gender");

        genderColumn.setCellValueFactory(
                new PropertyValueFactory<>("gender")
        );

        // ==============================
        // ACTION COLUMN
        // ==============================

        TableColumn<Customers, Void> actionColumn =
                new TableColumn<>("Actions");

        actionColumn.setCellFactory(column -> {

            return new javafx.scene.control.TableCell<Customers, Void>() {

                private final Button viewButton =
                        new Button("View");

                private final Button deleteButton =
                        new Button("Delete");

                private final HBox actionBox =
                        new HBox(10);

                {
                    viewButton.setStyle(
                            "-fx-background-color: #3498db;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 5px;"
                    );

                    deleteButton.setStyle(
                            "-fx-background-color: #e74c3c;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 5px;"
                    );

                    actionBox.setAlignment(Pos.CENTER);

                    actionBox.getChildren().addAll(
                            viewButton,
                            deleteButton
                    );

                    // VIEW BUTTON

                    viewButton.setOnAction(event -> {
                        Runnable back=()->root.setCenter(new CustomersPage().getTotalCustomersView(root));
                        Customers customer =
                                getTableView().getItems().get(getIndex());

                        root.setCenter(showCustomerDetails(customer,back));
                    });

                    // DELETE BUTTON

                    deleteButton.setOnAction(event -> {

                        Customers customer =
                                getTableView().getItems().get(getIndex());

                        deleteCustomer(customer);
                    });
                }

                @Override
                protected void updateItem(
                        Void item,
                        boolean empty) {

                    super.updateItem(item, empty);

                    if (empty) {

                        setGraphic(null);

                    } else {

                        setGraphic(actionBox);
                    }
                }
            };
        });

        // ==============================
        // ADD COLUMNS
        // ==============================

        tableView.getColumns().addAll(
                nameColumn,
                emailColumn,
                mobileColumn,
                genderColumn,
                actionColumn
        );

        // ==============================
        // FETCH DATA
        // ==============================

        List<Customers> customers =
                customerController.getCustomerData();

        customerList =
                FXCollections.observableArrayList(customers);

        tableView.setItems(customerList);

        totalLabel.setText(
                "Total Customers : " + customerList.size()
        );

        // ==============================
        // SEARCH
        // ==============================

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    String searchText =
                            newValue.toLowerCase();

                    ObservableList<Customers> filteredList =
                            FXCollections.observableArrayList();

                    for (Customers customer : customerList) {

                        if (customer.getFullName()
                                .toLowerCase()
                                .contains(searchText)

                                || customer.getEmail()
                                .toLowerCase()
                                .contains(searchText)

                                || customer.getMobile()
                                .contains(searchText)) {

                            filteredList.add(customer);
                        }
                    }

                    tableView.setItems(filteredList);
                }
        );

        // ==============================
        // MAIN LAYOUT
        // ==============================

        mainContainer.getChildren().addAll(
                titleBox,
                searchBox,
                tableView
        );

        return mainContainer;
    }

    // =========================================================
    // VIEW CUSTOMER DETAILS
    // =========================================================

    private VBox showCustomerDetails(Customers customer, Runnable back) {

    // =========================================================
    // MAIN ROOT
    // =========================================================

    VBox rootBox = new VBox(0);

    rootBox.setMaxSize(
            Double.MAX_VALUE,
            Double.MAX_VALUE
    );

    rootBox.setStyle(
            "-fx-background-color: #0B1017;"
    );

    // =========================================================
    // TOP BAR
    // =========================================================

    Button closeButton = new Button("←  Close");

    closeButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #36CFC9;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8px 15px;" +
            "-fx-cursor: hand;"
    );

    closeButton.setOnMouseEntered(e -> {
        closeButton.setStyle(
                "-fx-background-color: #1E454C;" +
                "-fx-text-fill: #36CFC9;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8px 15px;" +
                "-fx-background-radius: 6px;" +
                "-fx-cursor: hand;"
        );
    });

    closeButton.setOnMouseExited(e -> {
        closeButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #36CFC9;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8px 15px;" +
                "-fx-cursor: hand;"
        );
    });

    closeButton.setOnAction(e -> {
        back.run();
    });

    HBox topBar = new HBox();

    topBar.setAlignment(Pos.CENTER_LEFT);
    topBar.setPadding(
            new Insets(20, 30, 10, 30)
    );

    topBar.setStyle(
            "-fx-background-color: #111821;" +
            "-fx-border-color: #273442;" +
            "-fx-border-width: 0 0 1 0;"
    );

    topBar.getChildren().add(closeButton);

    // =========================================================
    // PAGE TITLE
    // =========================================================

    Label title = new Label("Customer Details");

    title.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #F5F7FA;"
    );

    Label subtitle = new Label(
            "View complete information about this customer"
    );

    subtitle.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #8A9BB0;"
    );

    VBox titleBox = new VBox(5);

    titleBox.getChildren().addAll(
            title,
            subtitle
    );

    // =========================================================
    // CUSTOMER INFORMATION CARD
    // =========================================================

    Label customerSectionTitle =
            new Label("Personal Information");

    customerSectionTitle.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #F5F7FA;"
    );

    // ---------- Full Name ----------

    Label nameLabel =
            new Label("Full Name");

    nameLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #8A9BB0;"
    );

    Label nameValue =
            new Label(customer.getFullName());

    nameValue.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-text-fill: #F5F7FA;"
    );

    VBox nameBox = new VBox(5);

    nameBox.getChildren().addAll(
            nameLabel,
            nameValue
    );

    // ---------- Email ----------

    Label emailLabel =
            new Label("Email");

    emailLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #8A9BB0;"
    );

    Label emailValue =
            new Label(customer.getEmail());

    emailValue.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-text-fill: #F5F7FA;"
    );

    VBox emailBox = new VBox(5);

    emailBox.getChildren().addAll(
            emailLabel,
            emailValue
    );

    // ---------- Mobile ----------

    Label mobileLabel =
            new Label("Mobile");

    mobileLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #8A9BB0;"
    );

    Label mobileValue =
            new Label(customer.getMobile());

    mobileValue.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-text-fill: #F5F7FA;"
    );

    VBox mobileBox = new VBox(5);

    mobileBox.getChildren().addAll(
            mobileLabel,
            mobileValue
    );

    // ---------- Gender ----------

    Label genderLabel =
            new Label("Gender");

    genderLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #8A9BB0;"
    );

    Label genderValue =
            new Label(customer.getGender());

    genderValue.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-text-fill: #F5F7FA;"
    );

    VBox genderBox = new VBox(5);

    genderBox.getChildren().addAll(
            genderLabel,
            genderValue
    );

    // =========================================================
    // CUSTOMER DETAILS GRID
    // =========================================================

    GridPane customerGrid = new GridPane();

    customerGrid.setHgap(80);
    customerGrid.setVgap(25);

    customerGrid.add(nameBox, 0, 0);
    customerGrid.add(emailBox, 1, 0);
    customerGrid.add(mobileBox, 0, 1);
    customerGrid.add(genderBox, 1, 1);

    // =========================================================
    // CUSTOMER CARD
    // =========================================================

    VBox customerCard = new VBox(20);

    customerCard.setPadding(
            new Insets(25)
    );

    customerCard.setMaxWidth(
            Double.MAX_VALUE
    );

    customerCard.setStyle(
            "-fx-background-color: #151C25;" +
            "-fx-border-color: #273442;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;"
    );

    customerCard.getChildren().addAll(
            customerSectionTitle,
            customerGrid
    );

    // =========================================================
    // ADDRESS SECTION
    // =========================================================

    Label addressTitle =
            new Label("Addresses");

    addressTitle.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #F5F7FA;"
    );

    Label addressSubtitle =
            new Label(
                    "Saved delivery addresses of the customer"
            );

    addressSubtitle.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #8A9BB0;"
    );

    VBox addressHeading = new VBox(5);

    addressHeading.getChildren().addAll(
            addressTitle,
            addressSubtitle
    );

    // =========================================================
    // ADDRESS BOX
    // =========================================================

    VBox addressBox = new VBox(15);

    if (customer.getAddresses() != null &&
            !customer.getAddresses().isEmpty()) {

        for (Address address : customer.getAddresses()) {

            // ---------------------------------------------
            // Address Type
            // ---------------------------------------------

            Label addressType =
                    new Label(
                            address.getAddressType()
                    );

            addressType.setStyle(
                    "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #36CFC9;"
            );

            // ---------------------------------------------
            // Name
            // ---------------------------------------------

            Label addressName =
                    new Label(
                            "Name: " +
                            address.getFullName()
                    );

            addressName.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-text-fill: #F5F7FA;"
            );

            // ---------------------------------------------
            // Mobile
            // ---------------------------------------------

            Label addressMobile =
                    new Label(
                            "Mobile: " +
                            address.getMobile()
                    );

            addressMobile.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-text-fill: #F5F7FA;"
            );

            // ---------------------------------------------
            // Street
            // ---------------------------------------------

            Label street =
                    new Label(
                            "Street: " +
                            address.getStreet()
                    );

            street.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-text-fill: #F5F7FA;"
            );

            // ---------------------------------------------
            // City
            // ---------------------------------------------

            Label city =
                    new Label(
                            "City: " +
                            address.getCity()
                    );

            city.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-text-fill: #F5F7FA;"
            );

            // ---------------------------------------------
            // State
            // ---------------------------------------------

            Label state =
                    new Label(
                            "State: " +
                            address.getState()
                    );

            state.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-text-fill: #F5F7FA;"
            );

            // ---------------------------------------------
            // PIN Code
            // ---------------------------------------------

            Label pinCode =
                    new Label(
                            "PIN Code: " +
                            address.getPinCode()
                    );

            pinCode.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-text-fill: #F5F7FA;"
            );

            // ---------------------------------------------
            // Default Address
            // ---------------------------------------------

            Label defaultAddress =
                    new Label(
                            address.isDefault()
                                    ? "✓ Default Address"
                                    : "Not Default Address"
                    );

            if (address.isDefault()) {

                defaultAddress.setStyle(
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #36CFC9;"
                );

            } else {

                defaultAddress.setStyle(
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #66778A;"
                );
            }

            // ---------------------------------------------
            // Address Details
            // ---------------------------------------------

            VBox addressDetails =
                    new VBox(8);

            addressDetails.getChildren().addAll(
                    addressName,
                    addressMobile,
                    street,
                    city,
                    state,
                    pinCode,
                    defaultAddress
            );

            // ---------------------------------------------
            // Single Address Card
            // ---------------------------------------------

            VBox singleAddress =
                    new VBox(15);

            singleAddress.setPadding(
                    new Insets(20)
            );

            singleAddress.setMaxWidth(
                    Double.MAX_VALUE
            );

            singleAddress.setStyle(
                    "-fx-background-color: #151C25;" +
                    "-fx-background-radius: 8px;" +
                    "-fx-border-color: #273442;" +
                    "-fx-border-radius: 8px;"
            );

            singleAddress.getChildren().addAll(
                    addressType,
                    addressDetails
            );

            addressBox.getChildren().add(
                    singleAddress
            );
        }

    } else {

        Label noAddress =
                new Label(
                        "No address available"
                );

        noAddress.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #66778A;"
        );

        addressBox.getChildren().add(
                noAddress
        );
    }

    // =========================================================
    // MAIN CONTENT
    // =========================================================

    VBox content = new VBox(25);

    content.setPadding(
            new Insets(30)
    );

    content.setFillWidth(true);

    content.setMaxWidth(
            Double.MAX_VALUE
    );

    content.getChildren().addAll(
            titleBox,
            customerCard,
            addressHeading,
            addressBox
    );

    // =========================================================
    // SCROLL PANE
    // =========================================================

    ScrollPane scrollPane =
            new ScrollPane(content);

    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(false);

    scrollPane.setMaxSize(
            Double.MAX_VALUE,
            Double.MAX_VALUE
    );

    scrollPane.setHbarPolicy(
            ScrollPane.ScrollBarPolicy.NEVER
    );

    scrollPane.setVbarPolicy(
            ScrollPane.ScrollBarPolicy.AS_NEEDED
    );

    scrollPane.setStyle(
            "-fx-background-color: #0B1017;" +
            "-fx-background: #0B1017;"
    );

    VBox.setVgrow(
            scrollPane,
            Priority.ALWAYS
    );

    // =========================================================
    // ADD EVERYTHING TO ROOT
    // =========================================================

    rootBox.getChildren().addAll(
            topBar,
            scrollPane
    );

    return rootBox;
}
    // =========================================================
    // DELETE CUSTOMER
    // =========================================================

    private void deleteCustomer(Customers customer) {

        Alert confirmation =
                        new Alert(
                                        Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Delete Customer");

        confirmation.setHeaderText(
                        "Delete " + customer.getFullName() + "?");

        confirmation.setContentText(
                        "This customer will be permanently deleted.");

        // =====================================================
        // CONFIRMATION ALERT THEME
        // =====================================================

        confirmation.getDialogPane().setStyle(
                        "-fx-background-color: #151C25;"
        );

        confirmation.getDialogPane()
                        .lookup(".header-panel")
                        .setStyle(
                                        "-fx-background-color: #111821;"
                        );

        confirmation.getDialogPane()
                        .lookup(".header-panel")
                        .lookup(".label")
                        .setStyle(
                                        "-fx-text-fill: #F5F7FA;" +
                                                        "-fx-font-size: 18px;" +
                                                        "-fx-font-weight: bold;"
                        );

        confirmation.getDialogPane()
                        .lookup(".content.label")
                        .setStyle(
                                        "-fx-text-fill: #8A9BB0;" +
                                                        "-fx-font-size: 14px;"
                        );

        Button okButton =
                        (Button) confirmation.getDialogPane()
                                        .lookupButton(ButtonType.OK);

        Button cancelButton =
                        (Button) confirmation.getDialogPane()
                                        .lookupButton(ButtonType.CANCEL);

        okButton.setStyle(
                        "-fx-background-color: #D64545;" +
                                        "-fx-text-fill: white;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-background-radius: 5px;" +
                                        "-fx-padding: 8px 18px;" +
                                        "-fx-cursor: hand;"
        );

        cancelButton.setStyle(
                        "-fx-background-color: #273442;" +
                                        "-fx-text-fill: #F5F7FA;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-background-radius: 5px;" +
                                        "-fx-padding: 8px 18px;" +
                                        "-fx-cursor: hand;"
        );

        // =====================================================
        // CONFIRM DELETE
        // =====================================================

        if (confirmation.showAndWait()
                        .orElse(ButtonType.CANCEL)
                        == ButtonType.OK) {

                boolean deleted =
                                customerController.deleteCustomer(
                                                customer.getDocumentId());

                if (deleted) {

                        tableView.getItems().remove(customer);

                        customerList.remove(customer);

                        // =================================================
                        // SUCCESS ALERT
                        // =================================================

                        Alert success =
                                        new Alert(
                                                        Alert.AlertType.INFORMATION);

                        success.setTitle("Success");

                        success.setHeaderText(null);

                        success.setContentText(
                                        "Customer deleted successfully.");

                        success.getDialogPane().setStyle(
                                        "-fx-background-color: #151C25;"
                        );

                        success.getDialogPane()
                                        .lookup(".content.label")
                                        .setStyle(
                                                        "-fx-text-fill: #F5F7FA;" +
                                                                        "-fx-font-size: 14px;"
                                        );

                        Button successButton =
                                        (Button) success.getDialogPane()
                                                        .lookupButton(
                                                                        ButtonType.OK);

                        successButton.setStyle(
                                        "-fx-background-color: #36CFC9;" +
                                                        "-fx-text-fill: #0B1017;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-background-radius: 5px;" +
                                                        "-fx-padding: 8px 18px;" +
                                                        "-fx-cursor: hand;"
                        );

                        success.showAndWait();

                } else {

                        // =================================================
                        // ERROR ALERT
                        // =================================================

                        Alert error =
                                        new Alert(
                                                        Alert.AlertType.ERROR);

                        error.setTitle("Error");

                        error.setHeaderText(
                                        "Unable to delete customer");

                        error.setContentText(
                                        "Something went wrong while deleting the customer.");

                        error.getDialogPane().setStyle(
                                        "-fx-background-color: #151C25;"
                        );

                        error.getDialogPane()
                                        .lookup(".header-panel")
                                        .setStyle(
                                                        "-fx-background-color: #111821;"
                                        );

                        error.getDialogPane()
                                        .lookup(".header-panel")
                                        .lookup(".label")
                                        .setStyle(
                                                        "-fx-text-fill: #F5F7FA;" +
                                                                        "-fx-font-size: 18px;" +
                                                                        "-fx-font-weight: bold;"
                                        );

                        error.getDialogPane()
                                        .lookup(".content.label")
                                        .setStyle(
                                                        "-fx-text-fill: #8A9BB0;" +
                                                                        "-fx-font-size: 14px;"
                                        );

                        Button errorButton =
                                        (Button) error.getDialogPane()
                                                        .lookupButton(
                                                                        ButtonType.OK);

                        errorButton.setStyle(
                                        "-fx-background-color: #D64545;" +
                                                        "-fx-text-fill: white;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-background-radius: 5px;" +
                                                        "-fx-padding: 8px 18px;" +
                                                        "-fx-cursor: hand;"
                        );

                        error.showAndWait();
                }
        }
}
}