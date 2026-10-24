package com.c2w.controller.authentication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthController {

    private final HttpClient client;

    // Firebase Web API Key
    private final String API_KEY = "AIzaSyCQ_ARQo3aBAh1Lzr77jycqYhN5vT3I8JY";

    // Firebase Authentication URLs
    private final String SIGNUP_URL =
            "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key="
                    + API_KEY;

    private final String LOGIN_URL =
            "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="
                    + API_KEY;


    public AuthController() {

        client = HttpClient.newHttpClient();
    }


    // ================= SIGNUP =================

    public String signup(String name, String email, String password) {

        String jsonData =
                "{"
                + "\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\","
                + "\"returnSecureToken\":true"
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SIGNUP_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();

        try {

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println("Signup Status Code : "
                    + response.statusCode());

            System.out.println("Signup Response : "
                    + response.body());

            if (response.statusCode() == 200) {

                return "200";

            } else {

                return getFirebaseError(response.body());
            }

        } catch (Exception e) {

            e.printStackTrace();

            return "Unable to connect to Firebase";
        }
    }


    // ================= LOGIN =================

    public String login(String email, String password) {

        String jsonData =
                "{"
                + "\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\","
                + "\"returnSecureToken\":true"
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LOGIN_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();

        try {

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println("Login Status Code : "
                    + response.statusCode());

            System.out.println("Login Response : "
                    + response.body());

            if (response.statusCode() == 200) {

                return "200";

            } else {

                return getFirebaseError(response.body());
            }

        } catch (Exception e) {

            e.printStackTrace();

            return "Unable to connect to Firebase";
        }
    }


    // ================= FIREBASE ERROR =================

    private String getFirebaseError(String response) {

        if (response.contains("EMAIL_EXISTS")) {

            return "Email already exists.";

        } else if (response.contains("INVALID_EMAIL")) {

            return "Invalid email.";

        } else if (response.contains("WEAK_PASSWORD")) {

            return "Password is too weak.";

        } else if (response.contains("EMAIL_NOT_FOUND")) {

            return "Email not found.";

        } else if (response.contains("INVALID_PASSWORD")) {

            return "Invalid password.";

        } else if (response.contains("USER_DISABLED")) {

            return "User account is disabled.";

        } else if (response.contains("OPERATION_NOT_ALLOWED")) {

            return "Email/Password authentication is disabled.";

        }

        return "Authentication failed.";
    }
}
