package com.c2w.model.admin;

import java.util.List;

public class Customers {

    private String fullName;
    private String email;
    private String mobile;
    private String gender;
    private List<Address> addresses;
    private String documentId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Customers() {
    }

    public Customers(String fullName, String email, String mobile,
                      String gender, List<Address> addresses,String documentId) {

        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
        this.addresses = addresses;
        this.documentId=documentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
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
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
