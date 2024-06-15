package com.example.orionsupplychain.persistence;

import java.sql.SQLException;

public interface ICustomerDAO {
    public CustomerDAO open() throws SQLException;

    public void close();
}
