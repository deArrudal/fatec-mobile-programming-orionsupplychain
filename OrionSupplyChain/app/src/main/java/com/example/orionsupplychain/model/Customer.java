package com.example.orionsupplychain.model;

import androidx.annotation.NonNull;

public class Customer {
    private int customerId;
    private String customerCNPJ;
    private String customerName;
    private String customerAddress;
    private String customerPhone;

    public Customer(int customerId, String customerCNPJ, String customerName,
                    String customerAddress, String customerPhone) {
        this.customerId = customerId;
        this.customerCNPJ = customerCNPJ;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCNPJ() {
        return customerCNPJ;
    }

    public void setCustomerCNPJ(String customerCNPJ) {
        this.customerCNPJ = customerCNPJ;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(customerId) + " - " + customerName;
    }
}
