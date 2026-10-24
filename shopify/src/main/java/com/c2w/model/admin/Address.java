package com.c2w.model.admin;

public class Address{
    public String addressType;
    public String city;
    public String fullName;
    public boolean isDefault;
    public String mobile;
    public String pinCode;
    public String state;
    public String street;
      public Address() {
    }
    public Address(String addressType, String city, String fullName, boolean isDefault, String mobile, String pinCode,
            String state, String street) {
        this.addressType = addressType;
        this.city = city;
        this.fullName = fullName;
        this.isDefault = isDefault;
        this.mobile = mobile;
        this.pinCode = pinCode;
        this.state = state;
        this.street = street;
    }
    public String getAddressType() {
        return addressType;
    }
    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public boolean isDefault() {
        return isDefault;
    }
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getPinCode() {
        return pinCode;
    }
    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    
}
