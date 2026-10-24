package com.c2w.model.admin;



public class ShippingAddress {

    private String addressType;
    private String city;
    private boolean isDefault;
    private String formattedAddress;
    private String fullName;
    private String mobile;
    private String pinCode;
    private String state;
    private String street;

    public ShippingAddress() {
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

    public boolean isisDefault() {
        return isDefault;
    }

    public void setisDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    @Override
    public String toString() {
        return "ShippingAddress [addressType=" + addressType + ", city=" + city + ", defaultAddress=" + isDefault
                + ", formattedAddress=" + formattedAddress + ", fullName=" + fullName + ", mobile=" + mobile
                + ", pinCode=" + pinCode + ", state=" + state + ", street=" + street + "]";
    }

}
