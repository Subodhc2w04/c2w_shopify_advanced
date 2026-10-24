package com.c2w.model.admin;

public class OrderItem {

    private double discountValue;
    private double lineMRP;
    private double linePrice;
    private double lineSavings;
    private double originalPrice;

    private String productId;
    private String productImage;
    private String productName;

    private double productPrice;

    private long quantity;

    public OrderItem() {
    }

    public OrderItem(
            double discountValue,
            double lineMRP,
            double linePrice,
            double lineSavings,
            double originalPrice,
            String productId,
            String productImage,
            String productName,
            double productPrice,
            long quantity) {

        this.discountValue = discountValue;
        this.lineMRP = lineMRP;
        this.linePrice = linePrice;
        this.lineSavings = lineSavings;
        this.originalPrice = originalPrice;
        this.productId = productId;
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public double getLineMRP() {
        return lineMRP;
    }

    public void setLineMRP(double lineMRP) {
        this.lineMRP = lineMRP;
    }

    public double getLinePrice() {
        return linePrice;
    }

    public void setLinePrice(double linePrice) {
        this.linePrice = linePrice;
    }

    public double getLineSavings() {
        return lineSavings;
    }

    public void setLineSavings(double lineSavings) {
        this.lineSavings = lineSavings;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
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

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }


}
