package com.epam.esm.service.exception;

import java.util.Arrays;
import java.util.Objects;

/**
 * Enum of exception codes
 */
public enum ExceptionCode {
    UNKNOWN_EXCEPTION(0, 500),
    OPERATION_IS_NOT_SUPPORTED(1, 500),
    INTERNAL_DB_EXCEPTION(2, 500),
    VALIDATION_FAILED_EXCEPTION(3, 422),
    ENTITY_NOT_FOUND(4, 404),
    BAD_CREDENTIALS(5, 401);

    private final Integer exceptionCode;
    private final Integer httpStatus;

    ExceptionCode(Integer exceptionCode, Integer httpStatus){
        this.exceptionCode = exceptionCode;
        this.httpStatus = httpStatus;
    }

    public Integer getExceptionCode(){
        return exceptionCode;
    }

    public Integer getHttpStatus(){
        return httpStatus; }

    public static ExceptionCode getByExceptionCode(Integer id){
        return Arrays.stream(ExceptionCode.values())
                .filter(exceptionCode -> Objects.equals(exceptionCode.getExceptionCode(), id))
                .findFirst()
                .orElse(UNKNOWN_EXCEPTION);
    }
}
