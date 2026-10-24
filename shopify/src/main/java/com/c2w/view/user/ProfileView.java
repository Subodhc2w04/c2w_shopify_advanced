package com.c2w.view.user;

import com.c2w.model.user.Address;
import com.c2w.model.user.SessionModel;
import com.c2w.model.user.User;
import com.c2w.controller.user.UserController;
import com.c2w.view.user.ToastView;
import com.c2w.view.authentication.AuthPage;
import com.c2w.view.user.Theme;
import com.c2w.view.user.UIHelper;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import com.c2w.model.user.SessionModel;

import java.util.ArrayList;
import java.util.List;
import com.c2w.view.user.NavbarView;
import com.c2w.view.user.ChatbotView;


public class ProfileView extends StackPane {

    private final ScrollPane scrollPane;
    private final VBox container;
    private final StackPane modalOverlay;
    private Scene profileScene;
    private Button backBtn;
    Address newAddr;
    UserController userController = new UserController();

    private List<Address> addresseList = new ArrayList<>();

    public ProfileView() {
        setStyle("-fx-background-color: " + Theme.COLOR_BG_PAGE + ";");

        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: " + Theme.COLOR_BG_PAGE
                + "; -fx-border-color: transparent;");

        container = new VBox(24);
        container.setPadding(new Insets(24, 36, 48, 36));
        container.setMaxWidth(1040);
        container.setAlignment(Pos.TOP_CENTER);

        StackPane centerWrapper = new StackPane(container);
        centerWrapper.setAlignment(Pos.TOP_CENTER); // horizontal centering
        centerWrapper.setStyle("-fx-background-color: transparent;");
        scrollPane.setContent(centerWrapper);

        // Modal overlay layer (hidden initially)
        modalOverlay = new StackPane();
        modalOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75);");
        modalOverlay.setAlignment(Pos.CENTER);
        modalOverlay.setVisible(false);
        modalOverlay.setManaged(false);

        getChildren().addAll(scrollPane, modalOverlay);

        buildUI();
    }

    /**
     * Rebuilds the entire profile screen content to reflect changes dynamically.
     */
    private void buildUI() {
        container.getChildren().clear();

        System.out.println("user email :" + AuthPage.email);
        User user = userController.getUser(AuthPage.email);
        addresseList = new ArrayList<>(user.getAddresses());

        System.out.println("Existing addresses: " + addresseList);
        System.out.println(user);
        System.out.println("User name:" + user.getFullName());

        // --- 1. PROFILE HEADER CARD ---
        HBox profileCard = new HBox(20);
        profileCard.setAlignment(Pos.CENTER_LEFT);
        profileCard.setPadding(new Insets(24));
        profileCard.setStyle("-fx-background-color: " + Theme.COLOR_BG_SECTION + "; -fx-background-radius: 12px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 12px;");
        profileCard.setEffect(Theme.subtleShadow());

        // Avatar with first letter of user name
        StackPane avatar = new StackPane();
        avatar.setMinSize(68, 68);
        avatar.setMaxSize(68, 68);
        avatar.setStyle("-fx-background-color: " + Theme.COLOR_PRIMARY + "; -fx-background-radius: 34px;");
        String initialLetter = (user.getFullName() != null && !user.getFullName().trim().isEmpty())
                ? user.getFullName().trim().substring(0, 1).toUpperCase()
                : "U";
        Label avLbl = new Label(initialLetter);
        avLbl.setFont(Theme.font(28, FontWeight.BOLD));
        avLbl.setTextFill(Color.WHITE);
        avatar.getChildren().add(avLbl);

        VBox userDetails = new VBox(6);
        Label nameLbl = new Label(user.getFullName());
        nameLbl.setFont(Theme.font(20, FontWeight.BOLD));
        nameLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        Label contactLbl = new Label(user.getEmail() + "  •  " + user.getMobile());
        contactLbl.setFont(Theme.font(12.5));
        contactLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

        HBox badgesRow = new HBox(8);
        badgesRow.setAlignment(Pos.CENTER_LEFT);

        Label genderBadge = UIHelper.createBadge("⚧ " + (user.getGender() != null ? user.getGender() : "Not specified"),
                Theme.COLOR_BG_CARD, Theme.COLOR_SECONDARY_TEXT);
        badgesRow.getChildren().addAll(genderBadge);

        userDetails.getChildren().addAll(nameLbl, contactLbl, badgesRow);

        Region pSpacer = new Region();
        HBox.setHgrow(pSpacer, Priority.ALWAYS);

        HBox actionsBox = new HBox(10);
        actionsBox.setAlignment(Pos.CENTER_RIGHT);

        final User currentUserRef = user;
        Button editProfileBtn = UIHelper.createOutlineButton("✏ EDIT PROFILE");
        editProfileBtn.setOnAction(e -> showEditProfileModal(currentUserRef));

        Button logoutBtn = UIHelper.createOutlineButton("LOG OUT");
        logoutBtn.setOnAction(e -> AuthPage.getInstance().logout());

        actionsBox.getChildren().addAll(editProfileBtn, logoutBtn);
        profileCard.getChildren().addAll(avatar, userDetails, pSpacer, actionsBox);

        // --- 2. QUICK SHORTCUTS ---
        HBox shortcuts = new HBox(16);
        shortcuts.setAlignment(Pos.CENTER);
        shortcuts.getChildren().addAll(
                createShortcutCard("📦 My Orders", "Track shipments & view invoices", () -> {
                    OrderHistoryView orders = new OrderHistoryView();
                    AuthPage.myStage
                            .setScene(orders.getOrderHistoryViewScene(() -> AuthPage.myStage.setScene(profileScene)));
                }),
                createShortcutCard("♡ My Wishlist", "Review saved dream pieces", () -> {
                    WishlistView wish = new WishlistView();
                    AuthPage.myStage.setScene(wish.getWishlistViewScene(() -> AuthPage.myStage.setScene(profileScene)));
                })

        );

        // --- 3. SAVED ADDRESSES SECTION ---
        VBox addrSection = new VBox(16);
        addrSection.setPadding(new Insets(20));
        addrSection.setStyle("-fx-background-color: " + Theme.COLOR_BG_CARD + "; -fx-background-radius: 10px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 10px;");
        addrSection.setEffect(Theme.subtleShadow());

        HBox addrHeader = new HBox();
        addrHeader.setAlignment(Pos.CENTER_LEFT);

        Label addrTitle = new Label("SAVED DELIVERY ADDRESSES");
        addrTitle.setFont(Theme.font(14, FontWeight.BOLD));
        addrTitle.setTextFill(Color.web(Theme.COLOR_DARK));

        Region addrSpacer = new Region();
        HBox.setHgrow(addrSpacer, Priority.ALWAYS);

        Button addAddrBtn = UIHelper.createPrimaryButton("+ ADD ADDRESS");
        addAddrBtn.setOnAction(e -> showAddressModal(currentUserRef, null, -1));

        addrHeader.getChildren().addAll(addrTitle, addrSpacer, addAddrBtn);

        VBox addrList = new VBox(12);
        List<Address> addresses = user.getAddresses();
        if (addresses.isEmpty()) {
            VBox emptyBox = new VBox(8);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(24));
            Label emptyLbl = new Label("No delivery addresses saved yet.");
            emptyLbl.setFont(Theme.font(13));
            emptyLbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
            emptyBox.getChildren().add(emptyLbl);
            addrList.getChildren().add(emptyBox);
        } else {
            for (int i = 0; i < addresses.size(); i++) {
                final int addrIndex = i;
                final Address a = addresses.get(i);

                System.out.println("///////////address:" + a);
                System.out.println("///////////address:" + i);
                VBox aBox = new VBox(6);
                aBox.setPadding(new Insets(14, 18, 14, 18));
                aBox.setStyle("-fx-background-color: " + Theme.COLOR_BG_SECTION + "; -fx-background-radius: 8px; " +
                        "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 8px;");

                HBox aTop = new HBox(10);
                aTop.setAlignment(Pos.CENTER_LEFT);

                Label aName = new Label(a.getFullName());
                aName.setFont(Theme.font(13.5, FontWeight.BOLD));
                aName.setTextFill(Color.web(Theme.COLOR_DARK));

                Label aType = UIHelper.createBadge(a.getAddressType(), Theme.COLOR_PRIMARY_LIGHT, Theme.COLOR_PRIMARY);
                aTop.getChildren().addAll(aName, aType);

                if (a.isDefault()) {
                    Label defBadge = UIHelper.createBadge("DEFAULT", Theme.COLOR_SUCCESS_LIGHT, Theme.COLOR_SUCCESS);
                    aTop.getChildren().add(defBadge);
                }

                Region aSpacer = new Region();
                HBox.setHgrow(aSpacer, Priority.ALWAYS);

                Button editBtn = UIHelper.createGhostButton("✏ Edit");
                editBtn.setOnAction(e -> showAddressModal(currentUserRef, a, addrIndex));

                Button deleteBtn = UIHelper.createGhostButton("🗑 Delete");
                deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Theme.COLOR_MUTED_TEXT + ";");
                deleteBtn.setOnAction(e -> {
                    currentUserRef.removeAddress(addrIndex);
                    buildUI();
                    ToastView.showToast("Address Removed", "Address has been removed from your account.");
                });

                HBox cardActions = new HBox(4, editBtn, deleteBtn);
                cardActions.setAlignment(Pos.CENTER_RIGHT);

                aTop.getChildren().addAll(aSpacer, cardActions);

                Label aStreet = new Label(a.getFormattedAddress());
                aStreet.setFont(Theme.font(12));
                aStreet.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));

                Label aPhone = new Label("Phone: +91 " + a.getMobile());
                aPhone.setFont(Theme.font(11.5));
                aPhone.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

                aBox.getChildren().addAll(aTop, aStreet, aPhone);
                addrList.getChildren().add(aBox);
            }
        }

        addrSection.getChildren().addAll(addrHeader, addrList);
        // container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(profileCard, shortcuts, addrSection);
    }

    private void showEditProfileModal(User user) {
        VBox form = new VBox(14);
        form.setPadding(new Insets(24));
        form.setMaxWidth(460);
        form.setStyle("-fx-background-color: " + Theme.COLOR_BG_ELEVATED + "; -fx-background-radius: 12px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 12px; -fx-border-width: 1.5px;");
        form.setEffect(Theme.floatingShadow());

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(2);
        Label title = new Label("Edit Profile Details");
        title.setFont(Theme.font(17, FontWeight.BOLD));
        title.setTextFill(Color.web(Theme.COLOR_DARK));

        Label subtitle = new Label("Update your personal profile information");
        subtitle.setFont(Theme.font(11.5));
        subtitle.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button("✕");
        closeBtn.setFont(Theme.font(13, FontWeight.BOLD));
        closeBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + Theme.COLOR_MUTED_TEXT + "; -fx-cursor: hand;");
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + Theme.COLOR_PRIMARY + "; -fx-cursor: hand;"));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + Theme.COLOR_MUTED_TEXT + "; -fx-cursor: hand;"));
        closeBtn.setOnAction(e -> hideModal());

        header.getChildren().addAll(titleBox, spacer, closeBtn);

        // Fields
        Label nameLbl = createFormLabel("FULL NAME *");
        TextField nameField = UIHelper.createTextField("Enter full name");
        nameField.setText(user.getFullName());

        Label emailLbl = createFormLabel("EMAIL ADDRESS *");
        TextField emailField = UIHelper.createTextField("Enter email address");
        emailField.setText(user.getEmail());

        Label mobileLbl = createFormLabel("MOBILE NUMBER *");
        TextField mobileField = UIHelper.createTextField("10-digit mobile number");
        mobileField.setText(user.getMobile());

        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox genderBox = new VBox(6);
        Label genderLbl = createFormLabel("GENDER");
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male", "Female", "Non-Binary / Other", "Prefer not to say");
        genderCombo.setValue(user.getGender() != null ? user.getGender() : "Prefer not to say");
        genderCombo.setMaxWidth(Double.MAX_VALUE);
        genderCombo.setStyle(
                "-fx-background-color: " + Theme.COLOR_BG_INPUT + "; -fx-border-color: " + Theme.COLOR_BORDER + "; " +
                        "-fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 6 10 6 10; -fx-text-fill: "
                        + Theme.COLOR_DARK + ";");
        genderBox.getChildren().addAll(genderLbl, genderCombo);
        HBox.setHgrow(genderBox, Priority.ALWAYS);

        row.getChildren().addAll(genderBox);

        Label errorLbl = new Label();
        errorLbl.setFont(Theme.font(11.5));
        errorLbl.setTextFill(Color.web(Theme.COLOR_PRIMARY));
        errorLbl.setVisible(false);
        errorLbl.setManaged(false);

        // Footer buttons
        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(8, 0, 0, 0));

        Button cancelBtn = UIHelper.createOutlineButton("Cancel");
        cancelBtn.setOnAction(e -> hideModal());

        Button saveBtn = UIHelper.createPrimaryButton("Save Changes");

        UserController userController = new UserController();
        saveBtn.setOnAction(e -> {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newMobile = mobileField.getText().trim();

            if (newName.isEmpty()) {
                showError(errorLbl, "Please enter your full name.");
                return;
            }
            if (newEmail.isEmpty() || !newEmail.contains("@")) {
                showError(errorLbl, "Please enter a valid email address.");
                return;
            }
            if (newMobile.isEmpty()) {
                showError(errorLbl, "Please enter your mobile number.");
                return;
            }

            // Update user entity
            user.setFullName(newName);
            user.setEmail(newEmail);
            user.setMobile(newMobile);
            user.setGender(genderCombo.getValue());

            System.out.println(newName);
            System.out.println(newEmail);
            System.out.println(newMobile);
            System.out.println(genderCombo.getValue());
            userController.updateUser(newName, newEmail, newMobile, genderCombo.getValue(), user.getAddresses());

            // Notify session listeners (e.g. Navbar username badge)
            SessionModel.getInstance().notifyListeners();

            hideModal();
            buildUI();
            ToastView.showToast("Profile Updated", "Your profile details have been saved successfully.");
        });

        footer.getChildren().addAll(cancelBtn, saveBtn);

        form.getChildren().addAll(header, nameLbl, nameField, emailLbl, emailField, mobileLbl, mobileField, row,
                errorLbl, footer);
        displayModal(form);
    }

    private void showAddressModal(User user, Address addressToEdit, int addressIndex) {
        boolean isEdit = (addressToEdit != null && addressIndex >= 0);

        VBox form = new VBox(12);
        form.setPadding(new Insets(24));
        form.setMaxWidth(480);
        form.setStyle("-fx-background-color: " + Theme.COLOR_BG_ELEVATED + "; -fx-background-radius: 12px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 12px; -fx-border-width: 1.5px;");
        form.setEffect(Theme.floatingShadow());

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(2);
        Label title = new Label(isEdit ? "Edit Delivery Address" : "Add New Delivery Address");
        title.setFont(Theme.font(17, FontWeight.BOLD));
        title.setTextFill(Color.web(Theme.COLOR_DARK));

        Label subtitle = new Label(
                isEdit ? "Update your existing shipping destination" : "Enter a new shipping address");
        subtitle.setFont(Theme.font(11.5));
        subtitle.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button("✕");
        closeBtn.setFont(Theme.font(13, FontWeight.BOLD));
        closeBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + Theme.COLOR_MUTED_TEXT + "; -fx-cursor: hand;");
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + Theme.COLOR_PRIMARY + "; -fx-cursor: hand;"));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + Theme.COLOR_MUTED_TEXT + "; -fx-cursor: hand;"));
        closeBtn.setOnAction(e -> hideModal());

        header.getChildren().addAll(titleBox, spacer, closeBtn);

        // Fields
        HBox contactRow = new HBox(12);
        VBox nameBox = new VBox(4);
        Label nameLbl = createFormLabel("CONTACT NAME *");
        TextField nameField = UIHelper.createTextField("Contact person name");
        nameField.setText(isEdit ? addressToEdit.getFullName() : user.getFullName());
        nameBox.getChildren().addAll(nameLbl, nameField);
        HBox.setHgrow(nameBox, Priority.ALWAYS);

        VBox phoneBox = new VBox(4);
        Label phoneLbl = createFormLabel("PHONE NUMBER *");
        TextField phoneField = UIHelper.createTextField("10-digit mobile");
        phoneField.setText(isEdit ? addressToEdit.getMobile() : user.getMobile());
        phoneBox.getChildren().addAll(phoneLbl, phoneField);
        HBox.setHgrow(phoneBox, Priority.ALWAYS);
        contactRow.getChildren().addAll(nameBox, phoneBox);

        Label streetLbl = createFormLabel("STREET / HOUSE / AREA *");
        TextField streetField = UIHelper.createTextField("Flat/House No., Building, Street, Area");
        if (isEdit)
            streetField.setText(addressToEdit.getStreet());

        HBox cityStateRow = new HBox(12);
        VBox cityBox = new VBox(4);
        Label cityLbl = createFormLabel("CITY *");
        TextField cityField = UIHelper.createTextField("e.g. Bengaluru");
        if (isEdit)
            cityField.setText(addressToEdit.getCity());
        cityBox.getChildren().addAll(cityLbl, cityField);
        HBox.setHgrow(cityBox, Priority.ALWAYS);

        VBox stateBox = new VBox(4);
        Label stateLbl = createFormLabel("STATE *");
        TextField stateField = UIHelper.createTextField("e.g. Karnataka");
        if (isEdit)
            stateField.setText(addressToEdit.getState());
        stateBox.getChildren().addAll(stateLbl, stateField);
        HBox.setHgrow(stateBox, Priority.ALWAYS);
        cityStateRow.getChildren().addAll(cityBox, stateBox);

        HBox pinTypeRow = new HBox(12);
        VBox pinBox = new VBox(4);
        Label pinLbl = createFormLabel("PIN CODE *");
        TextField pinField = UIHelper.createTextField("e.g. 560038");
        if (isEdit)
            pinField.setText(addressToEdit.getPinCode());
        pinBox.getChildren().addAll(pinLbl, pinField);
        HBox.setHgrow(pinBox, Priority.ALWAYS);

        VBox typeBox = new VBox(4);
        Label typeLbl = createFormLabel("ADDRESS TYPE");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Home", "Work", "Other");
        typeCombo.setValue(isEdit ? addressToEdit.getAddressType() : "Home");
        typeCombo.setMaxWidth(Double.MAX_VALUE);
        typeCombo.setStyle(
                "-fx-background-color: " + Theme.COLOR_BG_INPUT + "; -fx-border-color: " + Theme.COLOR_BORDER + "; " +
                        "-fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 6 10 6 10; -fx-text-fill: "
                        + Theme.COLOR_DARK + ";");
        typeBox.getChildren().addAll(typeLbl, typeCombo);
        HBox.setHgrow(typeBox, Priority.ALWAYS);
        pinTypeRow.getChildren().addAll(pinBox, typeBox);

        CheckBox defaultCb = new CheckBox("Set as default delivery address");
        defaultCb.setTextFill(Color.web(Theme.COLOR_DARK));
        defaultCb.setFont(Theme.font(12));
        defaultCb.setSelected(isEdit ? addressToEdit.isDefault() : user.getAddresses().isEmpty());
        defaultCb.setStyle("-fx-text-fill: " + Theme.COLOR_DARK + "; -fx-cursor: hand;");

        Label errorLbl = new Label();
        errorLbl.setFont(Theme.font(11.5));
        errorLbl.setTextFill(Color.web(Theme.COLOR_PRIMARY));
        errorLbl.setVisible(false);
        errorLbl.setManaged(false);

        // Footer
        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(8, 0, 0, 0));

        Button cancelBtn = UIHelper.createOutlineButton("Cancel");
        cancelBtn.setOnAction(e -> hideModal());

        Button saveBtn = UIHelper.createPrimaryButton(isEdit ? "Save Address" : "Add Address");
        saveBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String mobile = phoneField.getText().trim();
            String street = streetField.getText().trim();
            String city = cityField.getText().trim();
            String state = stateField.getText().trim();
            String pin = pinField.getText().trim();
            String type = typeCombo.getValue();
            boolean isDef = defaultCb.isSelected();

            if (name.isEmpty() || mobile.isEmpty() || street.isEmpty() || city.isEmpty() || state.isEmpty()
                    || pin.isEmpty()) {
                showError(errorLbl, "Please fill in all required address fields.");
                return;
            }

            // If setting default, clear other addresses' default status
            if (isDef) {
                for (Address a : user.getAddresses()) {
                    a.setDefault(false);
                }
            }

            if (isEdit) {

                addressToEdit.setFullName(name);
                addressToEdit.setMobile(mobile);
                addressToEdit.setStreet(street);
                addressToEdit.setCity(city);
                addressToEdit.setState(state);
                addressToEdit.setPinCode(pin);
                addressToEdit.setAddressType(type);
                addressToEdit.setDefault(isDef);

                user.updateAddress(addressIndex, addressToEdit);

            } else {

                newAddr = new Address(
                        name,
                        mobile,
                        street,
                        city,
                        state,
                        pin,
                        type,
                        isDef);

                addresseList.add(newAddr);
                user.setAddresses(addresseList);

                System.out.println("User addresses after add: " + user.getAddresses());
            }

            userController.updateUser(
                    name,
                    user.getEmail(),
                    mobile,
                    user.getGender(),
                    new ArrayList<>(user.getAddresses()));
            hideModal();
            buildUI();

            ToastView.showToast(
                    "Address Saved",
                    isEdit
                            ? "Delivery address updated successfully."
                            : "New address added to your profile.");
        });

        footer.getChildren().addAll(cancelBtn, saveBtn);

        form.getChildren().addAll(header, contactRow, streetLbl, streetField, cityStateRow, pinTypeRow, defaultCb,
                errorLbl, footer);
        displayModal(form);
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // MODAL PRESENTATION HELPERS
    // ─────────────────────────────────────────────────────────────────────────────

    private void displayModal(VBox modalCard) {
        modalOverlay.getChildren().clear();

        // Wrap modalCard in a ScrollPane in case height exceeds window viewport
        ScrollPane modalScroll = new ScrollPane(modalCard);
        modalScroll.setFitToWidth(true);
        modalScroll.setMaxWidth(520);
        modalScroll.setMaxHeight(640);
        modalScroll.setStyle(
                "-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        modalOverlay.getChildren().add(modalScroll);
        modalOverlay.setVisible(true);
        modalOverlay.setManaged(true);
        modalOverlay.setOpacity(0.0);

        modalCard.setScaleX(0.92);
        modalCard.setScaleY(0.92);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(180), modalOverlay);
        fadeIn.setToValue(1.0);

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(180), modalCard);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);

        ParallelTransition pt = new ParallelTransition(fadeIn, scaleIn);
        pt.play();
    }

    private void hideModal() {
        if (!modalOverlay.isVisible())
            return;

        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), modalOverlay);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            modalOverlay.setVisible(false);
            modalOverlay.setManaged(false);
            modalOverlay.getChildren().clear();
        });
        fadeOut.play();
    }

    private Label createFormLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Theme.font(10.5, FontWeight.BOLD));
        lbl.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
        return lbl;
    }

    private void showError(Label errorLbl, String message) {
        errorLbl.setText(message);
        errorLbl.setVisible(true);
        errorLbl.setManaged(true);
    }

    private VBox createShortcutCard(String title, String subtitle, Runnable action) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(16));
        card.setPrefWidth(320);
        card.setStyle("-fx-background-color: " + Theme.COLOR_BG_CARD + "; -fx-background-radius: 8px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 8px; -fx-cursor: hand;");
        card.setEffect(Theme.subtleShadow());

        Label t = new Label(title);
        t.setFont(Theme.font(14, FontWeight.BOLD));
        t.setTextFill(Color.web(Theme.COLOR_DARK));

        Label s = new Label(subtitle);
        s.setFont(Theme.font(11.5));
        s.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));

        card.getChildren().addAll(t, s);
        card.setOnMouseClicked(e -> action.run());

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: " + Theme.COLOR_BG_ELEVATED + "; -fx-background-radius: 8px; " +
                    "-fx-border-color: " + Theme.COLOR_PRIMARY + "; -fx-border-radius: 8px; -fx-cursor: hand;");
        });
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: " + Theme.COLOR_BG_CARD + "; -fx-background-radius: 8px; " +
                    "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 8px; -fx-cursor: hand;");
        });

        return card;
    }

    public Scene getProfileViewScene(Runnable backAction) {
        if (backBtn != null && backAction != null) {
            backBtn.setOnAction(e -> backAction.run());
        }
        BorderPane rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + Theme.COLOR_BG_PAGE + ";");
        rootLayout.setTop(new NavbarView());
        rootLayout.setCenter(this);
        StackPane root = new StackPane(rootLayout, new ToastView(), new ChatbotView());
        profileScene = new Scene(root, 1550, 850);
        profileScene.setFill(Theme.PAINT_BG_PAGE);
        return profileScene;
    }

    public Scene getProfilePageScene(Runnable backAction) {
        return getProfileViewScene(backAction);
    }
}
