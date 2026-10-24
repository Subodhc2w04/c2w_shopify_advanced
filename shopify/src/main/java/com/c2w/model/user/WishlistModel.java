
package com.c2w.model.user;

import java.util.*;
import java.util.stream.Collectors;

import com.c2w.dao.User.WishlistDao;
import com.c2w.view.authentication.AuthPage;

public class WishlistModel {
    private static WishlistModel instance;
    private final Set<String> wishlistProductIds = new LinkedHashSet<>();
    private final List<Runnable> changeListeners = new ArrayList<>();
    private final WishlistDao wishlistDao = new WishlistDao();
    private boolean loaded = false;

    private WishlistModel() {

    }

    public static synchronized WishlistModel getInstance() {
        if (instance == null) {
            instance = new WishlistModel();
        }
        return instance;
    }

    public synchronized void loadFromFirebase() {
        String user_email = AuthPage.email;
        System.out.println("--------Loading Wishlist in Model---------");
        try {
            List<String> ids = wishlistDao.getWishlistProductIds(user_email);
            wishlistProductIds.clear();
            wishlistProductIds.addAll(ids);
            loaded = true;
            System.out.println("loaded-wishlist :" + loaded);
            System.out.println("wishlist ids:" + wishlistProductIds.size());
            notifyListeners();
        } catch (Exception e) {
            System.err.println("WishlistModel - Failed to load wishlist from Firebase: " + e.getMessage());
        }
    }

    public synchronized void refresh() {
        loadFromFirebase();
    }

    public void addListener(Runnable listener) {
        if (listener != null && !changeListeners.contains(listener)) {
            changeListeners.add(listener);
        }
    }

    public void removeListener(Runnable listener) {
        changeListeners.remove(listener);
    }

    public synchronized void clear() {
        wishlistProductIds.clear();
        loaded = false;
        notifyListeners();
    }

    private void notifyListeners() {
        for (Runnable r : changeListeners) {
            try {
                r.run();
            } catch (Exception ignored) {
            }
        }
    }

    public boolean isWishlisted(String productId) {
        if (!loaded) {
            loadFromFirebase();
        }
        return wishlistProductIds.contains(productId);
    }

    public boolean toggle(ProductsforUser product) {
        if (product == null)
            return false;
        String user_email = AuthPage.email;
        boolean added;

        if (wishlistProductIds.contains(product.getProductId())) {
            wishlistProductIds.remove(product.getProductId());
            wishlistDao.removeFromWishlist(user_email, product.getProductId());
            added = false;
        } else {
            wishlistProductIds.add(product.getProductId());
            wishlistDao.addToWishlist(user_email, product.getProductId());
            added = true;
        }
        notifyListeners();
        return added;
    }

    public void remove(String productId) {
        String user_email = AuthPage.email;
        if (wishlistProductIds.remove(productId)) {
            wishlistDao.removeFromWishlist(user_email, productId);
            notifyListeners();
        }
    }

    public int getCount() {
        if (!loaded) {
            loadFromFirebase();
        }
        return wishlistProductIds.size();
    }

    public List<ProductsforUser> getWishlistedProducts() {
        if (!loaded) {
            loadFromFirebase();
        }
        ProductRepository repo = ProductRepository.getInstance();
        return wishlistProductIds.stream()
                .map(repo::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}