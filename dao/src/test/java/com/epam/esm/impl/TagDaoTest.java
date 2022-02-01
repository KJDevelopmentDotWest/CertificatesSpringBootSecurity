package com.epam.esm.impl;

import com.epam.esm.model.giftcertificate.GiftCertificate;
import com.epam.esm.model.tag.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagDaoTest {

    @Test
    void saveTest() {
        Tag tag = new Tag(null, "secondTag");
        TagDao dao = new TagDao();
        Tag result = dao.save(tag);
        System.out.println(result);
    }

}