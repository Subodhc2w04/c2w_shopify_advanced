package com.c2w.dao.delivery;

import java.util.HashMap;
import java.util.Map;

import com.c2w.config.FirebaseInitialize;
import com.c2w.model.delivery.DeliveryOrderNotification;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;

public class DeliveryOrderNotificationDao {

    private Firestore db = FirebaseInitialize.getFirestore();

    // TO SEND ORDER STATUS NOTIFICATION TO ADMIN COLLECTION
    public void sendNotificationToAdmin(DeliveryOrderNotification notif) {
        try {
            Map<String, Object> data = new HashMap<>();
            
            if(notif.getDeliveryPersonEmail().contains("_user")){
                data.put("role", "customer");
            }else{
                data.put("role", "deliveryPerson");
            }
            
            data.put("orderId", notif.getOrderId());
            data.put("email", notif.getDeliveryPersonEmail());
            data.put("name", notif.getDeliveryPersonName());
            data.put("status", notif.getStatus());
            data.put("message", notif.getMessage());
            data.put("timestamp", notif.getTimestamp() != null ? notif.getTimestamp() : FieldValue.serverTimestamp());
            data.put("notiType", "ReadOnly");

            db.collection("Notifications").add(data).get();
            System.out.println("Notification sent to admin for order #" + notif.getOrderId() + " with status: "
                    + notif.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
