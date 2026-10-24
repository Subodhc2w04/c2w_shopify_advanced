package com.c2w.view.admin;

import java.util.List;

import com.c2w.controller.admin.ProductAddController;
import com.c2w.model.admin.Products;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AllProducts {

    BorderPane root;

    public VBox buildProductsPanel(BorderPane root) {

        this.root = root;

        VBox mainContainer = new VBox(15);

        mainContainer.setPadding(
                new Insets(20)
        );

        mainContainer.setStyle(
                "-fx-background-color: #0B1017;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #273442;" +
                "-fx-border-radius: 12;"
        );

        // ========================================================
        // TITLE
        // ========================================================

        Label title = new Label(
                "Products"
        );

        title.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #F5F7FA;"
        );

        Label subtitle = new Label(
                "Manage products available in your store."
        );

        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #8A9BB0;"
        );

        TextField searchField = new TextField();

        searchField.setPromptText(
                "Search products..."
        );

        searchField.setPrefHeight(40);

        searchField.setStyle(
                "-fx-background-color: #151C25;" +
                "-fx-text-fill: #F5F7FA;" +
                "-fx-prompt-text-fill: #8A9BB0;" +
                "-fx-border-color: #273442;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 8 12;" +
                "-fx-font-size: 14px;"
        );

        VBox heading = new VBox(5);

        heading.getChildren().addAll(
                title,
                subtitle,
                searchField
        );

        // ========================================================
        // PRODUCT LISTVIEW
        // ========================================================

        ListView<Products> productListView =
                new ListView<>();

        productListView.setStyle(
                "-fx-background-color: #0B1017;" +
                "-fx-control-inner-background: #0B1017;"
        );

        productListView.setPlaceholder(
                new Label(
                        "No products available."
                )
        );

        VBox.setVgrow(
                productListView,
                Priority.ALWAYS
        );

        List<Products> products =
                new ProductAddController().getAllProductsList();

        ObservableList<Products> productData =
                FXCollections.observableArrayList(
                        products
                );

        productListView.getItems().addAll(
                productData
        );

        // ========================================================
        // SEARCH
        // ========================================================

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    String searchText =
                            newValue.toLowerCase().trim();

                    productListView.getItems().clear();

                    for (Products product : productData) {

                        if (product.getProductName()
                                .toLowerCase()
                                .contains(searchText)) {

                            productListView.getItems().add(
                                    product
                            );
                        }
                    }
                }
        );

        // ========================================================
        // PRODUCT CELL
        // ========================================================

        productListView.setCellFactory(
                list -> new ListCell<Products>() {

                    @Override
                    protected void updateItem(
                            Products product,
                            boolean empty) {

                        super.updateItem(
                                product,
                                empty
                        );

                        if (empty || product == null) {

                            setGraphic(null);
                            setText(null);

                            return;
                        }

                        // =================================================
                        // PRODUCT CARD
                        // =================================================

                        VBox card = new VBox(10);

                        card.setPadding(
                                new Insets(15)
                        );

                        card.setStyle(
                                "-fx-background-color: #151C25;" +
                                "-fx-background-radius: 8;" +
                                "-fx-border-color: #273442;" +
                                "-fx-border-radius: 8;"
                        );

                        // =================================================
                        // PRODUCT NAME
                        // =================================================

                        Label productName =
                                new Label(
                                        product.getProductName()
                                );

                        productName.setStyle(
                                "-fx-font-size: 16px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: #F5F7FA;"
                        );

                        // =================================================
                        // PRODUCT ID
                        // =================================================

                        Label productId =
                                new Label(
                                        "Product ID: "
                                        + product.getProductId()
                                );

                        productId.setStyle(
                                "-fx-font-size: 13px;" +
                                "-fx-text-fill: #8A9BB0;"
                        );

                        // =================================================
                        // PRICE
                        // =================================================

                        Label price =
                                new Label(
                                        "Price: ₹"
                                        + product.getProductPrice()
                                );

                        // =================================================
                        // QUANTITY
                        // =================================================

                        Label quantity =
                                new Label(
                                        "Quantity: "
                                        + product.getProductQuantity()
                                );

                        // =================================================
                        // CATEGORY
                        // =================================================

                        Label category =
                                new Label(
                                        "Category: "
                                        + product.getProductCategory()
                                );

                        price.setStyle(
                                "-fx-font-size: 13px;" +
                                "-fx-text-fill: #8A9BB0;"
                        );

                        quantity.setStyle(
                                "-fx-font-size: 13px;" +
                                "-fx-text-fill: #8A9BB0;"
                        );

                        category.setStyle(
                                "-fx-font-size: 13px;" +
                                "-fx-text-fill: #8A9BB0;"
                        );

                        // =================================================
                        // PRODUCT INFORMATION
                        // =================================================

                        HBox information =
                                new HBox(25);

                        information.setAlignment(
                                Pos.CENTER_LEFT
                        );

                        information.getChildren().addAll(
                                price,
                                quantity,
                                category
                        );

                        // =================================================
                        // VIEW BUTTON
                        // =================================================

                        Button viewButton =
                                new Button("View");

                        viewButton.setStyle(
                                "-fx-background-color: #36CFC9;" +
                                "-fx-text-fill: #0B1017;" +
                                "-fx-font-weight: bold;" +
                                "-fx-cursor: hand;" +
                                "-fx-background-radius: 6;" +
                                "-fx-padding: 7 16;"
                        );

                        // =================================================
                        // EDIT BUTTON
                        // =================================================

                        Button editButton =
                                new Button("Edit");

                        editButton.setStyle(
                                "-fx-background-color: #273442;" +
                                "-fx-text-fill: #F5F7FA;" +
                                "-fx-font-weight: bold;" +
                                "-fx-cursor: hand;" +
                                "-fx-background-radius: 6;" +
                                "-fx-padding: 7 16;"
                        );

                        // =================================================
                        // BUTTON ACTIONS
                        // =================================================

                        viewButton.setOnAction(e -> {

                            Runnable back = () ->
                                    root.setCenter(
                                            new AllProducts()
                                                    .buildProductsPanel(root)
                                    );

                            root.setCenter(
                                    new ProductViewPage(product)
                                            .show(back)
                            );

                        });

                        editButton.setOnAction(e -> {

                            Runnable back = () ->
                                    root.setCenter(
                                            new AllProducts()
                                                    .buildProductsPanel(root)
                                    );

                            root.setCenter(
                                    new ProductEditPage(product)
                                            .show(back)
                            );

                        });

                        // =================================================
                        // BUTTON BOX
                        // =================================================

                        HBox buttons =
                                new HBox(10);

                        buttons.setAlignment(
                                Pos.CENTER_LEFT
                        );

                        buttons.getChildren().addAll(
                                viewButton,
                                editButton
                        );

                        // =================================================
                        // ADD EVERYTHING TO CARD
                        // =================================================

                        card.getChildren().addAll(
                                productName,
                                productId,
                                information,
                                buttons
                        );

                        setGraphic(card);

                        setStyle(
                                "-fx-background-color: #0B1017;"
                        );
                    }
                }
        );

        // ========================================================
        // ADD EVERYTHING
        // ========================================================

        mainContainer.getChildren().addAll(
                heading,
                productListView
        );

        return mainContainer;
    }
}