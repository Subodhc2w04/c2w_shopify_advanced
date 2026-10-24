package com.c2w.view.user;
import com.c2w.view.authentication.AuthPage;
import com.c2w.view.user.ChatbotView;
import com.c2w.view.user.NavbarView;
import com.c2w.view.user.ToastView;
import com.c2w.view.user.Theme;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;


public class MainUser {

    private Scene userHomePageScene;
    public static BorderPane rootLayout;

    public Scene getUserHomePageScene(Runnable backAction) {
        // Wire the AuthPage-supplied back action (logout) so that signing out
        // returns to the AuthPage login scene instead of a user-side screen.
      
        AuthPage.getInstance().setLogoutHandler(backAction);

        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + Theme.COLOR_BG_PAGE + ";");

        NavbarView navbarView = new NavbarView();
        rootLayout.setTop(navbarView);

        HomeView homeView = new HomeView();
        rootLayout.setCenter(homeView);

        ToastView toastView = new ToastView();
        ChatbotView chatbotView = new ChatbotView();

        StackPane root = new StackPane(rootLayout, toastView, chatbotView);
        userHomePageScene = new Scene(root, 1550, 850);
        userHomePageScene.setFill(Theme.PAINT_BG_PAGE);

        return userHomePageScene;
    }
}
