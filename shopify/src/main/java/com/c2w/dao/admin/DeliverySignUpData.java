package com.c2w.dao.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.c2w.config.FirebaseInitialize;
import com.c2w.model.admin.DeliverySignUpNotifications;
import com.c2w.model.admin.NotificationModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class DeliverySignUpData {
    Firestore db = FirebaseInitialize.getFirestore();

    public List<DeliverySignUpNotifications> getDeliveryNotification(String userType) {

        List<DeliverySignUpNotifications> notificationList = new ArrayList<>();

        ApiFuture<QuerySnapshot> query = db.collection(userType)
                .whereEqualTo("approveStatus", "Pending")
                .get();

        try {

            QuerySnapshot querySnapshot = query.get();

            for (QueryDocumentSnapshot doc : querySnapshot.getDocuments()) {

                if (doc.exists()) {
                    // doc.getTimestamp("createdAt").toString();
                    DeliverySignUpNotifications notification = doc.toObject(
                            DeliverySignUpNotifications.class);

                    notificationList.add(notification);

                    System.out.println(
                            "Notification : " + notification);
                }
            }

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();
        }

        return notificationList;
    }

    public void updateApprovalStatus(String userType,String id, String status) {
        ApiFuture<QuerySnapshot> snapshot = db.collection(userType).whereEqualTo("id", id).get();

        try {
            for (QueryDocumentSnapshot doc : snapshot.get().getDocuments()) {
                if (doc.exists()) {
                    doc.getReference().update("approveStatus", status);
                }
            }
            ;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public List<DeliverySignUpNotifications> getAllNotifications() {
        List<DeliverySignUpNotifications> notificationList = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = db.collection("delivery").get();

        try {
            // QuerySnapshot querySnapshot = query.get();

            for (QueryDocumentSnapshot doc : query.get().getDocuments()) {

                if (doc.exists()) {
                    DeliverySignUpNotifications notification = doc.toObject(DeliverySignUpNotifications.class);

                    System.out.println(
                            "Notification : " + notification);
                    notificationList.add(notification);
                }
            }

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();
        }
        return notificationList;
    }

    public List<DeliverySignUpNotifications> getDeliveryPersonData(){
        List<DeliverySignUpNotifications> deliveryPersonsList =new ArrayList<>();
        try {
            QuerySnapshot query=db.collection("delivery").get().get();
            for(DocumentSnapshot doc:query.getDocuments()){
                DeliverySignUpNotifications deliverySignUpData=doc.toObject(DeliverySignUpNotifications.class);
                if(deliverySignUpData != null){
                    deliveryPersonsList.add(deliverySignUpData);
                }
            }

            } catch (InterruptedException | ExecutionException e) {
    
            e.printStackTrace();
        }
        Collections.sort(deliveryPersonsList,new Comparator<DeliverySignUpNotifications>() {
            @Override 
            public int compare(DeliverySignUpNotifications d1, DeliverySignUpNotifications d2) {
                return d1.getId().compareTo(d2.getId());
            }
        });
        return deliveryPersonsList;
    }

    public void addNotificationData(NotificationModel notifications){
        try{
            db.collection("Notifications").add(notifications);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteDeliveryPerson(String id){
        try {
            db.collection("delivery").document(id).delete();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
