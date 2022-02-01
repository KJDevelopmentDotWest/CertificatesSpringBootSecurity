package com.epam.esm.impl;

import com.epam.esm.api.Dao;
import com.epam.esm.connectionpool.api.ConnectionPool;
import com.epam.esm.connectionpool.impl.ConnectionPoolImpl;
import com.epam.esm.model.tag.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TagDao implements Dao<Tag> {

    private static final Logger logger = LogManager.getLogger(TagDao.class);

    private static final String SQL_SAVE_TAG = "INSERT INTO tag (name) VALUES (?)";

    private final ConnectionPool connectionPool = ConnectionPoolImpl.getInstance();

    @Override
    public Tag saveEntity(Tag entity) {
        Connection connection = connectionPool.takeConnection();
        try {
            return saveTag(connection, entity);
        } catch (SQLException e){
            logger.error(e);
            return null;
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    //todo to be implemented
    @Override
    public Boolean updateEntity(Tag entity) {
        return null;
    }

    //todo to be implemented
    @Override
    public Boolean deleteEntity(Tag entity) {
        return null;
    }

    //todo to be implemented
    @Override
    public List<Tag> findAllEntities() {
        return null;
    }

    //todo to be implemented
    @Override
    public Tag findEntityById(Integer id) {
        return null;
    }

    //todo to be implemented
    @Override
    public Integer getRowsNumber() {
        return null;
    }

    private Tag saveTag(Connection connection, Tag tag) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_TAG, new String[]{"id"});
        preparedStatement.setString(1, tag.name());
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        Integer id = resultSet.getInt(1);
        preparedStatement.close();
        resultSet.close();
        return new Tag(id, tag.name());
    }
}
