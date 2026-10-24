package com.c2w.controller.admin;

import java.util.List;

import com.c2w.dao.admin.OrderData;
import com.c2w.model.admin.Orders;

public class OrderController {
    OrderData orderData=new OrderData();
    public List<Orders> goToAllOrders(){
        return orderData.getAllOrders();
    }

    public boolean updateOrderStatus(String docId,String status){
        return orderData.updateOrderStatusData(docId, status);
    }

    public boolean deleteOrder(String docId){
        return orderData.deleteOrderData(docId);
    }
}
