package com.c2w.view.user;

import com.c2w.controller.user.CatalogController;
import com.c2w.model.user.ProductsforUser;
import com.c2w.view.user.ChatbotView;
import com.c2w.view.user.EmptyStateView;
import com.c2w.view.user.NavbarView;
import com.c2w.view.user.ProductCardView;
import com.c2w.view.user.ToastView;
import com.c2w.view.user.Theme;
import com.c2w.view.user.UIHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

import java.util.List;

public class CatalogView extends ScrollPane {
    private String selectedCategory = "All";
    private String searchQuery = "";
    private double maxPrice = 100000;
    private double minRating = 0;
    private String sortBy = "Popularity";

    private final FlowPane productGrid;
    private final Label resultCountLbl;
    private final StackPane gridContainer;
    private Scene catalogScene;
    private Button backBtn;
    private static final String TEXT = "#f2f5f7";
    public CatalogView() {
        this("All");
    }

    public CatalogView(String initialFilter) {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: " + Theme.COLOR_BG_PAGE + "; -fx-border-color: transparent;");

        if (initialFilter != null && !initialFilter.trim().isEmpty()) {
            String init = initialFilter.trim();
            if (isCategoryFilter(init)) {
                this.selectedCategory = normalizeCategory(init);
            } else {
                this.searchQuery = init;
            }
        }

        HBox mainLayout = new HBox(24);
        mainLayout.setPadding(new Insets(20, 32, 40, 32));
        mainLayout.setMaxWidth(1300);

        // --- 1. LEFT FILTER SIDEBAR ---
        VBox sidebar = new VBox(20);
        sidebar.setPrefWidth(240);
        sidebar.setMinWidth(240);
        sidebar.setPadding(new Insets(18));
        sidebar.setStyle("-fx-background-color: " + Theme.COLOR_BG_SECTION + "; -fx-background-radius: 8px; " +
                "-fx-border-color: " + Theme.COLOR_BORDER + "; -fx-border-radius: 8px;");

        Label filterHeader = new Label("FILTERS");
        filterHeader.setFont(Theme.font(14, FontWeight.BOLD));
        filterHeader.setTextFill(Color.web(Theme.COLOR_DARK));

        Button clearFilterBtn = new Button("CLEAR ALL");
        clearFilterBtn.setFont(Theme.font(11, FontWeight.BOLD));
        clearFilterBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Theme.COLOR_PRIMARY + "; -fx-cursor: hand;");
        clearFilterBtn.setOnAction(e -> {
            selectedCategory = "All";
            searchQuery = "";
            maxPrice = 100000;
            minRating = 0;
            refreshCatalog();
        });

        HBox filterTop = new HBox();
        filterTop.setAlignment(Pos.CENTER_LEFT);
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        filterTop.getChildren().addAll(filterHeader, sp, clearFilterBtn);

        // Category Filter
        VBox catFilterBox = new VBox(8);
        Label catTitle = new Label("CATEGORIES");
        catTitle.setFont(Theme.font(11.5, FontWeight.BOLD));
        catTitle.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));
        catFilterBox.getChildren().add(catTitle);

        ToggleGroup catGroup = new ToggleGroup();
        String[] cats = {"All", "Clothing", "Accessories", "Shoes", "Electronics", "Book"};
        for (String c : cats) {
            RadioButton rb = new RadioButton(c);
            rb.setToggleGroup(catGroup);
            rb.setSelected(c.equalsIgnoreCase(selectedCategory));
            rb.setFont(Theme.font(12));
            rb.setOnAction(e -> {
                selectedCategory = c;
                refreshCatalog();
            });
            catFilterBox.getChildren().add(rb);
        }

        // Price Slider Filter
        VBox priceBox = new VBox(8);
        Label priceTitle = new Label("MAX PRICE");
        priceTitle.setFont(Theme.font(11.5, FontWeight.BOLD));
        priceTitle.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));

        Label priceValueLbl = new Label("Up to "+UIHelper.formatCurrency(maxPrice));
        priceValueLbl.setFont(Theme.font(12, FontWeight.BOLD));
        priceValueLbl.setTextFill(Color.web(Theme.COLOR_PRIMARY));

        Slider priceSlider = new Slider(500, maxPrice, maxPrice);
        priceSlider.setBlockIncrement(500);
        priceSlider.valueProperty().addListener((obs, oldV, newV) -> {
            maxPrice = newV.doubleValue();
            priceValueLbl.setText("Up to " + UIHelper.formatCurrency(maxPrice));
            refreshCatalog();
        });

        priceBox.getChildren().addAll(priceTitle, priceValueLbl, priceSlider);

        // Brand Filter
        // VBox brandBox = new VBox(8);
        // Label brandTitle = new Label("BRAND");
        // brandTitle.setFont(Theme.font(11.5, FontWeight.BOLD));
        // brandTitle.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));

                VBox ratingBox = new VBox(8);
        Label ratingTitle = new Label("MINIMUM RATING");
        ratingTitle.setFont(Theme.font(11.5, FontWeight.BOLD));
        ratingTitle.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));

        ComboBox<String> ratingCombo = new ComboBox<>();
        ratingCombo.getItems().addAll("All Ratings", "4.0 ★ & Above", "4.5 ★ & Above");
        ratingCombo.setValue("All Ratings");
        ratingCombo.setMaxWidth(Double.MAX_VALUE);
        ratingCombo.setStyle("-fx-background-color: "  + "; -fx-border-color: " 
                + "; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-text-fill: " + TEXT
                + "; -fx-prompt-text-fill: #ffffff; -fx-font-size: 13px;");
        
        ratingCombo.setOnAction(e -> {
            if ("4.5 ★ & Above".equals(ratingCombo.getValue())) minRating = 4.5;
            else if ("4.0 ★ & Above".equals(ratingCombo.getValue())) minRating = 4.0;
            else minRating = 0;
            refreshCatalog();
        });

        ratingBox.getChildren().addAll(ratingTitle, ratingCombo);

        sidebar.getChildren().addAll(filterTop, UIHelper.createSeparator(), catFilterBox, UIHelper.createSeparator(), priceBox, UIHelper.createSeparator(),  ratingBox);

        // --- 2. RIGHT CATALOG CONTENT ---
        VBox catalogContent = new VBox(16);
        HBox.setHgrow(catalogContent, Priority.ALWAYS);

        // Sort & Result Bar
        HBox topBar = new HBox(12);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 10, 0));

        resultCountLbl = new Label("Showing products");
        resultCountLbl.setFont(Theme.font(13));
        resultCountLbl.setTextFill(Color.web(Theme.COLOR_SECONDARY_TEXT));

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        Label sortLbl = new Label("Sort By:");
        sortLbl.setFont(Theme.font(12, FontWeight.BOLD));
        sortLbl.setTextFill(Color.web(Theme.COLOR_DARK));

        ComboBox<String> sortCombo = new ComboBox<>();
        sortCombo.getItems().addAll("Popularity", "Price: Low to High", "Price: High to Low", "Customer Rating", "Newest Arrivals");
        sortCombo.setValue(sortBy);
        sortCombo.setStyle("-fx-background-color: "  + "; -fx-border-color: " 
                + "; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-text-fill: " + TEXT
                + "; -fx-prompt-text-fill: #ffffff; -fx-font-size: 13px;");
        sortCombo.setOnAction(e -> {
            sortBy = sortCombo.getValue();
            refreshCatalog();
        });

        backBtn = UIHelper.createGhostButton("← Back");
        topBar.getChildren().addAll(backBtn, resultCountLbl, topSpacer, sortLbl, sortCombo);

        // Product FlowPane Grid
        productGrid = new FlowPane(16, 18);
        productGrid.setAlignment(Pos.TOP_LEFT);

        gridContainer = new StackPane(productGrid);
        gridContainer.setAlignment(Pos.TOP_LEFT);

        catalogContent.getChildren().addAll(topBar, gridContainer);
        mainLayout.getChildren().addAll(sidebar, catalogContent);

        StackPane centerWrapper = new StackPane(mainLayout);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        centerWrapper.setStyle("-fx-background-color: transparent;");
        setContent(centerWrapper);
        refreshCatalog();
    }

    public Scene getCatalogViewScene(Runnable backAction) {
        if (backBtn != null && backAction != null) {
            backBtn.setOnAction(e -> backAction.run());
        }
        BorderPane rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + Theme.COLOR_BG_PAGE + ";");
        rootLayout.setTop(new NavbarView());
        rootLayout.setCenter(this);
        StackPane root = new StackPane(rootLayout, new ToastView(), new ChatbotView());
        catalogScene = new Scene(root, 1550,850);
        catalogScene.setFill(Theme.PAINT_BG_PAGE);
        return catalogScene;
    }

    public Scene getProductPageScene(Runnable backAction) {
        return getCatalogViewScene(backAction);
    }

    private void refreshCatalog() {
        productGrid.getChildren().clear();

        List<ProductsforUser> matches = CatalogController.getInstance().filterAndSort(
                selectedCategory, maxPrice, minRating, sortBy, searchQuery
        );
        //  List<ProductsforUser> matches = ProductRepository.getInstance().getTrending();
        System.out.println("========matches"+matches.size());

        resultCountLbl.setText("Showing " + matches.size() + " items for " + selectedCategory + (searchQuery.isEmpty() ? "" : " matching \"" + searchQuery + "\""));

        if (matches.isEmpty()) {
            EmptyStateView emptyView = new EmptyStateView("🔍", "No Products Found",
                    "We couldn't find any styles matching your selected filters. Try broadening your criteria.",
                    "CLEAR FILTERS", () -> {
                selectedCategory = "All";
                searchQuery = "";
                maxPrice = 10000;
                minRating = 0;
                refreshCatalog();
            });
            gridContainer.getChildren().setAll(emptyView);
        } else {
            for (ProductsforUser p : matches) {
                productGrid.getChildren().add(new ProductCardView(p));
            }
            gridContainer.getChildren().setAll(productGrid);
        }
    }

    private boolean isCategoryFilter(String text) {
        if (text == null) return false;
        String s = text.trim().toLowerCase();
        return s.equals("all") || s.equals("clothing") || s.equals("clothes") || s.equals("apparel")
                || s.equals("men") || s.equals("women") || s.equals("kids")
                || s.equals("accessories") || s.equals("accessory")
                || s.equals("shoes") || s.equals("shoe") || s.equals("footwear") || s.equals("footware")
                || s.equals("Electronics") || s.equals("electronic") || s.equals("tech")
                || s.equals("book") || s.equals("books");
    }

    private String normalizeCategory(String text) {
        if (text == null) return "All";
        String s = text.trim().toLowerCase();
        if (s.equals("clothing") || s.equals("clothes") || s.equals("apparel") || s.equals("men") || s.equals("women") || s.equals("kids")) {
            return "Clothing";
        }
        if (s.equals("accessories") || s.equals("accessory")) {
            return "Accessories";
        }
        if (s.equals("shoes") || s.equals("shoe") || s.equals("footwear") || s.equals("footware")) {
            return "Shoes";
        }
        if (s.equals("Electronics") || s.equals("electronic") || s.equals("tech")) {
            return "Electronics";
        }
        if (s.equals("book") || s.equals("books")) {
            return "Book";
        }
        return "All";
    }
}
