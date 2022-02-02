package com.epam.esm.dao.connectionpool.api;

import java.sql.Connection;

/**
 * interface for connection pools
 */
public interface ConnectionPool {

    /**
     *
     * @return taken connection
     */
    Connection takeConnection();

    /**
     *
     * @param connection connection to be returned
     */
    void returnConnection(Connection connection);

    /**
     * method shutdowns connection pool
     */
    void shutdown();

    /**
     * method initializes connection pool
     */
    void initialize();

}