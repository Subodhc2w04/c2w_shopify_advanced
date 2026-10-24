package com.c2w.view.admin;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.c2w.config.CloudinaryConfig;
import com.c2w.controller.admin.ProductAddController;
import com.c2w.model.admin.Products;
import com.cloudinary.utils.ObjectUtils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class ProductEditPage {

    private Products product;

    public ProductEditPage(Products product) {
        this.product = product;
    }

    public VBox show(Runnable back) {

        // =========================================
        // MAIN CONTAINER
        // =========================================

        VBox mainContainer =
                new VBox(20);

        mainContainer.setPadding(
                new Insets(25)
        );

        mainContainer.setStyle(
                "-fx-background-color: #0D1117;"
        );

        // =========================================
        // HEADER
        // =========================================

        HBox header =
                new HBox(15);

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        // =========================================
        // BACK BUTTON
        // =========================================

        Button backButton =
                new Button("← Back");

        backButton.setStyle(
                "-fx-background-color: #273445;" +
                "-fx-text-fill: #F5F7FA;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 18;" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;"
        );

        backButton.setOnAction(e -> {

            back.run();

        });

        // =========================================
        // TITLE
        // =========================================

        Label title =
                new Label("Edit Product");

        title.setStyle(
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #F5F7FA;"
        );

        header.getChildren().addAll(
                backButton,
                title
        );

        // =========================================
        // PRODUCT INFORMATION
        // =========================================

        GridPane details =
                new GridPane();

        details.setHgap(30);
        details.setVgap(16);

        details.setPadding(
                new Insets(20)
        );

        details.setStyle(
                "-fx-background-color: #151C26;" +
                "-fx-border-color: #273445;" +
                "-fx-border-width: 1;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;"
        );

        // =========================================
        // PRODUCT ID
        // =========================================

        Label productIdLabel =
                new Label(product.getProductId());

        productIdLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #8FA3B8;"
        );

        addLabel(
                details,
                0,
                "Product ID",
                productIdLabel
        );

        // =========================================
        // PRODUCT NAME
        // =========================================

        TextField productName =
                new TextField(
                        product.getProductName()
                );

        addField(
                details,
                1,
                "Product Name",
                productName
        );

        // =========================================
        // DESCRIPTION
        // =========================================

        TextField productDescription =
                new TextField(
                        product.getProductDescription()
                );

        addField(
                details,
                2,
                "Description",
                productDescription
        );

        // =========================================
        // PRICE
        // =========================================

        TextField productPrice =
                new TextField(
                        String.valueOf(
                                product.getProductPrice()
                        )
                );

        addField(
                details,
                3,
                "Price",
                productPrice
        );

        // =========================================
        // QUANTITY
        // =========================================

        TextField productQuantity =
                new TextField(
                        String.valueOf(
                                product.getProductQuantity()
                        )
                );

        addField(
                details,
                4,
                "Quantity",
                productQuantity
        );

        // =========================================
        // CATEGORY
        // =========================================

        TextField productCategory =
                new TextField(
                        product.getProductCategory()
                );

        addField(
                details,
                5,
                "Category",
                productCategory
        );

        // =========================================
        // PRODUCT IMAGE
        // =========================================

        Label imageLabel =
                new Label("Product Image");

        imageLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #8FA3B8;"
        );

        ImageView productImage =
                new ImageView();
        productImage.setOnMouseClicked(e -> {

    FileChooser fileChooser =
            new FileChooser();

    fileChooser.setTitle(
            "Select Product Image"
    );

    fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                    "Image Files",
                    "*.png",
                    "*.jpg",
                    "*.jpeg",
                    "*.webp"
            )
    );

    File selectedFile =
            fileChooser.showOpenDialog(
                    productImage.getScene().getWindow()
            );

    if (selectedFile != null) {
         try {
        Map<String,Object> image=CloudinaryConfig.cloudinary.uploader().upload(selectedFile,ObjectUtils.emptyMap());
        product.setProductImage((String)image.get("secure_url"));
    } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
        // Show newly selected image
        Image newImage =
                new Image(
                        selectedFile.toURI().toString(),
                        180,
                        180,
                        true,
                        true
                );

        productImage.setImage(newImage);

        System.out.println(
                "New image selected: "
                + product.getProductImage()
        );
    }
});
        try {

            if (product.getProductImage() != null &&
                    !product.getProductImage().isEmpty()) {

                Image image =
                        new Image(
                                product.getProductImage(),
                                180,
                                180,
                                true,
                                true
                        );

                productImage.setImage(image);
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to load product image"
            );
        }

        productImage.setFitWidth(180);
        productImage.setFitHeight(180);
        productImage.setPreserveRatio(true);

        details.add(
                imageLabel,
                0,
                6
        );

        details.add(
                productImage,
                1,
                6
        );
        

        // =========================================
        // COUPON CODE
        // =========================================

        TextField couponCode =
                new TextField(
                        product.getCouponCode()
                );

        addField(
                details,
                7,
                "Coupon Code",
                couponCode
        );

        // =========================================
        // DISCOUNT TYPE
        // =========================================

        TextField discountType =
                new TextField(
                        product.getDiscountType()
                );

        addField(
                details,
                8,
                "Discount Type",
                discountType
        );

        // =========================================
        // DISCOUNT VALUE
        // =========================================

        TextField discountValue =
                new TextField(
                        String.valueOf(
                                product.getDiscountValue()
                        )
                );

        addField(
                details,
                9,
                "Discount Value",
                discountValue
        );

        // =========================================
        // COUPON STATUS
        // =========================================

        Label couponStatusLabel =
                new Label("Coupon Status");

        couponStatusLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #8FA3B8;"
        );

        ComboBox<String> couponStatus = new ComboBox<>();

couponStatus.getItems().addAll(
        "ACTIVE",
        "INACTIVE"
);

// Selected value shown inside ComboBox
couponStatus.setButtonCell(new ListCell<String>() {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
        } else {
            setText(item);
        }

        setTextFill(Color.WHITE);

        setStyle(
                "-fx-background-color: #273445;"
        );
    }
});

// Items shown when dropdown is opened
couponStatus.setCellFactory(listView -> {

    ListCell<String> cell = new ListCell<String>() {

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
            } else {
                setText(item);
            }

            setTextFill(Color.WHITE);

            setStyle(
                    "-fx-background-color: #273445;"
            );
        }
    };

    return cell;
});

// ComboBox itself
couponStatus.setStyle(
        "-fx-background-color: #273445;" +
        "-fx-font-size: 14px;"
);

couponStatus.setPrefWidth(200);

        if (product.isCouponStatus()) {

            couponStatus.setValue("ACTIVE");

        } else {

            couponStatus.setValue("INACTIVE");
        }

        couponStatus.setPrefWidth(200);

        couponStatus.setStyle(
                "-fx-background-color: #273445;" +
                "-fx-text-fill: #F5F7FA;" +
                "-fx-background-radius: 6;"
        );

        details.add(
                couponStatusLabel,
                0,
                10
        );

        details.add(
                couponStatus,
                1,
                10
        );

        // =========================================
        // SAVE BUTTON
        // =========================================

        Button saveButton =
                new Button("Save Changes");

        saveButton.setStyle(
                                "-fx-background-color: teal;" 
                                                 
                                                 +

                                                "-fx-text-fill:ink; " +
                                                 
                                                ";" +

                                                "-fx-font-weight: bold;" +

                                                "-fx-background-radius: 5px;" +

                                                "-fx-cursor: hand;");


        saveButton.setOnAction(e -> {

    try {

        // =====================================
        // UPDATE PRODUCT OBJECT
        // =====================================

        product.setProductName(
                productName.getText()
        );

        product.setProductDescription(
                productDescription.getText()
        );

        product.setProductPrice(
                Double.parseDouble(
                        productPrice.getText()
                )
        );

        product.setProductQuantity(
                Double.parseDouble(
                        productQuantity.getText()
                )
        );

        product.setProductCategory(
                productCategory.getText()
        );

        product.setCouponCode(
                couponCode.getText()
        );

        product.setDiscountType(
                discountType.getText()
        );

        product.setDiscountValue(
                Double.parseDouble(
                        discountValue.getText()
                )
        );

        // =====================================
        // COUPON STATUS
        // =====================================

        if (couponStatus
                .getValue()
                .equals("ACTIVE")) {

            product.setCouponStatus(true);

        } else {

            product.setCouponStatus(false);
        }

        // =====================================
        // UPDATE PRODUCT IN FIREBASE
        // =====================================

        boolean updated =
                new ProductAddController()
                        .updateProductData(product);

        // =====================================
        // SHOW POPUP
        // =====================================

        if (updated) {

            Alert alert =
                    new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Product Updated");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Product updated successfully!"
            );

            alert.showAndWait();

            System.out.println(
                    "Product ID: "
                    + product.getProductId()
            );

            System.out.println(
                    "Coupon Status: "
                    + product.isCouponStatus()
            );

        } else {

            Alert alert =
                    new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Update Failed");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Failed to update product."
            );

            alert.showAndWait();
        }

    } catch (NumberFormatException ex) {

        Alert alert =
                new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(
                "Please enter valid numeric values."
        );

        alert.showAndWait();

    } catch (Exception ex) {

        ex.printStackTrace();

        Alert alert =
                new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(
                "Something went wrong while updating the product."
        );

        alert.showAndWait();
    }
});

        // =========================================
        // BUTTON CONTAINER
        // =========================================

        HBox buttonBox =
                new HBox(10);

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonBox.getChildren().add(
                saveButton
        );

        // =========================================
        // ADD TO MAIN CONTAINER
        // =========================================

        mainContainer.getChildren().addAll(
                header,
                details,
                buttonBox
        );

        return mainContainer;
    }

    // =============================================
    // ADD TEXT FIELD
    // =============================================

    private void addField(
            GridPane grid,
            int row,
            String field,
            TextField textField) {

        Label fieldLabel =
                new Label(field);

        fieldLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #8FA3B8;"
        );

        textField.setPrefWidth(400);

        textField.setStyle(
                "-fx-background-color: #0D1117;" +
                "-fx-text-fill: #F5F7FA;" +
                "-fx-prompt-text-fill: #6B7C8F;" +
                "-fx-border-color: #273445;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;"
        );

        grid.add(
                fieldLabel,
                0,
                row
        );

        grid.add(
                textField,
                1,
                row
        );
    }

    // =============================================
    // ADD LABEL
    // =============================================

    private void addLabel(
            GridPane grid,
            int row,
            String field,
            Label valueLabel) {

        Label fieldLabel =
                new Label(field);

        fieldLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #8FA3B8;"
        );

        grid.add(
                fieldLabel,
                0,
                row
        );

        grid.add(
                valueLabel,
                1,
                row
        );
    }
}
