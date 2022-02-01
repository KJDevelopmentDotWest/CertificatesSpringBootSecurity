package com.epam.esm.connectionpool.impl;

import com.epam.esm.connectionpool.api.ConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class represents connection pool
 */
public class ConnectionPoolImpl implements ConnectionPool {

    private static final Logger logger = LogManager.getLogger(ConnectionPoolImpl.class);

    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/certificates";
    private static final String USER = "postgres";
    private static final String PASS = "1234";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final int MAX_CONNECTIONS = 8;
    private static final int PREFERRED_CONNECTIONS = 4;

    private boolean initialized = false;

    private static final ReentrantLock getInstanceLock = new ReentrantLock();

    private final BlockingDeque<ProxyConnection> availableConnections = new LinkedBlockingDeque<>();
    private final BlockingDeque<ProxyConnection> givenAwayConnections = new LinkedBlockingDeque<>();

    private static ConnectionPoolImpl instance;

    private ConnectionPoolImpl(){}

    /**
     *
     * @return instance of connection pool
     */
    public static ConnectionPool getInstance(){
        getInstanceLock.lock();
        if(Objects.isNull(instance)){
            instance = new ConnectionPoolImpl();
            instance.initialize();
        }
        getInstanceLock.unlock();
        return instance;
    }

    /**
     *
     * @return taken connection
     */
    @Override
    public synchronized Connection takeConnection() {

        if (!availableConnections.isEmpty()){
            ProxyConnection connection = availableConnections.poll();
            givenAwayConnections.add(connection);
            logger.info("Connection taken");
            return connection;
        } else if (givenAwayConnections.size() < MAX_CONNECTIONS){
            try {
                createConnectionAndAddToPool();
                ProxyConnection connection = availableConnections.poll();
                givenAwayConnections.add(connection);
                logger.info("Connection taken");
                return connection;
            } catch (SQLException e) {
                logger.error(e);
            }
        }

        while (availableConnections.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error(e);
            }
        }
        ProxyConnection connection = availableConnections.poll();
        givenAwayConnections.add(connection);
        logger.info("Connection taken");
        return connection;
    }

    /**
     *
     * @param connection connection to be returned
     */
    @Override
    public synchronized void returnConnection(Connection connection) {
        if (givenAwayConnections.remove((ProxyConnection) connection)){
            logger.info("Connection returned");
            try {
                if (!connection.getAutoCommit()){
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                logger.error(e);
                closeConnection((ProxyConnection) connection);
            }

            if (availableConnections.size() + givenAwayConnections.size() < PREFERRED_CONNECTIONS
                    || (availableConnections.isEmpty() && givenAwayConnections.size() < MAX_CONNECTIONS)){
                availableConnections.add((ProxyConnection) connection);
            } else {
                closeConnection((ProxyConnection) connection);
            }
        }
        notify();
    }

    /**
     * method shutdowns connection pool
     */
    @Override
    public void shutdown() {
        closeConnection(availableConnections);
        closeConnection(givenAwayConnections);
        availableConnections.clear();
        givenAwayConnections.clear();
        initialized = false;
    }

    /**
     * method initializes connection pool
     */
    @Override
    public void initialize() {
        if (!initialized){
            try {
                Class.forName(DRIVER);
            } catch (ClassNotFoundException e) {
                logger.error(e);
            }
            try{
                for (int i = 0; i < PREFERRED_CONNECTIONS; i++) {
                    createConnectionAndAddToPool();
                }
                initialized = true;
                logger.info("ConnectionPool initialized");
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    private void createConnectionAndAddToPool() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        ProxyConnection proxyConnection = new ProxyConnection(connection, this);
        availableConnections.add(proxyConnection);
    }

    private void closeConnection(Deque<ProxyConnection> connections){
        connections.forEach(this::closeConnection);
    }

    private void closeConnection(ProxyConnection connection){
        logger.info("Connection closed");
        try {
            connection.realClose();
        } catch (SQLException e) {
            logger.error(e);
        }
    }
}
