package com.c2w.controller.user;

import com.c2w.dao.User.ProductDao;
import com.c2w.model.user.ProductsforUser;

import java.util.List;

public class ProductController {

    private static ProductController instance;
    private final ProductDao productdao;

    public ProductController() {
        this.productdao = new ProductDao();
    }

    public static synchronized ProductController getInstance() {
        if (instance == null) {
            instance = new ProductController();
        }
        return instance;
    }


    public List<ProductsforUser> getAllProducts() {
        return productdao.getAllProducts();
    }

    public List<ProductsforUser> getProductsByCategory(String category) {
        return productdao.getProductsByCategory(category);
    }

    public ProductsforUser getProductById(String productId) {
        return productdao.getProductById(productId);
    }


    public List<ProductsforUser> getProduct(String category) {
        System.out.println("ProductController - Fetching products for: " + category);
        return productdao.getProductsByCategory(category);
    }
}
