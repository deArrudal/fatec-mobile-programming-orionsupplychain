package com.example.orionsupplychain.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.orionsupplychain.model.Customer;
import com.example.orionsupplychain.model.Food;
import com.example.orionsupplychain.model.Goods;
import com.example.orionsupplychain.model.Item;
import com.example.orionsupplychain.model.SupplyOrder;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SupplyOrderDAO implements ISupplyOrderDAO, ICRUDDAO<SupplyOrder> {
    private final Context context;
    private GenericDAO genericDAO;
    private SQLiteDatabase db;

    public SupplyOrderDAO(Context context) {
        this.context = context;
    }

    @Override
    public SupplyOrderDAO open() throws SQLException {
        genericDAO = new GenericDAO(context);
        db = genericDAO.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");

        return this;
    }

    @Override
    public void close() {
        genericDAO.close();
    }

    private static ContentValues getSupplyOrderContentValues(SupplyOrder supplyOrder) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("supplyOrderId", supplyOrder.getSupplyOrderId());
        contentValues.put("supplyOrderCustomerId", supplyOrder.getSupplyOrderCustomer().getCustomerId());
        contentValues.put("supplyOrderDate", supplyOrder.getSupplyOrderDate().toString());
        contentValues.put("supplyOrderDeliveryDate", supplyOrder.getSupplyOrderDeliveryDate().toString());

        return contentValues;
    }

    private static ContentValues getItemContentValues(Item item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("itemSupplyOrderId", item.getItemSupplyOrderId());
        contentValues.put("itemProductId", item.getItemProduct().getProductId());
        contentValues.put("itemQuantity", item.getItemQuantity());

        return contentValues;
    }

    @Override
    public void registerEntry(SupplyOrder supplyOrder) throws SQLException {
        db.insert("supplyOrder", null, getSupplyOrderContentValues(supplyOrder));

        // insert each item of the list
        List<Item> items = supplyOrder.getSupplyOrderItems();
        for (Item item : items) {
            db.insert("item", null, getItemContentValues(item));
        }
    }

    @Override
    public void updateEntry(SupplyOrder supplyOrder) throws SQLException {
        // not used
        db.update("supplyOrder", getSupplyOrderContentValues(supplyOrder),
                "supplyOrderId = " + supplyOrder.getSupplyOrderId(), null);

        // update items
        db.delete("item", "itemSupplyOrderId = " + supplyOrder.getSupplyOrderId(), null);
        List<Item> items = supplyOrder.getSupplyOrderItems();
        for (Item item : items) {
            db.insert("item", null, getItemContentValues(item));
        }
    }

    @Override
    public void removeEntry(SupplyOrder supplyOrder) throws SQLException {
        db.delete("supplyOrder", "supplyOrderId = " + supplyOrder.getSupplyOrderId(), null);
        db.delete("item", "itemSupplyOrderId = " + supplyOrder.getSupplyOrderId(), null);
    }

    @SuppressLint("Range")
    @Override
    public SupplyOrder searchEntry(SupplyOrder supplyOrder) throws SQLException {
        String querySQL = "SELECT " +
                "supplyOrderId, supplyOrderDate, supplyOrderDeliveryDate, " +
                "customerId, customerCNPJ, customerName, customerAddress, customerPhone " +
                "FROM supplyOrder, customer " +
                "WHERE supplyOrder.supplyOrderId = " + supplyOrder.getSupplyOrderId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            Customer customer = new Customer(
                    cursor.getInt(cursor.getColumnIndex("customerId")),
                    cursor.getString(cursor.getColumnIndex("customerCNPJ")),
                    cursor.getString(cursor.getColumnIndex("customerName")),
                    cursor.getString(cursor.getColumnIndex("customerAddress")),
                    cursor.getString(cursor.getColumnIndex("customerPhone"))
            );

            supplyOrder.setSupplyOrderCustomer(customer);

            supplyOrder.setSupplyOrderDate(LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("supplyOrderDate"))));

            supplyOrder.setSupplyOrderDeliveryDate(LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("supplyOrderDeliveryDate"))));

        } else {
            return null;
        }

        // get all items in the order
        List<Item> items = new ArrayList<>();

        querySQL = "SELECT " +
                "product.productId, product.productName, product.productPrice, " +
                "food.foodProducer, " +
                "goods.goodsCategory, goods.goodsIsBuild, " +
                "itemQuantity " +
                "FROM item " +
                "INNER JOIN product ON item.itemProductId = product.productId " +
                "LEFT JOIN food ON product.productId = food.foodProductId " +
                "LEFT JOIN goods ON product.productId = goods.goodsProductId " +
                "WHERE item.itemSupplyOrderId = " + supplyOrder.getSupplyOrderId();

        cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {
            Item item = new Item();

            if (!cursor.isNull(cursor.getColumnIndex("foodProducer"))) {
                Food food = new Food(
                        cursor.getInt(cursor.getColumnIndex("productId")),
                        cursor.getString(cursor.getColumnIndex("productName")),
                        cursor.getDouble(cursor.getColumnIndex("productPrice")),
                        cursor.getString(cursor.getColumnIndex("foodProducer"))
                );

                item.setItemProduct(food);

            } else {
                Goods goods = new Goods(
                        cursor.getInt(cursor.getColumnIndex("productId")),
                        cursor.getString(cursor.getColumnIndex("productName")),
                        cursor.getDouble(cursor.getColumnIndex("productPrice")),
                        cursor.getString(cursor.getColumnIndex("goodsCategory")),
                        convertSQLBoolean(cursor.getInt(cursor.getColumnIndex("goodsIsBuild")))
                );

                item.setItemProduct(goods);
            }

            item.setItemQuantity(cursor.getInt(cursor.getColumnIndex("itemQuantity")));

            items.add(item);

            cursor.moveToNext();
        }

        supplyOrder.setSupplyOrderItems(items);

        cursor.close();

        return supplyOrder;
    }

    @SuppressLint("Range")
    @Override
    public List<SupplyOrder> listEntry() throws SQLException {
        List<SupplyOrder> supplyOrders = new ArrayList<>();

        String querySQL = "SELECT " +
                "supplyOrderId, supplyOrderDate, supplyOrderDeliveryDate, " +
                "customerId, customerCNPJ, customerName, customerAddress, customerPhone " +
                "FROM supplyOrder, customer " +
                "WHERE supplyOrder.supplyOrderCustomerId = customer.customerId";

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {
            Customer customer = new Customer(
                    cursor.getInt(cursor.getColumnIndex("customerId")),
                    cursor.getString(cursor.getColumnIndex("customerCNPJ")),
                    cursor.getString(cursor.getColumnIndex("customerName")),
                    cursor.getString(cursor.getColumnIndex("customerAddress")),
                    cursor.getString(cursor.getColumnIndex("customerPhone"))
            );

            SupplyOrder supplyOrder = new SupplyOrder(
                    cursor.getInt(cursor.getColumnIndex("supplyOrderId")),
                    customer,
                    LocalDate.parse(cursor.getString(cursor.getColumnIndex("supplyOrderDate")))
            );

            supplyOrders.add(supplyOrder);

            cursor.moveToNext();
        }

        cursor.close();

        return supplyOrders;
    }

    @Override
    public boolean checkIfEntryExist(SupplyOrder supplyOrder) throws SQLException {
        boolean existSupplyOrder = false;

        String querySQL =
                "SELECT supplyOrderId FROM supplyOrder WHERE supplyOrderId = " + supplyOrder.getSupplyOrderId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            existSupplyOrder = true;

            cursor.moveToNext();
        }

        cursor.close();

        return existSupplyOrder;
    }

    private boolean convertSQLBoolean(int value) {
        return value == 1;
    }
}
