package com.c2w.view.authentication;

import com.c2w.view.admin.AdminDashBoard;
import com.c2w.model.delivery.DeliveryPerson;
import com.c2w.controller.authentication.AuthController;
import com.c2w.model.user.SessionModel;
import com.c2w.controller.delivery.*;
import com.c2w.controller.user.UserController;
import com.c2w.view.delivery.*;
import com.google.cloud.Timestamp;
import com.c2w.view.user.ToastView;
import com.c2w.view.user.MainUser;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AuthPage extends Application {

    private static final String INK = "#0d1117";
    private static final String PANEL = "#151b24";
    private static final String BORDER = "#293444";
    private static final String TEXT = "#f2f5f7";
    private static final String MUTED = "#8e9aaa";
    private static final String TEAL = "#48d1c8";
    private static final String GOLD = "#f2b866";
    private static final String RED = "#ee7d83";

    public static Stage myStage;
    public static String email;

    @Override
    public void start(Stage stage) {

        myStage = stage;

        // myStage.setHeight(800);
        // myStage.setWidth(800);

        myStage.setTitle("Authentication");

        myStage.setMaximized(true);

        myStage.setScene(showLoginPage());

        myStage.show();
    }

    // ================= LOGIN PAGE =================

    public Scene showLoginPage() {
        AuthController authController = new AuthController();
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll(
                "User",
                "Delivery Person",
                "Admin");
        roleBox.setButtonCell(new ListCell<String>() {

            @Override
            protected void updateItem(String item, boolean empty) {

                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText("Select your role");
                    setStyle("-fx-text-fill: white;");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white;");
                }
            }
        });
        roleBox.setPromptText("Select your role");
        Button loginButton = new Button("Login");
        Button signupButton = new Button("Create an account");
        Label messageLabel = authMessage();

        styleField(emailField);
        styleField(passwordField);
        styleComboBox(roleBox);
        stylePrimaryButton(loginButton);
        styleLinkButton(signupButton);

        loginButton.setOnAction(e -> {
            String email1 = emailField.getText();
            String password = passwordField.getText();
            String selectedRole = roleBox.getValue();
            if (email1.isEmpty()
                    || password.isEmpty()
                    || selectedRole == null) {
                showError(messageLabel, "Please fill all fields.");
                return;
            }
            String role = getRole(selectedRole);
            String[] str = email1.split("@");
            if (str.length != 2 || str[0].isEmpty() || str[1].isEmpty()) {
                showError(messageLabel, "Enter a valid email address.");
                return;
            }
            email = str[0] + "_" + role + "@" + str[1];
            System.out.println("Login Email : " + email);
            System.out.println("Role : " + role);
            String code = authController.login(email, password);
            if (code.equals("200")) {
                if (role.equals("admin")) {
                    showSuccess(messageLabel, "Login successful!");

                    gotoAdminDashboard();
                } else if (role.equals("user")) {
                    showSuccess(messageLabel, "Login successful!");

                    MainUser userHomePage = new MainUser();
                    myStage.setScene(userHomePage.getUserHomePageScene(
                            () -> myStage.setScene(showLoginPage())));
                } else if (role.equals("deliveryPerson")) {
                    DeliveryPersonController deliveryPersonController = new DeliveryPersonController();
                    String status = deliveryPersonController.getDeliveryData(email, "approveStatus").toLowerCase();
                    System.out.println("Auth status for " + email + " is " + status);

                    if (status.equals("pending")) {
                        System.out.println("Status pending");
                        showError(messageLabel, "You are not approved by Admin yet");

                    } else if (status.equals("approved")) {
                        showSuccess(messageLabel, "Login successful!");

                        gotoDeliveryHomePage();

                    } else if (status.equals("rejected")) {
                        showError(messageLabel, "Request Rejected unable to login");

                    } else {
                        showError(messageLabel, "Something went wrong");
                    }

                }
            } else {
                showError(messageLabel, "Invalid email or password.");
            }
        });
        signupButton.setOnAction(e -> myStage.setScene(showSignupPage()));

        VBox form = new VBox(17);
        form.setMaxWidth(430);
        form.getChildren().addAll(
                text("WELCOME BACK", 11, TEAL, true),
                text("Sign in to your store", 30, TEXT, true),
                text("Manage your catalogue, orders, and customers from one place.", 13, MUTED, false),
                field("Email address", emailField),
                field("Password", passwordField),
                field("Sign in as", roleBox),
                loginButton,
                messageLabel,
                signupButton);
        return authScene(form, "C2W SHOPIFY", "Store operations,\nwith clarity.",
                "Your command center for the modern storefront.", "",1550, 850);
    }

    // ================= SIGNUP PAGE =================

    public Scene showSignupPage() {
        AuthController authController = new AuthController();
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll(
                "User",
                "Delivery Person",
                "Admin");
        roleBox.setButtonCell(new ListCell<String>() {

            @Override
            protected void updateItem(String item, boolean empty) {

                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText("Select your role");
                    setStyle("-fx-text-fill: white;");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white;");
                }
            }
        });
        roleBox.setPromptText("Select your role");
        Button signupButton = new Button("Create account");
        Button loginButton = new Button("Back to login");
        Label messageLabel = authMessage();

        styleField(nameField);
        styleField(emailField);
        styleField(passwordField);
        styleField(confirmPasswordField);
        styleComboBox(roleBox);
        stylePrimaryButton(signupButton);
        styleLinkButton(loginButton);

        signupButton.setOnAction(e -> {
            String name = nameField.getText();
            String email1 = emailField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String selectedRole = roleBox.getValue();
            if (name.isEmpty()
                    || email1.isEmpty()
                    || password.isEmpty()
                    || confirmPassword.isEmpty()
                    || selectedRole == null) {
                showError(messageLabel, "Please fill all fields.");
                return;
            }
            if (!password.equals(confirmPassword)) {
                showError(messageLabel, "Passwords do not match.");
                return;
            }
            String role = getRole(selectedRole);
            String[] str = email1.split("@");
            if (str.length != 2 || str[0].isEmpty() || str[1].isEmpty()) {
                showError(messageLabel, "Enter a valid email address.");
                return;
            }
            email = str[0] + "_" + role + "@" + str[1];
            System.out.println(
                    "Name : " + name);
            System.out.println(
                    "Email : " + email);
            System.out.println(
                    "Role : " + role);
            String code = authController.signup(name, email, password);
            if (code.equals("200")) {
                System.out.println();
                if (role == "deliveryPerson") {

                    System.out.println("Adding Delivery persons data to firebase");

                    DeliveryPersonController controller = new DeliveryPersonController();
                    controller.addDeliveryPerson(controller.getId(email), name, email, "", "", "", "", "false",
                            "Pending", "Request",0.0);

                }
                showSuccess(messageLabel, "Signup successful!");
            }
            if (code.equals("200")) {
                System.out.println();
                if (role == "user") {

                    System.out.println("Adding user's data to firebase");
                    UserController userController = new UserController();
                    userController.addUser(name, email1, null, null, null);

                }
                showSuccess(messageLabel, "Signup successful!");
            } else {
                showError(messageLabel, "Signup failed!");
            }
            nameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
            emailField.clear();
            roleBox.getSelectionModel().clearSelection();

        });
        loginButton.setOnAction(e -> myStage.setScene(showLoginPage()));

        VBox form = new VBox(14);
        form.setMaxWidth(430);
        form.getChildren().addAll(
                text("GET STARTED", 11, GOLD, true),
                text("Create your account", 30, TEXT, true),
                text("Set up your role and start running your storefront.", 13, MUTED, false),
                field("Full name", nameField),
                field("Email address", emailField),
                field("Password", passwordField),
                field("Confirm password", confirmPasswordField),
                field("Account role", roleBox),
                signupButton,
                messageLabel,
                loginButton);
        return authScene(form, "C2W SHOPIFY", "Build a store\nthat moves.",
                "A focused workspace for every part of your commerce operation.", "NEW ACCOUNT", 1550, 850);
    }

    private Scene authScene(VBox form, String brand, String headline, String description, String tag, double width,
            double height) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + INK + ";");
        VBox brandPanel = new VBox(18);
        brandPanel.setPrefWidth(365);
        brandPanel.setPadding(new Insets(48, 42, 48, 42));
        brandPanel.setStyle(
                "-fx-background-color: #111720; -fx-border-color: " + BORDER + "; -fx-border-width: 0 1 0 0;");
        Label brandLabel = text(brand, 18, TEXT, true);
        Label tagLabel = text(tag, 10, TEAL, true);
        Label headlineLabel = text(headline, 34, TEXT, true);
        headlineLabel.setWrapText(true);
        Label descriptionLabel = text(description, 14, MUTED, false);
        descriptionLabel.setWrapText(true);
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Label footer = text("C2W  /  STORE OPERATIONS", 10, MUTED, true);
        brandPanel.getChildren().addAll(brandLabel, tagLabel, spacer, headlineLabel, descriptionLabel, footer);

        VBox formHolder = new VBox(form);
        formHolder.setAlignment(Pos.CENTER);
        formHolder.setPadding(new Insets(42));
        root.setLeft(brandPanel);
        root.setCenter(formHolder);
        return new Scene(root, width, height);
    }

    private VBox field(String labelText, Control control) {
        Label label = text(labelText, 11, "#ffffff", true);
        VBox group = new VBox(7, label, control);
        return group;
    }

    private Label text(String value, int size, String color, boolean bold) {
        Label label = new Label(value);
        label.setStyle("-fx-text-fill: " + color + "; -fx-font-size: " + size + "px; -fx-font-weight: "
                + (bold ? "bold" : "normal") + ";");
        return label;
    }

    private void styleField(TextInputControl field) {
        field.setMaxWidth(Double.MAX_VALUE);
        field.setPadding(new Insets(12, 14, 12, 14));
        field.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: " + BORDER
                + "; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-text-fill: " + TEXT
                + "; -fx-prompt-text-fill: #ffffff; -fx-font-size: 13px;");
    }

    private void styleComboBox(ComboBox<String> comboBox) {
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: " + BORDER
                + "; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-size: 13px;");
    }

    private void stylePrimaryButton(Button button) {
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPadding(new Insets(13));
        button.setStyle("-fx-background-color: " + TEAL + "; -fx-text-fill: " + INK
                + "; -fx-font-weight: bold; -fx-background-radius: 5px; -fx-cursor: hand;");
    }

    private void styleLinkButton(Button button) {
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + MUTED
                + "; -fx-font-size: 12px; -fx-cursor: hand;");
    }

    private Label authMessage() {
        return text("", 12, MUTED, false);
    }

    private void showError(Label message, String value) {
        message.setStyle("-fx-text-fill: " + RED + "; -fx-font-size: 12px;");
        message.setText(value);
    }

    private void showSuccess(Label message, String value) {
        message.setStyle("-fx-text-fill: " + TEAL + "; -fx-font-size: 12px;");
        message.setText(value);
    }

    // ================= ROLE CONVERSION =================

    public String getRole(String selectedRole) {

        if (selectedRole.equals("User")) {

            return "user";

        } else if (selectedRole.equals("Delivery Person")) {

            return "deliveryPerson";

        } else {

            return "admin";
        }
    }

    // ================= ADMIN DASHBOARD =================

    public Scene gotoAdminDashboard() {

        AdminDashBoard adminDashboard = new AdminDashBoard();

        Scene scene = adminDashboard.getScene();

        myStage.setScene(scene);

        return scene;
    }

    public void gotoDeliveryHomePage() {

        DeliveryDashboardPage dashboardPage = new DeliveryDashboardPage();

        dashboardPage.isProfileUpdated(
                () -> myStage.setScene(showLoginPage()));
    }

    /// manasi

    private static AuthPage instance;

    public static synchronized AuthPage getInstance() {
        if (instance == null) {
            instance = new AuthPage();
        }
        return instance;
    }

    private Runnable logoutHandler;

    public void setLogoutHandler(Runnable logoutHandler) {
        this.logoutHandler = logoutHandler;
    }

    public void logout() {
        SessionModel.getInstance().clear();
        ToastView.showInfoToast("Signed Out", "You have been safely logged out.");
        if (logoutHandler != null) {
            // Back to the AuthPage login scene (via AuthPage.myStage.setScene(...)).
            logoutHandler.run();
        } else {
            AuthPage.myStage.setScene(new AuthPage().showLoginPage());
        }
    }
}
