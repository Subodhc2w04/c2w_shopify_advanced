package com.c2w.view.admin;

import java.util.List;

import com.c2w.controller.admin.DeliverySignUpController;
import com.c2w.model.admin.DeliverySignUpNotifications;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DeliveryPersonsPage {

        private TableView<DeliverySignUpNotifications> tableView;

        private ObservableList<DeliverySignUpNotifications> deliveryPersonList;

        private DeliverySignUpController deliveryPersonController;
        private BorderPane root;

        public VBox getTotalDeliveryPersonsView(BorderPane root) {
                this.root = root;
                deliveryPersonController = new DeliverySignUpController();

                // =====================================================
                // MAIN CONTAINER
                // =====================================================

                VBox mainContainer = new VBox(20);

                mainContainer.setPadding(
                                new Insets(30));

                mainContainer.setMaxSize(
                                Double.MAX_VALUE,
                                Double.MAX_VALUE);

                mainContainer.setStyle(
                                "-fx-background-color: #0B1017;");

                // =====================================================
                // TITLE
                // =====================================================

                Label title = new Label("Delivery Persons");

                title.setStyle(
                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #F5F7FA;");

                Label subtitle = new Label(
                                "Manage and view all registered delivery persons");

                subtitle.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #8A9BB0;");

                VBox titleBox = new VBox(5);

                titleBox.getChildren().addAll(
                                title,
                                subtitle);

                // =====================================================
                // SEARCH
                // =====================================================

                TextField searchField = new TextField();

                searchField.setPromptText(
                                "Search delivery persons...");

                searchField.setPrefWidth(400);

                searchField.setStyle(
                                "-fx-background-color: #151C25;" +
                                                "-fx-text-fill: #F5F7FA;" +
                                                "-fx-prompt-text-fill: #66778A;" +
                                                "-fx-border-color: #273442;" +
                                                "-fx-border-radius: 6px;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-padding: 11px;" +
                                                "-fx-font-size: 14px;");

                Label totalLabel = new Label();

                totalLabel.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #36CFC9;");

                HBox searchBox = new HBox(20);

                searchBox.setAlignment(
                                Pos.CENTER_LEFT);

                searchBox.getChildren().addAll(
                                searchField,
                                totalLabel);

                // =====================================================
                // TABLE
                // =====================================================

                tableView = new TableView<>();

                tableView.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);

                tableView.setPrefHeight(600);

                tableView.setStyle(
                                "-fx-background-color: #151C25;" +
                                                "-fx-control-inner-background: #151C25;" +
                                                "-fx-table-cell-border-color: #273442;");

                // =====================================================
                // ID COLUMN
                // =====================================================

                TableColumn<DeliverySignUpNotifications, String> idColumn = new TableColumn<>("ID");

                idColumn.setCellValueFactory(
                                new PropertyValueFactory<>("id"));

                // =====================================================
                // NAME COLUMN
                // =====================================================

                TableColumn<DeliverySignUpNotifications, String> nameColumn = new TableColumn<>("Name");

                nameColumn.setCellValueFactory(
                                new PropertyValueFactory<>("name"));

                // =====================================================
                // EMAIL COLUMN
                // =====================================================

                TableColumn<DeliverySignUpNotifications, String> emailColumn = new TableColumn<>("Email");

                emailColumn.setCellValueFactory(
                                new PropertyValueFactory<>("email"));

                // =====================================================
                // PHONE COLUMN
                // =====================================================

                TableColumn<DeliverySignUpNotifications, String> phoneColumn = new TableColumn<>("Phone");

                phoneColumn.setCellValueFactory(
                                new PropertyValueFactory<>("phone"));

                // =====================================================
                // VEHICLE COLUMN
                // =====================================================

                TableColumn<DeliverySignUpNotifications, String> vehicleColumn = new TableColumn<>("Vehicle");

                vehicleColumn.setCellValueFactory(
                                new PropertyValueFactory<>("vehicleType"));

                // =====================================================
                // APPROVAL STATUS COLUMN
                // =====================================================

                TableColumn<DeliverySignUpNotifications, String> statusColumn = new TableColumn<>("Status");

                statusColumn.setCellValueFactory(
                                new PropertyValueFactory<>("approveStatus"));

                // =====================================================
                // ACTION COLUMN
                // =====================================================

                TableColumn<DeliverySignUpNotifications, Void> actionColumn = new TableColumn<>("Actions");

                actionColumn.setCellFactory(column -> {

                        return new TableCell<DeliverySignUpNotifications, Void>() {

                                private final Button viewButton = new Button("View");
                                private final Button deleteButton = new Button("Delete");

                               

                                private final HBox actionBox = new HBox(10);

                                {
                                        viewButton.setStyle(
                                                        "-fx-background-color: #36CFC9;" +
                                                                        "-fx-text-fill: #0B1017;" +
                                                                        "-fx-font-weight: bold;" +
                                                                        "-fx-background-radius: 5px;" +
                                                                        "-fx-padding: 7px 16px;" +
                                                                        "-fx-cursor: hand;");

                                        viewButton.setOnMouseEntered(e -> {

                                                viewButton.setStyle(
                                                                "-fx-background-color: #52DDD8;" +
                                                                                "-fx-text-fill: #0B1017;" +
                                                                                "-fx-font-weight: bold;" +
                                                                                "-fx-background-radius: 5px;" +
                                                                                "-fx-padding: 7px 16px;" +
                                                                                "-fx-cursor: hand;");
                                        });

                                        viewButton.setOnMouseExited(e -> {

                                                viewButton.setStyle(
                                                                "-fx-background-color: #36CFC9;" +
                                                                                "-fx-text-fill: #0B1017;" +
                                                                                "-fx-font-weight: bold;" +
                                                                                "-fx-background-radius: 5px;" +
                                                                                "-fx-padding: 7px 16px;" +
                                                                                "-fx-cursor: hand;");
                                        });

                                        actionBox.setAlignment(
                                                        Pos.CENTER);

                                        actionBox.getChildren().addAll(
                                                        viewButton,deleteButton);

                                        viewButton.setOnAction(event -> {
                                                Runnable back = () -> root.setCenter(new DeliveryPersonsPage()
                                                                .getTotalDeliveryPersonsView(root));
                                                DeliverySignUpNotifications deliveryPerson = getTableView()
                                                                .getItems()
                                                                .get(getIndex());

                                                root.setCenter(showDeliveryPersonDetails(
                                                                deliveryPerson,
                                                                back));

                                        });

                                        deleteButton.setStyle(
                                                        "-fx-background-color: #D64545;" +
                                                                        "-fx-text-fill: white;" +
                                                                        "-fx-font-size: 14px;" +
                                                                        "-fx-font-weight: bold;" +
                                                                        "-fx-background-radius: 5px;" +
                                                                        "-fx-padding: 8px 18px;" +
                                                                        "-fx-cursor: hand;");

                                        deleteButton.setOnMouseEntered(e -> {

                                                deleteButton.setStyle(
                                                                "-fx-background-color: #E05252;" +
                                                                                "-fx-text-fill: white;" +
                                                                                "-fx-font-size: 14px;" +
                                                                                "-fx-font-weight: bold;" +
                                                                                "-fx-background-radius: 5px;" +
                                                                                "-fx-padding: 8px 18px;" +
                                                                                "-fx-cursor: hand;");
                                        });

                                        deleteButton.setOnMouseExited(e -> {

                                                deleteButton.setStyle(
                                                                "-fx-background-color: #D64545;" +
                                                                                "-fx-text-fill: white;" +
                                                                                "-fx-font-size: 14px;" +
                                                                                "-fx-font-weight: bold;" +
                                                                                "-fx-background-radius: 5px;" +
                                                                                "-fx-padding: 8px 18px;" +
                                                                                "-fx-cursor: hand;");
                                        });

                                        // =====================================================
                                        // DELETE ACTION
                                        // =====================================================

                                        // deleteButton.setOnAction(e -> {

                                        //         DeliverySignUpNotifications deliveryPerson = getTableView().getItems()
                                        //                         .get(getIndex());

                                        //         Alert confirmation = new Alert(
                                        //                         Alert.AlertType.CONFIRMATION);

                                        //         confirmation.setTitle("Delete Delivery Person");

                                        //         confirmation.setHeaderText(
                                        //                         "Delete " + deliveryPerson.getName() + "?");

                                        //         confirmation.setContentText(
                                        //                         "This delivery person will be permanently deleted.");

                                        //         if (confirmation.showAndWait()
                                        //                         .orElse(ButtonType.CANCEL) == ButtonType.OK) {

                                        //                 deliveryPersonController.goToDeleteDelivery(
                                        //                                 deliveryPerson.getEmail());
                                        //         }
                                        // });

                                        deleteButton.setOnAction(e -> {

        DeliverySignUpNotifications deliveryPerson =
                        getTableView().getItems().get(getIndex());

        Alert confirmation = new Alert(
                        Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Delete Delivery Person");
        confirmation.setHeaderText(
                        "Delete " + deliveryPerson.getName() + "?");

        confirmation.setContentText(
                        "This delivery person will be permanently deleted.");

        // =====================================================
        // DASHBOARD THEME
        // =====================================================

        confirmation.getDialogPane().setStyle(
                        "-fx-background-color: #151C25;"
        );

        confirmation.getDialogPane().lookup(".header-panel").setStyle(
                        "-fx-background-color: #111821;"
        );

        confirmation.getDialogPane().lookup(".header-panel")
                        .lookup(".label").setStyle(
                                        "-fx-text-fill: #F5F7FA;" +
                                                        "-fx-font-size: 18px;" +
                                                        "-fx-font-weight: bold;"
                        );

        confirmation.getDialogPane().lookup(".content.label").setStyle(
                        "-fx-text-fill: #8A9BB0;" +
                                        "-fx-font-size: 14px;"
        );

        // =====================================================
        // BUTTONS
        // =====================================================

        Button okButton = (Button) confirmation.getDialogPane()
                        .lookupButton(ButtonType.OK);

        Button cancelButton = (Button) confirmation.getDialogPane()
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

        if (confirmation.showAndWait()
                        .orElse(ButtonType.CANCEL) == ButtonType.OK) {

                deliveryPersonController.goToDeleteDelivery(
                                deliveryPerson.getEmail());
        }
});
                                }

                                @Override
                                protected void updateItem(
                                                Void item,
                                                boolean empty) {

                                        super.updateItem(
                                                        item,
                                                        empty);

                                        if (empty) {

                                                setGraphic(null);

                                        } else {

                                                setGraphic(actionBox);
                                        }
                                }
                        };
                });

                // =====================================================
                // ADD COLUMNS
                // =====================================================

                tableView.getColumns().addAll(
                                idColumn,
                                nameColumn,
                                emailColumn,
                                phoneColumn,
                                vehicleColumn,
                                statusColumn,
                                actionColumn);

                // =====================================================
                // FETCH DATA
                // =====================================================

                List<DeliverySignUpNotifications> persons = deliveryPersonController
                                .goToDeliveryPersonData();

                deliveryPersonList = FXCollections.observableArrayList(
                                persons);

                tableView.setItems(
                                deliveryPersonList);

                totalLabel.setText(
                                "Total Delivery Persons : "
                                                + deliveryPersonList.size());

                // =====================================================
                // SEARCH
                // =====================================================

                searchField.textProperty().addListener(
                                (observable, oldValue, newValue) -> {

                                        String searchText = newValue.trim().toLowerCase();

                                        ObservableList<DeliverySignUpNotifications> filteredList = FXCollections
                                                        .observableArrayList();

                                        for (DeliverySignUpNotifications person : deliveryPersonList) {

                                                String name = person.getName() == null
                                                                ? ""
                                                                : person.getName().toLowerCase();

                                                String email = person.getEmail() == null
                                                                ? ""
                                                                : person.getEmail().toLowerCase();

                                                String phone = person.getPhone() == null
                                                                ? ""
                                                                : person.getPhone();

                                                String vehicleNumber = person.getVehicleNumber() == null
                                                                ? ""
                                                                : person.getVehicleNumber().toLowerCase();

                                                String id = person.getId() == null
                                                                ? ""
                                                                : person.getId().toLowerCase();

                                                if (name.contains(searchText)
                                                                || email.contains(searchText)
                                                                || phone.contains(searchText)
                                                                || vehicleNumber.contains(searchText)
                                                                || id.contains(searchText)) {

                                                        filteredList.add(person);
                                                }
                                        }

                                        tableView.setItems(filteredList);
                                });

                // =====================================================
                // ADD TO MAIN CONTAINER
                // =====================================================

                mainContainer.getChildren().addAll(
                                titleBox,
                                searchBox,
                                tableView);

                VBox.setVgrow(
                                tableView,
                                Priority.ALWAYS);

                return mainContainer;
        }

        // =========================================================
        // DELIVERY PERSON DETAILS PAGE
        // =========================================================

        private VBox showDeliveryPersonDetails(
                        DeliverySignUpNotifications person,
                        Runnable back) {

                VBox rootBox = new VBox(0);

                rootBox.setMaxSize(
                                Double.MAX_VALUE,
                                Double.MAX_VALUE);

                rootBox.setStyle(
                                "-fx-background-color: #0B1017;");

                // =====================================================
                // TOP BAR
                // =====================================================

                Button closeButton = new Button("←  Close");

                closeButton.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #36CFC9;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-padding: 8px 15px;" +
                                                "-fx-cursor: hand;");

                closeButton.setOnAction(
                                e -> back.run());

                

                HBox topBar = new HBox();

                topBar.setAlignment(
                                Pos.CENTER_LEFT);

                topBar.setPadding(
                                new Insets(
                                                20,
                                                30,
                                                10,
                                                30));

                topBar.setStyle(
                                "-fx-background-color: #111821;" +
                                                "-fx-border-color: #273442;" +
                                                "-fx-border-width: 0 0 1 0;");

                topBar.getChildren().add(
                                closeButton);

                // =====================================================
                // TITLE
                // =====================================================

                Label title = new Label("Delivery Person Details");

                title.setStyle(
                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #F5F7FA;");

                Label subtitle = new Label(
                                "Complete information about this delivery person");

                subtitle.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #8A9BB0;");

                VBox titleBox = new VBox(5);

                titleBox.getChildren().addAll(
                                title,
                                subtitle);

                // =====================================================
                // PERSONAL INFORMATION
                // =====================================================

                Label personalTitle = new Label("Personal Information");

                personalTitle.setStyle(
                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #F5F7FA;");

                VBox personalCard = new VBox(20);

                personalCard.setPadding(
                                new Insets(25));

                personalCard.setStyle(
                                "-fx-background-color: #151C25;" +
                                                "-fx-border-color: #273442;" +
                                                "-fx-border-radius: 8px;" +
                                                "-fx-background-radius: 8px;");

                GridPane grid = new GridPane();

                grid.setHgap(80);
                grid.setVgap(25);

                grid.add(
                                createDetailBox(
                                                "ID",
                                                person.getId()),
                                0,
                                0);

                grid.add(
                                createDetailBox(
                                                "Name",
                                                person.getName()),
                                1,
                                0);

                grid.add(
                                createDetailBox(
                                                "Email",
                                                person.getEmail()),
                                0,
                                1);

                grid.add(
                                createDetailBox(
                                                "Phone",
                                                person.getPhone()),
                                1,
                                1);

                grid.add(
                                createDetailBox(
                                                "Address",
                                                person.getAddress()),
                                0,
                                2);

                grid.add(
                                createDetailBox(
                                                "Notification Type",
                                                person.getNotiType()),
                                1,
                                2);

                personalCard.getChildren().addAll(
                                personalTitle,
                                grid);

                // =====================================================
                // VEHICLE INFORMATION
                // =====================================================

                Label vehicleTitle = new Label("Vehicle Information");

                vehicleTitle.setStyle(
                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #F5F7FA;");

                VBox vehicleCard = new VBox(20);

                vehicleCard.setPadding(
                                new Insets(25));

                vehicleCard.setStyle(
                                "-fx-background-color: #151C25;" +
                                                "-fx-border-color: #273442;" +
                                                "-fx-border-radius: 8px;" +
                                                "-fx-background-radius: 8px;");

                GridPane vehicleGrid = new GridPane();

                vehicleGrid.setHgap(80);
                vehicleGrid.setVgap(25);

                vehicleGrid.add(
                                createDetailBox(
                                                "Vehicle Type",
                                                person.getVehicleType()),
                                0,
                                0);

                vehicleGrid.add(
                                createDetailBox(
                                                "Vehicle Number",
                                                person.getVehicleNumber()),
                                1,
                                0);

                vehicleCard.getChildren().addAll(
                                vehicleTitle,
                                vehicleGrid);

                // =====================================================
                // ACCOUNT INFORMATION
                // =====================================================

                Label accountTitle = new Label("Account Information");

                accountTitle.setStyle(
                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #F5F7FA;");

                VBox accountCard = new VBox(20);

                accountCard.setPadding(
                                new Insets(25));

                accountCard.setStyle(
                                "-fx-background-color: #151C25;" +
                                                "-fx-border-color: #273442;" +
                                                "-fx-border-radius: 8px;" +
                                                "-fx-background-radius: 8px;");

                GridPane accountGrid = new GridPane();

                accountGrid.setHgap(80);
                accountGrid.setVgap(25);

                accountGrid.add(
                                createDetailBox(
                                                "Approval Status",
                                                person.getApproveStatus()),
                                0,
                                0);

                // accountGrid.add(
                // createDetailBox(
                // "Profile Completed",
                // person.get
                // ),
                // 1,
                // 0
                // );

                accountGrid.add(
                                createDetailBox(
                                                "Created At",
                                                String.valueOf(person.getCreatedAt())),
                                1,
                                0);

                accountCard.getChildren().addAll(
                                accountTitle,
                                accountGrid);

                // =====================================================
                // CONTENT
                // =====================================================

                VBox content = new VBox(25);

                content.setPadding(
                                new Insets(30));

                content.setMaxWidth(
                                Double.MAX_VALUE);

                content.getChildren().addAll(
                                titleBox,
                                personalCard,
                                vehicleCard,
                                accountCard);

                // =====================================================
                // SCROLL
                // =====================================================

                ScrollPane scrollPane = new ScrollPane(content);

                scrollPane.setFitToWidth(true);

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scrollPane.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scrollPane.setStyle(
                                "-fx-background-color: #0B1017;" +
                                                "-fx-background: #0B1017;");

                VBox.setVgrow(
                                scrollPane,
                                Priority.ALWAYS);

                rootBox.getChildren().addAll(
                                topBar,
                                scrollPane);

                return rootBox;
        }

        // =========================================================
        // DETAIL BOX
        // =========================================================

        private VBox createDetailBox(
                        String title,
                        String value) {

                Label titleLabel = new Label(title);

                titleLabel.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #8A9BB0;");

                Label valueLabel = new Label(
                                value == null
                                                ? "-"
                                                : value);

                valueLabel.setStyle(
                                "-fx-font-size: 15px;" +
                                                "-fx-text-fill: #F5F7FA;");

                VBox box = new VBox(5);

                box.getChildren().addAll(
                                titleLabel,
                                valueLabel);

                return box;
        }
}
