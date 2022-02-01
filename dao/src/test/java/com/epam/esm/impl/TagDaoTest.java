package com.epam.esm.impl;

import com.epam.esm.model.tag.Tag;
import org.junit.jupiter.api.Test;

class TagDaoTest {

    @Test
    void saveTest() {
        Tag tag = new Tag(null, "secondTag");
        TagDao dao = new TagDao();
        Tag result = dao.saveEntity(tag);
        System.out.println(result);
    }

}