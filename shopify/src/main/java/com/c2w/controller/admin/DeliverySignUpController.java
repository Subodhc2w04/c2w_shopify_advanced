package com.c2w.controller.admin;

import java.util.List;

import com.c2w.dao.admin.DeliverySignUpData;
import com.c2w.model.admin.DeliverySignUpNotifications;
import com.c2w.model.admin.NotificationModel;

public class DeliverySignUpController {
    DeliverySignUpData deliverySignUpData = new DeliverySignUpData();

    public List<DeliverySignUpNotifications> goToDeliverySignUpNotifications(String userType) {

        

        return deliverySignUpData.getDeliveryNotification(userType);
    }

    public void updateApprovalStatus(String userType,String id, String status) {
        

        deliverySignUpData.updateApprovalStatus(userType,id, status);
    }

    public List<DeliverySignUpNotifications> goToDeliverySignUpNotificationsHistory() {
        
        return deliverySignUpData.getAllNotifications();
    }

    public List<DeliverySignUpNotifications> goToDeliveryPersonData(){
        return new DeliverySignUpData().getDeliveryPersonData();
    }

    public void addNotification(NotificationModel notificationModel){
        deliverySignUpData.addNotificationData(notificationModel);
    }

    public void goToDeleteDelivery(String id){
        deliverySignUpData.deleteDeliveryPerson(id);
    }
}
