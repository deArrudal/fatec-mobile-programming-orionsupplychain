package com.example.orionsupplychain.controller;

import com.example.orionsupplychain.model.Goods;
import com.example.orionsupplychain.persistence.GoodsDAO;

import java.sql.SQLException;
import java.util.List;

public class GoodsController implements IController<Goods> {
    private final GoodsDAO goodsDAO;

    public GoodsController(GoodsDAO goodsDAO) {
        this.goodsDAO = goodsDAO;
    }

    @Override
    public void registerEntry(Goods goods) throws SQLException {
        if (goodsDAO.open() == null) {
            goodsDAO.open();
        }

        // check if id is in use.
        if (goodsDAO.checkIfEntryExist(goods)) {
            throw new SQLException("A product with this id already exists");
        }

        goodsDAO.registerEntry(goods);

        goodsDAO.close();
    }

    @Override
    public void updateEntry(Goods goods) throws SQLException {
        if (goodsDAO.open() == null) {
            goodsDAO.open();
        }

        // check if id exists.
        if (!goodsDAO.checkIfEntryExist(goods)) {
            throw new SQLException("A product with this id was not found");
        }

        goodsDAO.updateEntry(goods);

        goodsDAO.close();
    }

    @Override
    public void removeEntry(Goods goods) throws SQLException {
        if (goodsDAO.open() == null) {
            goodsDAO.open();
        }

        // check if id exists.
        if (!goodsDAO.checkIfEntryExist(goods)) {
            throw new SQLException("A product with this id was not found");
        }

        if (goodsDAO.checkIfItemExist(goods)) {
            throw new SQLException("An item with this product id exists");
        }

        goodsDAO.removeEntry(goods);

        goodsDAO.close();
    }

    @Override
    public Goods searchEntry(Goods goods) throws SQLException {
        if (goodsDAO.open() == null) {
            goodsDAO.open();
        }

        return goodsDAO.searchEntry(goods);

    }

    @Override
    public List<Goods> listEntry() throws SQLException {
        if (goodsDAO.open() == null) {
            goodsDAO.open();
        }

        return goodsDAO.listEntry();
    }
}
