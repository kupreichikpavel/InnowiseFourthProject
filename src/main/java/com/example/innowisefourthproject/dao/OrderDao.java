package com.example.innowisefourthproject.dao;

import com.example.innowisefourthproject.entity.Order;
import com.example.innowisefourthproject.exception.DaoException;

import java.util.List;

public interface OrderDao extends BaseDao<Order> {
    List<Order> findByUserId(long userId) throws DaoException;

    boolean updateStatus(long orderId, String status) throws DaoException;
}
