package com.epam.esm.service.converter.impl;

import com.epam.esm.dao.model.tag.Tag;
import com.epam.esm.service.ServiceTestConfig;
import com.epam.esm.service.dto.tag.TagDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class, loader = AnnotationConfigContextLoader.class)
class TagConverterTest {

    @Autowired
    private TagConverter converter;

    private final Tag tag = new Tag(1, "firstTag");
    private final TagDto tagDto = new TagDto(1, "firstTag");

    @Test
    void convertEntityToDtoTest() {
        Assertions.assertEquals(tagDto, converter.convert(tag));
    }

    @Test
    void convertDtoToEntityTest() {
        Assertions.assertEquals(tag, converter.convert(tagDto));
    }
}