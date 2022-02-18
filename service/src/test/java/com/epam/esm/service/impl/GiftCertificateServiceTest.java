package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.GiftCertificateDao;
import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.model.tag.Tag;
import com.epam.esm.service.ServiceTestConfig;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class, loader = AnnotationConfigContextLoader.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateServiceTest {

    @Autowired
    private GiftCertificateService service;

    @Autowired
    private GiftCertificateDao giftCertificateDao;

    @Autowired
    private TagDao tagDao;

    private final GiftCertificate giftCertificate = new GiftCertificate(1,
            "name",
            "description",
            200.5D,
            30L,
            LocalDateTime.MIN,
            LocalDateTime.MIN,
            List.of(new Tag(1, "first tag"), new Tag(2, "secondTag")));

    private final GiftCertificateDto giftCertificateDto = new GiftCertificateDto(1,
            "name",
            "description",
            200.5D,
            30L,
            LocalDateTime.MIN,
            LocalDateTime.MIN,
            List.of(new TagDto(1, "first tag"), new TagDto(2, "secondTag")));

    private final GiftCertificate giftCertificate2 = new GiftCertificate(2,
            "name2",
            "description2",
            200.5D,
            30L,
            LocalDateTime.MIN,
            LocalDateTime.MIN,
            List.of(new Tag(1, "first tag")));

    private final GiftCertificateDto giftCertificateDto2 = new GiftCertificateDto(2,
            "name2",
            "description2",
            200.5D,
            30L,
            LocalDateTime.MIN,
            LocalDateTime.MIN,
            List.of(new TagDto(1, "first tag")));

    private final GiftCertificate giftCertificateNullId = new GiftCertificate(null,
            "name",
            "description",
            200.5D,
            30L,
            LocalDateTime.MIN,
            LocalDateTime.MIN,
            List.of(new Tag(1, "first tag"), new Tag(2, "secondTag")));

    private final GiftCertificateDto giftCertificateDtoNullId = new GiftCertificateDto(null,
            "name",
            "description",
            200.5D,
            30L,
            LocalDateTime.MIN,
            LocalDateTime.MIN,
            List.of(new TagDto(1, "first tag"), new TagDto(2, "secondTag")));


    private final GiftCertificateDto giftCertificateDtoInvalid = new GiftCertificateDto(null,
            "name",
            null,
            200.5D,
            30L,
            LocalDateTime.MAX,
            LocalDateTime.MAX,
            List.of(new TagDto(1, "first tag"), new TagDto(2, "secondTag")));

    private static final Integer OK_INTEGER = 1;
    private static final Integer NOT_OK_INTEGER = 2;

    private final List<GiftCertificate> giftCertificates = new ArrayList<>();
    private final List<GiftCertificateDto> giftCertificateDtos = new ArrayList<>();

    @BeforeAll
    public void setup(){
        giftCertificates.add(giftCertificate);
        giftCertificates.add(giftCertificate2);
        giftCertificateDtos.add(giftCertificateDto);
        giftCertificateDtos.add(giftCertificateDto2);
        Mockito.when(giftCertificateDao.findEntityById(OK_INTEGER)).thenReturn(giftCertificate);
        Mockito.when(giftCertificateDao.findEntityById(NOT_OK_INTEGER)).thenThrow(BadSqlGrammarException.class);
        Mockito.when(giftCertificateDao.findAllEntities()).thenReturn(giftCertificates);
        Mockito.when(giftCertificateDao.saveEntity(giftCertificateNullId)).thenReturn(giftCertificate);
        Mockito.when(giftCertificateDao.updateEntity(Mockito.any())).thenReturn(giftCertificate);
        Mockito.when(giftCertificateDao.deleteEntity(OK_INTEGER)).thenReturn(true);
        Mockito.when(giftCertificateDao.deleteEntity(NOT_OK_INTEGER)).thenThrow(BadSqlGrammarException.class);
        Mockito.when(giftCertificateDao.findGiftCertificatesWithParameters(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(giftCertificates);
    }

    @Test
    public void getByIdPositiveTest() throws ServiceException {
        Assertions.assertEquals(giftCertificateDto, service.getById(OK_INTEGER));
    }

    @Test
    public void getByIdNegativeTest() {
        Assertions.assertThrows(ServiceException.class, () -> service.getById(null));
        Assertions.assertThrows(ServiceException.class, () -> service.getById(NOT_OK_INTEGER));
    }

    @Test
    public void getAllTest() throws ServiceException {
        Assertions.assertEquals(giftCertificateDtos, service.getAll());
    }

    @Test
    public void savePositiveTest() throws ServiceException {
        Assertions.assertEquals(giftCertificateDto, service.create(giftCertificateDtoNullId));
    }

    @Test
    public void saveNegativeTest(){
        Assertions.assertThrows(ServiceException.class, () -> service.create(giftCertificateDtoInvalid));
    }

    @Test
    public void updatePositiveTest() throws ServiceException {
        Assertions.assertEquals(true, service.update(giftCertificateDto));
    }

    @Test
    public void updateNegativeTest() {
        Assertions.assertThrows(ServiceException.class, () -> service.update(giftCertificateDtoInvalid));
    }

    @Test
    public void deletePositiveTest() throws ServiceException {
        Assertions.assertEquals(true, service.delete(OK_INTEGER));
    }

    @Test
    public void deleteNegativeTest() {
        Assertions.assertThrows(ServiceException.class, () -> service.delete(NOT_OK_INTEGER));
    }

    @Test
    public void getAllWithParametersTest() throws ServiceException {
        Assertions.assertEquals(giftCertificateDtos, service.getAllWithParameters("first tag", "name", "description", true, true, true));
    }
}