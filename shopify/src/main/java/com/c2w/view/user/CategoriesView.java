package com.c2w.view.user;

import com.c2w.view.authentication.AuthPage;
import com.c2w.view.user.ChatbotView;
import com.c2w.view.user.NavbarView;
import com.c2w.view.user.ToastView;
import com.c2w.view.user.Theme;
import com.c2w.view.user.UIHelper;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;


public class CategoriesView extends ScrollPane {
    private Scene categoriesScene;

    public CategoriesView() {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: " + Theme.COLOR_BG_PAGE + "; -fx-border-color: transparent;");

        VBox content = new VBox(24);
        content.setPadding(new Insets(24, 36, 48, 36));
        content.setMaxWidth(1280);
        content.setAlignment(Pos.TOP_CENTER);

        Label pageTitle = new Label("EXPLORE CATEGORIES");
        pageTitle.setFont(Theme.font(22, FontWeight.BOLD));
        pageTitle.setTextFill(Color.web(Theme.COLOR_DARK));

        Label pageSub = new Label("Discover tailored fashion across our iconic wardrobe departments");
        pageSub.setFont(Theme.font(13));
        pageSub.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

        FlowPane catGrid = new FlowPane(24, 24);
        catGrid.setAlignment(Pos.CENTER);

        catGrid.getChildren().addAll(
                createCategoryCard("Clothing", "Designer shirts, denim, stylish jackets, formal tailoring, and trendsetting apparel."),
                createCategoryCard("Accessories", "Luxury chronographs, artisanal leather totes, designer shades, and curated statements."),
                createCategoryCard("Shoes", "Handcrafted Chelsea boots, athletic court sneakers, formal footwear, and block heels."),
                createCategoryCard("Electronics", "Smart noise-canceling audio, premium lifestyle wearables, and tech gadgets."),
                createCategoryCard("Book", "Bestselling literary fiction, curated editions, art books, and self-growth works.")
        );

        content.getChildren().addAll(pageTitle, pageSub, catGrid);
        setContent(content);
    }

    private VBox createCategoryCard(String categoryName, String desc) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setCursor(Cursor.HAND);
        card.setPrefWidth(360);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: " + Theme.COLOR_BG_CARD + "; -fx-background-radius: 12px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 12px;");
        card.setEffect(Theme.cardShadow());

        ImageView img = UIHelper.createImageView("categories/" + categoryName.toLowerCase() + ".png", 328, 200);
        img.setFitWidth(328);
        img.setFitHeight(200);
        img.setPreserveRatio(false);

        Rectangle clip = new Rectangle(328, 200);
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        img.setClip(clip);

        Label catTitle = new Label(categoryName.toUpperCase());
        catTitle.setFont(Theme.font(16, FontWeight.BOLD));
        catTitle.setTextFill(Color.web(Theme.COLOR_DARK));

        Label descLbl = new Label(desc);
        descLbl.setFont(Theme.font(12));
        descLbl.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));
        descLbl.setWrapText(true);
        descLbl.setMaxWidth(320);

        Label actionLbl = new Label("Shop " + categoryName + " →");
        actionLbl.setFont(Theme.font(12.5, FontWeight.BOLD));
        actionLbl.setTextFill(Color.web(Theme.COLOR_PRIMARY));

        card.getChildren().addAll(img, catTitle, descLbl, actionLbl);

        card.setOnMouseEntered(e -> {
            card.setEffect(Theme.cardHoverShadow());
            ScaleTransition st = new ScaleTransition(Duration.millis(140), card);
            st.setToX(1.03);
            st.setToY(1.03);
            st.play();
        });

        card.setOnMouseExited(e -> {
            card.setEffect(Theme.cardShadow());
            ScaleTransition st = new ScaleTransition(Duration.millis(140), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        card.setOnMouseClicked(e -> {
            Scene currentScene = AuthPage.myStage.getScene();
            CatalogView catalog = new CatalogView(categoryName);
            AuthPage.myStage.setScene(catalog.getCatalogViewScene(() -> AuthPage.myStage.setScene(currentScene)));
        });
        return card;
    }

    public Scene getCategoriesViewScene(Runnable backAction) {
        if (categoriesScene == null) {
            BorderPane root = new BorderPane();
            NavbarView navbar = new NavbarView();
            root.setTop(navbar);
            root.setCenter(this);

            StackPane overlayContainer = new StackPane(root);
            ToastView toast = new ToastView();
            ChatbotView chatbot = new ChatbotView();
            overlayContainer.getChildren().addAll(toast, chatbot);

            categoriesScene = new Scene(overlayContainer, 1550,850);
          
        }
        return categoriesScene;
    }
}
