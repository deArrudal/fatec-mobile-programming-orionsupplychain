package com.example.orionsupplychain.model;

import androidx.annotation.NonNull;

public class Goods extends Product {
    private String goodsCategory;
    private boolean goodsIsBuild;

    public Goods(int productId, String productName, double productPrice, String goodsCategory,
                 boolean goodsIsBuild) {
        super(productId, productName, productPrice);
        this.goodsCategory = goodsCategory;
        this.goodsIsBuild = goodsIsBuild;
    }

    public String getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(String goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public boolean getGoodsIsBuild() {
        return goodsIsBuild;
    }

    public void setGoodsIsBuild(boolean goodsIsBuild) {
        this.goodsIsBuild = goodsIsBuild;
    }

    @NonNull
    @Override
    public String toString() {
        return getProductName() + " - " + String.valueOf(getProductPrice());
    }
}
