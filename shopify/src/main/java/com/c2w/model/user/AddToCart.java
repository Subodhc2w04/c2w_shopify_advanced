package com.c2w.model.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import com.c2w.controller.delivery.LocationController;

public class AddToCart {

    // --- Line Item Fields ---
    private String productId;
    private String productName;
    private String productImg;
    private double productPrice;
    private double productOrignalPrice;
    private int quantity;
    private double couponDiscount = 0.0;
    private String appliedCoupon = "";
    private String appliedCouponType = "";
    private double appliedCouponValue = 0.0;
    private ProductsforUser product;
    // private String codUrl;


    private static AddToCart instance;
    private List<AddToCart> items = new ArrayList<>();
    private List<Runnable> changeListeners = new ArrayList<>();
    private double customDeliveryCharges = -1.0;
    private double deliveryDistanceKm = 0.0;
    private String deliveryAddress = "";



    public AddToCart() {
    }

  
    public AddToCart(String productId,
            double couponDiscount,
            String appliedCoupon,
            String productImg,
            String productName,
            double productPrice,
            double productOrignalPrice,
            int quantity
) {
        this.productId = productId;
        this.couponDiscount = couponDiscount;
        this.appliedCoupon = appliedCoupon != null ? appliedCoupon : "";
        this.productImg = productImg;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productOrignalPrice = productOrignalPrice;
        this.quantity = Math.max(1, quantity);
        

    }

    
    public AddToCart(String productName, String productId, String productImage,
            double productPrice, double originalPrice, int quantity) {
        this.productName = productName;
        this.productId = productId;
        this.productImg = productImage;
        this.productPrice = productPrice;
        this.productOrignalPrice = originalPrice;
        this.quantity = Math.max(1, quantity);
    }

 
    public AddToCart(ProductsforUser product, int quantity) {
        this.product = product;
        this.productName = product != null ? product.getProductName() : null;
        this.productId = product != null ? product.getProductId() : null;
        this.productImg = product != null ? product.getProductImage() : null;
        this.productPrice = product != null ? product.getProductPrice() : 0.0;
        this.productOrignalPrice = product != null ? product.getOriginalPrice() : 0.0;
        this.quantity = Math.max(1, quantity);
        this.couponDiscount = product != null ? product.getDiscountValue() : 0.0;
        this.appliedCoupon = product != null ? product.getCouponCode() : "";
    }

    
    public static synchronized AddToCart getInstance() {
        if (instance == null) {
            instance = new AddToCart();
        }
        return instance;
    }


    public void addListener(Runnable listener) {
        if (listener != null && !changeListeners.contains(listener)) {
            changeListeners.add(listener);
        }
    }

    public void removeListener(Runnable listener) {
        changeListeners.remove(listener);
    }

    public void notifyListeners() {
        Runnable task = () -> {
            for (Runnable r : new ArrayList<>(changeListeners)) {
                try {
                    r.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            if (Platform.isFxApplicationThread()) {
                task.run();
            } else {
                Platform.runLater(task);
            }
        } catch (Exception e) {
            task.run();
        }
    }

    // --- Cart Items Collection Operations ---

    public List<AddToCart> getItems() {
        if (this == instance) {
            return Collections.unmodifiableList(items);
        }
        return items;
    }

    public void setItems(List<AddToCart> items) {
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        recalculateCouponDiscount();
        notifyListeners();
    }

    public int getItemCount() {
        if (this == instance) {
            return items.stream().mapToInt(AddToCart::getQuantity).sum();
        }
        return quantity;
    }

    public void addItem(ProductsforUser prod, int qty) {
        if (prod == null)
            return;
        for (AddToCart item : items) {
            if (item.getProductId() != null && item.getProductId().equals(prod.getProductId())) {
                item.setQuantity(item.getQuantity() + qty);
                recalculateCouponDiscount();
                notifyListeners();
                return;
            }
        }
        items.add(new AddToCart(prod, qty));
        recalculateCouponDiscount();
        notifyListeners();
    }

    public void addItem(AddToCart cartItem) {
        if (cartItem == null)
            return;
        for (AddToCart item : items) {
            if (item.getProductId() != null && item.getProductId().equals(cartItem.getProductId())) {
                item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                recalculateCouponDiscount();
                notifyListeners();
                return;
            }
        }
        items.add(cartItem);
        recalculateCouponDiscount();
        notifyListeners();
    }

    public void removeItem(AddToCart item) {
        if (item == null)
            return;
        items.removeIf(i -> i == item || (i.getProductId() != null && i.getProductId().equals(item.getProductId())));
        recalculateCouponDiscount();
        notifyListeners();
    }

    public void updateQuantity(AddToCart item, int newQty) {
        if (item == null) {
            return;
        }

        if (newQty <= 0) {
            removeItem(item);
            return;
        }

        boolean found = false;
        for (AddToCart i : items) {
            if (i == item
                    || (i.getProductId() != null
                            && i.getProductId().equals(item.getProductId()))) {
                i.setQuantity(newQty);
                item.setQuantity(newQty);
                found = true;
                break;
            }
        }
        if (!found) {
            item.setQuantity(newQty);
        }
        recalculateCouponDiscount();
        notifyListeners();
    }

    public void recalculateCouponDiscount() {
        if (items.isEmpty()) {
            this.couponDiscount = 0.0;
            return;
        }
        if (appliedCoupon != null && !appliedCoupon.isEmpty()) {
            if (appliedCoupon.equalsIgnoreCase("C2W300")) {
                if (getTotalMRP() >= 1500) {
                    this.couponDiscount = 300.0;
                } else {
                    this.couponDiscount = 0.0;
                }
            } else if (appliedCouponValue > 0) {
                if (appliedCouponType != null
                        && (appliedCouponType.equalsIgnoreCase("Percentage") || appliedCouponType.contains("%"))) {
                    double target = getTotalPrice();
                    this.couponDiscount = Math.round((target * appliedCouponValue) / 100.0);
                } else {
                    this.couponDiscount = appliedCouponValue;
                }
                if (this.couponDiscount > getTotalPrice()) {
                    this.couponDiscount = getTotalPrice();
                }
            }
        }
    }

    public void clear() {
        items.clear();
        couponDiscount = 0.0;
        appliedCoupon = "";
        appliedCouponType = "";
        appliedCouponValue = 0.0;
        customDeliveryCharges = -1.0;
        deliveryDistanceKm = 0.0;
        deliveryAddress = "";
        notifyListeners();
    }

    // --- Line Item / Cart Pricing Calculations ---

    public double getTotalPrice() {
        if (this == instance) {
            return items.stream().mapToDouble(AddToCart::getLinePrice).sum();
        }
        return productPrice * quantity;
    }

    public double getLinePrice() {
        return productPrice * quantity;
    }

    public double getTotalMRP() {
        if (this == instance) {
            return items.stream().mapToDouble(AddToCart::getLineMRP).sum();
        }
        return (productOrignalPrice > 0 ? productOrignalPrice : productPrice) * (quantity > 0 ? quantity : 1);
    }

    public double getLineMRP() {
        return (productOrignalPrice > 0 ? productOrignalPrice : productPrice) * (quantity > 0 ? quantity : 1);
    }

    public double getTotalSavings() {
        if (this == instance) {
            return items.stream().mapToDouble(AddToCart::getTotalSavings).sum();
        }
        return Math.max(0.0, getLineMRP() - getLinePrice());
    }

    public double getLineSavings() {
        return getTotalSavings();
    }

    public double getProductDiscount() {
        if (this == instance) {
            return items.stream().mapToDouble(AddToCart::getTotalSavings).sum();
        }
        return getTotalSavings();
    }

    public double getDeliveryCharges() {
        if (items.isEmpty()) {
            return 0.0;
        }
        if (customDeliveryCharges >= 0) {
            return customDeliveryCharges;
        }
        return 40.0;
    }

    public void setDeliveryCharges(double deliveryCharges) {
        this.customDeliveryCharges = deliveryCharges;
        notifyListeners();
    }

    public double getDeliveryDistanceKm() {
        return deliveryDistanceKm;
    }

    public void setDeliveryDistanceKm(double deliveryDistanceKm) {
        this.deliveryDistanceKm = deliveryDistanceKm;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void updateDeliveryChargesByAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            this.customDeliveryCharges = -1.0;
            this.deliveryDistanceKm = 0.0;
            this.deliveryAddress = "";
            return;
        }
        this.deliveryAddress = address.trim();
        try {
            LocationController locController = new LocationController();
            this.deliveryDistanceKm = locController.calculateDistanceForAddress(this.deliveryAddress);
            this.customDeliveryCharges = locController.calculateDeliveryChargeForDistance(this.deliveryDistanceKm);
            System.out.println("customDeliveryCharges based on distance (" + deliveryDistanceKm + " km) is: " + customDeliveryCharges);
        } catch (Exception e) {
            e.printStackTrace();
            this.customDeliveryCharges = 40.0;
            this.deliveryDistanceKm = 0.0;
            System.out.println("IN CATCH customDeliveryCharges are: " + customDeliveryCharges);
        }
        notifyListeners();
    }

    public boolean applyCoupon(String code) {
        if (code == null)
            return false;
        String clean = code.trim().toUpperCase();
        if (clean.equals("C2W300") && getTotalMRP() >= 1500) {
            return applyCoupon("C2W300", 300.0, "Fixed Amount", 300.0);
        } else if (clean.equals("FIRSTLOOK10")) {
            double disc = Math.round(items.stream().mapToDouble(AddToCart::getLinePrice).sum() * 0.10);
            return applyCoupon("FIRSTLOOK10", disc, "Percentage", 10.0);
        }
        return false;
    }

    public boolean applyCoupon(String code, double discount, String discountType, double discountValue) {
        this.appliedCoupon = code != null ? code : "";
        this.couponDiscount = discount;
        this.appliedCouponType = discountType != null ? discountType : "";
        this.appliedCouponValue = discountValue;
        notifyListeners();
        return true;
    }

    public void removeCoupon() {
        this.appliedCoupon = "";
        this.couponDiscount = 0.0;
        this.appliedCouponType = "";
        this.appliedCouponValue = 0.0;
        notifyListeners();
    }

    public double getFinalPayable() {
        double subtotal = items.stream().mapToDouble(AddToCart::getLinePrice).sum();
        double delivery = getDeliveryCharges();
        double finalAmt = subtotal + delivery - couponDiscount;
        return Math.max(0, finalAmt);
    }

    


    public double getDiscountValue() {
        if (productOrignalPrice > 0 && productOrignalPrice > productPrice) {
            return Math.round(((productOrignalPrice - productPrice) / productOrignalPrice) * 100.0);
        }
        return 0.0;
    }

    // --- Line Item Getters & Setters ---

    public String getProductId() {
        if (productId != null && !productId.trim().isEmpty()) {
            return productId;
        }
        if (product != null && product.getProductId() != null) {
            return product.getProductId();
        }
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductImage() {
        return productImg;
    }

    public void setProductImage(String productImage) {
        this.productImg = productImage;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getProductOrignalPrice() {
        return productOrignalPrice;
    }

    public void setProductOrignalPrice(double productOrignalPrice) {
        this.productOrignalPrice = productOrignalPrice;
    }

    public double getOriginalPrice() {
        return productOrignalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.productOrignalPrice = originalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(1, quantity);
    }

    public double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public String getAppliedCoupon() {
        return appliedCoupon != null ? appliedCoupon : "";
    }

    public void setAppliedCoupon(String appliedCoupon) {
        this.appliedCoupon = appliedCoupon;
    }

    public ProductsforUser getProduct() {
        if (product == null && productId != null) {
            return ProductRepository.getInstance().getById(productId).orElse(null);
        }
        return product;
    }

    public void setProduct(ProductsforUser product) {
        this.product = product;
    }

    public String getAppliedCouponType() {
        return appliedCouponType != null ? appliedCouponType : "";
    }

    public void setAppliedCouponType(String appliedCouponType) {
        this.appliedCouponType = appliedCouponType;
    }

    public double getAppliedCouponValue() {
        return appliedCouponValue;
    }

    public void setAppliedCouponValue(double appliedCouponValue) {
        this.appliedCouponValue = appliedCouponValue;
    }

    public void setDiscountValue(double discountValue) {
        this.couponDiscount = discountValue;
    }
}
