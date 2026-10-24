package com.c2w.controller.user;

import com.c2w.model.user.ProductsforUser;
import com.c2w.model.user.ProductRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class CatalogController {
    private static CatalogController instance;

    private CatalogController() {}

    public static synchronized CatalogController getInstance() {
        if (instance == null) {
            instance = new CatalogController();
        }
        return instance;
    }

    public List<ProductsforUser> filterAndSort(String category, Double maxPrice, Double minRating, String sortBy, String searchQuery) {
        ProductRepository repo = ProductRepository.getInstance();
        List<ProductsforUser> result = new ArrayList<>(repo.getAll());

        // Search query filter
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            String q = searchQuery.toLowerCase().trim();
            result.removeIf(p -> {
                String name = p.getProductName() != null ? p.getProductName().toLowerCase() : "";
                String cat = p.getProductCategory() != null ? p.getProductCategory().toLowerCase() : "";
                String desc = p.getProductDescription() != null ? p.getProductDescription().toLowerCase() : "";
                return !name.contains(q) && !cat.contains(q) && !desc.contains(q);
            });
        }

        // Category filter
        if (category != null && !category.equalsIgnoreCase("All") && !category.trim().isEmpty()) {
            result.removeIf(p -> !matchesCategory(p.getProductCategory(), category.trim()));
        }

        // Price filter
        if (maxPrice != null && maxPrice > 0) {
            result.removeIf(p -> p.getProductPrice() > maxPrice);
        }

        // Rating filter
        if (minRating != null && minRating > 0) {
            result.removeIf(p -> p.getRating() < minRating);
        }

        // Sorting
        if (sortBy != null) {
            switch (sortBy) {
                case "Price: Low to High":
                    result.sort(Comparator.comparingDouble(ProductsforUser::getProductPrice));
                    break;
                case "Price: High to Low":
                    result.sort((a, b) -> Double.compare(b.getProductPrice(), a.getProductPrice()));
                    break;
                case "Customer Rating":
                    result.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));
                    break;
                case "Popularity":
                default:
                    result.sort((a, b) -> Integer.compare(b.getRatingCount(), a.getRatingCount()));
                    break;
            }
        }

        return result;
    }

    public static boolean matchesCategory(String productCat, String filterCat) {
        if (filterCat == null || filterCat.equalsIgnoreCase("All") || filterCat.trim().isEmpty()) {
            return true;
        }
        if (productCat == null || productCat.trim().isEmpty()) {
            return false;
        }
        String p = productCat.trim().toLowerCase();
        String f = filterCat.trim().toLowerCase();

        if (p.equals(f)) {
            return true;
        }

        switch (f) {
            case "clothing":
            case "clothes":
            case "apparel":
                return p.contains("cloth") || p.contains("apparel") || p.contains("men") || p.contains("women") || p.contains("kid");
            case "shoes":
            case "shoe":
            case "footwear":
                return p.contains("shoe") || p.contains("footwear") || p.contains("sneaker") || p.contains("boot");
            case "accessories":
            case "accessory":
                return p.contains("accessori") || p.contains("watch") || p.contains("bag") || p.contains("sunglasses") || p.contains("belt");
            case "electronics":
            case "electronic":
            case "tech":
                return p.contains("electronic") || p.contains("tech") || p.contains("gadget") || p.contains("audio") || p.contains("headphone");
            case "book":
            case "books":
            case "literature":
                return p.contains("book") || p.contains("novel") || p.contains("literature") || p.contains("reading");
            default:
                return p.equalsIgnoreCase(f);
        }
    }
}
