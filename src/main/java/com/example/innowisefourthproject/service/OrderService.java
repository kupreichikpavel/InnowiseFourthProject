package com.example.innowisefourthproject.service;

import com.example.innowisefourthproject.entity.Order;
import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.ServiceException;

import java.util.List;

public interface OrderService {

    boolean createOrder(User user, String itemId) throws ServiceException;

    List<Order> findOrders(User user) throws ServiceException;

    boolean cancelOrder(User user, String orderId) throws ServiceException;

    boolean completeOrder(User user, String orderId) throws ServiceException;
}
