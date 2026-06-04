package com.example.innowisefourthproject.dao.impl;

import com.example.innowisefourthproject.dao.OrderDao;
import com.example.innowisefourthproject.entity.Order;
import com.example.innowisefourthproject.entity.OrderStatus;
import com.example.innowisefourthproject.exception.DaoException;
import com.example.innowisefourthproject.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {
    private static final Logger logger = LogManager.getLogger(OrderDaoImpl.class);
    private static final OrderDaoImpl instance = new OrderDaoImpl();

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String INSERT_ORDER = "INSERT INTO orders(user_id, item_id, status) VALUES (?, ?, ?)";

    private static final String FIND_ALL_ORDERS = "SELECT o.id, o.user_id, o.item_id, i.name AS item_name, i.price AS item_price, o.status, o.created_at FROM orders o JOIN items i ON o.item_id = i.id ORDER BY o.created_at DESC";

    private static final String FIND_ORDER_BY_ID = "SELECT o.id, o.user_id, o.item_id, i.name AS item_name, i.price AS item_price, o.status, o.created_at FROM orders o JOIN items i ON o.item_id = i.id WHERE o.id = ?";

    private static final String FIND_ORDERS_BY_USER_ID = "SELECT o.id, o.user_id, o.item_id, i.name AS item_name, i.price AS item_price, o.status, o.created_at FROM orders o JOIN items i ON o.item_id = i.id WHERE o.user_id = ? ORDER BY o.created_at DESC";

    private static final String UPDATE_ORDER = "UPDATE orders SET user_id = ?, item_id = ?, status = ? WHERE id = ?";

    private static final String UPDATE_ORDER_STATUS = "UPDATE orders SET status = ? WHERE id = ?";

    private static final String DELETE_ORDER_BY_ID = "DELETE FROM orders WHERE id = ?";


    private OrderDaoImpl() {
    }

    public static OrderDaoImpl getInstance() {
        return instance;
    }

    @Override
    public boolean insert(Order order) throws DaoException {
        logger.info("Creating order");
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, order.getUserId());
                statement.setLong(2, order.getItemId());
                statement.setString(3, order.getStatus().name());

                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            order.setId(resultSet.getLong(1));
                        }
                    }
                }
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            logger.error("Error creating order");
            throw new DaoException("Failed creating order", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean delete(long id) throws DaoException {
        logger.info("Deleting order by id: {}", id);

        Connection connection = null;

        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(DELETE_ORDER_BY_ID)) {
                statement.setLong(1, id);

                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            logger.error("Error deleting order by id: {}", id, e);
            throw new DaoException("Failed to delete order by id: " + id, e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<Order> findAll() throws DaoException {
        logger.info("Finding all orders");

        Connection connection = null;

        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_ORDERS);
                 ResultSet resultSet = statement.executeQuery()) {

                List<Order> orders = new ArrayList<>();

                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }

                return orders;
            }
        } catch (SQLException e) {
            logger.error("Error finding all orders", e);
            throw new DaoException("Failed to find all orders", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public Optional<Order> findById(long id) throws DaoException {
        logger.info("Finding order by id: {}", id);

        Connection connection = null;

        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(FIND_ORDER_BY_ID)) {
                statement.setLong(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(mapOrder(resultSet));
                    }

                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding order by id: {}", id, e);
            throw new DaoException("Failed to find order by id: " + id, e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean update(Order order) throws DaoException {
        logger.info("Updating order with id: {}", order.getId());

        Connection connection = null;

        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER)) {
                statement.setLong(1, order.getUserId());
                statement.setLong(2, order.getItemId());
                statement.setString(3, order.getStatus().name());
                statement.setLong(4, order.getId());

                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            logger.error("Error updating order with id: {}", order.getId(), e);
            throw new DaoException("Failed to update order with id: " + order.getId(), e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<Order> findByUserId(long userId) throws DaoException {
        logger.info("Finding orders by user id: {}", userId);

        Connection connection = null;

        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(FIND_ORDERS_BY_USER_ID)) {
                statement.setLong(1, userId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Order> orders = new ArrayList<>();

                    while (resultSet.next()) {
                        orders.add(mapOrder(resultSet));
                    }

                    return orders;
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding orders by user id: {}", userId, e);
            throw new DaoException("Failed to find orders by user id: " + userId, e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean updateStatus(long orderId, String status) throws DaoException {
        logger.info("Updating order status. orderId={}, status={}", orderId, status);

        Connection connection = null;

        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER_STATUS)) {
                statement.setString(1, status);
                statement.setLong(2, orderId);

                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            logger.error("Error updating order status. orderId={}, status={}", orderId, status, e);
            throw new DaoException("Failed to update order status", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private Order mapOrder(ResultSet resultSet) throws SQLException {
        Timestamp createdAt = resultSet.getTimestamp("created_at");

        return new Order(
                resultSet.getLong("id"),
                resultSet.getLong("user_id"),
                resultSet.getLong("item_id"),
                resultSet.getString("item_name"),
                resultSet.getBigDecimal("item_price"),
                OrderStatus.from(resultSet.getString("status")),
                createdAt.toLocalDateTime()
        );
    }
}