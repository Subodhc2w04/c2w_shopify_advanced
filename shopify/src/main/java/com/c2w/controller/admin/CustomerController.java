package com.c2w.controller.admin;

import java.util.List;

import com.c2w.dao.admin.UsersData;
import com.c2w.model.admin.Customers;

public class CustomerController {
    public List<Customers> getCustomerData(){
        return new UsersData().getAllCustomers();
    }
    public boolean deleteCustomer(String documentId) {

    return new UsersData().deleteCustomer(documentId);
}
}
