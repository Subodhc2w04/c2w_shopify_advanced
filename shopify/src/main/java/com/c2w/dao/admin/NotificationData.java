package com.c2w.dao.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.c2w.config.FirebaseInitialize;
import com.c2w.model.admin.NotificationModel;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

public class NotificationData {
    Firestore db=FirebaseInitialize.getFirestore();

    public List<List<NotificationModel>> getNotifications(){
        List<List<NotificationModel>> mergedList = new ArrayList<>();
        List<NotificationModel> deliveryNotificationList=new ArrayList<>();
        List<NotificationModel> customerNotificationList=new ArrayList<>();
        try {
            QuerySnapshot snapshot = db.collection("Notifications").get().get();
            for(DocumentSnapshot doc:snapshot.getDocuments()){
                if(doc.getString("role").equals("deliveryPerson")){
                    NotificationModel n=doc.toObject(NotificationModel.class);
                    deliveryNotificationList.add(n);
        
                }else if(doc.getString("role").equals("customer")){
                    NotificationModel n=doc.toObject(NotificationModel.class);
                    customerNotificationList.add(n);
                
                }
            }
            mergedList.add(deliveryNotificationList);
            mergedList.add(customerNotificationList);
    
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            
        }
        return mergedList;
    }
}
