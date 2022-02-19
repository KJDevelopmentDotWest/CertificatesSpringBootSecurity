package com.epam.esm.service.validator.impl;

import com.epam.esm.service.ServiceTestConfig;
import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class, loader = AnnotationConfigContextLoader.class)
class TagValidatorTest {

    @Autowired
    private TagValidator validator;

    private final TagDto tagDto = new TagDto(1, "firstTag");
    private final TagDto tagDtoNUllId = new TagDto(null, "firstTag");
    private final TagDto tagDtoNegativeId = new TagDto(-1, " firstTag ");
    private final TagDto tagDtoShortName = new TagDto(1, "");
    private final TagDto tagDtoLongName = new TagDto(1, "aaaaaaaaaaaaaaaaaaaaaaaaa");
    private final TagDto tagDtoInvalid = new TagDto(1, null);

    @Test
    public void validatePositiveTest() {
        Assertions.assertDoesNotThrow(() -> validator.validate(tagDto, true));
    }

    @Test
    public void validateNegativeTest() {
        Assertions.assertThrows(ServiceException.class, () -> validator.validate(tagDtoInvalid, true));
        Assertions.assertThrows(ServiceException.class, () -> validator.validate(null, true));
        Assertions.assertThrows(ServiceException.class, () -> validator.validate(tagDtoNUllId, true));
        Assertions.assertThrows(ServiceException.class, () -> validator.validate(tagDtoNegativeId, true));
        Assertions.assertThrows(ServiceException.class, () -> validator.validate(tagDtoShortName, true));
        Assertions.assertThrows(ServiceException.class, () -> validator.validate(tagDtoLongName, true));
    }
}