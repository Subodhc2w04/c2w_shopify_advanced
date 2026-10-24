package com.c2w.dao.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.c2w.config.FirebaseInitialize;
import com.c2w.model.admin.Orders;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

public class OrderData {
    Firestore db= FirebaseInitialize.getFirestore();

    public List<Orders> getAllOrders(){
        List<Orders> orderList=new ArrayList<>();
        try {
            QuerySnapshot query=db.collection("Orders").get().get();
            for(DocumentSnapshot doc:query.getDocuments()){
                Orders order=doc.toObject(Orders.class);
                if(order != null){
                    orderList.add(order);
                }

            }
            Collections.sort(orderList,new Comparator<Orders>() {
                @Override 
                public int compare(Orders o1,Orders o2){
                    int status1=getCountForSorting(o1.getStatus()); 
                    int status2=getCountForSorting(o2.getStatus()); 
                    return Integer.compare(status1, status2);
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return orderList;
    }

    public int getCountForSorting(String status){
        switch(status){
            case "ORDER PLACED": return 1;
            case "ORDER_READY_FOR_DISPATCH": return 2;
            case "ACCEPTED": return 3;
            case "PICKED_UP": return 4;
            case "OUT_FOR_DELIVERY": return 5;
            case "DELIVERED" : return 6;
            default : return 7;
        }
    }

    public boolean updateOrderStatusData(String docId,String status){
        System.out.println("Status---"+status);
        System.out.println("docId---"+docId);
        try{
            db.collection("Orders").document(docId).update(Map.of("status",status));
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrderData(String docId){
        try{
            db.collection("Orders").document(docId).delete();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
