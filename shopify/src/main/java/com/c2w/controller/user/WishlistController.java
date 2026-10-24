
package com.c2w.controller.user;

import com.c2w.dao.User.WishlistDao;
import com.c2w.model.user.ProductsforUser;
import com.c2w.model.user.WishlistModel;
import com.c2w.view.authentication.AuthPage;
import com.c2w.view.user.ToastView;

public class WishlistController {
    private static WishlistController instance;
    WishlistDao wishlistDao = new WishlistDao();
    static String user_email = AuthPage.email;

    private WishlistController() {}

    public static synchronized WishlistController getInstance() {
        if (instance == null) {
            instance = new WishlistController();
        }
        return instance;
    }

    public boolean toggle(ProductsforUser product) {
        boolean added = WishlistModel.getInstance().toggle(product);
        if (added) {
            ToastView.showToast("Saved to Wishlist ❤️", product.getProductName() + " saved.");
        } else {
            ToastView.showInfoToast("Removed from Wishlist", product.getProductName() + " removed.");
        }
        return added;
    }

    public void remove(String productId) {
        WishlistModel.getInstance().remove(productId);
        ToastView.showInfoToast("Removed from Wishlist", "Item removed from wishlist.");
    }

    public void loadWishlist() {
        WishlistModel.getInstance().loadFromFirebase();
    }
}