package com.epam.esm.service.exception;

public class ServiceException extends Exception{

    private ExceptionCode exceptionCode = ExceptionCode.UNKNOWN_EXCEPTION;

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

    public ServiceException(String message, ExceptionCode exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}