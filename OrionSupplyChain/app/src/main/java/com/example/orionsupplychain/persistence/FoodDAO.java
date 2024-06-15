package com.example.orionsupplychain.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.orionsupplychain.model.Food;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodDAO implements IFoodDAO, ICRUDDAO<Food> {
    private final Context context;
    private GenericDAO genericDAO;
    private SQLiteDatabase db;

    public FoodDAO(Context context) {
        this.context = context;
    }

    @Override
    public FoodDAO open() throws SQLException {
        genericDAO = new GenericDAO(context);
        db = genericDAO.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");

        return this;
    }

    @Override
    public void close() {
        genericDAO.close();
    }

    private static ContentValues getContentValues(Food food, boolean isSuper) {
        ContentValues contentValues = new ContentValues();
        if (isSuper) {
            contentValues.put("productId", food.getProductId());
            contentValues.put("productName", food.getProductName());
            contentValues.put("productPrice", food.getProductPrice());
        } else {
            contentValues.put("foodProductId", food.getProductId());
            contentValues.put("foodProducer", food.getFoodProducer());
        }

        return contentValues;
    }

    @Override
    public void registerEntry(Food food) throws SQLException {
        db.insert("product", null, getContentValues(food, true));
        db.insert("food", null, getContentValues(food, false));
    }

    @Override
    public void updateEntry(Food food) throws SQLException {
        db.update("product", getContentValues(food, true),
                "productId = " + food.getProductId(), null);
        db.update("food", getContentValues(food, false),
                "foodProductId = " + food.getProductId(), null);
    }

    @Override
    public void removeEntry(Food food) throws SQLException {
        db.delete("product", "productId = " + food.getProductId(), null);
        db.delete("food", "foodProductId = " + food.getProductId(), null);
    }

    @SuppressLint("Range")
    @Override
    public Food searchEntry(Food food) throws SQLException {
        String querySQL = "SELECT " +
                "product.productId, product.productName, product.productPrice, " +
                "food.foodProducer " +
                "FROM product, food " +
                "WHERE product.productId = food.foodProductId " +
                "AND food.foodProductId = " + food.getProductId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            food.setProductId(cursor.getInt(cursor.getColumnIndex("productId")));
            food.setProductName(cursor.getString(cursor.getColumnIndex("productName")));
            food.setProductPrice(cursor.getDouble(cursor.getColumnIndex("productPrice")));
            food.setFoodProducer(cursor.getString(cursor.getColumnIndex("foodProducer")));
        }

        cursor.close();

        return food;
    }

    @SuppressLint("Range")
    @Override
    public List<Food> listEntry() throws SQLException {
        List<Food> foods = new ArrayList<>();

        String querySQL = "SELECT " +
                "product.productId, product.productName, product.productPrice, " +
                "food.foodProducer " +
                "FROM product, food " +
                "WHERE product.productId = food.foodProductId ";

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {
            Food food = new Food(
                    cursor.getInt(cursor.getColumnIndex("productId")),
                    cursor.getString(cursor.getColumnIndex("productName")),
                    cursor.getDouble(cursor.getColumnIndex("productPrice")),
                    cursor.getString(cursor.getColumnIndex("foodProducer"))
            );

            foods.add(food);

            cursor.moveToNext();
        }

        cursor.close();

        return foods;
    }

    public boolean checkIfEntryExist(Food food) {
        boolean existProduct = false;

        String querySQL =
                "SELECT product.productId FROM product WHERE product.productId = " + food.getProductId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            existProduct = true;

            cursor.moveToNext();
        }

        cursor.close();

        return existProduct;
    }

    public boolean checkIfItemExist(Food food) {
        boolean existItem = false;

        String querySQL =
                "SELECT itemProductId FROM item WHERE itemProductId = " + food.getProductId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            existItem = true;

            cursor.moveToNext();
        }

        cursor.close();

        return existItem;
    }
}