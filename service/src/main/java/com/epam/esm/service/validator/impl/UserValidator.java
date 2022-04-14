package com.epam.esm.service.validator.impl;

import com.epam.esm.service.dto.role.Role;
import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ExceptionCode;
import com.epam.esm.service.exception.ExceptionMessage;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.api.Validator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserValidator implements Validator<UserDto> {

    private static final Integer FIRSTNAME_MIN_LENGTH = 2;
    private static final Integer FIRSTNAME_MAX_LENGTH = 30;
    private static final Integer LASTNAME_MIN_LENGTH = 2;
    private static final Integer LASTNAME_MAX_LENGTH = 30;
    private static final Integer USERNAME_MIN_LENGTH = 2;
    private static final Integer USERNAME_MAX_LENGTH = 30;
    private static final Integer PASSWORD_MIN_LENGTH = 2;
    private static final Integer PASSWORD_MAX_LENGTH = 30;

    private static final String WHITESPACE = " ";

    @Override
    public void validate(UserDto value, Boolean checkId) throws ServiceException {
        List<ExceptionMessage> exceptionMessages = new ArrayList<>();

        if (Objects.isNull(value)){
            throw new ServiceException(ExceptionCode.VALIDATION_FAILED_EXCEPTION, ExceptionMessage.USER_CANNOT_BE_NULL);
        }

        if (checkId){
            validateIdNotNullAndPositive(value.getId());
        }

        validateFirstname(value.getFirstname(), false, exceptionMessages);
        validateLastname(value.getLastname(), false, exceptionMessages);
        validateUsername(value.getUsername(), false, exceptionMessages);
        validatePassword(value.getPassword(), false, exceptionMessages);
        validateRole(value.getRole(), false, exceptionMessages);

        if (!exceptionMessages.isEmpty()){
            throw new ServiceException(ExceptionCode.VALIDATION_FAILED_EXCEPTION, exceptionMessages);
        }
    }

    private void validateFirstname(String firstname, Boolean canBeNull, List<ExceptionMessage> exceptionMessages) {
        if (Objects.isNull(firstname)){
            if (!canBeNull) {
                exceptionMessages.add(ExceptionMessage.USER_FIRSTNAME_CANNOT_BE_NULL);
            }
            return;
        }

        if (firstname.length() < FIRSTNAME_MIN_LENGTH) {
            exceptionMessages.add(ExceptionMessage.USER_FIRSTNAME_TOO_SHORT);
        }
        if (firstname.length() > FIRSTNAME_MAX_LENGTH) {
            exceptionMessages.add(ExceptionMessage.USER_FIRSTNAME_TOO_LONG);
        }
        if (firstname.startsWith(WHITESPACE)){
            exceptionMessages.add(ExceptionMessage.USER_FIRSTNAME_STARTS_WITH_WHITESPACE);
        }
        if (firstname.endsWith(WHITESPACE)){
            exceptionMessages.add(ExceptionMessage.USER_FIRSTNAME_ENDS_WITH_WHITESPACE);
        }
    }

    private void validateLastname(String lastname, Boolean canBeNull, List<ExceptionMessage> exceptionMessages) {
        if (Objects.isNull(lastname)){
            if (!canBeNull) {
                exceptionMessages.add(ExceptionMessage.USER_LASTNAME_CANNOT_BE_NULL);
            }
            return;
        }

        if (lastname.length() < LASTNAME_MIN_LENGTH) {
            exceptionMessages.add(ExceptionMessage.USER_LASTNAME_TOO_SHORT);
        }
        if (lastname.length() > LASTNAME_MAX_LENGTH) {
            exceptionMessages.add(ExceptionMessage.USER_LASTNAME_TOO_LONG);
        }
        if (lastname.startsWith(WHITESPACE)){
            exceptionMessages.add(ExceptionMessage.USER_LASTNAME_STARTS_WITH_WHITESPACE);
        }
        if (lastname.endsWith(WHITESPACE)){
            exceptionMessages.add(ExceptionMessage.USER_LASTNAME_ENDS_WITH_WHITESPACE);
        }
    }

    private void validateUsername(String username, Boolean canBeNull, List<ExceptionMessage> exceptionMessages) {
        if (Objects.isNull(username)){
            if (!canBeNull) {
                exceptionMessages.add(ExceptionMessage.USER_USERNAME_CANNOT_BE_NULL);
            }
            return;
        }

        if (username.length() < USERNAME_MIN_LENGTH) {
            exceptionMessages.add(ExceptionMessage.USER_USERNAME_TOO_SHORT);
        }
        if (username.length() > USERNAME_MAX_LENGTH) {
            exceptionMessages.add(ExceptionMessage.USER_USERNAME_TOO_LONG);
        }
        if (username.startsWith(WHITESPACE)){
            exceptionMessages.add(ExceptionMessage.USER_USERNAME_STARTS_WITH_WHITESPACE);
        }
        if (username.endsWith(WHITESPACE)){
            exceptionMessages.add(ExceptionMessage.USER_USERNAME_ENDS_WITH_WHITESPACE);
        }
    }

    private void validatePassword(String password, Boolean canBeNull, List<ExceptionMessage> exceptionMessages) {
        if (Objects.isNull(password)){
            if (!canBeNull) {
                exceptionMessages.add(ExceptionMessage.USER_PASSWORD_CANNOT_BE_NULL);
            }
            return;
        }

        if (password.length() < PASSWORD_MIN_LENGTH) {
            exceptionMessages.add(ExceptionMessage.USER_PASSWORD_TOO_SHORT);
        }
        if (password.length() > PASSWORD_MAX_LENGTH) {
            exceptionMessages.add(ExceptionMessage.USER_PASSWORD_TOO_LONG);
        }
        if (password.startsWith(WHITESPACE)){
            exceptionMessages.add(ExceptionMessage.USER_PASSWORD_STARTS_WITH_WHITESPACE);
        }
        if (password.endsWith(WHITESPACE)){
            exceptionMessages.add(ExceptionMessage.USER_PASSWORD_ENDS_WITH_WHITESPACE);
        }
    }

    private void validateRole(Role role, Boolean canBeNull, List<ExceptionMessage> exceptionMessages) {
        if (Objects.isNull(role)){
            if (!canBeNull) {
                exceptionMessages.add(ExceptionMessage.USER_ROLE_CANNOT_BE_NULL);
            }
        }
    }
}
