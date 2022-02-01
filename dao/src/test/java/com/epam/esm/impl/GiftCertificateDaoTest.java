package com.epam.esm.impl;

import com.epam.esm.model.giftcertificate.GiftCertificate;
import com.epam.esm.model.tag.Tag;
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
}