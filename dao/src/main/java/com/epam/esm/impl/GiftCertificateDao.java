package com.epam.esm.impl;

import com.epam.esm.api.Dao;
import com.epam.esm.connectionpool.api.ConnectionPool;
import com.epam.esm.connectionpool.impl.ConnectionPoolImpl;
import com.epam.esm.model.giftcertificate.GiftCertificate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class GiftCertificateDao implements Dao<GiftCertificate> {

    private static final Logger logger = LogManager.getLogger(GiftCertificateDao.class);

    private static final String SQL_SAVE_GIFT_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SAVE_GIFT_CERTIFICATE_TO_TAG_ENTRY = "INSERT INTO gift_certificate_to_tag (gift_certificate_id, tag_id) VALUES (?, ?)";

    private final ConnectionPool connectionPool = ConnectionPoolImpl.getInstance();

    @Override
    public GiftCertificate save(GiftCertificate entity) {
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

    //todo to be implemented
    @Override
    public Boolean update(GiftCertificate entity) {
        return null;
    }

    //todo to be implemented
    @Override
    public Boolean delete(GiftCertificate entity) {
        return null;
    }

    //todo to be implemented
    @Override
    public List<GiftCertificate> findAll() {
        return null;
    }

    //todo to be implemented
    @Override
    public GiftCertificate findById(Integer id) {
        return null;
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
        preparedStatement.setTimestamp(6, Timestamp.valueOf(giftCertificate.lastUpdateTime()));
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        Integer id = resultSet.getInt(1);
        preparedStatement.close();
        resultSet.close();
        return new GiftCertificate(id, giftCertificate.name(), giftCertificate.description(), giftCertificate.price(),
                giftCertificate.duration(), giftCertificate.createDate(), giftCertificate.lastUpdateTime(), giftCertificate.tags());
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
}
