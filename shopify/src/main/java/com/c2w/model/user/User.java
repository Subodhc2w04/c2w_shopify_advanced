package com.c2w.model.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class User {
    private   String fullName;
    private String email;
    private String mobile;
    private String gender;
    private  List<Address> addresses=new ArrayList<>();

    public User(){

    }

    public User( String fullName, String email, String mobile, String gender,List<Address> addresses) {
        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender != null ? gender : null;
        this.addresses = addresses != null
            ? new ArrayList<>(addresses)
            : new ArrayList<>();

        System.out.println("************email:"+email);
        System.out.println("address in model"+addresses);
       
    }

    public   String getFullName() {
        return fullName;
    }

    public   void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    
    public List<Address> getAddresses() {
        
        System.out.println("================getAddress:"+Collections.unmodifiableList(addresses));
        System.out.println();
        return Collections.unmodifiableList(addresses);
    }

    public void addAddress(Address address) {
        addresses.add(address);
    }
     public void updateAddress(int index, Address address) {
        if (index >= 0 && index < addresses.size()) {
            addresses.set(index, address);
        }
    }

    public void removeAddress(int index) {
        if (index >= 0 && index < addresses.size()) {
            addresses.remove(index);
        }
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
    }

    public void setAddresses(List<Address> addresses) {
    this.addresses = addresses != null
            ? new ArrayList<>(addresses)
            : new ArrayList<>();
}

}
