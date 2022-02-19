package com.epam.esm.service.validator.api;

import com.epam.esm.service.dto.Dto;
import com.epam.esm.service.exception.ExceptionCode;
import com.epam.esm.service.exception.ExceptionMessage;
import com.epam.esm.service.exception.ServiceException;

import java.util.Objects;

/**
 * Validator interface
 * @param <T> abstract dto that converter will operate
 */
public interface Validator<T extends Dto> {

    /**
     * validates value
     * @param value value to be validated
     * @param checkId true if id must be validated, false otherwise
     * @throws ServiceException if validation fails
     */
    void validate(T value, Boolean checkId) throws ServiceException;

    /**
     * validates id not null and positive
     * @param value id to be validated
     * @throws ServiceException if value is null
     */
    default void validateIdNotNullAndPositive(Integer value) throws ServiceException {
        if (Objects.isNull(value)){
            throw new ServiceException(ExceptionCode.VALIDATION_FAILED_EXCEPTION, ExceptionMessage.ID_CANNOT_BE_NULL);
        }
        if (value < 0){
            throw new ServiceException(ExceptionCode.VALIDATION_FAILED_EXCEPTION, ExceptionMessage.ID_CANNOT_BE_NEGATIVE);
        }
    }
}
