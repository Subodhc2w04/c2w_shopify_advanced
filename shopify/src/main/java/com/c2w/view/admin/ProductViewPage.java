package com.c2w.view.admin;

import com.c2w.model.admin.Products;

import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProductViewPage {

    private Products product;

    public ProductViewPage(Products product) {
        this.product = product;
    }

    public VBox show(Runnable back) {

        System.out.println("In ViewPage");

        // =========================================
        // MAIN CONTAINER
        // =========================================

        VBox mainContainer = new VBox(20);

        mainContainer.setPadding(
                new Insets(25)
        );

        mainContainer.setStyle(
                "-fx-background-color: #0B1017;"
        );

        // =========================================
        // HEADER
        // =========================================

        HBox header = new HBox(15);

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        // =========================================
        // BACK BUTTON
        // =========================================

        Button backButton = new Button("← Back");

        backButton.setStyle(
                "-fx-background-color: #273442;" +
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

        Label title = new Label(
                "Product Details"
        );

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

        GridPane details = new GridPane();

        details.setHgap(30);
        details.setVgap(16);

        details.setPadding(
                new Insets(15)
        );

        details.setStyle(
                "-fx-background-color: #151C25;" +
                "-fx-border-color: #273442;" +
                "-fx-border-width: 1;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;"
        );

        // =========================================
        // PRODUCT ID
        // =========================================

        addRow(
                details,
                0,
                "Product ID",
                product.getProductId()
        );

        // =========================================
        // PRODUCT NAME
        // =========================================

        addRow(
                details,
                1,
                "Product Name",
                product.getProductName()
        );

        // =========================================
        // DESCRIPTION
        // =========================================

        addRow(
                details,
                2,
                "Description",
                product.getProductDescription()
        );

        // =========================================
        // PRICE
        // =========================================

        addRow(
                details,
                3,
                "Price",
                "₹" + product.getProductPrice()
        );

        // =========================================
        // QUANTITY
        // =========================================

        addRow(
                details,
                4,
                "Quantity",
                String.valueOf(
                        product.getProductQuantity()
                )
        );

        // =========================================
        // CATEGORY
        // =========================================

        addRow(
                details,
                5,
                "Category",
                product.getProductCategory()
        );

        // =========================================
        // PRODUCT IMAGE
        // =========================================

        Label imageLabel = new Label(
                "Product Image"
        );

        imageLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #8A9BB0;"
        );

        ImageView productImage =
                new ImageView();

        try {

            if (product.getProductImage() != null &&
                    !product.getProductImage().isEmpty()) {

                Image image = new Image(
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

        VBox imageContainer =
                new VBox(8);

        imageContainer.getChildren().add(
                productImage
        );

        details.add(
                imageLabel,
                0,
                6
        );

        details.add(
                imageContainer,
                1,
                6
        );

        // =========================================
        // COUPON CODE
        // =========================================

        addRow(
                details,
                7,
                "Coupon Code",
                product.getCouponCode()
        );

        // =========================================
        // DISCOUNT TYPE
        // =========================================

        addRow(
                details,
                8,
                "Discount Type",
                product.getDiscountType()
        );

        // =========================================
        // DISCOUNT VALUE
        // =========================================

        addRow(
                details,
                9,
                "Discount Value",
                String.valueOf(
                        product.getDiscountValue()
                )
        );

        // =========================================
        // COUPON STATUS
        // =========================================

        Label statusLabel = new Label(
                product.isCouponStatus()
                        ? "ACTIVE"
                        : "INACTIVE"
        );

        if (product.isCouponStatus()) {

            statusLabel.setStyle(
                    "-fx-background-color: #21494E;" +
                    "-fx-text-fill: #36CFC9;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 5 12;" +
                    "-fx-background-radius: 5;"
            );

        } else {

            statusLabel.setStyle(
                    "-fx-background-color: #273442;" +
                    "-fx-text-fill: #8A9BB0;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 5 12;" +
                    "-fx-background-radius: 5;"
            );
        }

        Label couponStatusLabel =
                new Label("Coupon Status");

        couponStatusLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #8A9BB0;"
        );

        details.add(
                couponStatusLabel,
                0,
                10
        );

        details.add(
                statusLabel,
                1,
                10
        );


        // =========================================
        // REVIEWS SECTION
        // =========================================

        VBox reviewsSection =
                createReviewsSection();


        // =========================================
        // ADD TO MAIN CONTAINER
        // =========================================

        mainContainer.getChildren().addAll(
                header,
                details,
                reviewsSection
        );

        return mainContainer;
    }


    // =============================================
    // REVIEWS SECTION
    // =============================================

    private VBox createReviewsSection() {

        VBox reviewsSection =
                new VBox(15);

        reviewsSection.setPadding(
                new Insets(20)
        );

        reviewsSection.setStyle(
                "-fx-background-color: #151C25;" +
                "-fx-border-color: #273442;" +
                "-fx-border-width: 1;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;"
        );


        // =========================================
        // SECTION TITLE
        // =========================================

        Label reviewTitle =
                new Label(
                        "Reviews & Ratings"
                );

        reviewTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #F5F7FA;"
        );

        reviewsSection.getChildren().add(
                reviewTitle
        );


        // =========================================
        // GET REVIEWS
        // =========================================

        List<Map<String, Object>> reviewList =
                product.getReviews();


        // =========================================
        // NO REVIEWS
        // =========================================

        if (reviewList == null ||
                reviewList.isEmpty()) {

            Label noReviews =
                    new Label(
                            "No reviews yet."
                    );

            noReviews.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-text-fill: #8A9BB0;"
            );

            reviewsSection.getChildren().add(
                    noReviews
            );

            return reviewsSection;
        }


        // =========================================
        // DISPLAY REVIEWS
        // =========================================

        for (Map<String, Object> review :
                reviewList) {

            VBox reviewCard =
                    createReviewCard(review);

            reviewsSection.getChildren().add(
                    reviewCard
            );
        }

        return reviewsSection;
    }


    // =============================================
    // REVIEW CARD
    // =============================================

    private VBox createReviewCard(
            Map<String, Object> review) {

        VBox reviewCard =
                new VBox(8);

        reviewCard.setPadding(
                new Insets(15)
        );

        reviewCard.setStyle(
                "-fx-background-color: #1C2430;" +
                "-fx-border-color: #293444;" +
                "-fx-border-width: 1;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;"
        );


        // =========================================
        // EMAIL
        // =========================================

        String email =
                review.get("email") == null
                        ? "-"
                        : String.valueOf(
                                review.get("email")
                        );


        Label emailLabel =
                new Label(
                        "By: " + email
                );

        emailLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #8A9BB0;"
        );


        // =========================================
        // RATING
        // =========================================

        String ratingText =
                review.get("ratings") == null
                        ? "0"
                        : String.valueOf(
                                review.get("ratings")
                        );


        Label ratingLabel =
                new Label(
                        "★ " + ratingText + " / 5"
                );

        ratingLabel.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #F2B866;"
        );


        // =========================================
        // REVIEW
        // =========================================

        String reviewText =
                review.get("review") == null
                        ? "-"
                        : String.valueOf(
                                review.get("review")
                        );


        Label reviewLabel =
                new Label(
                        reviewText
                );

        reviewLabel.setWrapText(true);

        reviewLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #F5F7FA;"
        );


        // =========================================
        // ADD
        // =========================================

        reviewCard.getChildren().addAll(
                emailLabel,
                ratingLabel,
                reviewLabel
        );

        return reviewCard;
    }


    // =============================================
    // ADD ROW
    // =============================================

    private void addRow(
            GridPane grid,
            int row,
            String field,
            String value) {

        Label fieldLabel =
                new Label(field);

        fieldLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #8A9BB0;"
        );

        Label valueLabel =
                new Label(
                        value == null
                                ? "-"
                                : value
                );

        valueLabel.setWrapText(true);

        valueLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #F5F7FA;"
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