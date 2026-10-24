package com.c2w.view.user;

import com.c2w.view.user.ChatbotView;
import com.c2w.view.user.NavbarView;
import com.c2w.view.user.ToastView;
import com.c2w.view.user.Theme;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;


public class MainView extends StackPane {
    private final BorderPane rootLayout;
    private final NavbarView navbarView;
    private final StackPane contentArea;
    private final ToastView toastView;
    private final ChatbotView chatbotView;

    public MainView() {
        setStyle("-fx-background-color: " + Theme.COLOR_BG_PAGE + ";");

        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + Theme.COLOR_BG_PAGE + ";");

        navbarView = new NavbarView();
        rootLayout.setTop(navbarView);

        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: " + Theme.COLOR_BG_PAGE + ";");
        rootLayout.setCenter(contentArea);

        toastView = new ToastView();
        chatbotView = new ChatbotView();

        getChildren().addAll(rootLayout, toastView, chatbotView);
    }

    public void setNavbarVisible(boolean visible) {
        navbarView.setVisible(visible);
        navbarView.setManaged(visible);
    }

    /**
     * Swaps the content area to the given Node with a fade transition,
     * keeping the sticky navbar visible.
     */
    public void showContent(Node content) {
        showContent(content, true);
    }

    /**
     * Swaps the content area to the given Node with a fade transition.
     */
    public void showContent(Node content, boolean showNavbar) {
        setNavbarVisible(showNavbar);
        content.setOpacity(0.0);
        contentArea.getChildren().setAll(content);
        FadeTransition ft = new FadeTransition(Duration.millis(200), content);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    public StackPane getContentArea() {
        return contentArea;
    }

    public NavbarView getNavbarView() {
        return navbarView;
    }

    public ToastView getToastView() {
        return toastView;
    }

    public ChatbotView getChatbotView() {
        return chatbotView;
    }
}
