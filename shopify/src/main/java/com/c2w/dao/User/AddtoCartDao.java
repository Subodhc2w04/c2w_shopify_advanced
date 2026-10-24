package com.c2w.dao.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.c2w.config.FirebaseInitialize;
import com.c2w.model.user.AddToCart;
import com.c2w.view.authentication.AuthPage;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

public class AddtoCartDao {

    private Firestore db = FirebaseInitialize.getFirestore();
    static String user_email = AuthPage.email;

    private String getEffectiveEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            return email.trim();
        }
        if (AuthPage.email != null && !AuthPage.email.trim().isEmpty()) {
            return AuthPage.email.trim();
        }
        return user_email != null ? user_email.trim() : "";
    }

    public void addtocart(AddToCart cart) {
        System.out.println("--------Cart Data in Dao---------");
        try {
            String email = getEffectiveEmail(null);
            if (email.isEmpty()) return;

            DocumentSnapshot doc = db.collection("CartItems")
                    .document(email)
                    .get()
                    .get();

            List<Map<String, Object>> cartItems = new ArrayList<>();
            if (doc.exists() && doc.get("items") != null) {
                cartItems = (List<Map<String, Object>>) doc.get("items");
            }

            boolean found = false;
            for (Map<String, Object> item : cartItems) {
                String pId = item.get("productId") != null ? String.valueOf(item.get("productId")) : null;
                if (pId == null && item.get("id") != null) {
                    pId = String.valueOf(item.get("id"));
                }
                if (pId != null && pId.equals(String.valueOf(cart.getProductId()))) {
                    long existingQty = 1;
                    Object qVal = item.get("quantity");
                    if (qVal == null) qVal = item.get("qty");
                    if (qVal instanceof Number) {
                        existingQty = ((Number) qVal).longValue();
                    }
                    long totalQty = existingQty + cart.getQuantity();
                    item.put("quantity", totalQty);
                    found = true;
                    break;
                }
            }

            if (!found) {
                Map<String, Object> newItem = new HashMap<>();
                newItem.put("productId", cart.getProductId());
                newItem.put("productImg", cart.getProductImg());
                newItem.put("productName", cart.getProductName());
                newItem.put("productPrice", cart.getProductPrice());
                newItem.put("productOrignalPrice", cart.getProductOrignalPrice());
                newItem.put("quantity", cart.getQuantity());
                newItem.put("CouponDiscount", cart.getCouponDiscount());
                newItem.put("AppliedCoupon", cart.getAppliedCoupon());

                cartItems.add(newItem);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("items", cartItems);

            db.collection("CartItems")
                    .document(email)
                    .set(data)
                    .get();

            System.out.println("===================Data Inserted=====================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<AddToCart> getCartItems(String userEmail) {
        List<AddToCart> cartItems = new ArrayList<>();
        System.out.println("--------Get Cart Data in Dao---------");
        try {
            String email = getEffectiveEmail(userEmail);
            if (email.isEmpty()) return cartItems;

            DocumentSnapshot doc = db.collection("CartItems")
                    .document(email)
                    .get()
                    .get();

            System.out.println("Doc exists: " + doc.exists());

            if (doc.exists() && doc.get("items") != null) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) doc.get("items");

                for (Map<String, Object> item : items) {
                    String productId = item.get("productId") != null ? String.valueOf(item.get("productId")) : null;
                    if (productId == null && item.get("id") != null) {
                        productId = String.valueOf(item.get("id"));
                    }
                    String productImg = (String) item.get("productImg");
                    String productName = (String) item.get("productName");
                    double productPrice = item.get("productPrice") != null ? ((Number) item.get("productPrice")).doubleValue() : 0.0;
                    double productOrignalPrice = item.get("productOrignalPrice") != null ? ((Number) item.get("productOrignalPrice")).doubleValue() : 0.0;

                    int quantity = 1;
                    Object qVal = item.get("quantity");
                    if (qVal == null) qVal = item.get("qty");
                    if (qVal instanceof Number) {
                        quantity = ((Number) qVal).intValue();
                    }

                    double couponDiscount = item.get("CouponDiscount") != null ? ((Number) item.get("CouponDiscount")).doubleValue() : 0.0;
                    String appliedCoupon = (String) item.get("AppliedCoupon");

                    AddToCart cartItem = new AddToCart(productId, couponDiscount, appliedCoupon, productImg, productName,
                            productPrice, productOrignalPrice, quantity);
                    cartItems.add(cartItem);
                }
            }
            System.out.println("===================Cart fetched: " + cartItems.size() + " items=====================");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    public void updateQuantity(String productId, int newQty) {
        if (productId == null) return;
        System.out.println("--------Update Quantity in Dao: " + productId + " -> " + newQty + "---------");
        try {
            String email = getEffectiveEmail(null);
            if (email.isEmpty()) return;

            DocumentSnapshot doc = db.collection("CartItems")
                    .document(email)
                    .get()
                    .get();

            if (doc.exists() && doc.get("items") != null) {
                List<Map<String, Object>> cartItems = (List<Map<String, Object>>) doc.get("items");
                List<Map<String, Object>> updatedList = new ArrayList<>();

                for (Map<String, Object> item : cartItems) {
                    String pId = item.get("productId") != null ? String.valueOf(item.get("productId")) : null;
                    if (pId == null && item.get("id") != null) {
                        pId = String.valueOf(item.get("id"));
                    }

                    if (pId != null && pId.equals(String.valueOf(productId))) {
                        if (newQty > 0) {
                            Map<String, Object> updatedItem = new HashMap<>(item);
                            updatedItem.put("quantity", newQty);
                            updatedList.add(updatedItem);
                        }
                        // if newQty <= 0, item is omitted (deleted from cart)
                    } else {
                        updatedList.add(new HashMap<>(item));
                    }
                }

                Map<String, Object> data = new HashMap<>();
                data.put("items", updatedList);

                db.collection("CartItems")
                        .document(email)
                        .set(data)
                        .get();

                System.out.println("===================Quantity Updated in Dao=====================");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeItem(String productId) {
        if (productId == null) return;
        System.out.println("--------Remove Item in Dao: " + productId + "---------");
        try {
            String email = getEffectiveEmail(null);
            if (email.isEmpty()) return;

            DocumentSnapshot doc = db.collection("CartItems")
                    .document(email)
                    .get()
                    .get();

            if (doc.exists() && doc.get("items") != null) {
                List<Map<String, Object>> cartItems = (List<Map<String, Object>>) doc.get("items");
                List<Map<String, Object>> updatedList = new ArrayList<>();

                for (Map<String, Object> item : cartItems) {
                    String pId = item.get("productId") != null ? String.valueOf(item.get("productId")) : null;
                    if (pId == null && item.get("id") != null) {
                        pId = String.valueOf(item.get("id"));
                    }

                    if (pId != null && pId.equals(String.valueOf(productId))) {
                        continue; // omit to remove
                    }
                    updatedList.add(new HashMap<>(item));
                }

                Map<String, Object> data = new HashMap<>();
                data.put("items", updatedList);

                db.collection("CartItems")
                        .document(email)
                        .set(data)
                        .get();

                System.out.println("===================Item Removed in Dao=====================");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCartItem(String userEmail, String productId) {
        removeItem(productId);
    }

    public void clearCart(String userEmail) {
        System.out.println("--------Clear Cart in Dao---------");
        try {
            String email = getEffectiveEmail(userEmail);
            if (email.isEmpty()) return;

            Map<String, Object> data = new HashMap<>();
            data.put("items", new ArrayList<>());

            db.collection("CartItems")
                    .document(email)
                    .set(data)
                    .get();

            System.out.println("===================Cart Cleared in Dao=====================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}