package com.c2w.dao.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.c2w.config.FirebaseInitialize;
import com.c2w.model.user.Address;
import com.c2w.model.user.User;
import com.c2w.view.authentication.AuthPage;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;

public class UserData {

    private Firestore db = FirebaseInitialize.getFirestore();
    static String user_email = AuthPage.email;

    public void addUser(User user) {
        System.out.println(("--------User Data in Dao---------"));
        try {

            db.collection("Users")
                    .document(user_email)
                    .set(user);

            System.out.println("===================Data Inserted=====================");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateUser(User user) {
        System.out.println("upadated email"+user.getEmail());
        try {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("fullName", user.getFullName());
            userMap.put("email", user.getEmail());
            userMap.put("mobile", user.getMobile());
            userMap.put("gender", user.getGender());

            List<Map<String, Object>> addressMaps = new ArrayList<>();
            List<Address> addresses = user.getAddresses();

            if (addresses != null) {
                for (Address a : addresses) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("fullName", a.getFullName());
                    m.put("mobile", a.getMobile());
                    m.put("street", a.getStreet());
                    m.put("city", a.getCity());
                    m.put("state", a.getState());
                    m.put("pinCode", a.getPinCode());
                    m.put("addressType", a.getAddressType());
                    m.put("isDefault", a.isDefault());
                    addressMaps.add(m);
                }
            }
            System.out.println("***********************full name:"+user.getFullName());
            userMap.put("addresses", addressMaps);

            db.collection("Users")
                    .document(user.getEmail())
                    .set(userMap, SetOptions.merge());

            System.out.println("====================Data Updated======================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUser(String user_email) {

        try {
            System.out.println("dao email:" + user_email);
            ApiFuture<DocumentSnapshot> future = db.collection("Users")
                    .document(user_email)
                    .get();

            DocumentSnapshot document = future.get();
            System.out.println(document.get("email"));
            System.out.println("+++++++++++++++++++++" + document);
            System.out.println(document.exists());
            if (document.exists()) {
                System.out.println("document exist");
                System.out.println(document.toObject(User.class));
                return document.toObject(User.class);
            }
            System.out.println("===================Data fetched=====================");
            System.out.println(user_email);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("return null hoil");
        return null;
    }

}
