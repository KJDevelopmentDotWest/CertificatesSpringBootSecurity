package com.epam.esm.impl;

import com.epam.esm.api.Dao;
import com.epam.esm.connectionpool.api.ConnectionPool;
import com.epam.esm.connectionpool.impl.ConnectionPoolImpl;
import com.epam.esm.model.giftcertificate.GiftCertificate;
import com.epam.esm.model.tag.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GiftCertificateDao implements Dao<GiftCertificate> {

    private static final Logger logger = LogManager.getLogger(GiftCertificateDao.class);

    private static final String SQL_SAVE_GIFT_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SAVE_GIFT_CERTIFICATE_TO_TAG_ENTRY = "INSERT INTO gift_certificate_to_tag (gift_certificate_id, tag_id) VALUES (?, ?)";

    private static final String SQL_FIND_ALL_GIFT_CERTIFICATES = "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate";
    private static final String SQL_FIND_GIFT_CERTIFICATE_BY_ID = "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate WHERE id = ?";

    private static final String SQL_FIND_TAGS_ID_BY_GIFT_CERTIFICATE_ID = "SELECT tag_id FROM gift_certificate_to_tag WHERE gift_certificate_id = ?";

    private static final String SQL_DELETE_GIFT_CERTIFICATE_BY_ID = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String SQL_DELETE_GIFT_CERTIFICATE_TO_TAG_ENTRY_BY_GIFT_CERTIFICATE_ID = "DELETE FROM gift_certificate_to_tag WHERE gift_certificate_id = ?";
    private static final String SQL_DELETE_GIFT_CERTIFICATE_TO_TAG_ENTRY_BY_GIFT_CERTIFICATE_ID_AND_TAG_ID = "DELETE FROM gift_certificate_to_tag WHERE gift_certificate_id = ? AND tag_id = ?";

    private static final String SQL_UPDATE_GIFT_CERTIFICATE_START = "UPDATE gift_certificate SET ";
    private static final String SQL_UPDATE_NAME_COLUMN = "name";
    private static final String SQL_UPDATE_DESCRIPTION_COLUMN = "description";
    private static final String SQL_UPDATE_PRICE_COLUMN = "price";
    private static final String SQL_UPDATE_DURATION_COLUMN = "duration";
    private static final String SQL_UPDATE_CREATE_DATE_COLUMN = "create_date";
    private static final String SQL_UPDATE_LAST_UPDATE_DATE_COLUMN = "last_update_date";
    private static final String EQUAL_SYMBOL = " = ";
    private static final String NOT_EQUAL_SYMBOL = " != ";
    private static final String QUESTION_SYMBOL = " ?";
    private static final String SQL_UPDATE_GIFT_CERTIFICATE_END = "WHERE id = ? AND ";


    private final ConnectionPool connectionPool = ConnectionPoolImpl.getInstance();

    @Override
    public GiftCertificate saveEntity(GiftCertificate entity) {
        Connection connection = connectionPool.takeConnection();
        try {
            connection.setAutoCommit(false);
            GiftCertificate giftCertificate = saveGiftCertificate(connection, entity);
            saveGiftCertificateToTagEntries(connection, giftCertificate);
            connection.commit();
            connection.setAutoCommit(true);
            return giftCertificate;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public Boolean updateEntity(GiftCertificate entity) {
        Connection connection = connectionPool.takeConnection();
        try {
            connection.setAutoCommit(false);
            Boolean result = updateGiftCertificate(connection, entity);
            updateGiftCertificateToTagEntries(connection, entity);
            connection.commit();
            connection.setAutoCommit(true);
            return result;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public Boolean deleteEntity(GiftCertificate entity) {
        Connection connection = connectionPool.takeConnection();
        try {
            connection.setAutoCommit(false);
            Boolean isDeleted = deleteGiftCertificateById(connection, entity.id());
            connection.commit();
            connection.setAutoCommit(true);
            return isDeleted;
        } catch (SQLException e){
            logger.error(e);
            return false;
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public List<GiftCertificate> findAllEntities() {
        Connection connection = connectionPool.takeConnection();
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        try {
            giftCertificates = findAllGiftCertificates(connection);
        } catch (SQLException e){
            logger.error(e);
        } finally {
            connectionPool.returnConnection(connection);
        }
        return giftCertificates;
    }

    @Override
    public GiftCertificate findEntityById(Integer id) {
        Connection connection = connectionPool.takeConnection();
        try {
            return findGiftCertificateById(connection, id);
        } catch (SQLException e){
            logger.error(e);
            return null;
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    //todo to be implemented
    @Override
    public Integer getRowsNumber() {
        return null;
    }

    private GiftCertificate saveGiftCertificate(Connection connection, GiftCertificate giftCertificate) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_GIFT_CERTIFICATE, new String[] {"id"});
        preparedStatement.setString(1, giftCertificate.name());
        preparedStatement.setString(2, giftCertificate.description());
        preparedStatement.setDouble(3, giftCertificate.price());
        preparedStatement.setLong(4, giftCertificate.duration());
        preparedStatement.setTimestamp(5, Timestamp.valueOf(giftCertificate.createDate()));
        preparedStatement.setTimestamp(6, Timestamp.valueOf(giftCertificate.lastUpdateDate()));
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        Integer id = resultSet.getInt(1);
        preparedStatement.close();
        resultSet.close();
        return new GiftCertificate(id, giftCertificate.name(), giftCertificate.description(), giftCertificate.price(),
                giftCertificate.duration(), giftCertificate.createDate(), giftCertificate.lastUpdateDate(), giftCertificate.tags());
    }

    private void saveGiftCertificateToTagEntries(Connection connection, GiftCertificate giftCertificate) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_GIFT_CERTIFICATE_TO_TAG_ENTRY);
        Integer giftCertificateId = giftCertificate.id();
        giftCertificate.tags().forEach(tag -> {
            try {
                preparedStatement.setInt(1, giftCertificateId);
                preparedStatement.setInt(2, tag.id());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                logger.error(e);
            }
        });
        preparedStatement.close();
    }

    private List<GiftCertificate> findAllGiftCertificates(Connection connection) throws SQLException {
        List<GiftCertificate> result = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_GIFT_CERTIFICATES);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            result.add(convertResultSetToUser(connection, resultSet));
        }
        preparedStatement.close();
        resultSet.close();
        return result;
    }

    private GiftCertificate findGiftCertificateById(Connection connection, Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_GIFT_CERTIFICATE_BY_ID);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        GiftCertificate giftCertificate;
        if (resultSet.next()){
            giftCertificate =  convertResultSetToUser(connection, resultSet);
        } else {
            giftCertificate = null;
        }
        preparedStatement.close();
        resultSet.close();
        return giftCertificate;
    }

    private Boolean deleteGiftCertificateById(Connection connection, Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_GIFT_CERTIFICATE_TO_TAG_ENTRY_BY_GIFT_CERTIFICATE_ID);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        preparedStatement = connection.prepareStatement(SQL_DELETE_GIFT_CERTIFICATE_BY_ID);
        preparedStatement.setInt(1, id);
        int giftCertificateUpdateCount = preparedStatement.executeUpdate();
        preparedStatement.close();
        return giftCertificateUpdateCount > 0;
    }

    public Boolean updateGiftCertificate(Connection connection, GiftCertificate giftCertificate) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(generateUpdateColString(SQL_UPDATE_NAME_COLUMN));
        preparedStatement.setString(1, giftCertificate.name());
        preparedStatement.setInt(2, giftCertificate.id());
        preparedStatement.setString(3, giftCertificate.name());
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement(generateUpdateColString(SQL_UPDATE_DESCRIPTION_COLUMN));
        preparedStatement.setString(1, giftCertificate.description());
        preparedStatement.setInt(2, giftCertificate.id());
        preparedStatement.setString(3, giftCertificate.description());
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement(generateUpdateColString(SQL_UPDATE_PRICE_COLUMN));
        preparedStatement.setDouble(1, giftCertificate.price());
        preparedStatement.setInt(2, giftCertificate.id());
        preparedStatement.setDouble(3, giftCertificate.price());
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement(generateUpdateColString(SQL_UPDATE_DURATION_COLUMN));
        preparedStatement.setLong(1, giftCertificate.duration());
        preparedStatement.setInt(2, giftCertificate.id());
        preparedStatement.setLong(3, giftCertificate.duration());
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement(generateUpdateColString(SQL_UPDATE_CREATE_DATE_COLUMN));
        preparedStatement.setTimestamp(1, Timestamp.valueOf(giftCertificate.createDate()));
        preparedStatement.setInt(2, giftCertificate.id());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(giftCertificate.createDate()));
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement(generateUpdateColString(SQL_UPDATE_LAST_UPDATE_DATE_COLUMN));
        preparedStatement.setTimestamp(1, Timestamp.valueOf(giftCertificate.lastUpdateDate()));
        preparedStatement.setInt(2, giftCertificate.id());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(giftCertificate.lastUpdateDate()));
        preparedStatement.executeUpdate();

        preparedStatement.close();

        return true;
    }

    private void updateGiftCertificateToTagEntries(Connection connection, GiftCertificate giftCertificate) throws SQLException {
        List<Integer> tagsIdCurrent = giftCertificate.tags().stream().map(Tag::id).toList();
        List<Integer> tagsIdPrevious = findTagsIdByGiftCertificate(connection, giftCertificate.id());

        List<Integer> tagsIdToAdd = new ArrayList<>();
        List<Integer> tagsIdToRemove = new ArrayList<>();

        tagsIdCurrent.forEach(tagId -> {
            if (!tagsIdPrevious.contains(tagId)){
                tagsIdToAdd.add(tagId);
            }
        });

        tagsIdPrevious.forEach(tagId -> {
            if (!tagsIdCurrent.contains(tagId)){
                tagsIdToRemove.add(tagId);
            }
        });

        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_GIFT_CERTIFICATE_TO_TAG_ENTRY);

        for(Integer tagId : tagsIdToAdd){
            preparedStatement.setInt(1, giftCertificate.id());
            preparedStatement.setInt(2, tagId);
            preparedStatement.executeUpdate();
        }

        preparedStatement = connection.prepareStatement(SQL_DELETE_GIFT_CERTIFICATE_TO_TAG_ENTRY_BY_GIFT_CERTIFICATE_ID_AND_TAG_ID);

        for(Integer tagId : tagsIdToRemove){
            preparedStatement.setInt(1, giftCertificate.id());
            preparedStatement.setInt(2, tagId);
            preparedStatement.executeUpdate();
        }

        preparedStatement.close();
    }

    private String generateUpdateColString(String columnName){
        StringBuilder sqlUpdateString = new StringBuilder();
        sqlUpdateString.append(SQL_UPDATE_GIFT_CERTIFICATE_START);
        sqlUpdateString.append(columnName);
        sqlUpdateString.append(EQUAL_SYMBOL);
        sqlUpdateString.append(QUESTION_SYMBOL);
        sqlUpdateString.append(SQL_UPDATE_GIFT_CERTIFICATE_END);
        sqlUpdateString.append(columnName);
        sqlUpdateString.append(NOT_EQUAL_SYMBOL);
        sqlUpdateString.append(QUESTION_SYMBOL);
        return sqlUpdateString.toString();
    }

    private GiftCertificate convertResultSetToUser(Connection connection, ResultSet giftCertificateResultSet) throws SQLException{
        TagDao tagDao = new TagDao();

        List<Integer> tagsId = findTagsIdByGiftCertificate(connection, giftCertificateResultSet.getInt(1));

        List<Tag> tags = new ArrayList<>();

        tagsId.forEach(tagId -> tags.add(tagDao.findEntityById(tagId)));

        return new GiftCertificate(giftCertificateResultSet.getInt(1),
                giftCertificateResultSet.getString(2),
                giftCertificateResultSet.getString(3),
                giftCertificateResultSet.getDouble(4),
                giftCertificateResultSet.getLong(5),
                giftCertificateResultSet.getTimestamp(6).toLocalDateTime(),
                giftCertificateResultSet.getTimestamp(7).toLocalDateTime(),
                tags);
    }

    private List<Integer> findTagsIdByGiftCertificate(Connection connection, Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_TAGS_ID_BY_GIFT_CERTIFICATE_ID);
        preparedStatement.setInt(1, id);
        ResultSet tagsIdResultSet = preparedStatement.executeQuery();
        List<Integer> tagsId = new ArrayList<>();
        while (tagsIdResultSet.next()) {
            tagsId.add(tagsIdResultSet.getInt(1));
        }
        preparedStatement.close();
        tagsIdResultSet.close();
        return tagsId;
    }
}
