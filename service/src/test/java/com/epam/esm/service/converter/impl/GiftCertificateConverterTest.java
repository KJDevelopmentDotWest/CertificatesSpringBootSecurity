package com.epam.esm.service.converter.impl;

import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateConverterTest {

    private final GiftCertificateConverter converter = new GiftCertificateConverter();

    private final LocalDateTime nowTime = LocalDateTime.now();

    private final GiftCertificate correctGiftCertificate = new GiftCertificate(1, "name", "description", 200d, 100L,
            nowTime, nowTime, new ArrayList<>());
    private final GiftCertificateDto correctGiftCertificateDto = new GiftCertificateDto(1, "name", "description", 200d, 100L,
            nowTime, nowTime, new ArrayList<>());

    @Test
    void convertFromModel() {
        assertEquals(correctGiftCertificateDto, converter.convert(correctGiftCertificate));
    }

    @Test
    void convertFromDto() {
        assertEquals(correctGiftCertificate, converter.convert(correctGiftCertificateDto));
    }
}