package com.c2w.view.user;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;


public class EmptyStateView extends VBox {

    public EmptyStateView(String iconSymbol, String title, String description, String buttonText, Runnable onAction) {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(50, 20, 50, 20));
        setSpacing(14);

        StackPane circle = new StackPane();
        circle.setMinSize(80, 80);
        circle.setMaxSize(80, 80);
        circle.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY_LIGHT + "; -fx-background-radius: 40px;");

        Label iconLbl = new Label(iconSymbol);
        iconLbl.setFont(Theme.font(36));
        circle.getChildren().add(iconLbl);

        Label titleLbl = new Label(title);
        titleLbl.setFont(Theme.font(18, FontWeight.BOLD));
        titleLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        Label descLbl = new Label(description);
        descLbl.setFont(Theme.font(13));
        descLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
        descLbl.setWrapText(true);
        descLbl.setMaxWidth(400);

        getChildren().addAll(circle, titleLbl, descLbl);

        if (buttonText != null && onAction != null) {
            Button btn = UIHelper.createPrimaryButton(buttonText);
            btn.setOnAction(e -> onAction.run());
            VBox.setMargin(btn, new Insets(10, 0, 0, 0));
            getChildren().add(btn);
        }
    }
}
