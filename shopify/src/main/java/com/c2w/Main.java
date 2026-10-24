package com.c2w;

import com.c2w.view.authentication.AuthPage;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        Application.launch(AuthPage.class, args);
        try {
            Class.forName("com.c2w.config.FirebaseInitialize");
            Class.forName(  "com.c2w.config.CloudinaryConfig");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Hello world!");

    }
}

