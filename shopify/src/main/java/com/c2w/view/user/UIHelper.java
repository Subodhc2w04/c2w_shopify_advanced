package com.c2w.view.user;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.text.NumberFormat;
import java.util.Locale;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.net.URL;

public final class UIHelper {
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    private UIHelper() {}

    public static String formatCurrency(double amount) {
        if (amount == (long) amount) {
            return "₹" + String.format("%,d", (long) amount);
        }
        return "₹" + String.format("%,.2f", amount);
    }

    // ─── Primary Button (Pink Gradient) ──────────────────────────────────────
    public static Button createPrimaryButton(String text) {
        return createPrimaryButton(text, null);
    }

    public static Button createPrimaryButton(String text, Node graphic) {
        Button btn = new Button(text);
        if (graphic != null) btn.setGraphic(graphic);
        btn.setFont(Theme.font(13, FontWeight.BOLD));
        btn.setTextFill(Color.WHITE);
        btn.setCursor(Cursor.HAND);
        btn.setPadding(new Insets(11, 24, 11, 24));

        String normal = "-fx-background-color: linear-gradient(to right, #FF3E6C, #FF6B8A); " +
                "-fx-background-radius: 6px; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;";
        String hover  = "-fx-background-color: linear-gradient(to right, #FF5A82, #FF8AA4); " +
                "-fx-background-radius: 6px; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;";

        btn.setStyle(normal);
        btn.setEffect(Theme.subtleShadow());

        btn.setOnMouseEntered(e -> {
            btn.setStyle(hover);
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.03); st.setToY(1.03); st.play();
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle(normal);
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.0); st.setToY(1.0); st.play();
        });
        return btn;
    }

    // ─── Outline Button ───────────────────────────────────────────────────────
    public static Button createOutlineButton(String text) {
        return createOutlineButton(text, null);
    }

    public static Button createOutlineButton(String text, Node graphic) {
        Button btn = new Button(text);
        if (graphic != null) btn.setGraphic(graphic);
        btn.setFont(Theme.font(13, FontWeight.BOLD));
        btn.setCursor(Cursor.HAND);
        btn.setPadding(new Insets(10, 22, 10, 22));

        String normal = "-fx-background-color: transparent; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; " +
                "-fx-border-width: 1.5px; -fx-border-radius: 6px; -fx-background-radius: 6px; " +
                "-fx-text-fill: " + Theme.COLOR_DARK + "; -fx-font-weight: bold;";
        String hover = "-fx-background-color: " + Theme.COLOR_PRIMARY_LIGHT + "; " +
                "-fx-border-color: " + Theme.COLOR_PRIMARY + "; " +
                "-fx-border-width: 1.5px; -fx-border-radius: 6px; -fx-background-radius: 6px; " +
                "-fx-text-fill: " + Theme.COLOR_PRIMARY + "; -fx-font-weight: bold;";

        btn.setStyle(normal);

        btn.setOnMouseEntered(e -> {
            btn.setStyle(hover);
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.02); st.setToY(1.02); st.play();
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle(normal);
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.0); st.setToY(1.0); st.play();
        });
        return btn;
    }

    // ─── Dark Solid Button ────────────────────────────────────────────────────
    public static Button createDarkButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Theme.font(13, FontWeight.BOLD));
        btn.setTextFill(Color.WHITE);
        btn.setCursor(Cursor.HAND);
        btn.setPadding(new Insets(11, 22, 11, 22));

        String normal = "-fx-background-color: " + Theme.COLOR_BG_ELEVATED + "; -fx-background-radius: 6px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-width: 1px; -fx-border-radius: 6px; " +
                "-fx-text-fill: " + Theme.COLOR_DARK + "; -fx-cursor: hand;";
        String hover  = "-fx-background-color: " + Theme.COLOR_BG_CARD + "; -fx-background-radius: 6px; " +
                "-fx-border-color: " + Theme.COLOR_PRIMARY + "; -fx-border-width: 1px; -fx-border-radius: 6px; " +
                "-fx-text-fill: " + Theme.COLOR_PRIMARY + "; -fx-cursor: hand;";
        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(normal));
        return btn;
    }

    // ─── Ghost Button ─────────────────────────────────────────────────────────
    public static Button createGhostButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Theme.font(13, FontWeight.BOLD));
        btn.setCursor(Cursor.HAND);
        btn.setPadding(new Insets(6, 12, 6, 12));
        String normal = "-fx-background-color: transparent; -fx-text-fill: " + Theme.COLOR_SECONDARY_TEXT + ";";
        String hover  = "-fx-background-color: " + Theme.COLOR_PRIMARY_LIGHT + "; -fx-background-radius: 4px; " +
                "-fx-text-fill: " + Theme.COLOR_PRIMARY + ";";
        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(normal));
        return btn;
    }

    // ─── Text Field ───────────────────────────────────────────────────────────
    public static TextField createTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setFont(Theme.font(13));
        tf.setPadding(new Insets(11, 14, 11, 14));

        String normal = "-fx-background-color: " + Theme.COLOR_BG_INPUT + "; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; " +
                "-fx-border-radius: 6px; -fx-background-radius: 6px; " +
                "-fx-text-fill: " + Theme.COLOR_DARK + "; " +
                "-fx-prompt-text-fill: " + Theme.COLOR_MUTED_TEXT + ";";
        String focused = "-fx-background-color: " + Theme.COLOR_BG_INPUT + "; " +
                "-fx-border-color: " + Theme.COLOR_PRIMARY + "; " +
                "-fx-border-radius: 6px; -fx-background-radius: 6px; " +
                "-fx-text-fill: " + Theme.COLOR_DARK + "; " +
                "-fx-prompt-text-fill: " + Theme.COLOR_MUTED_TEXT + ";" +
                "-fx-effect: dropshadow(three-pass-box, rgba(255,62,108,0.25), 8, 0, 0, 0);";

        tf.setStyle(normal);
        tf.focusedProperty().addListener((obs, oldV, newV) -> {
            if (newV) tf.setStyle(focused);
            else tf.setStyle(normal);
        });
        return tf;
    }

    // ─── Password Field With Toggle ───────────────────────────────────────────
    public static HBox createPasswordFieldWithToggle(String prompt, TextField exposedFieldRef) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setFont(Theme.font(13));
        pf.setPadding(new Insets(11, 14, 11, 14));
        HBox.setHgrow(pf, Priority.ALWAYS);

        TextField plainTf = new TextField();
        plainTf.setPromptText(prompt);
        plainTf.setFont(Theme.font(13));
        plainTf.setPadding(new Insets(11, 14, 11, 14));
        plainTf.setVisible(false);
        plainTf.setManaged(false);
        HBox.setHgrow(plainTf, Priority.ALWAYS);

        pf.textProperty().bindBidirectional(plainTf.textProperty());

        Button toggleBtn = new Button("👁");
        toggleBtn.setFont(Theme.font(13));
        toggleBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: " + Theme.COLOR_MUTED_TEXT + ";");

        toggleBtn.setOnAction(e -> {
            if (pf.isVisible()) {
                pf.setVisible(false); pf.setManaged(false);
                plainTf.setVisible(true); plainTf.setManaged(true);
                toggleBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: " + Theme.COLOR_PRIMARY + ";");
            } else {
                plainTf.setVisible(false); plainTf.setManaged(false);
                pf.setVisible(true); pf.setManaged(true);
                toggleBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: " + Theme.COLOR_MUTED_TEXT + ";");
            }
        });

        HBox box = new HBox(4, pf, plainTf, toggleBtn);
        box.setAlignment(Pos.CENTER_LEFT);

        String normal  = "-fx-background-color: " + Theme.COLOR_BG_INPUT + "; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; " +
                "-fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 0 8 0 0;";
        String focused = "-fx-background-color: " + Theme.COLOR_BG_INPUT + "; " +
                "-fx-border-color: " + Theme.COLOR_PRIMARY + "; " +
                "-fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 0 8 0 0;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(255,62,108,0.25), 8, 0, 0, 0);";

        box.setStyle(normal);
        String innerStyle = "-fx-background-color: transparent; -fx-border-color: transparent; " +
                "-fx-text-fill: " + Theme.COLOR_DARK + "; -fx-prompt-text-fill: " + Theme.COLOR_MUTED_TEXT + ";";
        pf.setStyle(innerStyle);
        plainTf.setStyle(innerStyle);

        pf.focusedProperty().addListener((obs, o, n) -> box.setStyle(n ? focused : normal));
        plainTf.focusedProperty().addListener((obs, o, n) -> box.setStyle(n ? focused : normal));

        return box;
    }

    // ─── Rating Pill ──────────────────────────────────────────────────────────
    public static HBox createRatingPill(double rating, int ratingCount) {
        HBox pill = new HBox(4);
        pill.setAlignment(Pos.CENTER);
        pill.setPadding(new Insets(3, 8, 3, 8));
        pill.setStyle("-fx-background-color: " + Theme.COLOR_BG_ELEVATED + "; " +
                "-fx-background-radius: 12px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; " +
                "-fx-border-radius: 12px; -fx-border-width: 1px;");

        Label ratingLbl = new Label(String.format("%.1f", rating));
        ratingLbl.setFont(Theme.font(11, FontWeight.BOLD));
        ratingLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        Label starLbl = new Label("★");
        starLbl.setFont(Theme.font(11, FontWeight.BOLD));
        starLbl.setTextFill(Color.web(Theme.COLOR_SUCCESS));

        Label sep = new Label("|");
        sep.setFont(Theme.font(10));
        sep.setTextFill(Color.web(Theme.COLOR_BORDER));

        String countStr = ratingCount > 1000 ? String.format("%.1fk", ratingCount / 1000.0) : String.valueOf(ratingCount);
        Label countLbl = new Label(countStr);
        countLbl.setFont(Theme.font(10));
        countLbl.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));

        pill.getChildren().addAll(ratingLbl, starLbl, sep, countLbl);
        return pill;
    }

    // ─── Price Row ────────────────────────────────────────────────────────────
    public static HBox createPriceRow(double finalPrice, double mrp, double discountPercent) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        Label finalPriceLbl = new Label(formatCurrency(finalPrice));
        // Label finalPriceLbl = new Label(UIHelper.formatCurrency(AddToCart.getInstance().getFinalPayable()));
        finalPriceLbl.setFont(Theme.font(14, FontWeight.BOLD));
        finalPriceLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        if (discountPercent > 0 && mrp > finalPrice) {
            Label mrpLbl = new Label(formatCurrency(mrp));
            mrpLbl.setFont(Theme.font(12));
            mrpLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
            mrpLbl.setStyle("-fx-strikethrough: true;");

            Label discLbl = new Label("(" + (int) discountPercent + "% OFF)");
            discLbl.setFont(Theme.font(11.5, FontWeight.BOLD));
            discLbl.setTextFill(Color.web(Theme.COLOR_PRIMARY));

            row.getChildren().addAll(finalPriceLbl, mrpLbl, discLbl);
        } else {
            row.getChildren().add(finalPriceLbl);
        }
        return row;
    }

    // ─── Card ─────────────────────────────────────────────────────────────────
    public static VBox createCard(boolean enableHover) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: " + Theme.COLOR_BG_CARD + "; " +
                "-fx-background-radius: 10px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; " +
                "-fx-border-radius: 10px; -fx-border-width: 1px;");
        card.setEffect(Theme.cardShadow());

        if (enableHover) {
            card.setOnMouseEntered(e -> {
                card.setEffect(Theme.cardHoverShadow());
                ScaleTransition st = new ScaleTransition(Duration.millis(160), card);
                st.setToX(1.02); st.setToY(1.02); st.play();
            });
            card.setOnMouseExited(e -> {
                card.setEffect(Theme.cardShadow());
                ScaleTransition st = new ScaleTransition(Duration.millis(160), card);
                st.setToX(1.0); st.setToY(1.0); st.play();
            });
        }
        return card;
    }

    // ─── Section Header ───────────────────────────────────────────────────────
    public static HBox createSectionHeader(String title, String subtitle, String actionText, Runnable onAction) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 0, 8, 0));

        VBox titleBox = new VBox(2);

        Label titleLbl = new Label(title.toUpperCase());
        titleLbl.setFont(Theme.font(18, FontWeight.BOLD));
        titleLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        if (subtitle != null && !subtitle.isEmpty()) {
            Label subLbl = new Label(subtitle);
            subLbl.setFont(Theme.font(13));
            subLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
            titleBox.getChildren().addAll(titleLbl, subLbl);
        } else {
            titleBox.getChildren().add(titleLbl);
        }

        HBox.setHgrow(titleBox, Priority.ALWAYS);
        header.getChildren().add(titleBox);

        if (actionText != null && onAction != null) {
            Hyperlink link = new Hyperlink(actionText);
            link.setFont(Theme.font(12.5, FontWeight.BOLD));
            link.setTextFill(Color.web(Theme.COLOR_PRIMARY));
            link.setStyle("-fx-border-color: transparent;");
            link.setOnAction(e -> onAction.run());
            header.getChildren().add(link);
        }
        return header;
    }

    // ─── Badge ────────────────────────────────────────────────────────────────
    public static Label createBadge(String text, String bgColor, String textColor) {
        Label badge = new Label(text);
        badge.setFont(Theme.font(10, FontWeight.BOLD));
        badge.setTextFill(Color.web(textColor));
        badge.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 4px; -fx-padding: 3 7 3 7;");
        return badge;
    }

    // ─── Separator ────────────────────────────────────────────────────────────
    public static Separator createSeparator() {
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + Theme.COLOR_BORDER + ";");
        return sep;
    }

    // ─── Direct Image Loading (Replaces ImageAssets dependency) ────────────────
    public static Image loadImage(String path, double width, double height) {
        if (path == null || path.trim().isEmpty()) {
            return null;
        }
        String clean = path.trim();
        try {
            // Remote HTTP/HTTPS URL (Cloudinary, Firebase, Imgur, etc.)
            if (clean.startsWith("http://") || clean.startsWith("https://")) {
                return new Image(clean, width, height, true, true, true);
            }
            if (clean.startsWith("file:")) {
                return new Image(clean, width, height, true, true);
            }

            // Direct classpath resource check
            String resPath = clean.startsWith("/") ? clean : "/" + clean;
            URL resUrl = UIHelper.class.getResource(resPath);
            if (resUrl != null) {
                return new Image(resUrl.toExternalForm(), width, height, true, true);
            }

            // Relative assets resource check
            String assetPath = "/assets/images/" + (clean.startsWith("/") ? clean.substring(1) : clean);
            URL assetUrl = UIHelper.class.getResource(assetPath);
            if (assetUrl != null) {
                return new Image(assetUrl.toExternalForm(), width, height, true, true);
            }

            // Local filesystem check
            File file = new File(clean);
            if (file.exists()) {
                return new Image(file.toURI().toString(), width, height, true, true);
            }

            File assetFile = new File("src/main/resources/assets/images/" + (clean.startsWith("/") ? clean.substring(1) : clean));
            if (assetFile.exists()) {
                return new Image(assetFile.toURI().toString(), width, height, true, true);
            }

            // Default fallback
            return new Image(clean, width, height, true, true);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static ImageView createImageView(String path, double width, double height) {
        ImageView iv = new ImageView();
        iv.setFitWidth(width);
        iv.setFitHeight(height);
        Image img = loadImage(path, width, height);
        if (img != null) {
            iv.setImage(img);
        }
        return iv;
    }
}
