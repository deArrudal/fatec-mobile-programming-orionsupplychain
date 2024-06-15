package com.example.orionsupplychain.model;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SupplyOrder {
    private int supplyOrderId;
    private Customer supplyOrderCustomer;
    private LocalDate supplyOrderDate;
    private LocalDate supplyOrderDeliveryDate;
    private List<Item> supplyOrderItems;
    private Double supplyOrderTotal;

    public SupplyOrder(int supplyOrderId, Customer supplyOrderCustomer, LocalDate supplyOrderDate) {
        this.supplyOrderId = supplyOrderId;
        this.supplyOrderCustomer = supplyOrderCustomer;
        this.supplyOrderDate = supplyOrderDate;
        this.supplyOrderDeliveryDate = supplyOrderDate.plusDays(7);
        supplyOrderItems = new ArrayList<>();
        supplyOrderTotal = 0.;
    }

    public int getSupplyOrderId() {
        return supplyOrderId;
    }

    public void setSupplyOrderId(int supplyOrderId) {
        this.supplyOrderId = supplyOrderId;
    }

    public Customer getSupplyOrderCustomer() {
        return supplyOrderCustomer;
    }

    public void setSupplyOrderCustomer(Customer supplyOrderCustomer) {
        this.supplyOrderCustomer = supplyOrderCustomer;
    }

    public LocalDate getSupplyOrderDate() {
        return supplyOrderDate;
    }

    public void setSupplyOrderDate(LocalDate supplyOrderDate) {
        this.supplyOrderDate = supplyOrderDate;
    }

    public LocalDate getSupplyOrderDeliveryDate() {
        return supplyOrderDeliveryDate;
    }

    public void setSupplyOrderDeliveryDate(LocalDate supplyOrderDeliveryDate) {
        this.supplyOrderDeliveryDate = supplyOrderDeliveryDate;
    }

    public List<Item> getSupplyOrderItems() {
        return supplyOrderItems;
    }

    public void setSupplyOrderItems(List<Item> supplyOrderItems) {
        this.supplyOrderItems = supplyOrderItems;
    }

    public void addItem(Item item) {
        supplyOrderItems.add(item);

        supplyOrderTotal += item.getItemQuantity() * item.getItemProduct().getProductPrice();
    }

    public Double getSupplyOrderTotal() {
        return supplyOrderTotal;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(supplyOrderId) + " - " +
                supplyOrderCustomer.getCustomerName() + " - " +
                supplyOrderDate.toString();
    }
}
