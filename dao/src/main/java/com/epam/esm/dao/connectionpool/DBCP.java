package com.epam.esm.dao.connectionpool;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class DBCP {
    private final BasicDataSource dataSource = new BasicDataSource();

    private static DBCP instance;

    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/certificates";
    private static final String USER = "postgres";
    private static final String PASS = "1234";
    private static final int MAX_CONNECTIONS = 8;
    private static final int PREFERRED_CONNECTIONS = 4;
    private static final int MAX_PREFERRED_STATEMENTS = 100;

    {
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASS);
        dataSource.setMinIdle(PREFERRED_CONNECTIONS);
        dataSource.setMaxIdle(MAX_CONNECTIONS);
        dataSource.setMaxOpenPreparedStatements(MAX_PREFERRED_STATEMENTS);
        dataSource.setRollbackOnReturn(true);
    }

    private DBCP(){}

    public static DBCP getInstance(){
        if (Objects.isNull(instance)){
            instance = new DBCP();
        }
        return instance;
    }

    public Connection takeConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
