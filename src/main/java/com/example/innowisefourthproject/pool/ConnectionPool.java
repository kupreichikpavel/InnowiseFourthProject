package com.example.innowisefourthproject.pool;

import com.example.innowisefourthproject.exception.ConnectionPoolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

public final class ConnectionPool {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class);

    private static final String DB_PROPERTIES = "db.properties";
    private static final int DEFAULT_POOL_SIZE = 5;

    private final LinkedBlockingQueue<Connection> freeConnections;
    private final LinkedBlockingQueue<Connection> usedConnections;

    private ConnectionPool() {
        Properties properties = loadProperties();

        int poolSize = Integer.parseInt(
                properties.getProperty("db.poolSize", String.valueOf(DEFAULT_POOL_SIZE))
        );

        freeConnections = new LinkedBlockingQueue<>(poolSize);
        usedConnections = new LinkedBlockingQueue<>(poolSize);

        try {
            Class.forName(properties.getProperty("db.driver"));

            for (int i = 0; i < poolSize; i++) {
                Connection connection = DriverManager.getConnection(
                        properties.getProperty("db.url"),
                        properties.getProperty("db.user"),
                        properties.getProperty("db.password")
                );

                freeConnections.add(connection);

                logger.info("Connection number {} was created", i + 1);
            }

            logger.info("Connection pool was initialized. Pool size: {}", poolSize);
        } catch (ClassNotFoundException | SQLException e) {
            logger.fatal("Connection pool initialization failed", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    private static class Holder {
        private static final ConnectionPool INSTANCE = new ConnectionPool();
    }

    public static ConnectionPool getInstance() {
        return Holder.INSTANCE;
    }

    public Connection getConnection() {
        try {
            Connection connection = freeConnections.take();
            usedConnections.add(connection);
            return connection;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ConnectionPoolException("Thread was interrupted while getting connection", e);
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }

        try {
            if (usedConnections.remove(connection)) {
                freeConnections.put(connection);
            } else {
                logger.warn("Trying to release connection that is not used by this pool");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ConnectionPoolException("Thread was interrupted while releasing connection", e);
        }
    }

    public void destroyPool() {
        closeConnections(freeConnections);
        closeConnections(usedConnections);

        freeConnections.clear();
        usedConnections.clear();

        logger.info("Connection pool was destroyed");
    }

    private void closeConnections(Iterable<Connection> connections) {
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Could not close connection", e);
            }
        }
    }

    private Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = ConnectionPool.class
                .getClassLoader()
                .getResourceAsStream(DB_PROPERTIES)) {

            if (inputStream == null) {
                throw new IllegalStateException("File db.properties was not found");
            }

            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Could not load db.properties", e);
        }
    }
}