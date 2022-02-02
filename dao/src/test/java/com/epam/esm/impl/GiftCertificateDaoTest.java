package com.epam.esm.impl;

import com.epam.esm.dao.impl.GiftCertificateDao;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.model.tag.Tag;
import com.epam.esm.dao.sqlgenerator.SqlGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class GiftCertificateDaoTest {

    @Test
    void saveTest() {
        Tag tag1 = new Tag(1, "firstTag");
        Tag tag2 = new Tag(2, "secondTag");
        GiftCertificate giftCertificate = new GiftCertificate(null, "name", "description", 200.1, 200L,
                LocalDateTime.now(), LocalDateTime.now().plusDays(110), List.of(tag1, tag2));
        GiftCertificateDao dao = new GiftCertificateDao();
        GiftCertificate result = dao.saveEntity(giftCertificate);
        System.out.println(result);
    }

    @Test
    void findAllTest(){
        GiftCertificateDao dao = new GiftCertificateDao();
        System.out.println(dao.findAllEntities());
    }

    @Test
    void findByIdTest(){
        GiftCertificateDao dao = new GiftCertificateDao();
        System.out.println(dao.findEntityById(10));
    }

    @Test
    void updateEntityTest(){
        GiftCertificateDao dao = new GiftCertificateDao();
        Tag tag1 = new Tag(1, "firstTag");
        Tag tag3 = new Tag(3, "secondTag");
        GiftCertificate giftCertificate = new GiftCertificate(11, "unchangedname", "changeddescription", 200.1, 200L,
                LocalDateTime.now(), LocalDateTime.now().plusDays(110), List.of(tag1, tag3));
        dao.updateEntity(giftCertificate);
        System.out.println(dao.findEntityById(11));
    }

    @Test
    void testGenerator(){
        GiftCertificateDao dao = new GiftCertificateDao();
        System.out.println(dao.findGiftCertificatesWithParameters(null, null, null, SqlGenerator.SortByCode.SORT_BY_NAME, false));
    }
}