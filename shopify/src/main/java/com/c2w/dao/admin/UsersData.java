package com.c2w.dao.admin;

import java.util.ArrayList;
import java.util.List;

import com.c2w.config.FirebaseInitialize;
import com.c2w.model.admin.Customers;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class UsersData {
    Firestore db = FirebaseInitialize.getFirestore();

    private String notificationFromUsers() {
        ApiFuture<QuerySnapshot> snapshot = db.collection("").whereEqualTo("notification_type", "readOnly").get();
        try {
            for (DocumentSnapshot doc : snapshot.get().getDocuments()) {
                System.out.println(doc.getData());
            }

            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<Customers> getAllCustomers(){
        List<Customers> customerList=new ArrayList<>();
        try{
            QuerySnapshot snapshot=db.collection("Users").get().get();
            for(DocumentSnapshot doc:snapshot.getDocuments()){
                Customers customer=doc.toObject(Customers.class);
                System.out.println(customer.toString());
                if (customer != null) {

                customer.setDocumentId(doc.getId());

                customerList.add(customer);
            }
            }
            //System.out.println(customerList);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return customerList;
    }
    public boolean deleteCustomer(String documentId) {

    try {

        db.collection("Users")
          .document(documentId)
          .delete()
          .get();

        return true;

    } catch (Exception e) {

        e.printStackTrace();
        return false;
    }
}
}