package com.epam.esm.service.converter.impl;

import com.epam.esm.dao.model.tag.Tag;
import com.epam.esm.service.dto.tag.TagDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagConverterTest {

    private final TagConverter converter = new TagConverter();

    private final Tag correctTag = new Tag(1, "tag");
    private final TagDto correctTagDto = new TagDto(1, "tag");

    @Test
    void convertFromModel() {
        assertEquals(correctTagDto, converter.convert(correctTag));
    }

    @Test
    void convertFromDto() {
        assertEquals(correctTag, converter.convert(correctTagDto));
    }
}