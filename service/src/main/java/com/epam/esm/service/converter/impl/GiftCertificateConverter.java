package com.epam.esm.service.converter.impl;

import com.epam.esm.service.converter.api.Converter;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.model.tag.Tag;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of converter interface fot gift certificates
 */

@Component
public class GiftCertificateConverter implements Converter<GiftCertificate, GiftCertificateDto> {
    @Override
    public GiftCertificate convert(GiftCertificateDto value) {
        Converter<Tag, TagDto> tagConverter = new TagConverter();
        List<Tag> tags = value.getTags().stream().map(tagConverter::convert).toList();
        return new GiftCertificate(value.getId(),
                value.getName(),
                value.getDescription(),
                value.getPrice(),
                value.getDuration(),
                value.getCreateDate(),
                value.getLastUpdateDate(),
                tags);
    }

    @Override
    public GiftCertificateDto convert(GiftCertificate value) {
        Converter<Tag, TagDto> tagConverter = new TagConverter();
        List<TagDto> tagDTOs = value.getTags().stream().map(tagConverter::convert).collect(Collectors.toCollection(ArrayList::new));
        return new GiftCertificateDto(value.getId(),
                value.getName(),
                value.getDescription(),
                value.getPrice(),
                value.getDuration(),
                value.getCreateDate(),
                value.getLastUpdateDate(),
                tagDTOs);
    }
}
