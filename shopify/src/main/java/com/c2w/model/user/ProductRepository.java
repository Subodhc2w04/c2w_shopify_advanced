package com.c2w.model.user;

import com.c2w.controller.user.CatalogController;
import com.c2w.controller.user.ProductController;

import java.util.*;
import java.util.stream.Collectors;

public class ProductRepository {
    private static ProductRepository instance;
    private final List<ProductsforUser> products = new ArrayList<>();
    private boolean loaded = false;

    private ProductRepository() {
        loadFromFirebase();
    }

    public static synchronized ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }

    public synchronized void loadFromFirebase() {
        try {
            List<ProductsforUser> fetched = ProductController.getInstance().getAllProducts();
            products.clear();
            if (fetched != null && !fetched.isEmpty()) {
                products.addAll(fetched);
            }
            loaded = true;
        } catch (Exception e) {
            System.err.println("ProductRepository - Failed to load products from Firebase: " + e.getMessage());
        }
    }

    public synchronized void refresh() {
        loadFromFirebase();
    }

    public List<ProductsforUser> getAll() {
        if (!loaded) {
            loadFromFirebase();
        }
        return Collections.unmodifiableList(products);
    }

    public List<ProductsforUser> getTrending() {
        List<ProductsforUser> all = getAll();
        return all.stream()
                .limit(8)
                .collect(Collectors.toList());
    }

    public List<ProductsforUser> getRecommended() {
        List<ProductsforUser> all = getAll();
        return all.stream()
                .filter(p -> p.getRating() >= 4.0)
                .limit(8)
                .collect(Collectors.toList());
    }

    public List<ProductsforUser> getByCategory(String category) {
        if (category == null || category.equalsIgnoreCase("All") || category.trim().isEmpty()) {
            return getAll();
        }
        List<ProductsforUser> all = getAll();
        List<ProductsforUser> filtered = all.stream()
                .filter(p -> CatalogController.matchesCategory(p.getProductCategory(), category))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            // Fetch directly from DAO/controller for this category
            try {
                List<ProductsforUser> fromDb = ProductController.getInstance().getProductsByCategory(category);
                if (fromDb != null && !fromDb.isEmpty()) {
                    return fromDb;
                }
            } catch (Exception ignored) {
            }
        }
        return filtered;
    }

    public Optional<ProductsforUser> getById(String id) {
        if (id == null) return Optional.empty();
        Optional<ProductsforUser> found = getAll().stream()
                .filter(p -> p.getProductId() != null && p.getProductId().equalsIgnoreCase(id))
                .findFirst();
        if (found.isPresent()) {
            return found;
        }
        // Fallback: fetch by ID directly from DB
        try {
            ProductsforUser direct = ProductController.getInstance().getProductById(id);
            return Optional.ofNullable(direct);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
