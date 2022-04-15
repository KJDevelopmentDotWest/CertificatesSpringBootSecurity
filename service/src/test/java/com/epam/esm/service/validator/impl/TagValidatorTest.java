package com.epam.esm.service.validator.impl;

import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagValidatorTest {

    private final TagValidator validator = new TagValidator();

    private final TagDto correctTagDto = new TagDto(1, "tag");
    private final TagDto incorrectTagDto = new TagDto(-1, " ");

    @Test
    void validate() {
        assertDoesNotThrow(() -> validator.validate(correctTagDto, true));
        assertThrows(ServiceException.class, () -> validator.validate(incorrectTagDto, true));
    }
}