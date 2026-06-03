package com.example.innowisefourthproject.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ConnectionPool {
    private static final int CONNECTIONS_AMOUNT = 1;
    private static ConnectionPool instance;
    private BlockingDeque<Connection> freeConnection = new LinkedBlockingDeque<>(CONNECTIONS_AMOUNT);
    private BlockingDeque<Connection> usedConnection = new LinkedBlockingDeque<>(CONNECTIONS_AMOUNT);

    static {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static ConnectionPool getInstance() {
        instance = new ConnectionPool();
        return instance;
    }

    private ConnectionPool() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        Properties prop = new Properties();
        prop.put("user", "postgres");
        prop.put("password", "qwerty");

        for (int i = 0; i < CONNECTIONS_AMOUNT; i++) {
            try {
                Connection connection = DriverManager.getConnection(url, prop);
                freeConnection.add(connection);
            } catch (SQLException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = freeConnection.take();
            usedConnection.put(connection);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void realeaseConnection(Connection connection) {
        try {
            usedConnection.remove(connection);
            freeConnection.put(connection);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroyPool() {
        for (int i = 0; i < CONNECTIONS_AMOUNT; i++) {
            try {
                freeConnection.take().close();
            } catch (SQLException | InterruptedException e) {
                // log e.printstack
            }
        }
    }
}