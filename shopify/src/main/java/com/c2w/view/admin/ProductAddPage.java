package com.c2w.view.admin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.c2w.config.CloudinaryConfig;
import com.c2w.controller.admin.ProductAddController;
import com.c2w.model.admin.Products;
import com.cloudinary.utils.ObjectUtils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class ProductAddPage {
    private String url;
    public VBox getProductAddPage(Runnable runnable) {

        // ================= BACK BUTTON =================

        Button backButton = new Button("Back");

        backButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #8e9aaa;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;"
        );

        backButton.setOnAction(e -> {
            runnable.run();
        });


        // ================= HEADING =================

        Label title = text("Add product", 28, "#f2f5f7");

        title.setStyle(
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #f2f5f7;" +
                "-fx-font-size: 28px;"
        );

        Label intro = text(
                "Create a new item for your store catalogue.",
                13,
                "#8e9aaa"
        );


        // ================= PRODUCT NAME =================

        Label nameLabel = text(
                "Product name",
                12,
                "#c2ccd6"
        );

        TextField nameField = new TextField();
        nameField.setPromptText("Enter product name");


        // ================= PRICE =================

        Label priceLabel = text(
                "Price",
                12,
                "#c2ccd6"
        );

        TextField priceField = new TextField();
        priceField.setPromptText("0.00");


        // ================= QUANTITY =================

        Label quantityLabel = text(
                "Quantity",
                12,
                "#c2ccd6"
        );

        TextField quantityField = new TextField();
        quantityField.setPromptText("0");


        // ================= CATEGORY =================

        Label categoryLabel = text(
                "Category",
                12,
                "#c2ccd6"
        );

        ComboBox<String> categoryBox = new ComboBox<>();

        categoryBox.getItems().addAll(
                "Electronics",
                "Clothing",
                "Books",
                "Shoes",
                "Accessories"
        );

        categoryBox.setPromptText("Select category");


        // ================= DESCRIPTION =================

        Label descriptionLabel = text(
                "Description",
                12,
                "#c2ccd6"
        );

        TextArea descriptionArea = new TextArea();

        descriptionArea.setPromptText(
                "Enter product description"
        );

        descriptionArea.setPrefRowCount(4);


        // ================= IMAGE =================

        Label imageLabel = text(
                "Product image",
                12,
                "#c2ccd6"
        );

        Button chooseImageButton = new Button(
                "Choose image"
        );

        chooseImageButton.setStyle(
                "-fx-background-color: #293444;" +
                "-fx-text-fill: #c2ccd6;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5px;" +
                "-fx-padding: 9px 16px;" +
                "-fx-cursor: hand;"
        );

        Label imageNameLabel = text(
                "No image selected",
                11,
                "#8e9aaa"
        );

        ImageView imageView = new ImageView();

        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        chooseImageButton.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Select Product Image");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Image Files",
                            "*.png",
                            "*.jpg",
                            "*.jpeg",
                            "*.webp"
                    )
            );

            File file = fileChooser.showOpenDialog(
                    chooseImageButton.getScene().getWindow()
            );
            

            if (file != null) {

                Image image = new Image(
                        file.toURI().toString()
                );

                imageView.setImage(image);

                imageNameLabel.setText(
                        file.getName()
                );
            }
            try {
                 Map<String,Object> data=CloudinaryConfig.getCloudinaryInstance().uploader().upload(file, ObjectUtils.emptyMap());
                    url=data.get("secure_url").toString();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        VBox imageBox = new VBox(
                8,
                chooseImageButton,
                imageNameLabel,
                imageView
        );

        imageBox.setAlignment(Pos.CENTER_LEFT);


        // ================= COUPON CODE =================

        Label couponLabel = text(
                "Coupon code",
                12,
                "#c2ccd6"
        );

        TextField couponField = new TextField();

        couponField.setPromptText(
                "Example: C2W20"
        );


        // ================= DISCOUNT TYPE =================

        Label discountTypeLabel = text(
                "Discount type",
                12,
                "#c2ccd6"
        );

        ComboBox<String> discountTypeBox =
                new ComboBox<>();

        discountTypeBox.getItems().addAll(
                "Percentage",
                "Fixed Amount"
        );

        discountTypeBox.setPromptText(
                "Select discount type"
        );


        // ================= DISCOUNT VALUE =================

        Label discountValueLabel = text(
                "Discount value",
                12,
                "#c2ccd6"
        );

        TextField discountValueField = new TextField();

        discountValueField.setPromptText(
                "Example: 20"
        );


        // ================= COUPON STATUS =================

        Label couponStatusLabel = text(
                "Coupon status",
                12,
                "#c2ccd6"
        );

        CheckBox couponEnabled = new CheckBox(
                "Enable coupon"
        );

        couponEnabled.setStyle(
                "-fx-text-fill: #c2ccd6;" +
                "-fx-font-size: 12px;"
        );


        // ================= BUTTONS =================

        Button addButton = new Button(
                "Save product"
        );

        addButton.setStyle(
                "-fx-background-color: #48d1c8;" +
                "-fx-text-fill: #0d1117;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5px;" +
                "-fx-padding: 11px 22px;" +
                "-fx-cursor: hand;"
        );


        Button clearButton = new Button(
                "Clear form"
        );

        clearButton.setStyle(
                "-fx-background-color: #293444;" +
                "-fx-text-fill: #c2ccd6;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5px;" +
                "-fx-padding: 11px 22px;" +
                "-fx-cursor: hand;"
        );


        Label messageLabel = text(
                "",
                12,
                "#48d1c8"
        );


        // ================= SAVE PRODUCT =================

        addButton.setOnAction(e -> {

            String name = nameField.getText();
            Double price = Double.parseDouble(priceField.getText());
            String quantity = quantityField.getText();
            String category = categoryBox.getValue();
            String description = descriptionArea.getText();

            String couponCode = couponField.getText();
            String discountType = discountTypeBox.getValue();
            Double discountValue = Double.parseDouble(discountValueField.getText());

            // -------- BASIC VALIDATION --------

            if (name.isEmpty()
                    || price == null
                    || quantity.isEmpty()
                    || category == null
                    || description.isEmpty()) {

                messageLabel.setStyle(
                        "-fx-text-fill: #ee7d83;" +
                        "-fx-font-size: 12px;"
                );

                messageLabel.setText(
                        "Please complete every field before saving."
                );

                return;
            }


            // -------- COUPON VALIDATION --------

            if (couponEnabled.isSelected()) {

                if (couponCode.isEmpty()
                        || discountType == null
                        || discountValue== null) {

                    messageLabel.setStyle(
                            "-fx-text-fill: #ee7d83;" +
                            "-fx-font-size: 12px;"
                    );

                    messageLabel.setText(
                            "Please complete all coupon details."
                    );

                    return;
                }
            }


            // -------- PRICE CALCULATION --------

            try {

                double productPrice =
                       price;

                double discount =
                        discountValue;

                double finalPrice =
                        productPrice;


                if (couponEnabled.isSelected()) {

                    if (discountType.equals(
                            "Percentage")) {

                        if (discount < 0 || discount > 100) {

                            messageLabel.setStyle(
                                    "-fx-text-fill: #ee7d83;" +
                                    "-fx-font-size: 12px;"
                            );

                            messageLabel.setText(
                                    "Percentage discount must be between 0 and 100."
                            );

                            return;
                        }

                        finalPrice =
                                productPrice
                                - (productPrice * discount / 100);

                    } else {

                        if (discount < 0
                                || discount > productPrice) {

                            messageLabel.setStyle(
                                    "-fx-text-fill: #ee7d83;" +
                                    "-fx-font-size: 12px;"
                            );

                            messageLabel.setText(
                                    "Fixed discount cannot exceed product price."
                            );

                            return;
                        }

                        finalPrice =
                                productPrice - discount;
                    }
                }


                // -------- SUCCESS --------

                messageLabel.setStyle(
                        "-fx-text-fill: #48d1c8;" +
                        "-fx-font-size: 12px;"
                );
                Products product = new Products();
                product.setProductName(name);
                product.setProductPrice(price);
                product.setProductQuantity(Double.parseDouble(quantity));
                product.setProductCategory(category);
                product.setProductDescription(description);
                product.setCouponCode(couponCode);
                product.setDiscountType(discountType);
                product.setDiscountValue(discountValue);
                product.setCouponStatus(couponEnabled.isSelected());
                product.setProductImage(url);

                List<Products> products = new ArrayList<>();
                products.add(product);
                new ProductAddController().addProduct(product);


                messageLabel.setText(
                        "Product added successfully!"
                );


                System.out.println(
                        "Product Name : " + name
                );

                System.out.println(
                        "Price        : ₹" + productPrice
                );

                System.out.println(
                        "Quantity     : " + quantity
                );

                System.out.println(
                        "Category     : " + category
                );

                System.out.println(
                        "Description  : " + description
                );

                System.out.println(
                        "Coupon Code  : " + couponCode
                );

                System.out.println(
                        "Discount Type: " + discountType
                );

                System.out.println(
                        "Discount     : " + discount
                );

                System.out.println(
                        "Final Price  : ₹" + finalPrice
                );

            } catch (NumberFormatException ex) {

                messageLabel.setStyle(
                        "-fx-text-fill: #ee7d83;" +
                        "-fx-font-size: 12px;"
                );

                messageLabel.setText(
                        "Price, quantity and discount must contain valid numbers."
                );
            }
        });


        // ================= CLEAR =================

        clearButton.setOnAction(e -> {

            nameField.clear();
            priceField.clear();
            quantityField.clear();
            categoryBox.setValue(null);
            descriptionArea.clear();

            couponField.clear();
            discountTypeBox.setValue(null);
            discountValueField.clear();

            couponEnabled.setSelected(false);

            imageView.setImage(null);
            imageNameLabel.setText(
                    "No image selected"
            );

            messageLabel.setText("");
        });


        // ================= GRID =================

        GridPane grid = new GridPane();

        grid.setHgap(18);
        grid.setVgap(12);

        grid.setPadding(
                new Insets(24)
        );

        grid.setStyle(
                "-fx-background-color: #151b24;" +
                "-fx-border-color: #293444;" +
                "-fx-border-radius: 7px;" +
                "-fx-background-radius: 7px;"
        );

        grid.getColumnConstraints().addAll(
                new ColumnConstraints(170),
                new ColumnConstraints(390)
        );


        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(priceLabel, 0, 1);
        grid.add(priceField, 1, 1);

        grid.add(quantityLabel, 0, 2);
        grid.add(quantityField, 1, 2);

        grid.add(categoryLabel, 0, 3);
        grid.add(categoryBox, 1, 3);

        grid.add(descriptionLabel, 0, 4);
        grid.add(descriptionArea, 1, 4);

        grid.add(imageLabel, 0, 5);
        grid.add(imageBox, 1, 5);

        grid.add(couponLabel, 0, 6);
        grid.add(couponField, 1, 6);

        grid.add(discountTypeLabel, 0, 7);
        grid.add(discountTypeBox, 1, 7);

        grid.add(discountValueLabel, 0, 8);
        grid.add(discountValueField, 1, 8);

        grid.add(couponStatusLabel, 0, 9);
        grid.add(couponEnabled, 1, 9);


        HBox actions = new HBox(
                12,
                addButton,
                clearButton
        );

        actions.setAlignment(
                Pos.CENTER_LEFT
        );

        grid.add(
                actions,
                1,
                10
        );


        // ================= PAGE LAYOUT =================

        HBox topBar = new HBox(
                backButton
        );

        topBar.setPadding(
                new Insets(24, 42, 0, 42)
        );


        VBox heading = new VBox(
                6,
                title,
                intro
        );

        heading.setPadding(
                new Insets(18, 0, 24, 42)
        );


        VBox body = new VBox(
                heading,
                grid,
                messageLabel
        );

        body.setPadding(
                new Insets(0, 42, 42, 42)
        );

        body.setSpacing(0);

        VBox.setVgrow(
                grid,
                Priority.ALWAYS
        );


        return body;
    }


    private Label text(
            String value,
            int size,
            String color) {

        Label label = new Label(value);

        label.setStyle(
                "-fx-text-fill: " + color +
                "; -fx-font-size: " +
                size + "px;"
        );

        return label;
    }
}