package com.example.orionsupplychain.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.orionsupplychain.model.Customer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements ICustomerDAO, ICRUDDAO<Customer> {
    private final Context context;
    private GenericDAO genericDAO;
    private SQLiteDatabase db;

    public CustomerDAO(Context context) {
        this.context = context;
    }

    @Override
    public CustomerDAO open() throws SQLException {
        genericDAO = new GenericDAO(context);
        db = genericDAO.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");

        return this;
    }

    @Override
    public void close() {
        genericDAO.close();
    }

    private static ContentValues getContentValues(Customer customer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("customerId", customer.getCustomerId());
        contentValues.put("customerCNPJ", customer.getCustomerCNPJ());
        contentValues.put("customerName", customer.getCustomerName());
        contentValues.put("customerAddress", customer.getCustomerAddress());
        contentValues.put("customerPhone", customer.getCustomerPhone());

        return contentValues;
    }

    @Override
    public void registerEntry(Customer customer) throws SQLException {
        db.insert("customer", null, getContentValues(customer));
    }

    @Override
    public void updateEntry(Customer customer) throws SQLException {
        db.update("customer", getContentValues(customer),
                "customerId = " + customer.getCustomerId(), null);
    }

    @Override
    public void removeEntry(Customer customer) throws SQLException {
        db.delete("customer", "customerId = " + customer.getCustomerId(), null);
    }

    @SuppressLint("Range")
    @Override
    public Customer searchEntry(Customer customer) throws SQLException {
        String querySQL = "SELECT " +
                "customerId, customerCNPJ, customerName, customerAddress, customerPhone " +
                "FROM customer WHERE customerId = " + customer.getCustomerId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            customer.setCustomerId(cursor.getInt(cursor.getColumnIndex("customerId")));
            customer.setCustomerCNPJ(cursor.getString(cursor.getColumnIndex("customerCNPJ")));
            customer.setCustomerName(cursor.getString(cursor.getColumnIndex("customerName")));
            customer.setCustomerAddress(cursor.getString(cursor.getColumnIndex("customerAddress")));
            customer.setCustomerPhone(cursor.getString(cursor.getColumnIndex("customerPhone")));
        }

        cursor.close();

        return customer;
    }

    @SuppressLint("Range")
    @Override
    public List<Customer> listEntry() throws SQLException {
        List<Customer> customers = new ArrayList<>();

        String querySQL = "SELECT " +
                "customerId, customerCNPJ, customerName, customerAddress, customerPhone " +
                "FROM customer ";

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

            customers.add(customer);

            cursor.moveToNext();
        }

        cursor.close();

        return customers;
    }

    public boolean checkIfEntryExist(Customer customer) {
        boolean existCustomer = false;

        String querySQL =
                "SELECT customerId FROM customer WHERE customerId = " + customer.getCustomerId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            existCustomer = true;

            cursor.moveToNext();
        }

        cursor.close();

        return existCustomer;
    }
}
