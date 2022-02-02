package com.epam.ems.service.validator.api;

import com.epam.ems.service.dto.AbstractDto;
import com.epam.ems.service.expecption.ServiceException;

import java.util.Objects;

/**
 *
 * @param <T> abstract dto that converter will operate
 */
public interface Validator<T extends AbstractDto> {

    /**
     *
     * @param value value to be validated
     * @param checkId true if id must be validated, false otherwise
     * @throws ServiceException if validation fails
     */
    void validate(T value, Boolean checkId) throws ServiceException;

    /**
     *
     * @param value id to be validated
     * @throws ServiceException if value is null
     */
    default void validateIdNotNull(Integer value) throws ServiceException {
        if (Objects.isNull(value)){
            throw new ServiceException("id is wrong");
        }
    }
}
