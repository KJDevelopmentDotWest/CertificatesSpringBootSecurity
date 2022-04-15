package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.GiftCertificateDao;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.service.converter.impl.GiftCertificateConverter;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateServiceTest {

    private final GiftCertificateService service = new GiftCertificateService();

    private final LocalDateTime nowTime = LocalDateTime.now();

    private final GiftCertificateDao giftCertificateDao = Mockito.mock(GiftCertificateDao.class);
    private final GiftCertificateConverter giftCertificateConverter = Mockito.mock(GiftCertificateConverter.class);
    private final GiftCertificateValidator giftCertificateValidator = Mockito.mock(GiftCertificateValidator.class);

    private final GiftCertificate correctGiftCertificate = new GiftCertificate(1, "name", "description", 200d, 100L,
            nowTime, nowTime, new ArrayList<>());
    private final GiftCertificateDto correctGiftCertificateDto = new GiftCertificateDto(1, "name", null, 200d, 100L,
            nowTime, nowTime, new ArrayList<>());

    @BeforeAll
    void setup() {
        Mockito.when(giftCertificateDao.saveEntity(Mockito.any())).thenReturn(correctGiftCertificate);
        Mockito.when(giftCertificateDao.updateEntity(Mockito.any())).thenReturn(correctGiftCertificate);
        Mockito.when(giftCertificateDao.findAllEntities()).thenReturn(new ArrayList<>());
        Mockito.when(giftCertificateDao.findAllEntities(Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>());
        Mockito.when(giftCertificateDao.findEntityById(Mockito.anyInt())).thenReturn(correctGiftCertificate);
        Mockito.when(giftCertificateDao.deleteEntity(Mockito.anyInt())).thenReturn(true);
        Mockito.when(giftCertificateDao.findGiftCertificatesWithParameters(Mockito.any(), Mockito.any(),
                        Mockito.any(), Mockito.any(),
                        Mockito.any(), Mockito.any(),
                        Mockito.any()))
                .thenReturn(new ArrayList<>());
        Mockito.when(giftCertificateConverter.convert(correctGiftCertificate)).thenReturn(correctGiftCertificateDto);
        Mockito.when(giftCertificateConverter.convert(correctGiftCertificateDto)).thenReturn(correctGiftCertificate);

        ReflectionTestUtils.setField(service, "dao",  giftCertificateDao, GiftCertificateDao.class);
        ReflectionTestUtils.setField(service, "validator",  giftCertificateValidator, GiftCertificateValidator.class);
        ReflectionTestUtils.setField(service, "converter",  giftCertificateConverter, GiftCertificateConverter.class);
    }

    @Test
    void createPositiveTest() throws ServiceException {
        assertEquals(correctGiftCertificateDto, service.create(correctGiftCertificateDto));
    }

    @Test
    void updatePositiveTest() throws ServiceException {
        assertEquals(correctGiftCertificateDto, service.update(correctGiftCertificateDto));
    }

    @Test
    void deletePositiveTest() throws ServiceException {
        assertEquals(true, service.delete(1));
    }

    @Test
    void getByIdPositiveTest() throws ServiceException {
        assertEquals(correctGiftCertificateDto, service.getById(1));
    }

    @Test
    void getAllPositiveTest() throws ServiceException {
        assertEquals(new ArrayList<>(), service.getAll());
        assertEquals(new ArrayList<>(), service.getAll("1", "1"));
    }

    @Test
    void getAllWithParametersPositiveTest() throws ServiceException {
        assertEquals(new ArrayList<>(), service
                .getAllWithParameters("tag", "part", "true",
                "1", "1"));
    }
}