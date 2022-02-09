package com.epam.esm.service.converter.impl;

import com.epam.esm.service.converter.api.Converter;
import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.dao.model.tag.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagConverter implements Converter<Tag, TagDto> {
    @Override
    public Tag convert(TagDto value) {
        return new Tag(value.getId(), value.getName());
    }

    @Override
    public TagDto convert(Tag value) {
        return new TagDto(value.getId(), value.getName());
    }
}
