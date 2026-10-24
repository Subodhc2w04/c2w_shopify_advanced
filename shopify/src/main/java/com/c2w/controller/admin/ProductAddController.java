package com.c2w.controller.admin;

import java.util.List;

import com.c2w.dao.admin.ProductData;
import com.c2w.model.admin.Products;

public class ProductAddController {
    ProductData productData = new ProductData();
    public void addProduct(Products product) {
        productData.addProduct(product);
    }

    public List<Products> getAllProductsList() {
        return productData.getAllProducts();
    }
    public boolean updateProductData(Products product){
        return productData.updateProduct(product);
    }
}
