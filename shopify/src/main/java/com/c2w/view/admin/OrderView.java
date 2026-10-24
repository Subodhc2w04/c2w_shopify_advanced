package com.c2w.view.admin;

import java.io.File;
import java.io.FileOutputStream;

import com.c2w.model.admin.Orders;
import com.c2w.model.admin.ShippingAddress;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class OrderView {
    public VBox showOrderDetails(
            Orders order,
            Runnable back) {

        VBox rootBox = new VBox(0);

        rootBox.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE);

        rootBox.setStyle(
                "-fx-background-color: #0B1017;");

        // =====================================================
        // TOP BAR
        // =====================================================

        Button closeButton = new Button("←  Close");

        closeButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #36CFC9;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8px 15px;" +
                        "-fx-cursor: hand;");

        closeButton.setOnAction(
                e -> back.run());

        // =====================================================
        // DOWNLOAD BUTTON
        // =====================================================

        Button downloadButton = new Button("↓  Download Order Slip");

        downloadButton.setStyle(
                "-fx-background-color: #36CFC9;" +
                        "-fx-text-fill: #0B1017;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8px 15px;" +
                        "-fx-cursor: hand;");

        downloadButton.setOnAction(e -> {

            Window window = downloadButton
                    .getScene()
                    .getWindow();

            downloadOrderSlip(
                    window,
                    order);
        });

        // =====================================================
        // TOP BAR
        // =====================================================

        HBox topBar = new HBox();

        topBar.setAlignment(
                Pos.CENTER_LEFT);

        topBar.setPadding(
                new Insets(
                        20,
                        30,
                        10,
                        30));

        topBar.setStyle(
                "-fx-background-color: #111821;" +
                        "-fx-border-color: #273442;" +
                        "-fx-border-width: 0 0 1 0;");

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        topBar.getChildren().addAll(
                closeButton,
                spacer,
                downloadButton);

        // =====================================================
        // TITLE
        // =====================================================

        Label title = new Label(
                "Order " +
                        order.getOrderId());

        title.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #F5F7FA;");

        Label subtitle = new Label(
                "Complete order information");

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #8A9BB0;");

        VBox titleBox = new VBox(5);

        titleBox.getChildren().addAll(
                title,
                subtitle);

        // =====================================================
        // ORDER INFORMATION
        // =====================================================

        VBox orderInfoCard = createCard();

        Label orderInfoTitle = sectionTitle(
                "Order Information");

        GridPane orderGrid = new GridPane();

        orderGrid.setHgap(80);
        orderGrid.setVgap(25);

        orderGrid.add(
                createDetailBox(
                        "Order ID",
                        order.getOrderId()),
                0,
                0);

        orderGrid.add(
                createDetailBox(
                        "Order Date",
                        order.getOrderDate()),
                1,
                0);

        orderGrid.add(
                createDetailBox(
                        "Expected Delivery",
                        order.getExpectedDelivery()),
                0,
                1);

        orderGrid.add(
                createDetailBox(
                        "Status",
                        order.getStatus()),
                1,
                1);

        orderInfoCard.getChildren().addAll(
                orderInfoTitle,
                orderGrid);

        // =====================================================
        // PAYMENT INFORMATION
        // =====================================================

        VBox paymentCard = createCard();

        Label paymentTitle = sectionTitle(
                "Payment Information");

        GridPane paymentGrid = new GridPane();

        paymentGrid.setHgap(80);
        paymentGrid.setVgap(25);

        paymentGrid.add(
                createDetailBox(
                        "Payment Method",
                        order.getPaymentMethod()),
                0,
                0);

        paymentGrid.add(
                createDetailBox(
                        "Payment Status",
                        order.getPaymentStatus()),
                1,
                0);

        paymentCard.getChildren().addAll(
                paymentTitle,
                paymentGrid);

        // =====================================================
        // ITEMS
        // =====================================================

        VBox itemsSection = new VBox(15);

        Label itemsTitle = sectionTitle(
                "Ordered Items");

        itemsSection.getChildren().add(
                itemsTitle);

        if (order.getItems() != null &&
                !order.getItems().isEmpty()) {

            for (var item : order.getItems()) {

                VBox itemCard = createCard();

                Label productName = new Label(
                        item.getProductName());

                productName.setStyle(
                        "-fx-font-size: 17px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: #36CFC9;");

                GridPane itemGrid = new GridPane();

                itemGrid.setHgap(70);
                itemGrid.setVgap(15);

                itemGrid.add(
                        createDetailBox(
                                "Product ID",
                                item.getProductId()),
                        0,
                        0);

                itemGrid.add(
                        createDetailBox(
                                "Quantity",
                                String.valueOf(
                                        item.getQuantity())),
                        1,
                        0);

                itemGrid.add(
                        createDetailBox(
                                "Product Price",
                                "₹" +
                                        item.getProductPrice()),
                        0,
                        1);

                itemGrid.add(
                        createDetailBox(
                                "Original Price",
                                "₹" +
                                        item.getOriginalPrice()),
                        1,
                        1);

                itemGrid.add(
                        createDetailBox(
                                "Line MRP",
                                "₹" +
                                        item.getLineMRP()),
                        0,
                        2);

                itemGrid.add(
                        createDetailBox(
                                "Line Price",
                                "₹" +
                                        item.getLinePrice()),
                        1,
                        2);

                itemGrid.add(
                        createDetailBox(
                                "Discount",
                                "₹" +
                                        item.getDiscountValue()),
                        0,
                        3);

                itemGrid.add(
                        createDetailBox(
                                "Savings",
                                "₹" +
                                        item.getLineSavings()),
                        1,
                        3);

                itemCard.getChildren().addAll(
                        productName,
                        itemGrid);

                itemsSection.getChildren().add(
                        itemCard);
            }
        }

        // =====================================================
        // SHIPPING ADDRESS
        // =====================================================

        VBox shippingCard = createCard();

        Label shippingTitle = sectionTitle(
                "Shipping Address");

        ShippingAddress address = order.getShippingAddress();

        if (address != null) {

            GridPane shippingGrid = new GridPane();

            shippingGrid.setHgap(80);
            shippingGrid.setVgap(20);

            shippingGrid.add(
                    createDetailBox(
                            "Address Type",
                            address.getAddressType()),
                    0,
                    0);

            shippingGrid.add(
                    createDetailBox(
                            "Full Name",
                            address.getFullName()),
                    1,
                    0);

            shippingGrid.add(
                    createDetailBox(
                            "Mobile",
                            address.getMobile()),
                    0,
                    1);

            shippingGrid.add(
                    createDetailBox(
                            "Street",
                            address.getStreet()),
                    1,
                    1);

            shippingGrid.add(
                    createDetailBox(
                            "City",
                            address.getCity()),
                    0,
                    2);

            shippingGrid.add(
                    createDetailBox(
                            "State",
                            address.getState()),
                    1,
                    2);

            shippingGrid.add(
                    createDetailBox(
                            "PIN Code",
                            address.getPinCode()),
                    0,
                    3);

            shippingGrid.add(
                    createDetailBox(
                            "Formatted Address",
                            address.getFormattedAddress()),
                    1,
                    3);

            shippingCard.getChildren().addAll(
                    shippingTitle,
                    shippingGrid);

        } else {

            shippingCard.getChildren().addAll(
                    shippingTitle,
                    new Label(
                            "No shipping address available"));
        }

        // =====================================================
        // PRICE SUMMARY
        // =====================================================

        VBox priceCard = createCard();

        Label priceTitle = sectionTitle(
                "Price Summary");

        GridPane priceGrid = new GridPane();

        priceGrid.setHgap(80);
        priceGrid.setVgap(20);

        priceGrid.add(
                createDetailBox(
                        "Total MRP",
                        "₹" +
                                order.getTotalMRP()),
                0,
                0);

        priceGrid.add(
                createDetailBox(
                        "Discount",
                        "₹" +
                                order.getDiscount()),
                1,
                0);

        priceGrid.add(
                createDetailBox(
                        "Delivery Charges",
                        "₹" +
                                order.getDeliveryCharges()),
                0,
                1);

        priceGrid.add(
                createDetailBox(
                        "Final Amount",
                        "₹" +
                                order.getFinalAmount()),
                1,
                1);

        priceCard.getChildren().addAll(
                priceTitle,
                priceGrid);

        // =====================================================
        // CONTENT
        // =====================================================

        VBox content = new VBox(25);

        content.setPadding(
                new Insets(30));

        content.setMaxWidth(
                Double.MAX_VALUE);

        content.getChildren().addAll(
                titleBox,
                orderInfoCard,
                paymentCard,
                itemsSection,
                shippingCard,
                priceCard);

        // =====================================================
        // SCROLL
        // =====================================================

        ScrollPane scrollPane = new ScrollPane(content);

        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollPane.setStyle(
                "-fx-background-color: #0B1017;" +
                        "-fx-background: #0B1017;");

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS);

        // =====================================================
        // ROOT
        // =====================================================

        rootBox.getChildren().addAll(
                topBar,
                scrollPane);

        return rootBox;
    }

    private void downloadOrderSlip(
            Window window,
            Orders order) {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle(
                "Save Order Slip");

        fileChooser.setInitialFileName(
                "Order_" +
                        order.getOrderId() +
                        ".pdf");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "PDF Files",
                        "*.pdf"));

        File file = fileChooser.showSaveDialog(window);

        if (file == null) {
            return;
        }

        Document document = new Document();

        try {

            PdfWriter.getInstance(
                    document,
                    new FileOutputStream(file));

            document.open();

            // =================================================
            // FONTS
            // =================================================

            Font titleFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    20);

            Font headingFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    14);

            Font normalFont = FontFactory.getFont(
                    FontFactory.HELVETICA,
                    10);

            Font boldFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    10);

            // =================================================
            // HEADER
            // =================================================

            Paragraph shopName = new Paragraph(
                    "C2W SHOPIFY",
                    titleFont);

            shopName.setAlignment(
                    Element.ALIGN_CENTER);

            document.add(shopName);

            Paragraph slipTitle = new Paragraph(
                    "ORDER SLIP",
                    headingFont);

            slipTitle.setAlignment(
                    Element.ALIGN_CENTER);

            document.add(slipTitle);

            document.add(
                    new Paragraph(" "));

            // =================================================
            // ORDER INFORMATION
            // =================================================

            document.add(
                    new Paragraph(
                            "Order Information",
                            headingFont));

            document.add(
                    new Paragraph(
                            "Order ID : " +
                                    safe(order.getOrderId()),
                            normalFont));

            document.add(
                    new Paragraph(
                            "Order Date : " +
                                    safe(order.getOrderDate()),
                            normalFont));

            document.add(
                    new Paragraph(
                            "Expected Delivery : " +
                                    safe(order.getExpectedDelivery()),
                            normalFont));

            document.add(
                    new Paragraph(
                            "Order Status : " +
                                    safe(order.getStatus()),
                            normalFont));

            document.add(
                    new Paragraph(" "));

            // =================================================
            // PAYMENT
            // =================================================

            document.add(
                    new Paragraph(
                            "Payment Information",
                            headingFont));

            document.add(
                    new Paragraph(
                            "Payment Method : " +
                                    safe(order.getPaymentMethod()),
                            normalFont));

            document.add(
                    new Paragraph(
                            "Payment Status : " +
                                    safe(order.getPaymentStatus()),
                            normalFont));

            document.add(
                    new Paragraph(" "));

            // =================================================
            // ORDER ITEMS
            // =================================================

            document.add(
                    new Paragraph(
                            "Ordered Items",
                            headingFont));

            document.add(
                    new Paragraph(" "));

            PdfPTable itemTable = new PdfPTable(5);

            itemTable.setWidthPercentage(100);

            itemTable.setWidths(
                    new float[] {
                            3.5f,
                            1,
                            1.5f,
                            1.5f,
                            1.5f
                    });

            addPdfCell(
                    itemTable,
                    "Product",
                    boldFont);

            addPdfCell(
                    itemTable,
                    "Qty",
                    boldFont);

            addPdfCell(
                    itemTable,
                    "Price",
                    boldFont);

            addPdfCell(
                    itemTable,
                    "Discount",
                    boldFont);

            addPdfCell(
                    itemTable,
                    "Total",
                    boldFont);

            if (order.getItems() != null &&
                    !order.getItems().isEmpty()) {

                for (var item : order.getItems()) {

                    addPdfCell(
                            itemTable,
                            safe(item.getProductName()),
                            normalFont);

                    addPdfCell(
                            itemTable,
                            String.valueOf(
                                    item.getQuantity()),
                            normalFont);

                    addPdfCell(
                            itemTable,
                            "₹" +
                                    item.getProductPrice(),
                            normalFont);

                    addPdfCell(
                            itemTable,
                            "₹" +
                                    item.getDiscountValue(),
                            normalFont);

                    addPdfCell(
                            itemTable,
                            "₹" +
                                    item.getLinePrice(),
                            normalFont);
                }

            } else {

                PdfPCell emptyCell = new PdfPCell(
                        new Phrase(
                                "No items"));

                emptyCell.setColspan(5);

                itemTable.addCell(emptyCell);
            }

            document.add(itemTable);

            document.add(
                    new Paragraph(" "));

            // =================================================
            // SHIPPING ADDRESS
            // =================================================

            document.add(
                    new Paragraph(
                            "Shipping Address",
                            headingFont));

            ShippingAddress address = order.getShippingAddress();

            if (address != null) {

                document.add(
                        new Paragraph(
                                "Address Type : " +
                                        safe(
                                                address.getAddressType()),
                                normalFont));

                document.add(
                        new Paragraph(
                                "Full Name : " +
                                        safe(
                                                address.getFullName()),
                                normalFont));

                document.add(
                        new Paragraph(
                                "Mobile : " +
                                        safe(
                                                address.getMobile()),
                                normalFont));

                document.add(
                        new Paragraph(
                                "Street : " +
                                        safe(
                                                address.getStreet()),
                                normalFont));

                document.add(
                        new Paragraph(
                                "City : " +
                                        safe(
                                                address.getCity()),
                                normalFont));

                document.add(
                        new Paragraph(
                                "State : " +
                                        safe(
                                                address.getState()),
                                normalFont));

                document.add(
                        new Paragraph(
                                "PIN Code : " +
                                        safe(
                                                address.getPinCode()),
                                normalFont));

                document.add(
                        new Paragraph(
                                "Address : " +
                                        safe(
                                                address.getFormattedAddress()),
                                normalFont));

            } else {

                document.add(
                        new Paragraph(
                                "No shipping address available",
                                normalFont));
            }

            document.add(
                    new Paragraph(" "));

            // =================================================
            // PRICE SUMMARY
            // =================================================

            document.add(
                    new Paragraph(
                            "Price Summary",
                            headingFont));

            document.add(
                    new Paragraph(
                            "Total MRP : ₹" +
                                    order.getTotalMRP(),
                            normalFont));

            document.add(
                    new Paragraph(
                            "Discount : ₹" +
                                    order.getDiscount(),
                            normalFont));

            document.add(
                    new Paragraph(
                            "Delivery Charges : ₹" +
                                    order.getDeliveryCharges(),
                            normalFont));

            document.add(
                    new Paragraph(" "));

            Paragraph finalAmount = new Paragraph(
                    "FINAL AMOUNT : ₹" +
                            order.getFinalAmount(),
                    headingFont);

            finalAmount.setAlignment(
                    Element.ALIGN_RIGHT);

            document.add(finalAmount);

            document.add(
                    new Paragraph(" "));

            // =================================================
            // FOOTER
            // =================================================

            Paragraph footer = new Paragraph(
                    "Thank you for shopping with C2W Shopify!",
                    normalFont);

            footer.setAlignment(
                    Element.ALIGN_CENTER);

            document.add(footer);

            // =================================================
            // CLOSE PDF
            // =================================================

            document.close();

            System.out.println(
                    "Order slip saved at: "
                            + file.getAbsolutePath());

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void addPdfCell(
            PdfPTable table,
            String text,
            Font font) {

        PdfPCell cell = new PdfPCell(
                new Phrase(
                        safe(text),
                        font));

        cell.setPadding(6);

        table.addCell(cell);
    }

    private String safe(Object value) {

        if (value == null) {
            return "";
        }

        return String.valueOf(value);
    }
    // =========================================================
    // CARD
    // =========================================================

    private VBox createCard() {

        VBox card =
                new VBox(20);

        card.setPadding(
                new Insets(25)
        );

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.setStyle(
                "-fx-background-color: #151C25;" +
                "-fx-border-color: #273442;" +
                "-fx-border-radius: 8px;" +
                "-fx-background-radius: 8px;"
        );

        return card;
    }

    // =========================================================
    // SECTION TITLE
    // =========================================================

    private Label sectionTitle(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #F5F7FA;"
        );

        return label;
    }

    // =========================================================
    // DETAIL BOX
    // =========================================================

    private VBox createDetailBox(
            String title,
            String value) {

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #8A9BB0;"
        );

        Label valueLabel =
                new Label(
                        value == null
                                ? "-"
                                : value
                );

        valueLabel.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-text-fill: #F5F7FA;"
        );

        valueLabel.setWrapText(true);

        VBox box =
                new VBox(5);

        box.getChildren().addAll(
                titleLabel,
                valueLabel
        );

        return box;
    }

}
