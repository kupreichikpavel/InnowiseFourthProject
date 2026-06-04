package com.example.innowisefourthproject.dao.impl;

import com.example.innowisefourthproject.dao.UserDao;
import com.example.innowisefourthproject.entity.Role;
import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.DaoException;
import com.example.innowisefourthproject.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);
    private static final UserDaoImpl INSTANCE = new UserDaoImpl();

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String INSERT_USER = "INSERT INTO users(login, password_hash, name, role) VALUES (?, ?, ?, ?)";
    private static final String FIND_USER_BY_LOGIN = "SELECT id, login, password_hash, name, role FROM users WHERE login = ?";
    private static final String EXISTS_BY_LOGIN = "SELECT EXISTS (SELECT 1 FROM users WHERE login = ?)";
    private static final String FIND_USER_BY_ID = "SELECT id, login, password_hash, name, role FROM users WHERE id = ?";
    private static final String FIND_ALL_USERS = "SELECT id, login, password_hash, name, role FROM users ORDER BY id";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_USER = "UPDATE users SET login = ?, password_hash = ?, name = ?, role = ? WHERE id = ?";

    private UserDaoImpl() {
    }

    public static UserDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean insert(User user) throws DaoException {
        logger.info("User DAO registration with login: {}", user.getLogin());

        Connection connection = null;

        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, user.getLogin());
                statement.setString(2, user.getPasswordHash());
                statement.setString(3, user.getName());
                statement.setString(4, user.getRole().name());

                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            user.setId(generatedKeys.getLong(1));
                        }
                    }
                }

                logger.info("User insert result: {}", affectedRows > 0);
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            logger.error("Error adding user with login: {}", user.getLogin(), e);
            throw new DaoException("Failed to add user: " + user.getLogin(), e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean delete(long id) throws DaoException {
        logger.info("Delete user by id: {}", id);

        Connection connection = null;

        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_ID)) {
                statement.setLong(1, id);
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            logger.error("Error deleting user by id: {}", id, e);
            throw new DaoException("Failed to delete user by id: " + id, e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<User> findAll() throws DaoException {
        logger.info("Finding all users");

        Connection connection = null;

        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_USERS);
                 ResultSet resultSet = statement.executeQuery()) {

                List<User> users = new ArrayList<>();

                while (resultSet.next()) {
                    users.add(mapUser(resultSet));
                }

                logger.info("Found {} users", users.size());
                return users;
            }
        } catch (SQLException e) {
            logger.error("Error finding all users", e);
            throw new DaoException("Failed to find all users", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public Optional<User> findById(long id) throws DaoException {
        logger.info("Find user by id: {}", id);

        Connection connection = null;

        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_ID)) {
                statement.setLong(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(mapUser(resultSet));
                    }

                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user by id: {}", id, e);
            throw new DaoException("Failed to find user by id: " + id, e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean update(User user) throws DaoException {
        logger.info("Updating user with id: {}", user.getId());

        Connection connection = null;

        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_USER)) {
                statement.setString(1, user.getLogin());
                statement.setString(2, user.getPasswordHash());
                statement.setString(3, user.getName());
                statement.setString(4, user.getRole().name());
                statement.setLong(5, user.getId());

                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            logger.error("Error updating user with id: {}", user.getId(), e);
            throw new DaoException("Failed to update user: " + user.getLogin(), e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) throws DaoException {
        logger.info("Find user by login: {}", login);

        Connection connection = null;

        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_LOGIN)) {
                statement.setString(1, login);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(mapUser(resultSet));
                    }

                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user by login: {}", login, e);
            throw new DaoException("Failed to find user by login: " + login, e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean existsByLogin(String login) throws DaoException {
        logger.info("Check user exists by login: {}", login);

        Connection connection = null;

        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(EXISTS_BY_LOGIN)) {
                statement.setString(1, login);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBoolean(1);
                    }

                    return false;
                }
            }
        } catch (SQLException e) {
            logger.error("Error determining existence of user: {}", login, e);
            throw new DaoException("Failed determining existence of user: " + login, e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("id"),
                resultSet.getString("login"),
                resultSet.getString("password_hash"),
                resultSet.getString("name"),
                Role.from(resultSet.getString("role"))
        );
    }
}