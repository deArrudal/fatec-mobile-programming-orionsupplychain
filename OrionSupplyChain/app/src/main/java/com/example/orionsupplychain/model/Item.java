package com.example.orionsupplychain.model;

import androidx.annotation.NonNull;

public class Item {
    private int itemSupplyOrderId;
    private Product itemProduct;
    private int itemQuantity;

    public Item() {
        super();
    }

    public Item(int itemSupplyOrderId, Product itemProduct, int itemQuantity) {
        this.itemSupplyOrderId = itemSupplyOrderId;
        this.itemProduct = itemProduct;
        this.itemQuantity = itemQuantity;
    }

    public int getItemSupplyOrderId() {
        return itemSupplyOrderId;
    }

    public void setItemSupplyOrderId(int itemSupplyOrderId) {
        this.itemSupplyOrderId = itemSupplyOrderId;
    }

    public Product getItemProduct() {
        return itemProduct;
    }

    public void setItemProduct(Product itemProduct) {
        this.itemProduct = itemProduct;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    @NonNull
    @Override
    public String toString() {
        return itemProduct + " - " + String.valueOf(itemQuantity);
    }
}
