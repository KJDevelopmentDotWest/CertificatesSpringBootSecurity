package com.epam.esm.controller.exceptionhandler;

import com.epam.esm.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Util class that provides exception handling
 */

@ControllerAdvice
public class ExceptionHandlerSupport {

    private ResourceBundle resourceBundle;

    private static final Logger logger = LogManager.getLogger(ExceptionHandlerSupport.class);

    @ExceptionHandler
    public ResponseEntity<Object> handleException(Exception exception, Locale locale){
        Map<String, Object> hashMap = new HashMap<>();
        resourceBundle = ResourceBundle.getBundle("locale", locale);

        if (exception instanceof ServiceException serviceException){
            logger.error(serviceException);
            logger.error(serviceException.getExceptionCode());
            logger.error(serviceException.getExceptionMessages());

            List<String> exceptionMessages = new ArrayList<>();

            serviceException.getExceptionMessages().forEach(exceptionMessage -> {
                try {
                    exceptionMessages.add(resourceBundle.getString(exceptionMessage.toString()));
                } catch (MissingResourceException ignored){
                    exceptionMessages.add(resourceBundle.getString("UNKNOWN_EXCEPTION"));
                }
            });

            hashMap.put("message", exceptionMessages);
            hashMap.put("internalExceptionCode", serviceException.getExceptionCode().getExceptionCode());
            return new ResponseEntity<>(hashMap, Optional.of(HttpStatus.valueOf(serviceException.getExceptionCode().getHttpStatus())).orElse(HttpStatus.INTERNAL_SERVER_ERROR) );
        } else if (exception instanceof HttpMessageNotReadableException parseException){
            logger.error(parseException);
            hashMap.put("message", resourceBundle.getString("PARSE_EXCEPTION"));
            return new ResponseEntity<>(hashMap, HttpStatus.BAD_REQUEST);
        } else {
            logger.error(exception);
            hashMap.put("message", resourceBundle.getString("UNKNOWN_EXCEPTION"));
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
