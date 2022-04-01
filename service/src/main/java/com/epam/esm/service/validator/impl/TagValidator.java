package com.epam.esm.service.validator.impl;

import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.exception.ExceptionCode;
import com.epam.esm.service.exception.ExceptionMessage;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.api.Validator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Validator implementation for TagDto
 */

@Component
public class TagValidator implements Validator<TagDto> {

    private static final Integer NAME_MIN_LENGTH = 1;
    private static final Integer NAME_MAX_LENGTH = 10;

    private static final String WHITESPACE = " ";

    @Override
    public void validate(TagDto value, Boolean checkId) throws ServiceException {
        List<ExceptionMessage> exceptionMessages = new ArrayList<>();;

        if (Objects.isNull(value)){
            throw new ServiceException(ExceptionCode.VALIDATION_FAILED_EXCEPTION, ExceptionMessage.TAG_CANNOT_BE_NULL);
        }

        if (checkId){
            validateIdNotNullAndPositive(value.getId());
        }
        validateName(value.getName(), exceptionMessages);

        if (!exceptionMessages.isEmpty()){
            throw new ServiceException(ExceptionCode.VALIDATION_FAILED_EXCEPTION, exceptionMessages);
        }
    }

    private void validateName(String name, List<ExceptionMessage> exceptionMessages) {
        if (Objects.isNull(name)){
            exceptionMessages.add(ExceptionMessage.TAG_NAME_CANNOT_BE_NULL);
            return;
        }
        if (name.length() < NAME_MIN_LENGTH) {
            exceptionMessages.add(ExceptionMessage.TAG_NAME_TOO_SHORT);
        }
        if (name.length() > NAME_MAX_LENGTH) {
            exceptionMessages.add(ExceptionMessage.TAG_NAME_TOO_LONG);
        }
        if (name.startsWith(WHITESPACE)){
            exceptionMessages.add(ExceptionMessage.TAG_NAME_STARTS_WITH_WHITESPACE);
        }
        if (name.endsWith(WHITESPACE)){
            exceptionMessages.add(ExceptionMessage.TAG_NAME_ENDS_WITH_WHITESPACE);
        }
    }
}
