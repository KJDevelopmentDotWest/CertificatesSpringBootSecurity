package com.epam.esm.impl;

import com.epam.esm.dao.impl.GiftCertificateDao;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.model.tag.Tag;
import com.epam.esm.dao.sqlgenerator.SqlGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
class GiftCertificateDaoTest {

    @Autowired
    GiftCertificateDao  dao = new GiftCertificateDao();

    @Test
    void saveTest() {
        Tag tag1 = new Tag(1, "firstTag");
        Tag tag2 = new Tag(null, "supeasfr new tag");
        GiftCertificate giftCertificate = new GiftCertificate(null, "name", "description", 200.1, 200L,
                LocalDateTime.now(), LocalDateTime.now().plusDays(110), List.of(tag1, tag2));
        GiftCertificate result = dao.saveEntity(giftCertificate);
        System.out.println(result);
    }

    @Test
    void findAllTest(){
        System.out.println(dao.findAllEntities());
    }

    @Test
    void findByIdTest(){
        System.out.println(dao.findEntityById(14));
    }

    @Test
    void updateEntityTest(){
        Tag tag1 = new Tag(1, "firstTag");
        Tag tag3 = new Tag(6, "supeasfr new tag");
        GiftCertificate giftCertificate = new GiftCertificate(11, "name", null, null, 250L,
                null, null, List.of(tag1, tag3));
        dao.updateEntity(giftCertificate);
        System.out.println(dao.findEntityById(11));
    }

    @Test
    void testGenerator(){
        System.out.println(dao.findGiftCertificatesWithParameters(null, "am", null, SqlGenerator.SortByCode.SORT_BY_NAME, true));
    }
}