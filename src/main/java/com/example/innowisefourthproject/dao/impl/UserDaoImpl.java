package com.example.innowisefourthproject.dao.impl;

import com.example.innowisefourthproject.dao.BaseDao;
import com.example.innowisefourthproject.dao.UserDao;
import com.example.innowisefourthproject.entity.User;
import com.example.innowisefourthproject.exception.DaoException;
import com.example.innowisefourthproject.pool.ConnectionPool;

import java.sql.*;
import java.util.List;

public class UserDaoImpl extends BaseDao<User> implements UserDao {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String users = "postgres";
    private final String dbPassword = "qwerty";

    private static UserDaoImpl instance = new UserDaoImpl();

    public static UserDaoImpl getInstance() {
        return instance;
    }

    private UserDaoImpl() {
    }

    @Override
    public boolean authenticate(String login, String password) throws DaoException {
        boolean match = false;

        String sql = """
                SELECT password
                FROM users
                WHERE login = ?
                """;

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = ConnectionPool.getInstance().getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, login);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String passFromDb = resultSet.getString("password");
                        match = password.equals(passFromDb);
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new DaoException();
        }

        return match;
    }

    @Override
    public boolean insert(User user) {
        return false;
    }

    @Override
    public boolean delete(User user) {
        throw new UnsupportedOperationException("delete");
    }

    @Override
    public List<User> findAll(User user) {
        return List.of();
    }

    @Override
    public User update(User user) {
        return null;
    }
}