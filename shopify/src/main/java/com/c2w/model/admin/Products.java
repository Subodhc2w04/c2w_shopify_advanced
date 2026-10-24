package com.c2w.model.admin;

import java.util.List;
import java.util.Map;

public class Products {
    private String productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productImage;
    private String productCategory;
    private Double productQuantity;
    private String couponCode;
    private String discountType;
    private Double discountValue;
    private boolean couponStatus;
    private List<Map<String, Object>> reviews;

    public List<Map<String, Object>> getReviews() {
        return reviews;
    }

    public void setReviews(
            List<Map<String, Object>> reviews) {

        this.reviews = reviews;
    }
    
    public String getProductId() {
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
    public String getProductDescription() {
        return productDescription;
    }
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    public Double getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
    public String getProductImage() {
        return productImage;
    }
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    public String getProductCategory() {
        return productCategory;
    }
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
    public Double getProductQuantity() {
        return productQuantity;
    }
    public void setProductQuantity(Double productQuantity) {
        this.productQuantity = productQuantity;
    }
    public String getCouponCode() {
        return couponCode;
    }
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
    public String getDiscountType() {
        return discountType;
    }
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
    public Double getDiscountValue() {
        return discountValue;
    }
    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }
    public boolean isCouponStatus() {
        return couponStatus;
    }
    public void setCouponStatus(boolean couponStatus) {
        this.couponStatus = couponStatus;
    }
    public Products() {
    }
    public Products(String productId, String productName, String productDescription, Double productPrice,
            String productImage, String productCategory, Double productQuantity, String couponCode,
            String discountType, Double discountValue, boolean couponStatus,String productUrl,List<Map<String, Object>> reviews) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productCategory = productCategory;
        this.productQuantity = productQuantity;
        this.couponCode = couponCode;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.couponStatus = couponStatus;
        this.reviews=reviews;
    }
}
