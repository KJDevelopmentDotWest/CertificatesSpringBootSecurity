package com.epam.esm.service.validator.impl;

import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.model.tag.Tag;
import com.epam.esm.service.ServiceTestConfig;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class, loader = AnnotationConfigContextLoader.class)
class GiftCertificateValidatorTest {

    @Autowired
    private GiftCertificateValidator validator;

    private final GiftCertificateDto giftCertificateDto = new GiftCertificateDto(1,
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

    private final GiftCertificateDto giftCertificateUpdateDtoNullId = new GiftCertificateDto(null,
            "name",
            "description",
            200.5D,
            30L,
            LocalDateTime.MIN,
            LocalDateTime.MIN,
            List.of(new TagDto(1, "first tag"), new TagDto(2, "secondTag")));

    private final GiftCertificateDto giftCertificateUpdateDtoNullFields = new GiftCertificateDto(1,
            "name",
            "description",
            200.5D,
            null,
            LocalDateTime.MIN,
           null,
            List.of(new TagDto(1, "first tag"), new TagDto(2, "secondTag")));


    @Test
    public void validatePositiveTest() {
        Assertions.assertDoesNotThrow(() -> validator.validate(giftCertificateDto, true));
    }

    @Test
    public void validateNegativeTest() {
        Assertions.assertThrows(ServiceException.class, () -> validator.validate(giftCertificateDtoInvalid, true));
    }

    @Test
    public void validateForUpdatePositiveTest() {
        Assertions.assertDoesNotThrow(() -> validator.validate(giftCertificateUpdateDtoNullFields, true, true));
    }

    @Test
    public void validateForUpdateNegativeTest() {
        Assertions.assertThrows(ServiceException.class,() -> validator.validate(giftCertificateUpdateDtoNullId, true, true));
    }
}