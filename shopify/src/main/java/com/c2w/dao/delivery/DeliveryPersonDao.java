package com.c2w.dao.delivery;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;


import com.c2w.config.*;
import com.c2w.model.delivery.*;
import com.c2w.view.authentication.*;

public class DeliveryPersonDao {

    private Firestore db = FirebaseInitialize.getFirestore();

    // To add delivery persons data

    public void addDeliveryPersonData(DeliveryPerson obj) {
        try {
            db.collection("delivery").document(String.valueOf(AuthPage.email)).create(obj);
            System.out.println("Data entered");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // To get only specific data

    public String getData(String email, String fieldName) {
        try {
            DocumentSnapshot data = db.collection("delivery")
                    .document(email)
                    .get()
                    .get();

            if (data.exists()) {

                return data.getString(fieldName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Get all data from delivery collection
    public DeliveryPerson getAllData(String email) {
        try {
            DocumentSnapshot data = db.collection("delivery").document(String.valueOf(AuthPage.email)).get().get();

            if (data.exists()) {
                Double wallet = data.getDouble("walletBalance");
                Long orders = data.getLong("totalOrders");
                return new DeliveryPerson(data.getString("id"), data.getString("name"), data.getString("email"),
                        data.getString("phone"), data.getString("vehicleType"), data.getString("vehicleNumber"),
                        data.getString("address"), data.getString("profileCompleted"), data.getString("approveStatus"),
                        data.getCreateTime(), data.getString("notiType"),
                        wallet != null ? wallet : 0.0,
                        orders != null ? orders.intValue() : 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateDeliveryPerson(String phone, String vehicleType, String vehicleNumber, String address,
            String isUpdate) {
        try {
            db.collection("delivery").document(AuthPage.email).update("phone", phone, "vehicleType", vehicleType,
                    "vehicleNumber", vehicleNumber, "address", address, "profileCompleted", isUpdate).get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // To generate delivery person ID

    public String generateDeliveryPersonId() {

        int maxId = 0;

        try {
            QuerySnapshot snapshot = db.collection("delivery")
                    .get()
                    .get();

            for (DocumentSnapshot document : snapshot.getDocuments()) {

                String id = document.getString("id");

                if (id != null && id.startsWith("DP")) {

                    int number = Integer.parseInt(
                            id.substring(2));

                    if (number > maxId) {
                        maxId = number;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "DP" + (maxId + 1);
    }

    public double getWalletBalance(String email) {

        try {

            DocumentSnapshot data = db.collection("delivery")
                    .document(email)
                    .get()
                    .get();

            if (data.exists()) {

                Double balance = data.getDouble("walletBalance");

                if (balance != null) {
                    return balance;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    public void addMoney(String email, double amount) {

        double currentBalance = getWalletBalance(email);

        try {

            db.collection("delivery")
                    .document(email)
                    .update("walletBalance", currentBalance + amount)
                    .get();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTotalOrders(String email) {

        try {
            DocumentSnapshot data = db.collection("delivery")
                    .document(email)
                    .get()
                    .get();

            if (data.exists()) {
                Long total = data.getLong("totalOrders");
                if (total != null) {
                    return total.intValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void incrementTotalOrders(String email) {

        int current = getTotalOrders(email);

        try {
            db.collection("delivery")
                    .document(email)
                    .update("totalOrders", current + 1)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
