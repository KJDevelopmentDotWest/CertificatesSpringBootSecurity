package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.dao.model.tag.Tag;
import com.epam.esm.service.ServiceTestConfig;
import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class, loader = AnnotationConfigContextLoader.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagServiceTest {

    @Autowired
    private TagDao tagDao;

    @Autowired
    private TagService service;

    private final Tag tag = new Tag(1, "firstTag");
    private final TagDto tagDto = new TagDto(1, "firstTag");
    private final Tag tag2 = new Tag(2, "secondTag");
    private final TagDto tagDto2 = new TagDto(2, "secondTag");
    private final Tag tagNullId = new Tag(null, "firstTag");
    private final TagDto tagDtoNullId = new TagDto(null, "firstTag");
    private final TagDto tagDtoInvalid = new TagDto(1, null);

    private final List<Tag> tags = new ArrayList<>();
    private final List<TagDto> tagDtos = new ArrayList<>();

    @BeforeAll
    public void setup(){
        tags.add(tag);
        tags.add(tag2);
        tagDtos.add(tagDto);
        tagDtos.add(tagDto2);

//        Mockito.when(tagDao.findEntityById(Mockito.any())).thenReturn(tag);
//        Mockito.when(tagDao.findAllEntities()).thenReturn(tags);
//        Mockito.when(tagDao.saveEntity(tagNullId)).thenReturn(tag);
//        Mockito.when(tagDao.deleteEntity(Mockito.any())).thenReturn(true);
//        Mockito.when(tagDao.findTagByName("name")).thenReturn(tag);
    }

    @Test
    public void savePositiveTest() throws ServiceException {
        Assertions.assertEquals(tagDto, service.create(tagDtoNullId));
    }

    @Test
    public void saveNegativeTest() {
        Assertions.assertThrows(ServiceException.class, () -> service.create(tagDtoInvalid));
    }

    @Test
    public void updateTest() {
        Assertions.assertThrows(ServiceException.class, () -> service.update(tagDto));
    }

    @Test
    public void delete() throws ServiceException {
        Assertions.assertEquals(true, service.delete(1));
    }

    @Test
    public void getByIdPositiveTest() throws ServiceException {
        Assertions.assertEquals(tagDto, service.getById(1));
    }

    @Test
    public void getByIdNegativeTest() {
        Assertions.assertThrows(ServiceException.class, () -> service.getById(-1));
    }

    @Test
    public void getAllTest() throws ServiceException {
        Assertions.assertEquals(tagDtos, service.getAll());
    }

    @Test
    public void getByNamePositiveTest() throws ServiceException {
        Assertions.assertEquals(tagDto, service.getByName("name"));
    }

    @Test
    public void getByNameNegativeTest() {
        Assertions.assertThrows(ServiceException.class, () -> service.getByName(null));
    }
}