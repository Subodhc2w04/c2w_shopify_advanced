package com.c2w.view.user;

import javafx.scene.Scene;

public class UserHomePage {

    private Scene userHomePageScene;

    public Scene getUserHomePageScene(Runnable backAction) {
        MainUser mainUser = new MainUser();
        this.userHomePageScene = mainUser.getUserHomePageScene(backAction);
        return this.userHomePageScene;
    }
}
