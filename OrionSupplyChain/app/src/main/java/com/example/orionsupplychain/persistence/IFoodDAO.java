package com.example.orionsupplychain.persistence;

import java.sql.SQLException;

public interface IFoodDAO {
    public FoodDAO open() throws SQLException;

    public void close();
}
