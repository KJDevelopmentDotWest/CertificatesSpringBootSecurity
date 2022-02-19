package com.epam.esm.service.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Service exception will br thrown by service methods
 */
public class ServiceException extends Exception{

    private ExceptionCode exceptionCode = ExceptionCode.UNKNOWN_EXCEPTION;
    private final List<ExceptionMessage> exceptionMessages = new ArrayList<>();

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(ExceptionCode exceptionCode){
        this.exceptionCode = exceptionCode;
    }

    public ServiceException(ExceptionCode exceptionCode, ExceptionMessage exceptionMessage){
        this.exceptionCode = exceptionCode;
        this.exceptionMessages.add(exceptionMessage);
    }

    public ServiceException(ExceptionCode exceptionCode, List<ExceptionMessage> exceptionMessage){
        this.exceptionCode = exceptionCode;
        this.exceptionMessages.addAll(exceptionMessage);
    }

    public ServiceException(String message, ExceptionCode exceptionCode, ExceptionMessage exceptionMessage) {
        super(message);
        this.exceptionCode = exceptionCode;
        this.exceptionMessages.add(exceptionMessage);
    }

    public ServiceException(String message, ExceptionCode exceptionCode, List<ExceptionMessage> exceptionMessage) {
        super(message);
        this.exceptionCode = exceptionCode;
        this.exceptionMessages.addAll(exceptionMessage);
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    public List<ExceptionMessage> getExceptionMessages() {
        return exceptionMessages;
    }
}