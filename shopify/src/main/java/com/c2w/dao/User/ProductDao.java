package com.c2w.dao.User;

import java.util.ArrayList;
import java.util.List;

import com.c2w.config.FirebaseInitialize;
import com.c2w.model.user.ProductsforUser;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;

public class ProductDao {

    private Firestore db = FirebaseInitialize.getFirestore();


    public List<ProductsforUser> getAllProducts() {
        List<ProductsforUser> products = new ArrayList<>();
        try {
            if (db == null) {
                System.err.println("ProductDao: Firestore instance is null!");
                return products;
            }

            ApiFuture<QuerySnapshot> future = db.collection("Products").get();
            QuerySnapshot querySnapshot = future.get();

            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                ProductsforUser product = parseProductDocument(document);
                if (product != null) {
                    products.add(product);
                }
            }
            System.out.println("ProductDao - Total products found: " + products.size());
        } catch (Exception e) {
            System.err.println("ProductDao - Error fetching all products: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    public List<ProductsforUser> getProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty() || category.equalsIgnoreCase("All")) {
            return getAllProducts();
        }

        List<ProductsforUser> products = new ArrayList<>();
        try {
            if (db == null) {
                System.err.println("ProductDao: Firestore instance is null!");
                return products;
            }

            // Primary query matching category
            ApiFuture<QuerySnapshot> future = db.collection("Products")
                    .whereEqualTo("productCategory", category)
                    .get();

            QuerySnapshot querySnapshot = future.get();

            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                ProductsforUser product = parseProductDocument(document);
                if (product != null) {
                    products.add(product);
                }
            }

            if (products.isEmpty()) {
                List<ProductsforUser> all = getAllProducts();
                for (ProductsforUser p : all) {
                    if (p.getProductCategory() != null && com.c2w.controller.user.CatalogController.matchesCategory(p.getProductCategory(), category)) {
                        products.add(p);
                    }
                }
            }

            System.out.println("ProductDao - Category [" + category + "] products found: " + products.size());
        } catch (Exception e) {
            System.err.println("ProductDao - Error fetching products for category " + category + ": " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }

    public ProductsforUser getProductById(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            return null;
        }

        try {
            if (db == null) {
                return null;
            }

            DocumentSnapshot doc = db.collection("Products").document(productId).get().get();
            if (doc.exists()) {
                return parseProductDocument(doc);
            }

         
            ApiFuture<QuerySnapshot> future = db.collection("Products")
                    .whereEqualTo("productId", productId)
                    .get();

            QuerySnapshot snapshot = future.get();
            if (!snapshot.isEmpty()) {
                return parseProductDocument(snapshot.getDocuments().get(0));
            }
        } catch (Exception e) {
            System.err.println("ProductDao - Error fetching product by id " + productId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private ProductsforUser parseProductDocument(DocumentSnapshot document) {
        try {
            ProductsforUser product = document.toObject(ProductsforUser.class);
            if (product != null) {
                if (product.getProductId() == null || product.getProductId().trim().isEmpty()) {
                    product.setProductId(document.getId());
                }
                return product;
            }
        } catch (Exception e) {
            System.err.println("ProductDao - Warning: standard toObject failed for doc " + document.getId() + ", falling back to manual mapping: " + e.getMessage());
        }

      
        try {
            ProductsforUser p = new ProductsforUser();
            p.setProductId(document.contains("productId") && document.getString("productId") != null 
                    ? document.getString("productId") : document.getId());
            p.setProductName(document.getString("productName"));
            p.setProductCategory(document.getString("productCategory"));
            p.setProductDescription(document.getString("productDescription"));
            p.setProductImage(document.getString("productImage"));
            p.setCouponCode(document.getString("couponCode"));
            p.setDiscountType(document.getString("discountType"));

            Boolean couponStatus = document.getBoolean("couponStatus");
            if (couponStatus != null) p.setCouponStatus(couponStatus);

            Double price = document.getDouble("productPrice");
            if (price != null) {
                p.setProductPrice(price);
            } else if (document.getLong("productPrice") != null) {
                p.setProductPrice(document.getLong("productPrice").doubleValue());
            }

            Double disc = document.getDouble("discountValue");
            if (disc != null) {
                p.setDiscountValue(disc);
            } else if (document.getLong("discountValue") != null) {
                p.setDiscountValue(document.getLong("discountValue").doubleValue());
            }

            Long qty = document.getLong("productQuantity");
            if (qty != null) p.setProductQuantity(qty.intValue());

            return p;
        } catch (Exception ex) {
            System.err.println("ProductDao - Failed to manually map doc " + document.getId() + ": " + ex.getMessage());
            return null;
        }
    }
}
