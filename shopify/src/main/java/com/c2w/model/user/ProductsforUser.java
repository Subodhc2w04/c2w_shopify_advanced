package com.c2w.model.user;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProductsforUser {

    private String couponCode;
    private boolean couponStatus;
    private String discountType;
    private double discountValue;
    private String productCategory;
    private String productDescription;
    private String productId;
    private String productImage;
    private String productName;
    private double productPrice;
    private int productQuantity;
    private Map<String, String> specifications = new LinkedHashMap<>();

    public ProductsforUser() {
    }

    public ProductsforUser(String couponCode,
                   boolean couponStatus,
                   String discountType,
                   double discountValue,
                   String productCategory,
                   String productDescription,
                   String productId,
                   String productImage,
                   String productName,
                   double productPrice,
                   Map<String, String> specifications,
                   int productQuantity) {

        this.couponCode = couponCode;
        this.couponStatus = couponStatus;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.productCategory = productCategory;
        this.productDescription = productDescription;
        this.productId = productId;
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.specifications = specifications != null ? new LinkedHashMap<>(specifications) : new LinkedHashMap<>();
        this.productQuantity = productQuantity;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public boolean isCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(boolean couponStatus) {
        this.couponStatus = couponStatus;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getRating() {
        return 4.5;
    }

    public int getRatingCount() {
        return 450;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Map<String, String> getSpecifications() {
        return specifications != null ? Collections.unmodifiableMap(specifications) : Collections.emptyMap();
    }

    public void setSpecifications(Map<String, String> specifications) {
        this.specifications = specifications != null ? new LinkedHashMap<>(specifications) : new LinkedHashMap<>();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getTotalPrice() {
        int qty = productQuantity > 0 ? productQuantity : 1;
        if (discountValue > 0) {
            double discountedUnit = productPrice - (productPrice * (discountValue / 100.0));
            return discountedUnit * qty;
        }
        return productPrice * qty;
    }

    public double getOriginalPrice() {
        if (discountValue > 0 && discountValue < 100) {
            return Math.round(productPrice / (1.0 - (discountValue / 100.0)));
        }
        return productPrice;
    }

    @Override
    public String toString() {
        return "ProductsforUser{" +
                "couponCode='" + couponCode + '\'' +
                ", couponStatus=" + couponStatus +
                ", discountType='" + discountType + '\'' +
                ", discountValue=" + discountValue +
                ", productCategory='" + productCategory + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productId='" + productId + '\'' +
                ", productImage='" + productImage + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productQuantity=" + productQuantity +
                '}';
    }
}
