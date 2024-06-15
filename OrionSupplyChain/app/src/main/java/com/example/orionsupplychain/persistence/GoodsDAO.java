package com.example.orionsupplychain.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.orionsupplychain.model.Goods;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GoodsDAO implements IGoodsDAO, ICRUDDAO<Goods> {
    private final Context context;
    private GenericDAO genericDAO;
    private SQLiteDatabase db;

    public GoodsDAO(Context context) {
        this.context = context;
    }

    @Override
    public GoodsDAO open() throws SQLException {
        genericDAO = new GenericDAO(context);
        db = genericDAO.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");

        return this;
    }

    @Override
    public void close() {
        genericDAO.close();
    }

    private static ContentValues getContentValues(Goods goods, boolean isSuper) {
        ContentValues contentValues = new ContentValues();
        if (isSuper) {
            contentValues.put("productId", goods.getProductId());
            contentValues.put("productName", goods.getProductName());
            contentValues.put("productPrice", goods.getProductPrice());
        } else {
            contentValues.put("goodsProductId", goods.getProductId());
            contentValues.put("goodsCategory", goods.getGoodsCategory());
            contentValues.put("goodsIsBuild", goods.getGoodsIsBuild());
        }

        return contentValues;
    }

    @Override
    public void registerEntry(Goods goods) throws SQLException {
        db.insert("product", null, getContentValues(goods, true));
        db.insert("goods", null, getContentValues(goods, false));
    }

    @Override
    public void updateEntry(Goods goods) throws SQLException {
        db.update("product", getContentValues(goods, true),
                "productId = " + goods.getProductId(), null);
        db.update("goods", getContentValues(goods, false),
                "goodsProductId = " + goods.getProductId(), null);
    }

    @Override
    public void removeEntry(Goods goods) throws SQLException {
        db.delete("product", "productId = " + goods.getProductId(), null);
        db.delete("goods", "goodsProductId = " + goods.getProductId(), null);
    }

    @SuppressLint("Range")
    @Override
    public Goods searchEntry(Goods goods) throws SQLException {
        String querySQL = "SELECT " +
                "product.productId, product.productName, product.productPrice, " +
                "goods.goodsCategory, goods.goodsIsBuild " +
                "FROM product, goods " +
                "WHERE product.productId = goods.goodsProductId " +
                "AND goods.goodsProductId = " + goods.getProductId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            goods.setProductId(cursor.getInt(cursor.getColumnIndex("productId")));
            goods.setProductName(cursor.getString(cursor.getColumnIndex("productName")));
            goods.setProductPrice(cursor.getDouble(cursor.getColumnIndex("productPrice")));
            goods.setGoodsCategory(cursor.getString(cursor.getColumnIndex("goodsCategory")));
            goods.setGoodsIsBuild(convertSQLBoolean(cursor.getInt(cursor.getColumnIndex("goodsIsBuild"))));
        }

        cursor.close();

        return goods;
    }

    @SuppressLint("Range")
    @Override
    public List<Goods> listEntry() throws SQLException {
        List<Goods> goodsList = new ArrayList<>();

        String querySQL = "SELECT " +
                "product.productId, product.productName, product.productPrice, " +
                "goods.goodsCategory, goods.goodsIsBuild " +
                "FROM product, goods " +
                "WHERE product.productId = goods.goodsProductId ";

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {
            Goods goods = new Goods(
                    cursor.getInt(cursor.getColumnIndex("productId")),
                    cursor.getString(cursor.getColumnIndex("productName")),
                    cursor.getDouble(cursor.getColumnIndex("productPrice")),
                    cursor.getString(cursor.getColumnIndex("goodsCategory")),
                    convertSQLBoolean(cursor.getInt(cursor.getColumnIndex("goodsIsBuild")))
            );

            goodsList.add(goods);

            cursor.moveToNext();
        }

        cursor.close();

        return goodsList;
    }

    public boolean checkIfEntryExist(Goods goods) {
        boolean existProduct = false;

        String querySQL =
                "SELECT product.productId FROM product WHERE product.productId = " + goods.getProductId();

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

    public boolean checkIfItemExist(Goods goods) {
        boolean existItem = false;

        String querySQL =
                "SELECT itemProductId FROM item WHERE itemProductId = " + goods.getProductId();

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

    private boolean convertSQLBoolean(int value) {
        return value == 1;
    }
}
