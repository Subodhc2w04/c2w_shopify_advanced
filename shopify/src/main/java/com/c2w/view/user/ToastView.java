package com.c2w.view.user;

import com.c2w.view.user.Theme;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;


public class ToastView extends VBox {
    private static ToastView currentInstance;

    public ToastView() {
        currentInstance = this;
        setAlignment(Pos.TOP_RIGHT);
        setPadding(new Insets(75, 25, 0, 0));
        setSpacing(10);
        setPickOnBounds(false);
        setMouseTransparent(false);
    }

    public static void showToast(String title, String message) {
        if (currentInstance != null) {
            currentInstance.show(title, message, "SUCCESS");
        }
    }

    public static void showInfoToast(String title, String message) {
        if (currentInstance != null) {
            currentInstance.show(title, message, "INFO");
        }
    }

    public void show(String title, String message, String type) {
        HBox toast = new HBox(12);
        toast.setAlignment(Pos.CENTER_LEFT);
        toast.setPadding(new Insets(12, 18, 12, 16));
        toast.setMaxWidth(360);
        toast.setMinWidth(280);

        String accentColor = Theme.COLOR_PRIMARY;
        String iconSymbol = "✓";
        if ("INFO".equalsIgnoreCase(type)) {
            accentColor = "#3B82F6";
            iconSymbol = "ℹ";
        } else if ("WARNING".equalsIgnoreCase(type)) {
            accentColor = Theme.COLOR_WARNING;
            iconSymbol = "⚠";
        }

        toast.setStyle("-fx-background-color: " + Theme.COLOR_BG_ELEVATED + "; " +
                "-fx-background-radius: 8px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; " +
                "-fx-border-width: 1px; " +
                "-fx-border-left-color: " + accentColor + "; " +
                "-fx-border-left-width: 4px;");
        toast.setEffect(Theme.floatingShadow());

        StackPane iconPane = new StackPane();
        iconPane.setMinSize(28, 28);
        iconPane.setMaxSize(28, 28);
        iconPane.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY_LIGHT + "; -fx-background-radius: 14px;");
        Label iconLbl = new Label(iconSymbol);
        iconLbl.setFont(Theme.font(13, FontWeight.BOLD));
        iconLbl.setTextFill(Color.web(accentColor));
        iconPane.getChildren().add(iconLbl);

        VBox textBox = new VBox(2);
        Label titleLbl = new Label(title);
        titleLbl.setFont(Theme.font(13, FontWeight.BOLD));
        titleLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        Label msgLbl = new Label(message);
        msgLbl.setFont(Theme.font(11.5));
        msgLbl.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));
        msgLbl.setWrapText(true);

        textBox.getChildren().addAll(titleLbl, msgLbl);
        toast.getChildren().addAll(iconPane, textBox);

        getChildren().add(0, toast);

        // Slide in
        toast.setTranslateY(-20);
        toast.setOpacity(0.0);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(250), toast);
        slideIn.setToY(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), toast);
        fadeIn.setToValue(1.0);

        ParallelTransition enter = new ParallelTransition(slideIn, fadeIn);
        enter.play();

        // Auto-dismiss
        PauseTransition pause = new PauseTransition(Duration.millis(2800));
        pause.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(250), toast);
            fadeOut.setToValue(0.0);
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(250), toast);
            slideOut.setToX(60);

            ParallelTransition exit = new ParallelTransition(fadeOut, slideOut);
            exit.setOnFinished(ev -> getChildren().remove(toast));
            exit.play();
        });
        pause.play();
    }
}
