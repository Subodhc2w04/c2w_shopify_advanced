package com.c2w.dao.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.c2w.config.FirebaseInitialize;
import com.c2w.view.authentication.AuthPage;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;

public class WishlistDao {

    private Firestore db = FirebaseInitialize.getFirestore();
    static String user_email = AuthPage.email;

    public List<String> getWishlistProductIds(String user_email) {
        List<String> productIds = new ArrayList<>();
        System.out.println("--------Wishlist Data in Dao---------");
        try {
            System.out.println("dao email:" + user_email);
            ApiFuture<DocumentSnapshot> future = db.collection("Wishlist")
                    .document(user_email)
                    .get();

            DocumentSnapshot document = future.get();
            System.out.println("+++++++++++++++++++++" + document);
            System.out.println(document.exists());

            if (document.exists()) {
                System.out.println("document exist");
                List<String> ids = (List<String>) document.get("productIds");
                if (ids != null) {
                    productIds.addAll(ids);
                }
            }
            System.out.println("===================Wishlist fetched=====================");
            System.out.println(user_email);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("wishlist size: " + productIds.size());
        return productIds;
    }


    public boolean addToWishlist(String user_email, String productId) {
    System.out.println("--------Add to Wishlist in Dao---------");
    try {
     
        DocumentSnapshot doc = db.collection("Wishlist")
                .document(user_email)
                .get()
                .get();

        List<String> productIds = new ArrayList<>();
        if (doc.exists() && doc.get("productIds") != null) {
            productIds = (List<String>) doc.get("productIds");
        }

    
        if (!productIds.contains(productId)) {
            productIds.add(productId);
        }

    
        Map<String, Object> data = new HashMap<>();
        data.put("productIds", productIds);

        db.collection("Wishlist")
                .document(user_email)
                .set(data, SetOptions.merge())
                .get();

        System.out.println("===================Wishlist item added=====================");
        return true;

    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

    public boolean removeFromWishlist(String user_email, String productId) {
        System.out.println("--------Remove from Wishlist in Dao---------");
        try {
            Map<String, Object> update = new HashMap<>();
            update.put("productIds", FieldValue.arrayRemove(productId));

            ApiFuture<WriteResult> future = db.collection("Wishlist")
                    .document(user_email)
                    .set(update, SetOptions.merge());

            WriteResult result = future.get();
            System.out.println("===================Wishlist item removed=====================");
            System.out.println("updated at: " + result.getUpdateTime());
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}