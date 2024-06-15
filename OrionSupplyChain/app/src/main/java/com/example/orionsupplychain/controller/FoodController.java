package com.example.orionsupplychain.controller;

import com.example.orionsupplychain.model.Food;
import com.example.orionsupplychain.persistence.FoodDAO;

import java.sql.SQLException;
import java.util.List;

public class FoodController implements IController<Food> {
    private final FoodDAO foodDAO;

    public FoodController(FoodDAO foodDAO) {
        this.foodDAO = foodDAO;
    }

    @Override
    public void registerEntry(Food food) throws SQLException {
        if (foodDAO.open() == null) {
            foodDAO.open();
        }

        // check if id is in use.
        if (foodDAO.checkIfEntryExist(food)) {
            throw new SQLException("Invalid insert: a product with this id already exists");
        }

        foodDAO.registerEntry(food);

        foodDAO.close();
    }

    @Override
    public void updateEntry(Food food) throws SQLException {
        if (foodDAO.open() == null) {
            foodDAO.open();
        }

        // check if id exists.
        if (!foodDAO.checkIfEntryExist(food)) {
            throw new SQLException("A product with this id was not found");
        }

        foodDAO.updateEntry(food);

        foodDAO.close();
    }

    @Override
    public void removeEntry(Food food) throws SQLException {
        if (foodDAO.open() == null) {
            foodDAO.open();
        }

        // check if id exists.
        if (!foodDAO.checkIfEntryExist(food)) {
            throw new SQLException("A product with this id was not found");
        }

        if (foodDAO.checkIfItemExist(food)) {
            throw new SQLException("An item with this product id exists");
        }

        foodDAO.removeEntry(food);

        foodDAO.close();
    }

    @Override
    public Food searchEntry(Food food) throws SQLException {
        if (foodDAO.open() == null) {
            foodDAO.open();
        }

        return foodDAO.searchEntry(food);

    }

    @Override
    public List<Food> listEntry() throws SQLException {
        if (foodDAO.open() == null) {
            foodDAO.open();
        }

        return foodDAO.listEntry();
    }
}
