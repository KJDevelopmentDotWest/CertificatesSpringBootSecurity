package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.connectionpool.DBCP;
import com.epam.esm.dao.model.tag.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagDao implements Dao<Tag> {

    private static final Logger logger = LogManager.getLogger(TagDao.class);

    private static final String SQL_SAVE_TAG = "INSERT INTO tag (name) VALUES (?)";

    private static final String SQL_UPDATE_TAG_BY_ID = "UPDATE tag SET name = ? WHERE id = ? AND name != ?";

    private static final String SQL_DELETE_TAG_BY_ID = "DELETE FROM tag WHERE id = ?";

    private static final String SQL_FIND_ALL_TAGS = "SELECT id, name FROM tag";
    private static final String SQL_FIND_TAG_BY_ID = "SELECT id, name FROM tag WHERE id = ?";
    private static final String SQL_FIND_TAG_BY_NAME = "SELECT id, name FROM tag WHERE name = ?";

    private final DBCP connectionPool = DBCP.getInstance();

    @Override
    public Tag saveEntity(Tag entity) {
        try (Connection connection = connectionPool.takeConnection()){
            return saveTag(connection, entity);
        } catch (SQLException e){
            logger.error(e);
            return null;
        }
    }

    @Override
    public Boolean updateEntity(Tag entity) {
        try (Connection connection = connectionPool.takeConnection()){
            return updateTag(connection, entity);
        } catch (SQLException e){
            logger.error(e);
            return false;
        }
    }

    @Override
    public Boolean deleteEntity(Tag entity) {
        try (Connection connection = connectionPool.takeConnection()){
            return deleteTag(connection, entity.id());
        } catch (SQLException e){
            logger.error(e);
            return false;
        }
    }

    @Override
    public List<Tag> findAllEntities() {
        try (Connection connection = connectionPool.takeConnection()){
            return findAllTags(connection);
        } catch (SQLException e){
            logger.error(e);
            return new ArrayList<>();
        }
    }

    @Override
    public Tag findEntityById(Integer id) {
        try (Connection connection = connectionPool.takeConnection()){
            return findTagById(connection, id);
        } catch (SQLException e){
            logger.error(e);
            return null;
        }
    }

    /**
     *
     * @param name name to be searched
     * @return tag with provided name, null otherwise
     */
    public Tag findTagByName(String name){
        try (Connection connection = connectionPool.takeConnection()){
            return findTagByName(connection, name);
        } catch (SQLException e){
            logger.error(e);
            return null;
        }
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

    private Boolean updateTag(Connection connection, Tag tag) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_TAG_BY_ID);
        preparedStatement.setString(1, tag.name());
        preparedStatement.setInt(2, tag.id());
        preparedStatement.setString(3, tag.name());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        return true;
    }

    private Boolean deleteTag(Connection connection, Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_TAG_BY_ID);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        return true;
    }

    private List<Tag> findAllTags(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_TAGS);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Tag> tags = new ArrayList<>();
        while (resultSet.next()){
            tags.add(convertResultSetToTag(resultSet));
        }
        preparedStatement.close();
        resultSet.close();
        return tags;
    }

    private Tag findTagById(Connection connection, Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_TAG_BY_ID);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Tag tag;
        if (resultSet.next()){
            tag = convertResultSetToTag(resultSet);
        } else {
            tag = null;
        }
        preparedStatement.close();
        resultSet.close();
        return tag;
    }

    private Tag findTagByName(Connection connection, String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_TAG_BY_NAME);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        Tag tag;
        if (resultSet.next()){
            tag = convertResultSetToTag(resultSet);
        } else {
            tag = null;
        }
        preparedStatement.close();
        resultSet.close();
        return tag;
    }

    private Tag convertResultSetToTag(ResultSet resultSet) throws SQLException {
        return new Tag(resultSet.getInt(1), resultSet.getString(2));
    }
}
