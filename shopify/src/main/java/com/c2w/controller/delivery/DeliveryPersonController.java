package com.c2w.controller.delivery;

import com.c2w.model.delivery.DeliveryPerson;
import com.google.cloud.Timestamp;
import com.c2w.dao.delivery.*;

public class DeliveryPersonController {

    DeliveryPersonDao dao = new DeliveryPersonDao();

    public void addDeliveryPerson(String id, String name, String email, String phone, String vehicleType,
            String vehicleNumber, String address, String profileCompleted, String approvalStatus,
            String notificationType, double walletBalance) {
        DeliveryPerson deliveryPerson = new DeliveryPerson(id, name, email, phone, vehicleType, vehicleNumber, address,
                profileCompleted, approvalStatus, Timestamp.now(), notificationType, walletBalance);
        dao.addDeliveryPersonData(deliveryPerson);
        System.out.println("In Controller");

    }

    // Get specific data
    public String getDeliveryData(String email, String field) {
        String status = dao.getData(email, field);
        System.out.println("In getDeliveryAuthStatus for " + email);
        return status;
    }

    // Get all data
    public DeliveryPerson getAllDeliveryPersonsData(String email) {
        return dao.getAllData(email);

    }

    // To get ID
    public String getId(String email) {
        String id = dao.generateDeliveryPersonId();
        return id;
    }

    // Update delivery person data//
    public boolean updateDeliveryPerson(
            String phone,
            String vehicleType,
            String vehicleNumber,
            String address, String isUpdate) {

        return dao.updateDeliveryPerson(
                phone,
                vehicleType,
                vehicleNumber,
                address, isUpdate);
    }
    public double getWalletBalance(String email) {
        return dao.getWalletBalance(email);
    }

    public int getTotalOrders(String email) {
        return dao.getTotalOrders(email);
    }

    public void incrementTotalOrders(String email) {
        dao.incrementTotalOrders(email);
    }
}
