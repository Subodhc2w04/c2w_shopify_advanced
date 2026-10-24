package com.c2w.view.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.c2w.controller.admin.OrderController;
import com.c2w.model.admin.Orders;
import com.c2w.model.admin.ShippingAddress;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class OrdersPage {

    private TableView<Orders> tableView;

    private ObservableList<Orders> orderList;

    private OrderController orderController;
    BorderPane root;

    public VBox getTotalOrdersView(BorderPane root) {
        this.root = root;
        orderController =new OrderController();

        // =====================================================
        // MAIN CONTAINER
        // =====================================================

        VBox mainContainer =
                new VBox(20);

        mainContainer.setPadding(
                new Insets(30)
        );

        mainContainer.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        mainContainer.setStyle(
                "-fx-background-color: #0B1017;"
        );

        // =====================================================
        // TITLE
        // =====================================================

        Label title =
                new Label("Orders");

        title.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #F5F7FA;"
        );

        Label subtitle =
                new Label(
                        "Manage and view all customer orders"
                );

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #8A9BB0;"
        );

        VBox titleBox =
                new VBox(5);

        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        // =====================================================
        // SEARCH
        // =====================================================

        TextField searchField =
                new TextField();

        searchField.setPromptText(
                "Search orders..."
        );

        searchField.setPrefWidth(400);

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

        Label totalLabel =
                new Label();

        totalLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #36CFC9;"
        );

        HBox searchBox =
                new HBox(20);

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        searchBox.getChildren().addAll(
                searchField,
                totalLabel
        );

        // =====================================================
        // TABLE
        // =====================================================

        tableView =
                new TableView<>();

        tableView.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        tableView.setPrefHeight(600);

        tableView.setStyle(
                "-fx-background-color: #151C25;" +
                "-fx-control-inner-background: #151C25;" +
                "-fx-table-cell-border-color: #273442;"
        );

        // =====================================================
        // ORDER ID
        // =====================================================

        TableColumn<Orders, String> orderIdColumn =
                new TableColumn<>("Order ID");

        orderIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("orderId")
        );

        // =====================================================
        // DATE
        // =====================================================

        TableColumn<Orders, String> dateColumn =
                new TableColumn<>("Order Date");

        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("orderDate")
        );

        // =====================================================
        // AMOUNT
        // =====================================================

        TableColumn<Orders, Double> amountColumn =
                new TableColumn<>("Amount");

        amountColumn.setCellValueFactory(
                new PropertyValueFactory<>("finalAmount")
        );

        amountColumn.setCellFactory(column ->
                new TableCell<Orders, Double>() {

                    @Override
                    protected void updateItem(
                            Double amount,
                            boolean empty) {

                        super.updateItem(
                                amount,
                                empty
                        );

                        if (empty ||
                                amount == null) {

                            setText(null);

                        } else {

                            setText(
                                    "₹" +
                                    String.format(
                                            "%.2f",
                                            amount
                                    )
                            );
                        }
                    }
                }
        );

        // =====================================================
        // PAYMENT
        // =====================================================

        TableColumn<Orders, String> paymentColumn =
                new TableColumn<>("Payment");

        paymentColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "paymentMethod"
                )
        );

        // =====================================================
        // STATUS
        // =====================================================

        TableColumn<Orders, String> statusColumn =
                new TableColumn<>("Status");

        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );

        // =====================================================
        // ACTIONS
        // =====================================================

        TableColumn<Orders, Void> actionColumn =
                new TableColumn<>("Actions");

        actionColumn.setCellFactory(column -> {

            return new TableCell<Orders, Void>() {

                private final Button viewButton =
                        new Button("View");

                private final Button readyButton =
                        new Button("Ready");

                private final Button deleteButton =
                        new Button("Delete");

                private final HBox actionBox =
                        new HBox(8);

                {

                    viewButton.setStyle(
                            "-fx-background-color: #36CFC9;" +
                            "-fx-text-fill: #0B1017;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 5px;" +
                            "-fx-cursor: hand;"
                    );

                    readyButton.setStyle(
                            "-fx-background-color: #1E454C;" +
                            "-fx-text-fill: #36CFC9;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 5px;" +
                            "-fx-cursor: hand;"
                    );

                    deleteButton.setStyle(
                            "-fx-background-color: #7A2E35;" +
                            "-fx-text-fill: #FFFFFF;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 5px;" +
                            "-fx-cursor: hand;"
                    );

                    actionBox.setAlignment(
                            Pos.CENTER
                    );

                    actionBox.getChildren().addAll(
                            viewButton,
                            readyButton,
                            deleteButton
                    );

                    // =========================================
                    // VIEW
                    // =========================================

                    viewButton.setOnAction(event -> {
                        Runnable back=()->root.setCenter(new OrdersPage().getTotalOrdersView(root));
                        Orders order =
                                getTableView()
                                        .getItems()
                                        .get(getIndex());

                        root.setCenter(new OrderView().showOrderDetails(
                                order,
                                back
                        ));
                    });

                    // =========================================
                    // READY
                    // =========================================

                    readyButton.setOnAction(event -> {

                        Orders order =
                                getTableView()
                                        .getItems()
                                        .get(getIndex());

                        updateOrderStatus(
                                order
                        );
                    });

                    // =========================================
                    // DELETE
                    // =========================================

                    deleteButton.setOnAction(event -> {

                        Orders order =
                                getTableView()
                                        .getItems()
                                        .get(getIndex());

                        deleteOrder(
                                order
                        );
                    });
                }

                @Override
                protected void updateItem(
                        Void item,
                        boolean empty) {

                    super.updateItem(
                            item,
                            empty
                    );

                    if (empty) {

                        setGraphic(null);

                    } else {

                        setGraphic(actionBox);
                    }
                }
            };
        });

        tableView.getColumns().addAll(
                orderIdColumn,
                dateColumn,
                amountColumn,
                paymentColumn,
                statusColumn,
                actionColumn
        );

        // =====================================================
        // FETCH ORDERS
        // =====================================================

        List<Orders> orders =
                orderController.goToAllOrders();

        orderList =
                FXCollections.observableArrayList(
                        orders
                );

        tableView.setItems(
                orderList
        );

        totalLabel.setText(
                "Total Orders : " +
                orderList.size()
        );

        // =====================================================
        // SEARCH
        // =====================================================

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    String searchText =
                            newValue.toLowerCase();

                    ObservableList<Orders>
                            filteredList =
                            FXCollections.observableArrayList();

                    for (Orders order :
                            orderList) {

                        if (
                                order.getOrderId()
                                        .toLowerCase()
                                        .contains(searchText)

                                || order.getOrderDate()
                                        .toLowerCase()
                                        .contains(searchText)

                                || order.getPaymentMethod()
                                        .toLowerCase()
                                        .contains(searchText)

                                || order.getStatus()
                                        .toLowerCase()
                                        .contains(searchText)
                        ) {

                            filteredList.add(
                                    order
                            );
                        }
                    }

                    tableView.setItems(
                            filteredList
                    );
                }
        );

        mainContainer.getChildren().addAll(
                titleBox,
                searchBox,
                tableView
        );

        VBox.setVgrow(
                tableView,
                Priority.ALWAYS
        );

        return mainContainer;
    }

    // =========================================================
    // UPDATE STATUS
    // =========================================================

    private void updateOrderStatus(
        Orders order) {

    if (
            "ORDER_READY_FOR_DISPATCH"
                    .equals(order.getStatus())) {

        showAlert(
                Alert.AlertType.INFORMATION,
                "Already Updated",
                "This order is already marked as ready for dispatched."
        );

        return;
    }
    else if("ORDER_READY_FOR_DISPATCH".equals(order.getStatus()) ||"ACCEPTED".equals(order.getStatus())||"PICKED_UP".equals(order.getStatus())||"OUT_FOR_DELIVERY".equals(order.getStatus())||"DELIVERED".equals(order.getStatus()) ){
        showAlert(
                Alert.AlertType.INFORMATION,
                "Already Updated",
                "You cannot change the order status now as Dispatched"
        );
    }else{

    Alert confirmation =
            new Alert(
                    Alert.AlertType.CONFIRMATION
            );

    confirmation.setTitle(
            "Update Order"
    );

    confirmation.setHeaderText(
            "Mark order as ready?"
    );

    confirmation.setContentText(
            "Order " +
            order.getOrderId() +
            " will be changed to:\n\n" +
            "ORDER READY FOR DISPATCH"
    );

    // =====================================================
    // DASHBOARD THEME
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
            "-fx-background-color: #36CFC9;" +
            "-fx-text-fill: #0B1017;" +
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

    if (
            confirmation.showAndWait()
                    .orElse(ButtonType.CANCEL)
                    == ButtonType.OK
    ) {

        boolean updated =
                orderController.updateOrderStatus(
                        order.getOrderId(),
                        "ORDER READY FOR DISPATCH"
                );

        if (updated) {

            order.setStatus(
                    "ORDER READY FOR DISPATCH"
            );

            tableView.refresh();

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Order status updated successfully."
            );

        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Unable to update the order status."
            );
        }

    }
}
}

    // =========================================================
    // DELETE ORDER
    // =========================================================

    private void deleteOrder(
        Orders order) {

    Alert confirmation =
            new Alert(
                    Alert.AlertType.CONFIRMATION
            );

    confirmation.setTitle(
            "Delete Order"
    );

    confirmation.setHeaderText(
            "Delete " +
            order.getOrderId() +
            "?"
    );

    confirmation.setContentText(
            "This order will be permanently deleted."
    );

    // =====================================================
    // DASHBOARD THEME
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

    if (
            confirmation.showAndWait()
                    .orElse(ButtonType.CANCEL)
                    == ButtonType.OK
    ) {

        boolean deleted =
                orderController.deleteOrder(
                        order.getOrderId()
                );

        if (deleted) {

            orderList.remove(
                    order
            );

            tableView.setItems(
                    orderList
            );

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Order deleted successfully."
            );

        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Unable to delete the order."
            );
        }
    }
}
    // =========================================================
    // ALERT
    // =========================================================

    private void showAlert(
        Alert.AlertType alertType,
        String title,
        String message) {

    Alert alert = new Alert(alertType);

    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);

    // =====================================================
    // DASHBOARD THEME
    // =====================================================

    alert.getDialogPane().setStyle(
            "-fx-background-color: #151C25;"
    );

    // =====================================================
    // CONTENT TEXT
    // =====================================================

    alert.getDialogPane()
            .lookup(".content.label")
            .setStyle(
                    "-fx-text-fill: #F5F7FA;" +
                    "-fx-font-size: 14px;"
            );

    // =====================================================
    // OK BUTTON
    // =====================================================

    Button okButton =
            (Button) alert.getDialogPane()
                    .lookupButton(ButtonType.OK);

    if (alertType == Alert.AlertType.ERROR) {

        okButton.setStyle(
                "-fx-background-color: #D64545;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5px;" +
                "-fx-padding: 8px 18px;" +
                "-fx-cursor: hand;"
        );

    } else {

        okButton.setStyle(
                "-fx-background-color: #36CFC9;" +
                "-fx-text-fill: #0B1017;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5px;" +
                "-fx-padding: 8px 18px;" +
                "-fx-cursor: hand;"
        );
    }

    alert.showAndWait();
}
}
