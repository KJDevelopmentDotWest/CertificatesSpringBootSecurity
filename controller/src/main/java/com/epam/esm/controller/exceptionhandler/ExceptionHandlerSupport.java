package com.epam.esm.controller.exceptionhandler;

import com.epam.esm.service.exception.ServiceException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Util class that provides exception handling
 */

@Component
public class ExceptionHandlerSupport {

    private ResourceBundle resourceBundle;

    public ResponseEntity<String> handleException(ServiceException exception, Locale locale){
        resourceBundle = ResourceBundle.getBundle("locale", locale);
        JSONObject response = new JSONObject();

        List<String> exceptionMessages = new ArrayList<>();

        exception.getExceptionMessages().forEach(exceptionMessage -> {
            try {
                exceptionMessages.add(resourceBundle.getString(exceptionMessage.toString()));
            } catch (MissingResourceException ignored){
                exceptionMessages.add(resourceBundle.getString("UNKNOWN_EXCEPTION"));
            }
        });

        response.put("message", exceptionMessages);
        response.put("internalExceptionCode", exception.getExceptionCode().getExceptionCode());
        return new ResponseEntity<>(response.toString(), Optional.of(HttpStatus.valueOf(exception.getExceptionCode().getHttpStatus())).orElse(HttpStatus.INTERNAL_SERVER_ERROR) );
    }
}
