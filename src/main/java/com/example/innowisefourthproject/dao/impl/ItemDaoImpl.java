package com.example.innowisefourthproject.dao.impl;

import com.example.innowisefourthproject.dao.ItemDao;
import com.example.innowisefourthproject.entity.Item;
import com.example.innowisefourthproject.exception.DaoException;
import com.example.innowisefourthproject.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDaoImpl implements ItemDao {
    private static final ItemDaoImpl instance = new ItemDaoImpl();
    private static final Logger logger = LogManager.getLogger(ItemDaoImpl.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String INSERT_ITEM = "INSERT INTO items(name, description, price) VALUES (?, ?, ?)";
    private static final String DELETE_ITEM_BY_ID = "DELETE FROM items WHERE id = ?";
    private static final String FIND_ALL_ITEMS = "SELECT id, name, description, price FROM items ORDER BY id";
    private static final String FIND_ITEM_BY_ID = "SELECT id, name, description, price FROM items WHERE id = ?";
    private static final String UPDATE_ITEM = "UPDATE items SET name = ?, description = ?, price = ? WHERE id = ?";

    private ItemDaoImpl() {
    }

    public static ItemDaoImpl getInstance() {
        return instance;
    }

    @Override
    public boolean insert(Item entity) throws DaoException {
        logger.info("Adding item with name: {}", entity.getName());
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(INSERT_ITEM, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, entity.getName());
                statement.setString(2, entity.getDescription());
                statement.setBigDecimal(3, entity.getPrice());
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            entity.setId((generatedKeys.getLong(1)));
                        }
                    }
                }
                logger.info("Item insert result: {}", affectedRows > 0);
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            logger.error("Error adding item with name: {}", entity.getName(), e);
            throw new DaoException("Failed to add  item", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean delete(long id) throws DaoException {
        logger.info("Deleting item with id {}", id);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(DELETE_ITEM_BY_ID)) {
                statement.setLong(1, id);
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            logger.error("Error deleting item by id: {}", id, e);
            throw new DaoException("Failed to delete item by id: " + id, e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }


    @Override
    public List<Item> findAll() throws DaoException {
        logger.info("Find all item from db");
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_ITEMS)) {
                ResultSet resultSet = statement.executeQuery();
                List<Item> items = new ArrayList<>();
                while (resultSet.next()) {
                    items.add(mapItem(resultSet));
                }
                logger.info("Foud {} items", items.size());
                return items;
            }
        } catch (SQLException e) {
            logger.error("Error finding items", e);
            throw new DaoException("Failed to find all items", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }

    }

    @Override
    public Optional<Item> findById(long id) throws DaoException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(FIND_ITEM_BY_ID)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(mapItem(resultSet));
                    }
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Errro to find item by id ", e);
            throw new DaoException("Failed to find item by id", e);
        }
    }

    @Override
    public boolean update(Item entity) throws DaoException {
        logger.info("Updating item with id: {}", entity.getId());

        Connection connection = null;

        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_ITEM)) {
                statement.setString(1, entity.getName());
                statement.setString(2, entity.getDescription());
                statement.setBigDecimal(3, entity.getPrice());
                statement.setLong(4, entity.getId());

                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            logger.error("Error updating item with id: {}", entity.getId(), e);
            throw new DaoException("Failed to update item: " + entity.getName(), e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }


    private Item mapItem(ResultSet resultSet) throws SQLException {
        return new Item(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getBigDecimal("price")
        );
    }
}
