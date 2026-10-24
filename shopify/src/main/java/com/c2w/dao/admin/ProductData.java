package com.c2w.dao.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.c2w.config.FirebaseInitialize;
import com.c2w.model.admin.Products;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class ProductData {
    Firestore db= FirebaseInitialize.getFirestore();
    public void addProduct(Products product) {
        try{
            QuerySnapshot query=db.collection("Products").get().get();
            int maxId=0;
            for(QueryDocumentSnapshot doc:query.getDocuments()){
                String productId[]=doc.getString("productId").split("_");
                int id=Integer.parseInt(productId[1]);
                if(id>maxId){
                    maxId=id;
                }
            }
            product.setProductId("Product_"+(maxId+1));
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            db.collection("Products").document(product.getProductId()).set(product);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<Products> getAllProducts(){
        List<Products> products=new ArrayList<>();
        try{
            QuerySnapshot query=db.collection("Products").get().get();
            for(QueryDocumentSnapshot doc:query.getDocuments()){
                Products product=doc.toObject(Products.class);
                products.add(product);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return products;
    }

    public boolean updateProduct(Products product) {
        System.out.println("ProductId - " + product.getProductId());
        try {
            db.collection("Products").document(product.getProductId()).update(
                    "productName", product.getProductName(),
                    "productDescription", product.getProductDescription(),
                    "productPrice", product.getProductPrice(),
                    "productImage", product.getProductImage(),
                    "productCategory", product.getProductCategory(),
                    "productQuantity", product.getProductQuantity(),
                    "couponCode", product.getCouponCode(),
                    "discountType", product.getDiscountType(),
                    "discountValue", product.getDiscountValue(),
                    "couponStatus", product.isCouponStatus());
            System.out.println("Product updated ...............");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
