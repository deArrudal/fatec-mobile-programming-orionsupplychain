package com.example.orionsupplychain.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GenericDAO extends SQLiteOpenHelper {
    private static final String DATABASE = "ORIONSC.DB";
    private static final int DATABASE_VER = 1;
    private static final String CREATE_TABLE_CUSTOMER =
            "CREATE TABLE customer( " +
                    "customerId INTEGER NOT NULL, " +
                    "customerCNPJ CHAR(14) NOT NULL, " +
                    "customerName VARCHAR(100) NOT NULL, " +
                    "customerAddress VARCHAR(100) NOT NULL, " +
                    "customerPhone VARCHAR(13) NOT NULL, " +
                    "PRIMARY KEY (customerId));";
    private static final String CREATE_TABLE_PRODUCT =
            "CREATE TABLE product( " +
                    "productId INTEGER NOT NULL, " +
                    "productName VARCHAR(100) NOT NULL, " +
                    "productPrice DECIMAL(10,2) NOT NULL, " +
                    "PRIMARY KEY (productId));";
    private static final String CREATE_TABLE_FOOD =
            "CREATE TABLE food( " +
                    "foodProductId INTEGER, " +
                    "foodProducer VARCHAR(100) NOT NULL, " +
                    "FOREIGN KEY (foodProductId) REFERENCES product(productId) ON DELETE CASCADE);";
    private static final String CREATE_TABLE_GOODS =
            "CREATE TABLE goods( " +
                    "goodsProductId INTEGER NOT NULL, " +
                    "goodsCategory VARCHAR(100) NOT NULL, " +
                    "goodsIsBuild BOOLEAN NOT NULL DEFAULT 0, " +
                    "FOREIGN KEY (goodsProductId) REFERENCES product(productId) ON DELETE CASCADE);";

    private static final String CREATE_TABLE_SUPPLYORDER =
            "CREATE TABLE supplyOrder( " +
                    "supplyOrderId INTEGER NOT NULL, " +
                    "supplyOrderCustomerId INTEGER, " +
                    "supplyOrderDate CHAR(8) NOT NULL, " +
                    "supplyOrderDeliveryDate CHAR(8) NOT NULL, " +
                    "PRIMARY KEY (supplyOrderId), " +
                    "FOREIGN KEY (supplyOrderCustomerId) REFERENCES customer(customerId) ON DELETE CASCADE);";

    private static final String CREATE_TABLE_ITEM =
            "CREATE TABLE item( " +
                    "itemSupplyOrderId INTEGER, " +
                    "itemProductId INTEGER, " +
                    "itemQuantity INTEGER NOT NULL, " +
                    "PRIMARY KEY (itemSupplyOrderId, itemProductId), " +
                    "FOREIGN KEY (itemSupplyOrderId) REFERENCES supplyOrder(supplyOrderId) ON DELETE CASCADE, " +
                    "FOREIGN KEY (itemProductId) REFERENCES product(productId) ON DELETE CASCADE);";

    public GenericDAO(Context context) {
        super(context, DATABASE, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CUSTOMER);
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_FOOD);
        db.execSQL(CREATE_TABLE_GOODS);
        db.execSQL(CREATE_TABLE_SUPPLYORDER);
        db.execSQL(CREATE_TABLE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS item");
            db.execSQL("DROP TABLE IF EXISTS supplyOrder");
            db.execSQL("DROP TABLE IF EXISTS goods");
            db.execSQL("DROP TABLE IF EXISTS food");
            db.execSQL("DROP TABLE IF EXISTS product");
            db.execSQL("DROP TABLE IF EXISTS customer");

            onCreate(db);
        }
    }
}
