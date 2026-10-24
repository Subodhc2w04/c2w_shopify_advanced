package com.c2w.controller.admin;

import java.util.List;

import com.c2w.dao.admin.NotificationData;
import com.c2w.model.admin.NotificationModel;

public class NotificationController{
    NotificationData notificationData=new NotificationData();
    public List<List<NotificationModel>> gotoDeliveryNotifications(){
        return notificationData.getNotifications();
    }
}