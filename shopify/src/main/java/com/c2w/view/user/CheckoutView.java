package com.c2w.view.user;

import com.c2w.controller.user.UserController;
import com.c2w.config.CloudinaryConfig;
import com.c2w.controller.delivery.OrderController;
import com.c2w.controller.user.CheckoutController;
import com.c2w.controller.user.RazorpayController;
import com.c2w.model.user.Address;
import com.c2w.model.user.OrderUser;
import com.c2w.model.user.AddToCart;
import com.c2w.model.user.SessionModel;
import com.c2w.model.user.User;
import com.c2w.view.authentication.AuthPage;
import com.c2w.view.user.ChatbotView;
import com.c2w.view.user.NavbarView;
import com.c2w.view.user.ToastView;
import com.cloudinary.utils.ObjectUtils;
import com.google.cloud.firestore.SetOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.c2w.view.user.Theme;
import com.c2w.view.user.UIHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.imageio.ImageIO;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;


public class CheckoutView extends ScrollPane {
    private String selectedPayment = "UPI (Google Pay / PhonePe)";
    private Scene checkoutScene;
    public static String codUrl;
    boolean isCod=false;

   private CompletableFuture<String> storePaymentQr(String orderId, double amount) {
    return CompletableFuture.supplyAsync(() -> {
        try {
            String upiLink = "upi://pay?pa=c2wfashion@upi&pn=C2W%20Fashion"
                    + "&am=" + String.format(Locale.US, "%.2f", amount)
                    + "&cu=INR&tn=" + URLEncoder.encode("Order " + orderId, StandardCharsets.UTF_8).replace("+", "%20");

            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix m = new QRCodeWriter().encode(upiLink, BarcodeFormat.QR_CODE, 520, 520, hints);

            BufferedImage img = new BufferedImage(m.getWidth(), m.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < m.getWidth(); x++)
                for (int y = 0; y < m.getHeight(); y++)
                    img.setRGB(x, y, m.get(x, y) ? 0x000000 : 0xFFFFFF);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(img, "PNG", out);

            Map<?, ?> res = CloudinaryConfig.getCloudinaryInstance().uploader().upload(
                    out.toByteArray(),
                    ObjectUtils.asMap("folder", "payment_qr", "public_id", "qr_" + orderId, "overwrite", true));

            String url = (String) res.get("secure_url");
            System.out.println("QR stored: " + url);
            return url;                       // success: return the URL
        } catch (Exception e) {
            System.err.println("QR store failed for order " + orderId + ": " + e.getMessage());
            return null;                      // failure: caller checks for null
        }
    });
}
       public CheckoutView() {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: " + Theme.COLOR_BG_PAGE
                + "; -fx-border-color: transparent;");

        VBox container = new VBox(24);
        container.setPadding(new Insets(24, 36, 48, 36));
        container.setMaxWidth(1160);
        container.setAlignment(Pos.TOP_CENTER);
 
        Label pageTitle = new Label("CHECKOUT");
        pageTitle.setFont(Theme.font(20, FontWeight.BOLD));
        pageTitle.setTextFill(Color.web(Theme.COLOR_DARK));
 
        HBox split = new HBox(32);
        split.setAlignment(Pos.TOP_CENTER);
 
        // --- LEFT COLUMN: ADDRESS & PAYMENT ---
        VBox leftSteps = new VBox(20);
        leftSteps.setPrefWidth(640);
        HBox.setHgrow(leftSteps, Priority.ALWAYS);
 
        // STEP 1: DELIVERY ADDRESS (taken from the Profile's saved addresses)
        VBox addressCard = new VBox(14);
        addressCard.setPadding(new Insets(20));
        addressCard.setStyle("-fx-background-color: " + Theme.COLOR_BG_CARD + "; -fx-background-radius: 8px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 8px;");
        addressCard.setEffect(Theme.subtleShadow());
 
        Label step1Title = new Label("1. DELIVERY ADDRESS");
        step1Title.setFont(Theme.font(14, FontWeight.BOLD));
        step1Title.setTextFill(Color.web(Theme.COLOR_DARK));
 
        // Load the user's saved addresses from their Profile page
        User profileUser = null;
        try {
            profileUser = new UserController().getUser(AuthPage.email);
        } catch (Exception ignored) {
            // Firestore not reachable -> fall back to manual address entry
        }
        List<Address> savedAddresses = profileUser != null && profileUser.getAddresses() != null
                ? new ArrayList<>(profileUser.getAddresses())
                : new ArrayList<>();
 
        // Holds the address selected by the user (null -> manual entry fallback)
        final Address[] selectedAddr = { null };

        // Manual-entry fallback fields (only used when the Profile has no saved
        // addresses).
        final TextField[] addrInputs = new TextField[6];

        Label deliveryFeeVal = new Label();
        deliveryFeeVal.setFont(Theme.font(12, FontWeight.BOLD));

        Label finalVal = new Label();
        finalVal.setFont(Theme.font(18, FontWeight.BOLD));
        finalVal.setTextFill(Color.web(Theme.COLOR_DARK));

        Runnable updateSummaryUI = () -> {
            double fee = AddToCart.getInstance().getDeliveryCharges();

            if (fee == 0) {
                deliveryFeeVal.setText("FREE");
                deliveryFeeVal.setTextFill(Color.web(Theme.COLOR_SUCCESS));
            } else {
                deliveryFeeVal.setText(UIHelper.formatCurrency(fee));
                deliveryFeeVal.setTextFill(Color.web(Theme.COLOR_DARK));
            }
            finalVal.setText(UIHelper.formatCurrency(AddToCart.getInstance().getFinalPayable()));
        };

        if (!savedAddresses.isEmpty()) {
            ToggleGroup addrGroup = new ToggleGroup();
            Address defaultAddr = savedAddresses.stream()
                    .filter(Address::isDefault)
                    .findFirst()
                    .orElse(savedAddresses.get(0));
            selectedAddr[0] = defaultAddr;

            if (defaultAddr != null) {
                String defaultAddrSummary = (defaultAddr.getStreet() == null ? "" : defaultAddr.getStreet()) + ", "
                        + (defaultAddr.getCity() == null ? "" : defaultAddr.getCity()) + ", "
                        + (defaultAddr.getState() == null ? "" : defaultAddr.getState()) + " - "
                        + (defaultAddr.getPinCode() == null ? "" : defaultAddr.getPinCode());
                AddToCart.getInstance().updateDeliveryChargesByAddress(defaultAddrSummary);
            }

            Label selectHint = new Label("Select a saved delivery address:");
            selectHint.setFont(Theme.font(12));
            selectHint.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));
 
            VBox addrOptions = new VBox(8);
            for (Address a : savedAddresses) {
                String addrSummary = (a.getStreet() == null ? "" : a.getStreet()) + ", "
                        + (a.getCity() == null ? "" : a.getCity()) + ", "
                        + (a.getState() == null ? "" : a.getState()) + " - "
                        + (a.getPinCode() == null ? "" : a.getPinCode());
                RadioButton rb = new RadioButton("[" + a.getAddressType() + "] " + a.getFullName()
                        + " | " + addrSummary);
                rb.setFont(Theme.font(12));
                rb.setToggleGroup(addrGroup);
                rb.setStyle("-fx-text-fill: " + Theme.COLOR_DARK + "; -fx-cursor: hand;");
                rb.setSelected(a == defaultAddr);
                rb.setOnAction(e -> {
                    selectedAddr[0] = a;
                    String selectedSummary = (a.getStreet() == null ? "" : a.getStreet()) + ", "
                            + (a.getCity() == null ? "" : a.getCity()) + ", "
                            + (a.getState() == null ? "" : a.getState()) + " - "
                            + (a.getPinCode() == null ? "" : a.getPinCode());
                    AddToCart.getInstance().updateDeliveryChargesByAddress(selectedSummary);
                    updateSummaryUI.run();
                });
                addrOptions.getChildren().add(rb);
            }
 
            Label manageHint = new Label("Manage your addresses from Profile → Saved Addresses.");
            manageHint.setFont(Theme.font(11));
            manageHint.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
 
            addressCard.getChildren().addAll(step1Title, selectHint, addrOptions, manageHint);
        } else {
            Label noAddrHint = new Label(
                    "No saved delivery address found. Please add one from Profile → Saved Addresses, or enter the delivery details below:");
            noAddrHint.setFont(Theme.font(11.5));
            noAddrHint.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
            noAddrHint.setWrapText(true);
 
            User user = SessionModel.getInstance().getCurrentUser();
            String initialName = user != null ? user.getFullName() : "Aditya Sharma";
            String initialMobile = user != null ? user.getMobile() : "9876543210";
 
            addrInputs[0] = UIHelper.createTextField("Contact Name");
            addrInputs[0].setText(initialName);
 
            addrInputs[1] = UIHelper.createTextField("10-digit Mobile Number");
            addrInputs[1].setText(initialMobile);
 
            addrInputs[2] = UIHelper.createTextField("Flat / House No., Apartment, Street");
            addrInputs[2].setText("Flat 402, Lotus Residency, Indiranagar");
 
            HBox cityRow = new HBox(12);
            addrInputs[3] = UIHelper.createTextField("City");
            addrInputs[3].setText("Bengaluru");
            HBox.setHgrow(addrInputs[3], Priority.ALWAYS);
 
            addrInputs[4] = UIHelper.createTextField("State");
            addrInputs[4].setText("Karnataka");
            HBox.setHgrow(addrInputs[4], Priority.ALWAYS);
 
            addrInputs[5] = UIHelper.createTextField("PIN Code");
            addrInputs[5].setText("560038");
            HBox.setHgrow(addrInputs[5], Priority.ALWAYS);
 
            cityRow.getChildren().addAll(addrInputs[3], addrInputs[4], addrInputs[5]);

            Runnable updateManualAddressCharges = () -> {
                String manualAddr = (addrInputs[2].getText() != null ? addrInputs[2].getText() : "") + ", "
                        + (addrInputs[3].getText() != null ? addrInputs[3].getText() : "") + ", "
                        + (addrInputs[4].getText() != null ? addrInputs[4].getText() : "") + " - "
                        + (addrInputs[5].getText() != null ? addrInputs[5].getText() : "");
                AddToCart.getInstance().updateDeliveryChargesByAddress(manualAddr);
                updateSummaryUI.run();
            };

            addrInputs[2].textProperty().addListener((obs, o, n) -> updateManualAddressCharges.run());
            addrInputs[3].textProperty().addListener((obs, o, n) -> updateManualAddressCharges.run());
            addrInputs[5].textProperty().addListener((obs, o, n) -> updateManualAddressCharges.run());
            updateManualAddressCharges.run();

            addressCard.getChildren().addAll(step1Title, noAddrHint, addrInputs[0], addrInputs[1], addrInputs[2],
                    cityRow);
        }
 
        // STEP 2: PAYMENT METHOD
        VBox paymentCard = new VBox(14);
        paymentCard.setPadding(new Insets(20));
        paymentCard.setStyle("-fx-background-color: " + Theme.COLOR_BG_CARD + "; -fx-background-radius: 8px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 8px;");
        paymentCard.setEffect(Theme.subtleShadow());
 
        Label step2Title = new Label("2. SELECT PAYMENT METHOD");
        step2Title.setFont(Theme.font(14, FontWeight.BOLD));
        step2Title.setTextFill(Color.web(Theme.COLOR_DARK));
 
        ToggleGroup payGroup = new ToggleGroup();
 
        RadioButton upiRb = new RadioButton("UPI (Google Pay / PhonePe / Paytm)");
        upiRb.setToggleGroup(payGroup);
        upiRb.setSelected(true);
        upiRb.setUserData("UPI");                       // <-- identifies this option
        upiRb.setFont(Theme.font(13));
        upiRb.setOnAction(e -> selectedPayment = "UPI (Google Pay / PhonePe)");
 
        RadioButton cardRb = new RadioButton("Credit / Debit Card (Visa / Mastercard / Rupay)");
        cardRb.setToggleGroup(payGroup);
        cardRb.setUserData("CARD");                     // <-- identifies this option
        cardRb.setFont(Theme.font(13));
        cardRb.setOnAction(e -> selectedPayment = "Credit / Debit Card");
 
        RadioButton codRb = new RadioButton("Cash on Delivery (Pay at Doorstep)");
        codRb.setToggleGroup(payGroup);
        codRb.setUserData("COD");                       // <-- identifies this option
        codRb.setFont(Theme.font(13));
        codRb.setOnAction(e -> selectedPayment = "Cash on Delivery");
 
        Label simNote = new Label("ℹ Note: Simulated payment environment for seamless evaluation.");
        simNote.setFont(Theme.font(11));
        simNote.setTextFill(Color.web(Theme.COLOR_MUTED_TEXT));
 
        paymentCard.getChildren().addAll(step2Title, upiRb, cardRb, codRb, simNote);
 
        leftSteps.getChildren().addAll(addressCard, paymentCard);
 
        // --- RIGHT COLUMN: ORDER REVIEW & PAY CTA ---
        VBox rightSummary = new VBox(16);
        rightSummary.setPrefWidth(380);
        rightSummary.setMinWidth(360);
        rightSummary.setPadding(new Insets(20));
        rightSummary.setStyle("-fx-background-color: " + Theme.COLOR_BG_SECTION + "; -fx-background-radius: 10px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 10px;");
        rightSummary.setEffect(Theme.cardShadow());
 
        Label summaryTitle = new Label("ORDER SUMMARY");
        summaryTitle.setFont(Theme.font(14, FontWeight.BOLD));
        summaryTitle.setTextFill(Color.web(Theme.COLOR_DARK));
 
        VBox itemsBox = new VBox(8);
        List<AddToCart> cartItems = AddToCart.getInstance().getItems();
        for (AddToCart ci : cartItems) {
            HBox itemRow = new HBox(8);
            itemRow.setAlignment(Pos.CENTER_LEFT);
 
            Label title = new Label(ci.getQuantity() + "x " + ci.getProductName());
            title.setFont(Theme.font(12));
            title.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));
            title.setMaxWidth(230);
 
            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);
 
            Label price = new Label(UIHelper.formatCurrency(ci.getLinePrice()));
            price.setFont(Theme.font(12, FontWeight.BOLD));
 
            itemRow.getChildren().addAll(title, sp, price);
            itemsBox.getChildren().add(itemRow);
        }

        HBox deliveryFeeRow = new HBox();
        deliveryFeeRow.setAlignment(Pos.CENTER_LEFT);
        Label delTitle = new Label("Delivery Fee");
        delTitle.setFont(Theme.font(12));
        delTitle.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));
        Region dSp = new Region();
        HBox.setHgrow(dSp, Priority.ALWAYS);
        deliveryFeeRow.getChildren().addAll(delTitle, dSp, deliveryFeeVal);

        VBox costRows = new VBox(8);
        costRows.getChildren().addAll(
                createSummaryLine("Total MRP", UIHelper.formatCurrency(AddToCart.getInstance().getTotalMRP())),
                createSummaryLine("Discount",
                        "- " + UIHelper.formatCurrency(AddToCart.getInstance().getProductDiscount()
                                + AddToCart.getInstance().getCouponDiscount())),
                deliveryFeeRow);

        HBox finalRow = new HBox();
        finalRow.setAlignment(Pos.CENTER_LEFT);
        Label finalLbl = new Label("Total Payable");
        finalLbl.setFont(Theme.font(15, FontWeight.BOLD));
        finalLbl.setTextFill(Color.web(Theme.COLOR_DARK));
 
        Region fSp = new Region();
        HBox.setHgrow(fSp, Priority.ALWAYS);
        finalRow.getChildren().addAll(finalLbl, fSp, finalVal);

        // Initial summary render
        updateSummaryUI.run();



        Button placeOrderBtn = UIHelper.createPrimaryButton("CONFIRM & PAY NOW");
        placeOrderBtn.setFont(Theme.font(14, FontWeight.BOLD));
        placeOrderBtn.setMaxWidth(Double.MAX_VALUE);
        placeOrderBtn.setPrefHeight(46);

        payGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            boolean isCodSelected = newToggle != null && "COD".equals(newToggle.getUserData());
            placeOrderBtn.setText(isCodSelected ? "CONFIRM" : "CONFIRM & PAY NOW");
        });
        
 
        placeOrderBtn.setOnAction(e -> {
 
            
            Toggle selectedToggle = payGroup.getSelectedToggle();
            String method = selectedToggle != null ? (String) selectedToggle.getUserData() : "UPI";
            isCod = "COD".equals(method);

            System.out.println("Selected payment method: " + method);
 
            double amount = AddToCart.getInstance().getFinalPayable();
            int payableAmt = Math.max(1, (int) Math.round(amount));
 
           
            OrderUser placedOrder;
            Address a = selectedAddr[0];
            if (a != null) {
                
                placedOrder = CheckoutController.getInstance().processOrder(
                        a.getFullName(),
                        a.getMobile(),
                        a.getStreet(),
                        a.getCity(),
                        a.getState(),
                        a.getPinCode(),
                        selectedPayment);
            } else {
                
                placedOrder = CheckoutController.getInstance().processOrder(
                        addrInputs[0].getText(),
                        addrInputs[1].getText(),
                        addrInputs[2].getText(),
                        addrInputs[3].getText(),
                        addrInputs[4].getText(),
                        addrInputs[5].getText(),
                        selectedPayment);
            }
 
            if (placedOrder == null) {
                System.out.println("Order could not be placed.");
                return;
            }
 
            String  orderId = placedOrder.getOrderId();
 
           
            if (isCod) {

                 
                    System.out.println("--------------- In COD ----------------");
                    storePaymentQr(orderId, amount).thenAccept(url -> {
                        if (url == null) {
                            System.err.println("QR upload failed for order " + orderId);
                            return;
                        }
                        try {
                            FirestoreClient.getFirestore()
                                    .collection("Orders")                 // same collection processOrder uses
                                    .document(orderId)                    // same document ID processOrder uses
                                    .set(Map.of("CodURL", url), SetOptions.merge())
                                    .get();                               // OK here: we're on a background thread
                            System.out.println("COD QR URL saved: " + url);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                } else {
                RazorpayController razorpayService = new RazorpayController();
                String razorpayOrderId = razorpayService.createOrder(payableAmt, orderId);
 
                if (razorpayOrderId != null) {
                    String paymentUrl = razorpayService.getPaymentLink(payableAmt, orderId, razorpayOrderId);
                    System.out.println(paymentUrl);
                    try {
                        Desktop.getDesktop().browse(new URI(paymentUrl));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    System.out.println("Razorpay Order Created Successfully. Order ID: " + razorpayOrderId);
                } else {
                    System.out.println("Failed to create Razorpay Order");
                }
            }
 
            // 5) Notification
            OrderController ord = new OrderController();
            User user = new UserController().getUser(AuthPage.email);
            String userName = user != null ? user.getFullName() : "Customer";
            String msg = userName + " placed " + orderId;
            ord.sendNotification(orderId, AuthPage.email, userName, "ORDER_PLACED", msg);
        });

        if(isCod){
            

        }
 
        rightSummary.getChildren().addAll(summaryTitle, itemsBox, UIHelper.createSeparator(), costRows, UIHelper.createSeparator(), finalRow, placeOrderBtn);
 
        split.getChildren().addAll(leftSteps, rightSummary);
        container.getChildren().addAll(pageTitle, split);
 
        StackPane centerWrapper = new StackPane(container);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        centerWrapper.setStyle("-fx-background-color: transparent;");
        setContent(centerWrapper);
    }
 

    private HBox createSummaryLine(String title, String val) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        Label t = new Label(title);
        t.setFont(Theme.font(12));
        t.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label v = new Label(val);
        v.setFont(Theme.font(12, FontWeight.BOLD));
        v.setTextFill(Color.web(Theme.COLOR_DARK));

        row.getChildren().addAll(t, sp, v);
        return row;
    }

    public Scene getCheckoutViewScene(Runnable backAction) {
        if (checkoutScene == null) {
            BorderPane root = new BorderPane();
            NavbarView navbar = new NavbarView();
            root.setTop(navbar);
            root.setCenter(this);

            StackPane overlayContainer = new StackPane(root);
            ToastView toast = new ToastView();
            ChatbotView chatbot = new ChatbotView();
            overlayContainer.getChildren().addAll(toast, chatbot);

            checkoutScene = new Scene(overlayContainer, 1550, 850);
        }
        return checkoutScene;
    }
}
