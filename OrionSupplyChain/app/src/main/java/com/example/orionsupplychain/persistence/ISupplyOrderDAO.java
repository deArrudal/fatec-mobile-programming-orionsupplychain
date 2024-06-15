package com.example.orionsupplychain.persistence;

import java.sql.SQLException;

public interface ISupplyOrderDAO {
    public SupplyOrderDAO open() throws SQLException;

    public void close();
}
