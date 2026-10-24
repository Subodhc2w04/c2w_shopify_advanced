package com.c2w.model.user;

public class Address {
    private String fullName;
    private String mobile;
    private String street;
    private String city;
    private String state;
    private String pinCode;
    private String addressType; 
    private boolean isDefault;

    public Address(){}

    public Address(String fullName, String mobile, String street, String city, String state, String pinCode, String addressType, boolean isDefault) {
        this.fullName = fullName;
        this.mobile = mobile;
        this.street = street;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.addressType = addressType != null ? addressType : "Home";
        this.isDefault = isDefault;
    }

    public String getFullName() { return fullName; }
    public String getMobile() { return mobile; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getPinCode() { return pinCode; }
    public String getAddressType() { return addressType; }
    public boolean isDefault() { return isDefault; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setStreet(String street) { this.street = street; }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    public void setPinCode(String pinCode) { this.pinCode = pinCode; }
    public void setAddressType(String addressType) { this.addressType = addressType; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }

    public String getFormattedAddress() {
        return street + ", " + city + ", " + state + " - " + pinCode;
    }
}
