package com.epam.ems.service.validator.impl;

import com.epam.ems.service.dto.tag.TagDto;
import com.epam.ems.service.expecption.ServiceException;
import com.epam.ems.service.validator.api.Validator;

import java.util.Objects;

public class TagValidator implements Validator<TagDto> {

    private static final Integer nameMinLength = 1;
    private static final Integer nameMaxLength = 10;

    @Override
    public void validate(TagDto value, Boolean checkId) throws ServiceException {

        if (Objects.isNull(value)){
            throw new ServiceException("tag cannot be null");
        }

        if (checkId){
            validateId(value.getId());
        }
        validateName(value.getName());
    }

    private void validateId(Integer id) throws ServiceException{
        if (Objects.isNull(id)){
            throw new ServiceException("id cannot be null");
        }
        if (id < 0){
            throw new ServiceException("id cannot be negative");
        }
    }

    private void validateName(String name) throws ServiceException{
        if (Objects.isNull(name)){
            throw new ServiceException("name cannot be null");
        }
        if (name.length() < nameMinLength) {
            throw new ServiceException("name length cannot be less than " + nameMinLength);
        }
        if (name.length() > nameMaxLength) {
            throw new ServiceException("name length cannot be more than " + nameMaxLength);
        }
    }
}
