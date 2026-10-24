package com.c2w.view.user;

import java.util.Map;

import com.c2w.controller.delivery.OrderController;
import com.c2w.controller.user.OrderControllerUser;
import com.c2w.view.authentication.AuthPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ProductReviewPage {


    private static final String INK = "#0d1117";
    private static final String PANEL = "#151b24";
    private static final String PANEL_LIGHT = "#1c2430";
    private static final String BORDER = "#293444";

    private static final String TEXT = "#f2f5f7";
    private static final String MUTED = "#8e9aaa";
    private static final String TEAL = "#48d1c8";


    private int selectedRating = 0;

    private final Label[] stars = new Label[5];

    public void show(String productName, String productId) {


        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle("Write a Review");
        dialog.setHeaderText(null);


        VBox main = new VBox(20);

        main.setPadding(
                new Insets(30)
        );

        main.setAlignment(
                Pos.TOP_CENTER
        );

        main.setStyle(
                "-fx-background-color: " + INK + ";"
        );


        Label title = new Label(
                "Write a Review"
        );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        26
                )
        );

        title.setTextFill(
                Color.web(TEXT)
        );


        // =================================================
        // PRODUCT NAME
        // =================================================

        Label productLabel = new Label(
                productName
        );

        productLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18
                )
        );

        productLabel.setTextFill(
                Color.web(TEAL)
        );


        // =================================================
        // CARD
        // =================================================

        VBox card = new VBox(20);

        card.setPadding(
                new Insets(25)
        );

        card.setPrefWidth(600);

        card.setStyle(
                "-fx-background-color: " + PANEL + ";" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;"
        );


        // =================================================
        // RATING TITLE
        // =================================================

        Label ratingTitle = new Label(
                "How would you rate this product?"
        );

        ratingTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16
                )
        );

        ratingTitle.setTextFill(
                Color.web(TEXT)
        );


        // =================================================
        // STARS
        // =================================================

        HBox starBox = new HBox(8);

        starBox.setAlignment(
                Pos.CENTER_LEFT
        );

        for (int i = 0; i < 5; i++) {

            final int rating = i + 1;

            stars[i] = new Label("☆");

            stars[i].setFont(
                    Font.font(
                            "Arial",
                            FontWeight.BOLD,
                            38
                    )
            );

            stars[i].setTextFill(
                    Color.web(MUTED)
            );

            stars[i].setCursor(
                    javafx.scene.Cursor.HAND
            );

            stars[i].setOnMouseClicked(e -> {

                selectedRating = rating;

                updateStars();
            });

            starBox.getChildren().add(
                    stars[i]
            );
        }


        // =================================================
        // REVIEW TITLE
        // =================================================

        Label reviewTitle = new Label(
                "Your Review"
        );

        reviewTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16
                )
        );

        reviewTitle.setTextFill(
                Color.web(TEXT)
        );


        // =================================================
        // REVIEW TEXT AREA
        // =================================================

        TextArea reviewArea = new TextArea();

        reviewArea.setPromptText(
                "Write your review here..."
        );

        reviewArea.setWrapText(true);

        reviewArea.setPrefRowCount(6);

        reviewArea.setStyle(
                "-fx-control-inner-background: "
                        + PANEL_LIGHT + ";" +
                "-fx-background-color: "
                        + PANEL_LIGHT + ";" +
                "-fx-text-fill: "
                        + TEXT + ";" +
                "-fx-prompt-text-fill: "
                        + MUTED + ";" +
                "-fx-border-color: "
                        + BORDER + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
        );


        // =================================================
        // BUTTONS
        // =================================================

        Button cancelButton = new Button(
                "Cancel"
        );

        cancelButton.setPrefWidth(120);

        cancelButton.setStyle(
                "-fx-background-color: " + PANEL_LIGHT + ";" +
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 10 20;"
        );


        Button submitButton = new Button(
                "Submit Review"
        );

        submitButton.setPrefWidth(150);

        submitButton.setStyle(
                "-fx-background-color: " + TEAL + ";" +
                "-fx-text-fill: " + INK + ";" +
                "-fx-font-weight: bold;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 10 20;"
        );


        // =================================================
        // BUTTON ACTION
        // =================================================

        cancelButton.setOnAction(e -> {

            dialog.close();

        });


        submitButton.setOnAction(e -> {

            if (selectedRating == 0) {

                showAlert(
                        "Please select a rating."
                );

                return;
            }


            if (reviewArea.getText()
                    .trim()
                    .isEmpty()) {

                showAlert(
                        "Please write a review."
                );

                return;
            }


            // =============================================
            // TEMPORARY OUTPUT
            // =============================================
            new OrderControllerUser().getUpdateRatings(productId,Map.of("email",AuthPage.email,"review",reviewArea.getText(),"ratings",selectedRating));
            System.out.println(
                    "Product ID : "
                            + productId
            );

            System.out.println(
                    "Product : "
                            + productName
            );

            System.out.println(
                    "Rating : "
                            + selectedRating
            );

            System.out.println(
                    "Review : "
                            + reviewArea.getText()
            );


            showAlert(
                    "Thank you! Your review has been submitted."
            );

            dialog.close();
        });


        // =================================================
        // BUTTON BOX
        // =================================================

        HBox buttonBox = new HBox(12);

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonBox.getChildren().addAll(
                cancelButton,
                submitButton
        );


        // =================================================
        // ADD EVERYTHING
        // =================================================

        card.getChildren().addAll(
                productLabel,
                ratingTitle,
                starBox,
                reviewTitle,
                reviewArea,
                buttonBox
        );


        main.getChildren().addAll(
                title,
                card
        );


        // =================================================
        // DIALOG CONTENT
        // =================================================

        dialog.getDialogPane().setContent(
                main
        );


        // =================================================
        // CLOSE BUTTON
        // =================================================

        dialog.getDialogPane()
                .getButtonTypes()
                .add(
                        new ButtonType(
                                "Close",
                                ButtonBar.ButtonData
                                        .CANCEL_CLOSE
                        )
                );


        // Hide default Dialog button
        Button closeButton =
                (Button) dialog.getDialogPane()
                        .lookupButton(
                                dialog.getDialogPane()
                                        .getButtonTypes()
                                        .get(0)
                        );

        closeButton.setVisible(false);

        closeButton.setManaged(false);


        // =================================================
        // SHOW DIALOG
        // =================================================

        dialog.showAndWait();
    }


    // =====================================================
    // UPDATE STARS
    // =====================================================

    private void updateStars() {

        for (int i = 0; i < 5; i++) {

            if (i < selectedRating) {

                stars[i].setText("★");

                stars[i].setTextFill(
                        Color.web("#f2b866")
                );

            } else {

                stars[i].setText("☆");

                stars[i].setTextFill(
                        Color.web(MUTED)
                );
            }
        }
    }


    // =====================================================
    // ALERT
    // =====================================================

    private void showAlert(String message) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION
        );

        alert.setTitle("Review");

        alert.setHeaderText(null);

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}