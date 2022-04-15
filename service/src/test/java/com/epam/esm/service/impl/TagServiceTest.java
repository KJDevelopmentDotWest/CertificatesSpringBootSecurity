package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.dao.model.tag.Tag;
import com.epam.esm.service.converter.impl.TagConverter;
import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.impl.TagValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagServiceTest {

    private final TagService service = new TagService();

    private final TagDao dao = Mockito.mock(TagDao.class);
    private final TagConverter converter = Mockito.mock(TagConverter.class);
    private final TagValidator validator = Mockito.mock(TagValidator.class);

    private final Tag correctTag = new Tag(1, "tag");
    private final TagDto correctTagDto = new TagDto(1, "tag");

    @BeforeAll
    void setup() {
        Mockito.when(dao.saveEntity(Mockito.any())).thenReturn(correctTag);
        Mockito.when(dao.deleteEntity(Mockito.any())).thenReturn(true);
        Mockito.when(dao.findTagByName("name1")).thenReturn(correctTag);
        Mockito.when(dao.findTagByName("name2")).thenReturn(null);
        Mockito.when(dao.findAllEntities()).thenReturn(new ArrayList<>(List.of(correctTag)));
        Mockito.when(dao.findAllEntities(Mockito.anyInt(), Mockito.anyInt())).thenReturn(new ArrayList<>(List.of(correctTag)));
        Mockito.when(dao.findEntityById(Mockito.anyInt())).thenReturn(correctTag);
        Mockito.when(dao.findMostWidelyUsedTagForUserWithHighestCostOfAllOrders()).thenReturn(correctTag);

        Mockito.when(converter.convert(correctTag)).thenReturn(correctTagDto);
        Mockito.when(converter.convert(correctTagDto)).thenReturn(correctTag);

        ReflectionTestUtils.setField(service, "dao",  dao, TagDao.class);
        ReflectionTestUtils.setField(service, "validator",  validator, TagValidator.class);
        ReflectionTestUtils.setField(service, "converter",  converter, TagConverter.class);
    }

    @Test
    void create() throws ServiceException {
        assertEquals(correctTagDto, service.create(correctTagDto));
    }

    @Test
    void update() {
        assertThrows(ServiceException.class, () -> service.update(correctTagDto));
    }

    @Test
    void delete() throws ServiceException {
        assertTrue(service.delete(1));
    }

    @Test
    void getById() throws ServiceException {
        assertEquals(correctTagDto, service.getById(1));
    }

    @Test
    void getAll() throws ServiceException {
        assertEquals(new ArrayList<>(List.of(correctTagDto)), service.getAll());
        assertEquals(new ArrayList<>(List.of(correctTagDto)), service.getAll("1", "1"));
    }

    @Test
    void getByName() throws ServiceException {
        assertEquals(correctTagDto, service.getByName("name1"));
    }

    @Test
    void getMostWidelyUsedTagForUserWithHighestCostOfAllOrders() {
        assertEquals(correctTagDto, service.getMostWidelyUsedTagForUserWithHighestCostOfAllOrders());
    }
}