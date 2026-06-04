package com.example.innowisefourthproject.service.impl;

import com.example.innowisefourthproject.dao.ItemDao;
import com.example.innowisefourthproject.dao.OrderDao;
import com.example.innowisefourthproject.dao.impl.ItemDaoImpl;
import com.example.innowisefourthproject.dao.impl.OrderDaoImpl;
import com.example.innowisefourthproject.entity.Order;
import com.example.innowisefourthproject.entity.OrderStatus;
import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.DaoException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);
    private final static OrderServiceImpl instance = new OrderServiceImpl();
    private final OrderDao orderDao = OrderDaoImpl.getInstance();
    private final ItemDao itemDao = ItemDaoImpl.getInstance();


    private OrderServiceImpl() {
    }

    public static OrderServiceImpl getInstance() {
        return instance;
    }

    @Override
    public boolean createOrder(User user, String itemId) throws ServiceException {
        logger.info("createOrder in OrderServiceImpl");
        validateUser(user);
        long parsedId = parserId(itemId, "Incorrect id");
        try {
            if (itemDao.findById(parsedId).isEmpty()) {
                throw new ServiceException("Item was not found");
            }
            Order order = new Order(
                    user.getId(),
                    parsedId,
                    OrderStatus.CREATED
            );
            return orderDao.insert(order);
        } catch (DaoException e) {
            logger.error("Error of createOrder", e);
            throw new ServiceException("Could not created order", e);
        }
    }

    @Override
    public List<Order> findOrders(User user) throws ServiceException {
        logger.info("findOrders in db");
        validateUser(user);
        try {
            if (user.isAdmin()) {
                return orderDao.findAll();
            }
            return orderDao.findByUserId(user.getId());
        } catch (DaoException e) {
            logger.error("Error can not find orders", e);
            throw new ServiceException("Failed to find orders", e);
        }
    }

    @Override
    public boolean cancelOrder(User user, String orderId) throws ServiceException {
        logger.info("Cancel order");
        validateUser(user);

        long parsedOrderId = parserId(orderId, "Incorrect order id");

        try {
            Optional<Order> orderOptional = orderDao.findById(parsedOrderId);

            if (orderOptional.isEmpty()) {
                return false;
            }

            Order order = orderOptional.get();

            if (!user.isAdmin() && order.getUserId() != user.getId()) {
                throw new ServiceException("Access denied");
            }

            if (order.getStatus() == OrderStatus.CANCELLED) {
                return false;
            }

            return orderDao.updateStatus(parsedOrderId, OrderStatus.CANCELLED.name());
        } catch (DaoException e) {
            logger.error("Error to cancel order", e);
            throw new ServiceException("Could not cancel order", e);
        }
    }


    @Override
    public boolean completeOrder(User user, String orderId) throws ServiceException {
        logger.info("CompleteOrder");
        validateUser(user);
        if (!user.isAdmin()) {
            throw new ServiceException("Access denied");
        }
        long parsedId = parserId(orderId, "Incorrect order id");
        try {
            Optional<Order> orderOptional = orderDao.findById(parsedId);
            if (orderOptional.isEmpty()) {
                return false;
            }
            Order order = orderOptional.get();
            if (order.getStatus() == OrderStatus.CANCELLED) {
                return false;
            }
            return orderDao.updateStatus(parsedId, OrderStatus.COMPLETED.name());
        } catch (DaoException e) {
            logger.error("Error to complete order ", e);
            throw new ServiceException("Could not complete order", e);
        }
    }

    private void validateUser(User user) throws ServiceException {
        if (user == null) {
            throw new ServiceException("User is not created");
        }
        if (user.getId() <= 0) {
            throw new ServiceException("Incorrect user id");
        }
    }

    private long parserId(String id, String errorMes) throws ServiceException {
        if (id == null || id.trim().isEmpty()) {
            throw new ServiceException(errorMes);
        }
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new ServiceException(errorMes, e);
        }
    }
}
