package com.epam.esm.dao.mapper;

import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    public GiftCertificateMapper(){}

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new GiftCertificate(rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getDouble(4),
                rs.getLong(5),
                rs.getTimestamp(6).toLocalDateTime(),
                rs.getTimestamp(7).toLocalDateTime(),
                null);
    }
}
