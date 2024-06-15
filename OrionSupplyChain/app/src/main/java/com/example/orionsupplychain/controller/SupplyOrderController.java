package com.example.orionsupplychain.controller;

import com.example.orionsupplychain.model.SupplyOrder;
import com.example.orionsupplychain.persistence.SupplyOrderDAO;

import java.sql.SQLException;
import java.util.List;

public class SupplyOrderController implements IController<SupplyOrder> {
    private final SupplyOrderDAO supplyOrderDAO;

    public SupplyOrderController(SupplyOrderDAO supplyOrderDAO) {
        this.supplyOrderDAO = supplyOrderDAO;
    }

    @Override
    public void registerEntry(SupplyOrder supplyOrder) throws SQLException {
        if (supplyOrderDAO.open() == null) {
            supplyOrderDAO.open();
        }

        // check if id is in use.
        if (supplyOrderDAO.checkIfEntryExist(supplyOrder)) {
            throw new SQLException("A order with this id already exists");
        }

        supplyOrderDAO.registerEntry(supplyOrder);

        supplyOrderDAO.close();
    }

    @Override
    public void updateEntry(SupplyOrder supplyOrder) throws SQLException {
        if (supplyOrderDAO.open() == null) {
            supplyOrderDAO.open();
        }

        // check if id exists.
        if (!supplyOrderDAO.checkIfEntryExist(supplyOrder)) {
            throw new SQLException("A order with this id was not found");
        }

        supplyOrderDAO.updateEntry(supplyOrder);

        supplyOrderDAO.close();
    }

    @Override
    public void removeEntry(SupplyOrder supplyOrder) throws SQLException {
        if (supplyOrderDAO.open() == null) {
            supplyOrderDAO.open();
        }

        // check if id exists.
        if (!supplyOrderDAO.checkIfEntryExist(supplyOrder)) {
            throw new SQLException("A order with this id was not found");
        }

        supplyOrderDAO.removeEntry(supplyOrder);

        supplyOrderDAO.close();
    }

    @Override
    public SupplyOrder searchEntry(SupplyOrder supplyOrder) throws SQLException {
        if (supplyOrderDAO.open() == null) {
            supplyOrderDAO.open();
        }

        return supplyOrderDAO.searchEntry(supplyOrder);
    }

    @Override
    public List<SupplyOrder> listEntry() throws SQLException {
        if (supplyOrderDAO.open() == null) {
            supplyOrderDAO.open();
        }

        return supplyOrderDAO.listEntry();
    }
}
