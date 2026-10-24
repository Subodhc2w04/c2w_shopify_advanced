package com.c2w.view.delivery;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

import javafx.stage.StageStyle;


public class DeliveryAlert {

   
    public static void styleAlert(Alert alert) {
        if (alert == null) return;

        alert.initStyle(StageStyle.TRANSPARENT);
        DialogPane pane = alert.getDialogPane();

        pane.setStyle(
                "-fx-background-color: #0f172a; " +
                "-fx-border-color: #334155; " +
                "-fx-border-width: 1.5px; " +
                "-fx-border-radius: 14px; " +
                "-fx-background-radius: 14px; " +
                "-fx-padding: 16px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 16, 0, 0, 6);");

        if (pane.getScene() != null) {
            pane.getScene().setFill(javafx.scene.paint.Color.TRANSPARENT);
        }

        // Style all labels inside
        pane.lookupAll(".label").forEach(node -> {
            if (node instanceof Label lbl) {
                lbl.setStyle("-fx-text-fill: #f1f5f9; -fx-font-size: 13px; -fx-wrap-text: true;");
            }
        });

        // Style header if present
        if (pane.getHeaderText() != null && !pane.getHeaderText().isEmpty()) {
            Label headerLbl = new Label(pane.getHeaderText());
            headerLbl.setStyle("-fx-text-fill: #38bdf8; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 0 0 8 0;");
            pane.setHeader(headerLbl);
        }

        // Style buttons
        for (ButtonType bt : pane.getButtonTypes()) {
            Button btn = (Button) pane.lookupButton(bt);
            if (btn != null) {
                if (bt.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                    btn.setStyle(
                            "-fx-background-color: #334155; -fx-text-fill: #cbd5e1; -fx-font-size: 13px; " +
                            "-fx-font-weight: bold; -fx-padding: 8 18; -fx-background-radius: 8px; -fx-cursor: hand;");
                } else {
                    btn.setStyle(
                            "-fx-background-color: #2563eb; -fx-text-fill: #ffffff; -fx-font-size: 13px; " +
                            "-fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 8px; -fx-cursor: hand;");
                }
            }
        }
    }

   
    public static void showWarning(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleAlert(alert);
        alert.showAndWait();
    }


    public static void showInfo(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleAlert(alert);
        alert.showAndWait();
    }

    public static void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleAlert(alert);
        alert.showAndWait();
    }


    public static void showConfirmation(String title, String header, String content, String confirmText, Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        ButtonType confirmBtn = new ButtonType(confirmText, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(confirmBtn, cancelBtn);

        styleAlert(alert);

        alert.showAndWait().ifPresent(response -> {
            if (response == confirmBtn && onConfirm != null) {
                onConfirm.run();
            }
        });
    }
}
