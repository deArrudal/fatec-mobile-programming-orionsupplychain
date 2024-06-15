package com.example.orionsupplychain.persistence;

import java.sql.SQLException;

public interface IGoodsDAO {
    public GoodsDAO open() throws SQLException;

    public void close();
}
