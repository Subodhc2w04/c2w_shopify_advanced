package com.c2w.view.admin;

import java.util.Map;

import com.c2w.controller.admin.DashBoardController;
import com.c2w.controller.admin.DeliverySignUpController;
import com.c2w.view.authentication.AuthPage;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AdminDashBoard {

        private static final String INK = "#0d1117";
        private static final String PANEL = "#151b24";
        private static final String PANEL_LIGHT = "#1c2430";
        private static final String BORDER = "#293444";
        private static final String TEXT = "#f2f5f7";
        private static final String MUTED = "#8e9aaa";
        private static final String TEAL = "#48d1c8";
        private static final String GOLD = "#f2b866";
        private static final String RED = "#ee7d83";

        public static BorderPane root;
        private Button activeNavButton;
        DashBoardController dashBoardController = new DashBoardController();

        // =========================================================
        // SCENE
        // =========================================================

        public Scene getScene() {

                root = new BorderPane();

                root.setLeft(buildSidebar());
                root.setCenter(buildOverview());

                root.setStyle(
                                "-fx-background-color: " + INK + ";");

                Scene scene = new Scene(root, 1550,850);

                scene.getStylesheets().clear();

                return scene;
        }

        // =========================================================
        // SIDEBAR
        // =========================================================

        private VBox buildSidebar() {

                VBox sidebar = new VBox(8);

                sidebar.setPrefWidth(242);

                sidebar.setPadding(
                                new Insets(28, 18, 22, 18));

                sidebar.setStyle(
                                "-fx-background-color: #111720;" +
                                                "-fx-border-color: " + BORDER + ";" +
                                                "-fx-border-width: 0 1 0 0;");

                Label brand = label("C2W SHOPIFY", 19, TEXT);

                brand.setStyle(
                                "-fx-font-weight: bold;" +
                                                "-fx-letter-spacing: 1.6px;" + "-fx-text-fill:white;");

                Label subtitle = label("COMMAND CENTER", 10, TEAL);

                subtitle.setStyle(
                                "-fx-font-weight: bold;" +
                                                "-fx-letter-spacing: 1.4px;" + "-fx-text-fill:white;");

                VBox brandBlock = new VBox(5, brand, subtitle);

                brandBlock.setPadding(
                                new Insets(0, 10, 34, 10));

                sidebar.getChildren().add(brandBlock);

                /*
                 * EXISTING NAVIGATION
                 * Kept as it was.
                 */

                sidebar.getChildren().addAll(

                                navigationButton(
                                                "Overview",
                                                true,
                                                () -> AuthPage.myStage.setScene(
                                                                getScene())),

                                navigationButton(
                                                "Products",
                                                false,
                                                () -> root.setCenter(
                                                                new AllProducts()
                                                                                .buildProductsPanel(root))),

                                navigationButton(
                                                "Orders",
                                                false,
                                                () -> root.setCenter(
                                                                new OrdersPage()
                                                                                .getTotalOrdersView(root))),

                                navigationButton(
                                                "Customers",
                                                false,
                                                () -> root.setCenter(
                                                                new CustomersPage()
                                                                                .getTotalCustomersView(root))),

                                navigationButton(
                                                "Delivery Partners",
                                                false,
                                                () -> root.setCenter(
                                                                new DeliveryPersonsPage()
                                                                                .getTotalDeliveryPersonsView(root))),

                                /*
                                 * Analytics navigation removed.
                                 */

                                navigationButton(
                                                "Approval Requests",
                                                false,
                                                () -> root.setCenter(
                                                                new RequestApprovalPage()
                                                                                .getApprovalRequestsPage())),

                                navigationButton(
                                                "Notification History",
                                                false,
                                                () -> root.setCenter(
                                                                new NotificationHistoryPage()
                                                                                .getNotificationHistoryPage())));

                Region spacer = new Region();

                VBox.setVgrow(
                                spacer,
                                Priority.ALWAYS);

                sidebar.getChildren().add(spacer);

                sidebar.getChildren().addAll(
                                navigationButton(
                                                "Sign out",
                                                false,
                                                () -> AuthPage.myStage.setScene(
                                                                new AuthPage().showLoginPage())));

                return sidebar;
        }

       private Button navigationButton(
                String text,
                boolean active,
                Runnable action) {

        Button button = new Button(text);

        button.setMaxWidth(
                        Double.MAX_VALUE);

        button.setAlignment(
                        Pos.CENTER_LEFT);

        button.setPadding(
                        new Insets(12, 14, 12, 16));

        button.setStyle(
                        navStyle(active));

        // Set initially active button
        if (active) {
                activeNavButton = button;
        }

        button.setOnMouseEntered(event -> {

                if (button != activeNavButton) {

                        button.setStyle(
                                        navStyle(false) +
                                                        "-fx-background-color: " +
                                                        PANEL_LIGHT +
                                                        ";");
                }
        });

        button.setOnMouseExited(event -> {

                if (button == activeNavButton) {

                        button.setStyle(
                                        navStyle(true));

                } else {

                        button.setStyle(
                                        navStyle(false));
                }
        });

        button.setOnAction(event -> {

                // Remove active style from previous button
                if (activeNavButton != null) {

                        activeNavButton.setStyle(
                                        navStyle(false));
                }

                // Make clicked button active
                activeNavButton = button;

                activeNavButton.setStyle(
                                navStyle(true));

                // Perform navigation
                action.run();
        });

        return button;
}

        private String navStyle(
                        boolean active) {

                return "-fx-background-color: " +
                                (active
                                                ? "#203c42"
                                                : "transparent")
                                +
                                ";" +

                                "-fx-text-fill: " +
                                (active
                                                ? TEAL
                                                : MUTED)
                                +
                                ";" +

                                "-fx-font-size: 13px;" +

                                "-fx-font-weight: " +
                                (active
                                                ? "bold"
                                                : "normal")
                                +
                                ";" +

                                "-fx-background-radius: 6px;" +

                                "-fx-cursor: hand;";
        }

        // =========================================================
        // OVERVIEW
        // =========================================================

        private VBox buildOverview() {

                VBox content = new VBox(22);

                content.setPadding(
                                new Insets(30, 42, 34, 42));

                content.setStyle(
                                "-fx-background-color: " +
                                                INK +
                                                ";");

                // -----------------------------------------------------
                // HEADER
                // -----------------------------------------------------

                HBox header = new HBox(18);

                

                header.setAlignment(
                                Pos.CENTER_LEFT);

                VBox heading = new VBox(
                                5,
                                label(
                                                "Good morning, Admin",
                                                27,
                                                TEXT),
                                label(
                                                "Here is what is happening in your store today.",
                                                13,
                                                MUTED));

                Region headerSpacer = new Region();

                HBox.setHgrow(
                                headerSpacer,
                                Priority.ALWAYS);

                Button addProduct = primaryButton(
                                "+  Add product");

                addProduct.setOnAction(
                                event -> openProducts());


                header.getChildren().addAll(
                                heading,
                                headerSpacer,
                                addProduct);

                // =====================================================
                // SUMMARY CARDS
                // =====================================================

                GridPane metrics = new GridPane();

                metrics.setHgap(14);
                metrics.setVgap(14);

                for (int i = 0; i < 4; i++) {

                        ColumnConstraints column = new ColumnConstraints();

                        column.setPercentWidth(25);

                        metrics.getColumnConstraints()
                                        .add(column);
                }

                Map<String, Object> cardMap = dashBoardController.goToCardsData();

                metrics.add(
                                analyticsMetric(
                                                "TOTAL REVENUE",
                                                String.valueOf(cardMap.get("Revenue")),
                                                "From Orders",
                                                TEAL),
                                0,
                                0);

                metrics.add(
                                analyticsMetric(
                                                "TOTAL ORDERS",
                                                String.valueOf(cardMap.get("OrdersCount")),
                                                "From Orders",
                                                TEAL),
                                1,
                                0);

                metrics.add(
                                analyticsMetric(
                                                "CUSTOMERS",
                                                String.valueOf(cardMap.get("CustomerCount")),
                                                "From Users",
                                                GOLD),
                                2,
                                0);

                metrics.add(
                                analyticsMetric(
                                                "PRODUCTS",
                                                String.valueOf(cardMap.get("ProductsCount")),
                                                "From Products",
                                                RED),
                                3,
                                0);

                // =====================================================
                // CHART ROW 1
                // =====================================================

                HBox chartRowOne = new HBox(16);

                VBox revenuePanel = buildTopSellingProductsChart();

                VBox statusPanel = buildOrderStatusChart();

                HBox.setHgrow(
                                revenuePanel,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                statusPanel,
                                Priority.ALWAYS);

                chartRowOne.getChildren().addAll(
                                revenuePanel,
                                statusPanel);

                // =====================================================
                // CHART ROW 2
                // =====================================================

                HBox chartRowTwo = new HBox(16);

                VBox ordersPanel = buildOrdersChart();

                VBox categoryPanel = buildCategoryChart();

                HBox.setHgrow(
                                ordersPanel,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                categoryPanel,
                                Priority.ALWAYS);

                chartRowTwo.getChildren().addAll(
                                ordersPanel,
                                categoryPanel);

              

                content.getChildren().addAll(
                                header,
                                metrics,
                                chartRowOne,
                                chartRowTwo);

                ScrollPane scrollPane = new ScrollPane(content);

                scrollPane.setFitToWidth(true);

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scrollPane.setStyle(
                                "-fx-background: " +
                                                INK +
                                                ";" +
                                                "-fx-background-color: " +
                                                INK +
                                                ";");

                VBox wrapper = new VBox(scrollPane);

                VBox.setVgrow(
                                scrollPane,
                                Priority.ALWAYS);

                return wrapper;
        }

        // =========================================================
        // SUMMARY CARD
        // =========================================================

        private VBox analyticsMetric(
                        String title,
                        String value,
                        String description,
                        String accent) {

                VBox card = panel(16);

                card.setPrefHeight(118);

                Label titleLabel = label(
                                title,
                                10,
                                MUTED);

                titleLabel.setStyle(
                                "-fx-font-weight: bold;" +
                                                "-fx-letter-spacing: 1px;");

                Label valueLabel = label(
                                value,
                                27,
                                TEXT);

                Label descriptionLabel = label(
                                description,
                                11,
                                accent);

                descriptionLabel.setStyle(
                                "-fx-font-weight: bold;");

                card.getChildren().addAll(
                                titleLabel,
                                valueLabel,
                                descriptionLabel);

                return card;
        }

        // =========================================================
        // REVENUE CHART
        // =========================================================

        private VBox buildTopSellingProductsChart() {

                VBox panel = panel(20);

                panel.setPrefHeight(320);

                HBox title = sectionTitle(
                                "Top Selling Products",
                                "Product sales");

                CategoryAxis xAxis = new CategoryAxis();

                NumberAxis yAxis = new NumberAxis();

                xAxis.setLabel("Product");

                yAxis.setLabel("Units Sold");

                BarChart<String, Number> chart = new BarChart<>(
                                xAxis,
                                yAxis);

                chart.setLegendVisible(false);

                chart.setAnimated(false);

                chart.setPrefHeight(220);

                // =====================================================
                // FIREBASE DATA
                // =====================================================

                Map<String, Integer> topProducts = dashBoardController.gotoTopSellingProducts();

                XYChart.Series<String, Number> series = new XYChart.Series<>();

                int count = 0;

                for (Map.Entry<String, Integer> entry : topProducts.entrySet()) {
                        if (count >= 5) {
                                break;
                        }

                        series.getData().add(
                                        new XYChart.Data<>(
                                                        entry.getKey(),
                                                        entry.getValue()));

                        count++;
                }

                chart.getData().add(series);

                // =====================================================

                panel.getChildren().addAll(
                                title,
                                chart);

                VBox.setVgrow(
                                chart,
                                Priority.ALWAYS);

                return panel;
        }

        // =========================================================
        // ORDER STATUS
        // =========================================================

        private VBox buildOrderStatusChart() {
                Map<String, Integer> statusMap = dashBoardController.goToStatusData();

                VBox panel = panel(20);

                panel.setPrefHeight(320);

                HBox title = sectionTitle(
                                "Order status",
                                "Current orders");

                PieChart chart = new PieChart();
                chart.setStyle(
                                "-fx-pie-label-visible: true;" +
                                                "-fx-pie-label-fill: white;" +
                                                "-fx-pie-label-line-length: 15px;");

                chart.getData().add(
                                new PieChart.Data("ORDERED", statusMap.get("TotalOrderedStatus")));
                /// System.out.println(dashBoardController.goToStatusData().get("TotalOrderedStatus"));

                chart.getData().add(
                                new PieChart.Data("ORDER_READY_FOR_DISPATCH",
                                                statusMap.get("TotalReadyForDispatchStatus")));
                // System.out.println(dashBoardController.goToStatusData().get("TotalReadyForDispatchStatus"));
                chart.getData().add(
                                new PieChart.Data("ACCEPTED", statusMap.get("TotalAcceptedStatus")));
                // System.out.println(dashBoardController.goToStatusData().get("TotalAcceptedStatus"));
                chart.getData().add(
                                new PieChart.Data("PICKED_UP", statusMap.get("TotalPickUpStatus")));
                // System.out.println(dashBoardController.goToStatusData().get("TotalPickUpStatus"));
                chart.getData().add(
                                new PieChart.Data("ORDER_FOR_DELIVERY", statusMap.get("TotalOutForDeliveryStatus")));

                chart.getData().add(
                                new PieChart.Data("DELIVERED", statusMap.get("TotalDeliveredStatus")));

                chart.setAnimated(false);

                chart.setLegendVisible(true);

                chart.setLabelsVisible(true);

                chart.setPrefHeight(220);

                Platform.runLater(() -> {
                        for (Object obj : chart.lookupAll(".chart-pie-label")) {
                                ((javafx.scene.text.Text) obj).setFill(Color.WHITE);
                        }
                });

                panel.getChildren().addAll(
                                title,
                                chart);

                VBox.setVgrow(
                                chart,
                                Priority.ALWAYS);

                return panel;
        }

        // =========================================================
        // ORDERS TREND
        // =========================================================

        private VBox buildOrdersChart() {

                VBox panel = panel(20);

                panel.setPrefHeight(320);

                HBox title = sectionTitle(
                                "Orders trend",
                                "Last 7 days");

                CategoryAxis xAxis = new CategoryAxis();

                NumberAxis yAxis = new NumberAxis();

                xAxis.setLabel("Date");

                yAxis.setLabel("Orders");

                LineChart<String, Number> chart = new LineChart<>(
                                xAxis,
                                yAxis);

                chart.setLegendVisible(false);

                chart.setAnimated(false);

                chart.setCreateSymbols(true);

                chart.setPrefHeight(220);

                // =====================================================
                // FIREBASE DATA
                // =====================================================

                Map<String, Integer> orderGrowth = dashBoardController.gotoOrderGrowth();

                XYChart.Series<String, Number> series = new XYChart.Series<>();

                for (Map.Entry<String, Integer> entry : orderGrowth.entrySet()) {

                        series.getData().add(
                                        new XYChart.Data<>(
                                                        entry.getKey(),
                                                        entry.getValue()));
                }

                chart.getData().add(series);

                // =====================================================

                panel.getChildren().addAll(
                                title,
                                chart);

                VBox.setVgrow(
                                chart,
                                Priority.ALWAYS);

                return panel;
        }
        // =========================================================
        // PRODUCT CATEGORY
        // =========================================================

        private VBox buildCategoryChart() {
                Map<String, Integer> category = dashBoardController.goToCategoryData();
                System.out.println(category);
                VBox panel = panel(20);

                panel.setPrefHeight(320);

                HBox title = sectionTitle(
                                "Products by category",
                                "Product distribution");

                CategoryAxis xAxis = new CategoryAxis();
                NumberAxis yAxis = new NumberAxis();

                xAxis.setLabel("Category");
                yAxis.setLabel("Products");

                BarChart<String, Number> chart = new BarChart<>(
                                xAxis,
                                yAxis);

                XYChart.Series<String, Number> series = new XYChart.Series<>();

                series.getData().add(
                                new XYChart.Data<>("Electronics", category.get("Electronics")));

                series.getData().add(
                                new XYChart.Data<>("Clothing", category.get("Clothing")));

                series.getData().add(
                                new XYChart.Data<>("Books", category.get("Books")));

                series.getData().add(
                                new XYChart.Data<>("Shoes", category.get("Shoes")));

                series.getData().add(
                                new XYChart.Data<>("Accessories", category.get("Accessories")));

                chart.getData().add(series);
                chart.setAnimated(false);

                chart.setLegendVisible(false);

                chart.setPrefHeight(220);

                panel.getChildren().addAll(
                                title,
                                chart
                                );

                VBox.setVgrow(
                                chart,
                                Priority.ALWAYS);

                return panel;
        }
        // =========================================================
        // COMMON UI
        // =========================================================

        private HBox sectionTitle(
                        String title,
                        String action) {

                HBox row = new HBox();

                row.setAlignment(
                                Pos.CENTER_LEFT);

                Label heading = label(
                                title,
                                16,
                                TEXT);

                heading.setStyle(
                                "-fx-font-weight: bold;");

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                Label actionLabel = label(
                                action,
                                11,
                                TEAL);

                row.getChildren().addAll(
                                heading,
                                spacer,
                                actionLabel);

                return row;
        }

        private VBox panel(
                        double padding) {

                VBox panel = new VBox(16);

                panel.setPadding(
                                new Insets(padding));

                panel.setStyle(
                                "-fx-background-color: " +
                                                PANEL +
                                                ";" +

                                                "-fx-border-color: " +
                                                BORDER +
                                                ";" +

                                                "-fx-border-radius: 7px;" +

                                                "-fx-background-radius: 7px;");

                return panel;
        }

        private Button primaryButton(
                        String text) {

                Button button = new Button(text);

                button.setPadding(
                                new Insets(
                                                11,
                                                18,
                                                11,
                                                18));

                button.setStyle(
                                "-fx-background-color: " +
                                                TEAL +
                                                ";" +

                                                "-fx-text-fill: " +
                                                INK +
                                                ";" +

                                                "-fx-font-weight: bold;" +

                                                "-fx-background-radius: 5px;" +

                                                "-fx-cursor: hand;");

                return button;
        }

        private Label label(
                        String text,
                        int size,
                        String color) {

                Label label = new Label(text);

                label.setStyle(
                                "-fx-text-fill: " +
                                                color +
                                                ";" +

                                                "-fx-font-size: " +
                                                size +
                                                "px;");

                return label;
        }

        // =========================================================
        // EXISTING PRODUCT NAVIGATION
        // =========================================================

        private void openProducts() {

                Runnable back = () -> AuthPage.myStage.setScene(
                                getBackToAdminDashBoard());

                root.setCenter(
                                new ProductAddPage()
                                                .getProductAddPage(back));
        }

        // =========================================================
        // BACK TO DASHBOARD
        // =========================================================

        public Scene getBackToAdminDashBoard() {

                return new AdminDashBoard().getScene();
        }
}