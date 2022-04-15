package com.epam.esm.service.validator.impl;

import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateValidatorTest {

    private final GiftCertificateValidator validator = new GiftCertificateValidator();

    private final LocalDateTime nowTime = LocalDateTime.now();
    private final GiftCertificateDto correctGiftCertificateDto = new GiftCertificateDto(1, "name", "description", 200d, 100L,
            nowTime, nowTime, new ArrayList<>());
    private final GiftCertificateDto incorrectGiftCertificateDto = new GiftCertificateDto(-1, "n", "description ", -200d, -100L,
            nowTime.plusDays(10L), nowTime.plusDays(5L), new ArrayList<>());

    @Test
    void validate() {
        assertDoesNotThrow(() -> validator.validate(correctGiftCertificateDto, true));
        assertDoesNotThrow(() -> validator.validate(correctGiftCertificateDto, true, false));
        assertThrows(ServiceException.class, () -> validator.validate(incorrectGiftCertificateDto, true));
        assertThrows(ServiceException.class, () -> validator.validate(incorrectGiftCertificateDto, true, false));
    }
}