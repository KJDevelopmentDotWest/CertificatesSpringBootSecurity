package com.epam.ems.service.converter.impl;

import com.epam.ems.service.converter.api.Converter;
import com.epam.ems.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.ems.service.dto.tag.TagDto;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.model.tag.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<TagDto> tagDTOs = value.tags().stream().map(tagConverter::convert).collect(Collectors.toCollection(ArrayList::new));
        return new GiftCertificateDto(value.id(),
                value.name(),
                value.description(),
                value.price(),
                value.duration(),
                value.createDate(),
                value.lastUpdateDate(),
                tagDTOs);
    }
}
