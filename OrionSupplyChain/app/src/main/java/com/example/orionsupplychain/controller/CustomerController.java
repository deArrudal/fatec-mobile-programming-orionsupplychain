package com.example.orionsupplychain.controller;

import com.example.orionsupplychain.model.Customer;
import com.example.orionsupplychain.persistence.CustomerDAO;

import java.sql.SQLException;
import java.util.List;

public class CustomerController implements IController<Customer> {
    private final CustomerDAO customerDAO;

    public CustomerController(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public void registerEntry(Customer customer) throws SQLException {
        if (customerDAO.open() == null) {
            customerDAO.open();
        }

        // check if id is in use.
        if (customerDAO.checkIfEntryExist(customer)) {
            throw new SQLException("A customer with this id already exists");
        }

        customerDAO.registerEntry(customer);

        customerDAO.close();
    }

    @Override
    public void updateEntry(Customer customer) throws SQLException {
        if (customerDAO.open() == null) {
            customerDAO.open();
        }

        // check if id exists.
        if (!customerDAO.checkIfEntryExist(customer)) {
            throw new SQLException("A customer with this id was not found");
        }

        customerDAO.updateEntry(customer);

        customerDAO.close();
    }

    @Override
    public void removeEntry(Customer customer) throws SQLException {
        if (customerDAO.open() == null) {
            customerDAO.open();
        }

        // check if id exists.
        if (!customerDAO.checkIfEntryExist(customer)) {
            throw new SQLException("A customer with this id was not found");
        }

        customerDAO.removeEntry(customer);

        customerDAO.close();
    }

    @Override
    public Customer searchEntry(Customer customer) throws SQLException {
        if (customerDAO.open() == null) {
            customerDAO.open();
        }

        return customerDAO.searchEntry(customer);
    }

    @Override
    public List<Customer> listEntry() throws SQLException {
        if (customerDAO.open() == null) {
            customerDAO.open();
        }

        return customerDAO.listEntry();
    }
}
