package com.epam.esm.service.validator.impl;

import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.expecption.ExceptionCode;
import com.epam.esm.service.expecption.ServiceException;
import com.epam.esm.service.validator.api.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TagValidator implements Validator<TagDto> {

    private static final Integer NAME_MIN_LENGTH = 1;
    private static final Integer NAME_MAX_LENGTH = 10;

    private static final String WHITESPACE = " ";

    @Override
    public void validate(TagDto value, Boolean checkId) throws ServiceException {

        if (Objects.isNull(value)){
            throw new ServiceException(ExceptionCode.TAG_CANNOT_BE_NULL);
        }

        if (checkId){
            validateId(value.getId());
        }
        validateName(value.getName());
    }

    private void validateId(Integer id) throws ServiceException{
        if (Objects.isNull(id)){
            throw new ServiceException(ExceptionCode.TAG_ID_CANNOT_BE_NULL);
        }
        if (id < 0){
            throw new ServiceException(ExceptionCode.TAG_ID_CANNOT_BE_NEGATIVE);
        }
    }

    private void validateName(String name) throws ServiceException{
        if (Objects.isNull(name)){
            throw new ServiceException(ExceptionCode.TAG_NAME_CANNOT_BE_NULL);
        }
        if (name.length() < NAME_MIN_LENGTH) {
            throw new ServiceException(ExceptionCode.TAG_NAME_TOO_SHORT);
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new ServiceException(ExceptionCode.TAG_NAME_TOO_LONG);
        }
        if (name.startsWith(WHITESPACE)){
            throw new ServiceException(ExceptionCode.TAG_NAME_STARTS_WITH_WHITESPACE);
        }
        if (name.endsWith(WHITESPACE)){
            throw new ServiceException(ExceptionCode.TAG_NAME_ENDS_WITH_WHITESPACE);
        }
    }
}
