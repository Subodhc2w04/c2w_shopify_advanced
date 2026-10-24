package com.c2w.controller.user;

import java.util.List;

import com.c2w.dao.User.AddtoCartDao;
import com.c2w.model.user.AddToCart;
import com.c2w.model.user.ProductRepository;
import com.c2w.model.user.ProductsforUser;
import com.c2w.view.authentication.AuthPage;
import com.c2w.view.user.UIHelper;
import com.c2w.view.user.ToastView;

public class CartController {

    private static CartController instance;
    private AddtoCartDao addtoCartDao = new AddtoCartDao();

    public CartController() {
    }

    public static synchronized CartController getInstance() {
        if (instance == null) {
            instance = new CartController();
        }
        return instance;
    }

    public void addtoCartController(String productId,
                                   double couponDiscount,
                                   String appliedCoupon,
                                   String productImg,
                                   String productName,
                                   double productPrice,
                                   double productOrignalPrice,
                                   int quantity
                                 
                                ) {
        AddToCart cart = new AddToCart(productId, couponDiscount, appliedCoupon, productImg, productName,
                productPrice, productOrignalPrice, quantity);
        System.out.println("In add to cart controller====================");
        AddToCart.getInstance().addItem(cart);
        new Thread(() -> addtoCartDao.addtocart(cart)).start();
    }

    public List<AddToCart> getCartItems() {
        List<AddToCart> inMemory = AddToCart.getInstance().getItems();
        if (inMemory != null && !inMemory.isEmpty()) {
            return inMemory;
        }
        return loadCartFromFirebase();
    }

    public List<AddToCart> loadCartFromFirebase() {
        String user_email = AuthPage.email;
        System.out.println("Fetching cart for email from Firebase: " + user_email);
        List<AddToCart> items = addtoCartDao.getCartItems(user_email);
        AddToCart.getInstance().setItems(items);
        return items;
    }

    public void addToCart(ProductsforUser product, int quantity) {
        if (product == null) return;
        AddToCart item = new AddToCart(product, quantity);
        AddToCart.getInstance().addItem(item);
        new Thread(() -> addtoCartDao.addtocart(item)).start();
        ToastView.showToast("Added to Bag", product.getProductName());
    }

    public void updateQuantity(AddToCart item, int newQty) {
        if (item == null) return;
        System.out.println("CartController updateQuantity: " + item.getProductName() + " -> " + newQty);

        if (newQty <= 0) {
            removeItem(item);
            return;
        }

        // Update in-memory singleton cart model (triggers reactive UI update immediately)
        AddToCart.getInstance().updateQuantity(item, newQty);

        // Asynchronously sync quantity change to Firestore DAO
        new Thread(() -> {
            addtoCartDao.updateQuantity(item.getProductId(), newQty);
        }).start();
    }

    public void removeItem(AddToCart item) {
        if (item == null) return;
        System.out.println("CartController removeItem: " + item.getProductName());

        // Update in-memory singleton cart model (triggers reactive UI update immediately)
        AddToCart.getInstance().removeItem(item);

        // Asynchronously sync removal to Firestore DAO
        new Thread(() -> {
            addtoCartDao.removeItem(item.getProductId());
        }).start();

        ToastView.showInfoToast("Item Removed", item.getProductName() + " removed from bag.");
    }

    public void clearCart() {
        // Clear in-memory cart model
        AddToCart.getInstance().clear();

        // Clear cart in Firebase Firestore storage
        new Thread(() -> {
            addtoCartDao.clearCart(AuthPage.email);
        }).start();
    }

    public boolean applyCoupon(String coupon) {
        if (coupon == null || coupon.trim().isEmpty()) {
            ToastView.showInfoToast("Enter Coupon", "Please enter a coupon code.");
            return false;
        }

        String code = coupon.trim();
        List<AddToCart> cartItems = AddToCart.getInstance().getItems();
        if (cartItems.isEmpty()) {
            cartItems = getCartItems();
        }

        if (cartItems.isEmpty()) {
            ToastView.showInfoToast("Empty Cart", "Your cart is empty. Add products before applying coupons.");
            return false;
        }

        AddToCart matchedItem = null;
        ProductsforUser matchedProduct = null;

        // 1. Search in the items currently in the cart
        for (AddToCart item : cartItems) {
            ProductsforUser prod = item.getProduct();
            if (prod == null && item.getProductId() != null) {
                prod = ProductRepository.getInstance().getById(item.getProductId()).orElse(null);
            }
            if (prod == null && item.getProductId() != null) {
                try {
                    prod = ProductController.getInstance().getProductById(item.getProductId());
                } catch (Exception ignored) {}
            }
            if (prod != null) {
                item.setProduct(prod);
                if (prod.getCouponCode() != null && prod.getCouponCode().trim().equalsIgnoreCase(code)) {
                    matchedItem = item;
                    matchedProduct = prod;
                    break;
                }
            }
        }

        if (matchedProduct == null) {
            List<ProductsforUser> allProducts = ProductRepository.getInstance().getAll();
            if (allProducts == null || allProducts.isEmpty()) {
                try {
                    allProducts = ProductController.getInstance().getAllProducts();
                } catch (Exception ignored) {}
            }

            ProductsforUser catalogProduct = null;
            if (allProducts != null) {
                for (ProductsforUser p : allProducts) {
                    if (p.getCouponCode() != null && p.getCouponCode().trim().equalsIgnoreCase(code)) {
                        catalogProduct = p;
                        break;
                    }
                }
            }

            if (catalogProduct != null) {
                AddToCart categoryItem = null;
                for (AddToCart item : cartItems) {
                    ProductsforUser prod = item.getProduct();
                    if (prod != null && prod.getProductCategory() != null &&
                            catalogProduct.getProductCategory() != null &&
                            prod.getProductCategory().equalsIgnoreCase(catalogProduct.getProductCategory())) {
                        categoryItem = item;
                        break;
                    }
                }

                if (categoryItem != null) {
                    matchedItem = categoryItem;
                    matchedProduct = catalogProduct;
                } else {
                    String targetName = catalogProduct.getProductName() != null ? catalogProduct.getProductName() : "this item";
                    String targetCat = catalogProduct.getProductCategory() != null ? " (" + catalogProduct.getProductCategory() + ")" : "";
                    ToastView.showInfoToast("Coupon Not Applicable",
                            "Coupon '" + code + "' is only valid for " + targetName + targetCat + ". Please add it to your bag!");
                    return false;
                }
            }
        }

        if (matchedProduct == null) {
            String cleanUpper = code.toUpperCase();
            if (cleanUpper.equals("C2W300")) {
                if (AddToCart.getInstance().getTotalMRP() >= 1500) {
                    AddToCart.getInstance().applyCoupon("C2W300", 300.0, "Fixed Amount", 300.0);
                    ToastView.showToast("Coupon Applied! 🎉", "Promo code 'C2W300' applied (- ₹300).");
                    return true;
                } else {
                    ToastView.showInfoToast("Order Condition Not Met", "Code 'C2W300' requires minimum MRP of ₹1,500.");
                    return false;
                }
            } else if (cleanUpper.equals("FIRSTLOOK10")) {
                double subtotal = cartItems.stream().mapToDouble(AddToCart::getLinePrice).sum();
                double discount = Math.round(subtotal * 0.10);
                AddToCart.getInstance().applyCoupon("FIRSTLOOK10", discount, "Percentage", 10.0);
                ToastView.showToast("Coupon Applied! 🎉", "Promo code 'FIRSTLOOK10' applied (10% off).");
                return true;
            }

            ToastView.showInfoToast("Invalid Coupon", "No valid coupon found for code '" + code + "'.");
            return false;
        }

        // Extract actual coupon details
        String actualCouponCode = matchedProduct.getCouponCode();
        boolean couponStatus = matchedProduct.isCouponStatus();
        String discountType = matchedProduct.getDiscountType();
        double discountValue = matchedProduct.getDiscountValue();

        if (!couponStatus) {
            ToastView.showInfoToast("Coupon Inactive", "Coupon code '" + actualCouponCode + "' is currently disabled or expired.");
            return false;
        }

        if (discountValue <= 0) {
            ToastView.showInfoToast("Invalid Coupon", "Coupon code '" + actualCouponCode + "' has no discount configured.");
            return false;
        }

        double discountAmount = 0.0;
        boolean isPercentage = discountType != null &&
                (discountType.equalsIgnoreCase("Percentage") || discountType.contains("%"));

        if (isPercentage) {
            double targetPrice = matchedItem != null ? matchedItem.getLinePrice() : AddToCart.getInstance().getTotalPrice();
            discountAmount = Math.round((targetPrice * discountValue) / 100.0);
        } else {
            discountAmount = discountValue;
        }

        double cartSubtotal = AddToCart.getInstance().getTotalPrice();
        if (discountAmount > cartSubtotal) {
            discountAmount = cartSubtotal;
        }

        AddToCart.getInstance().applyCoupon(actualCouponCode, discountAmount, discountType, discountValue);

        if (isPercentage) {
            ToastView.showToast("Coupon Applied! 🎉",
                    "Code '" + actualCouponCode + "' applied! " + (int) discountValue + "% off (- " + UIHelper.formatCurrency(discountAmount) + ")");
        } else {
            ToastView.showToast("Coupon Applied! 🎉",
                    "Code '" + actualCouponCode + "' applied! - " + UIHelper.formatCurrency(discountAmount) + " off");
        }
        return true;
    }

    public void removeCoupon() {
        AddToCart.getInstance().removeCoupon();
        ToastView.showInfoToast("Coupon Removed", "Coupon discount has been removed.");
    }
}
