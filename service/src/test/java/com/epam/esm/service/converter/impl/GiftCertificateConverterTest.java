package com.epam.esm.service.converter.impl;

import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.model.tag.Tag;
import com.epam.esm.service.ServiceTestConfig;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.dto.tag.TagDto;
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
class GiftCertificateConverterTest {

    @Autowired
    private GiftCertificateConverter converter;

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

    @Test
    public void convertEntityToDtoTest() {
        Assertions.assertEquals(giftCertificateDto, converter.convert(giftCertificate));
    }

    @Test
    public void convertDtoToEntityTest() {
        Assertions.assertEquals(giftCertificate, converter.convert(giftCertificateDto));
    }
}