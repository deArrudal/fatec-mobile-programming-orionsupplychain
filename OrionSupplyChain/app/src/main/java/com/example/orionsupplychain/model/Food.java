package com.example.orionsupplychain.model;

import androidx.annotation.NonNull;

public class Food extends Product {
    private String foodProducer;

    public Food(int productId, String productName, double productPrice, String foodProducer) {
        super(productId, productName, productPrice);
        this.foodProducer = foodProducer;
    }

    public String getFoodProducer() {
        return foodProducer;
    }

    public void setFoodProducer(String foodProducer) {
        this.foodProducer = foodProducer;
    }

    @NonNull
    @Override
    public String toString() {
        return getProductName() + " - " + String.valueOf(getProductPrice());
    }
}
