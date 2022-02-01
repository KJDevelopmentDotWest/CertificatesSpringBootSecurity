package com.epam.esm.impl;

import com.epam.esm.model.tag.Tag;
import org.junit.jupiter.api.Test;

class TagDaoTest {

    @Test
    void saveEntityTest() {
        Tag tag = new Tag(null, "thirdTag");
        TagDao dao = new TagDao();
        Tag result = dao.saveEntity(tag);
        System.out.println(result);
    }

    @Test
    void findAllEntitiesTest(){
        TagDao dao = new TagDao();
        System.out.println(dao.findAllEntities());
    }

    @Test
    void findEntityByIdTest(){
        TagDao dao = new TagDao();
        System.out.println(dao.findEntityById(1));
    }

    @Test
    void updateEntityTest(){
        Tag tag = new Tag(1, "firstTagEdited");
        TagDao dao = new TagDao();
        dao.updateEntity(tag);
        System.out.println(dao.findEntityById(1));
    }

}