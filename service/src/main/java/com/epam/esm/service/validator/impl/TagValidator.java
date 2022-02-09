package com.epam.esm.service.validator.impl;

import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.expecption.ServiceException;
import com.epam.esm.service.validator.api.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TagValidator implements Validator<TagDto> {

    private static final Integer NAME_MIN_LENGTH = 1;
    private static final Integer NAME_MAX_LENGTH = 10;

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
        if (name.length() < NAME_MIN_LENGTH) {
            throw new ServiceException("name length cannot be less than " + NAME_MIN_LENGTH);
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new ServiceException("name length cannot be more than " + NAME_MAX_LENGTH);
        }
    }
}
