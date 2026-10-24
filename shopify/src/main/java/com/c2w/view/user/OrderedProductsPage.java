package com.c2w.view.user;

import java.util.List;
import java.util.Map;

import com.c2w.controller.user.OrderControllerUser;
import com.c2w.model.user.AddToCart;
import com.c2w.view.authentication.AuthPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OrderedProductsPage {

    // ==========================================
    // THEME COLORS
    // ==========================================

    private static final String INK = "#0d1117";
    private static final String PANEL = "#151b24";
    private static final String PANEL_LIGHT = "#1c2430";
    private static final String BORDER = "#293444";
    private static final String TEXT = "#f2f5f7";
    private static final String MUTED = "#8e9aaa";
    private static final String TEAL = "#48d1c8";
    private static final String GOLD = "#f2b866";

    // ==========================================
    // ORDER PAGE VIEW
    // ==========================================

    public Scene getOrderPageView(
            List<AddToCart> product,
            Runnable backAction
    ) {

        // ==========================================
        // ROOT
        // ==========================================

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: " + INK + ";"
        );

        // ==========================================
        // BACK BUTTON
        // ==========================================

        Button backButton = new Button(
                "← Back"
        );

        backButton.setStyle(
                "-fx-background-color: " + PANEL_LIGHT + ";" +
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8px;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 8px;" +
                "-fx-cursor: hand;"
        );

        backButton.setOnAction(e -> {

            if (backAction != null) {

                backAction.run();
            }
        });

        // ==========================================
        // TOP BAR
        // ==========================================

        HBox topBar = new HBox(
                20
        );

        topBar.setAlignment(
                Pos.CENTER_LEFT
        );

        topBar.setPadding(
                new Insets(20, 30, 20, 30)
        );

        Label title = new Label(
                "Ordered Products"
        );

        title.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;"
        );

        topBar.getChildren().addAll(
                backButton,
                title
        );

        root.setTop(
                topBar
        );

        // ==========================================
        // MAIN CONTENT
        // ==========================================

        VBox content = new VBox(
                25
        );

        content.setPadding(
                new Insets(10, 30, 30, 30)
        );

        // ==========================================
        // CHECK EMPTY LIST
        // ==========================================

        if (product == null || product.isEmpty()) {

            Label emptyLabel = new Label(
                    "No products found."
            );

            emptyLabel.setStyle(
                    "-fx-text-fill: " + MUTED + ";" +
                    "-fx-font-size: 18px;"
            );

            content.setAlignment(
                    Pos.CENTER
            );

            content.getChildren().add(
                    emptyLabel
            );

        } else {

            // ==========================================
            // DISPLAY ALL PRODUCTS
            // ==========================================

            for (AddToCart item : product) {

                createProductSection(
                        content,
                        item
                );
            }
        }

        // ==========================================
        // SCROLL PANE
        // ==========================================

        ScrollPane scrollPane = new ScrollPane(
                content
        );

        scrollPane.setFitToWidth(
                true
        );

        scrollPane.setStyle(
                "-fx-background: " + INK + ";" +
                "-fx-background-color: " + INK + ";"
        );

        root.setCenter(
                scrollPane
        );

        // ==========================================
        // SCENE
        // ==========================================

        Scene scene = new Scene(
                root,
                1550,
                850
        );

        return scene;
    }

    // ==========================================
    // CREATE PRODUCT SECTION
    // ==========================================

    private void createProductSection(
            VBox content,
            AddToCart item
    ) {

        // ==========================================
        // PRODUCT CARD
        // ==========================================

        HBox productCard = new HBox(
                30
        );

        productCard.setPadding(
                new Insets(25)
        );

        productCard.setAlignment(
                Pos.CENTER_LEFT
        );

        productCard.setStyle(
                "-fx-background-color: " + PANEL + ";" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 14px;" +
                "-fx-background-radius: 14px;"
        );

        // ==========================================
        // PRODUCT IMAGE
        // ==========================================

        ImageView productImage = new ImageView();

        productImage.setFitWidth(
                250
        );

        productImage.setFitHeight(
                250
        );

        productImage.setPreserveRatio(
                true
        );

        try {

            if (item.getProductImg() != null
                    && !item.getProductImg().isEmpty()) {

                Image image = new Image(
                        item.getProductImg(),
                        250,
                        250,
                        true,
                        true
                );

                productImage.setImage(
                        image
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to load product image."
            );
        }

        VBox imageBox = new VBox();

        imageBox.setAlignment(
                Pos.CENTER
        );

        imageBox.setMinWidth(
                270
        );

        imageBox.getChildren().add(
                productImage
        );

        // ==========================================
        // PRODUCT INFORMATION
        // ==========================================

        VBox productDetails = new VBox(
                15
        );

        productDetails.setAlignment(
                Pos.CENTER_LEFT
        );

        Label productName = new Label(
                item.getProductName()
        );

        productName.setWrapText(
                true
        );

        productName.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;"
        );

        Label productIdLabel = new Label(
                "Product ID: " +
                item.getProductId()
        );

        productIdLabel.setStyle(
                "-fx-text-fill: " + MUTED + ";" +
                "-fx-font-size: 14px;"
        );

        Label price = new Label(
                "₹ " +
                item.getProductPrice()
        );

        price.setStyle(
                "-fx-text-fill: " + TEAL + ";" +
                "-fx-font-size: 25px;" +
                "-fx-font-weight: bold;"
        );

        Label quantity = new Label(
                "Quantity: " +
                item.getQuantity()
        );

        quantity.setStyle(
                "-fx-text-fill: " + MUTED + ";" +
                "-fx-font-size: 14px;"
        );

        Separator separator = new Separator();

        separator.setStyle(
                "-fx-background-color: " + BORDER + ";"
        );

        Label ratingInfo = new Label(
                "Customer Reviews & Ratings"
        );

        ratingInfo.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;"
        );

        productDetails.getChildren().addAll(
                productName,
                productIdLabel,
                price,
                quantity,
                separator,
                ratingInfo
        );

        productCard.getChildren().addAll(
                imageBox,
                productDetails
        );

        // ==========================================
        // WRITE REVIEW CARD
        // ==========================================

        VBox reviewCard = new VBox(
                18
        );

        reviewCard.setPadding(
                new Insets(25)
        );

        reviewCard.setStyle(
                "-fx-background-color: " + PANEL + ";" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 14px;" +
                "-fx-background-radius: 14px;"
        );

        Label reviewTitle = new Label(
                "Write a Review"
        );

        reviewTitle.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 21px;" +
                "-fx-font-weight: bold;"
        );

        // ==========================================
        // RATING LABEL
        // ==========================================

        Label ratingLabel = new Label(
                "Your Rating"
        );

        ratingLabel.setStyle(
                "-fx-text-fill: " + MUTED + ";" +
                "-fx-font-size: 14px;"
        );

        // ==========================================
        // STAR BUTTONS
        // ==========================================

        HBox stars = new HBox(
                8
        );

        Button[] starButtons = new Button[5];

        final int[] selectedRating = {0};

        for (int i = 0; i < 5; i++) {

            final int rating = i + 1;

            Button star = new Button(
                    "★"
            );

            starButtons[i] = star;

            setStarStyle(
                    star,
                    false
            );

            star.setOnAction(e -> {

                selectedRating[0] = rating;

                for (int j = 0; j < 5; j++) {

                    setStarStyle(
                            starButtons[j],
                            j < selectedRating[0]
                    );
                }
            });

            stars.getChildren().add(
                    star
            );
        }

        // ==========================================
        // REVIEW TEXT
        // ==========================================

        Label reviewLabel = new Label(
                "Your Review"
        );

        reviewLabel.setStyle(
                "-fx-text-fill: " + MUTED + ";" +
                "-fx-font-size: 14px;"
        );

        TextArea reviewText = new TextArea();

        reviewText.setPromptText(
                "Write your experience with this product..."
        );

        reviewText.setWrapText(
                true
        );

        reviewText.setPrefHeight(
                120
        );

        reviewText.setStyle(
                "-fx-control-inner-background: " + PANEL_LIGHT + ";" +
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-prompt-text-fill: " + MUTED + ";" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 8px;" +
                "-fx-background-radius: 8px;"
        );

        // ==========================================
        // EXISTING REVIEWS LIST
        // ==========================================

        ListView<VBox> reviewListView =
                new ListView<>();

        reviewListView.setPrefHeight(
                300
        );

        reviewListView.setStyle(
                "-fx-background-color: " + PANEL_LIGHT + ";" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 10px;" +
                "-fx-background-radius: 10px;"
        );

        // ==========================================
        // LOAD EXISTING REVIEWS
        // ==========================================

        loadReviews(
                reviewListView,
                item.getProductId()
        );

        // ==========================================
        // SUBMIT BUTTON
        // ==========================================

        Button submitButton = new Button(
                "Submit Review"
        );

        submitButton.setPrefHeight(
                42
        );

        submitButton.setPrefWidth(
                160
        );

        submitButton.setStyle(
                "-fx-background-color: " + TEAL + ";" +
                "-fx-text-fill: " + INK + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8px;" +
                "-fx-cursor: hand;"
        );

        submitButton.setOnAction(e -> {

            String review =
                    reviewText.getText().trim();

            // ==========================================
            // VALIDATE RATING
            // ==========================================

            if (selectedRating[0] == 0) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Review",
                        "Please select a rating."
                );

                return;
            }

            // ==========================================
            // VALIDATE REVIEW
            // ==========================================

            if (review.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Review",
                        "Please write a review."
                );

                return;
            }

            // ==========================================
            // GET PRODUCT ID
            // ==========================================

            String productId =
                    item.getProductId();

            // ==========================================
            // UPDATE RATING
            // ==========================================

            new OrderControllerUser()
                    .getUpdateRatings(
                            productId,
                            Map.of(
                                    "email",
                                    AuthPage.email,

                                    "review",
                                    review,

                                    "ratings",
                                    selectedRating[0]
                            )
                    );

            // ==========================================
            // PRINT DETAILS
            // ==========================================

            System.out.println(
                    "Product ID : " +
                    productId
            );

            System.out.println(
                    "Product : " +
                    item.getProductName()
            );

            System.out.println(
                    "Rating : " +
                    selectedRating[0]
            );

            System.out.println(
                    "Review : " +
                    review
            );

            // ==========================================
            // ADD NEW REVIEW TO LISTVIEW
            // ==========================================

            VBox newReview = createReviewBox(
                    AuthPage.email,
                    selectedRating[0],
                    review
            );

            reviewListView.getItems().add(
                    newReview
            );

            // ==========================================
            // CLEAR FORM
            // ==========================================

            reviewText.clear();

            selectedRating[0] = 0;

            for (Button starButton : starButtons) {

                setStarStyle(
                        starButton,
                        false
                );
            }

            // ==========================================
            // SUCCESS ALERT
            // ==========================================

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Review Submitted",
                    "Thank you! Your review has been submitted."
            );
        });

        HBox buttonBox = new HBox(
                submitButton
        );

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        reviewCard.getChildren().addAll(
                reviewTitle,
                ratingLabel,
                stars,
                reviewLabel,
                reviewText,
                buttonBox
        );

        // ==========================================
        // EXISTING REVIEWS CARD
        // ==========================================

        VBox existingReviews = new VBox(
                15
        );

        existingReviews.setPadding(
                new Insets(25)
        );

        existingReviews.setStyle(
                "-fx-background-color: " + PANEL + ";" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 14px;" +
                "-fx-background-radius: 14px;"
        );

        Label existingTitle = new Label(
                "Customer Reviews"
        );

        existingTitle.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 21px;" +
                "-fx-font-weight: bold;"
        );

        existingReviews.getChildren().addAll(
                existingTitle,
                reviewListView
        );

        // ==========================================
        // ADD EVERYTHING
        // ==========================================

        content.getChildren().addAll(
                productCard,
                reviewCard,
                existingReviews
        );
    }

    // ==========================================
    // LOAD REVIEWS
    // ==========================================

    private void loadReviews(
            ListView<VBox> reviewListView,
            String productId
    ) {

        List<Map<String, Object>> reviews =
                new OrderControllerUser()
                        .getReviews(
                                productId
                        );

        // ==========================================
        // NO REVIEWS
        // ==========================================

        if (reviews == null || reviews.isEmpty()) {

            Label noReviews = new Label(
                    "No reviews yet."
            );

            noReviews.setStyle(
                    "-fx-text-fill: " + MUTED + ";" +
                    "-fx-font-size: 14px;"
            );

            reviewListView.setPlaceholder(
                    noReviews
            );

            return;
        }

        // ==========================================
        // DISPLAY REVIEWS
        // ==========================================

        for (Map<String, Object> review : reviews) {

            String email =
                    String.valueOf(
                            review.get("email")
                    );

            String reviewText =
                    String.valueOf(
                            review.get("review")
                    );

            int rating = 0;

            Object ratingObject =
                    review.get("ratings");

            if (ratingObject != null) {

                try {

                    rating = Integer.parseInt(
                            String.valueOf(
                                    ratingObject
                            )
                    );

                } catch (Exception e) {

                    rating = 0;
                }
            }

            VBox reviewBox =
                    createReviewBox(
                            email,
                            rating,
                            reviewText
                    );

            reviewListView.getItems().add(
                    reviewBox
            );
        }
    }

    // ==========================================
    // CREATE REVIEW BOX
    // ==========================================

    private VBox createReviewBox(
            String email,
            int rating,
            String review
    ) {

        VBox reviewBox = new VBox(
                8
        );

        reviewBox.setPadding(
                new Insets(15)
        );

        reviewBox.setStyle(
                "-fx-background-color: " + PANEL + ";" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 10px;" +
                "-fx-background-radius: 10px;"
        );

        // ==========================================
        // EMAIL
        // ==========================================

        Label emailLabel = new Label(
                email
        );

        emailLabel.setStyle(
                "-fx-text-fill: " + TEAL + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;"
        );

        // ==========================================
        // RATING
        // ==========================================

        HBox ratingBox = new HBox(
                4
        );

        for (int i = 0; i < 5; i++) {

            Label star = new Label();

            if (i < rating) {

                star.setText(
                        "★"
                );

            } else {

                star.setText(
                        "☆"
                );
            }

            star.setStyle(
                    "-fx-text-fill: " + GOLD + ";" +
                    "-fx-font-size: 18px;"
            );

            ratingBox.getChildren().add(
                    star
            );
        }

        // ==========================================
        // REVIEW TEXT
        // ==========================================

        Label reviewLabel = new Label(
                review
        );

        reviewLabel.setWrapText(
                true
        );

        reviewLabel.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 14px;"
        );

        // ==========================================
        // ADD TO REVIEW BOX
        // ==========================================

        reviewBox.getChildren().addAll(
                emailLabel,
                ratingBox,
                reviewLabel
        );

        return reviewBox;
    }

    // ==========================================
    // STAR STYLE
    // ==========================================

    private void setStarStyle(
            Button star,
            boolean selected
    ) {

        if (selected) {

            star.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-text-fill: " + GOLD + ";" +
                    "-fx-font-size: 30px;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 0;"
            );

        } else {

            star.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-text-fill: " + MUTED + ";" +
                    "-fx-font-size: 30px;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 0;"
            );
        }
    }

    // ==========================================
    // ALERT
    // ==========================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {

        Alert alert = new Alert(
                type
        );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}