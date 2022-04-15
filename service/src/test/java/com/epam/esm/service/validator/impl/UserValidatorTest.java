package com.epam.esm.service.validator.impl;

import com.epam.esm.service.dto.role.Role;
import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private final UserValidator validator = new UserValidator();

    private final UserDto correctUserDto = new UserDto(1, "name", "surname", "username", "password", Role.USER);
    private final UserDto incorrectUserDto = new UserDto(-1, "n", " ", null, "p", null);

    @Test
    void validate() {
        assertDoesNotThrow(() -> validator.validate(correctUserDto, true));
        assertThrows(ServiceException.class, () -> validator.validate(incorrectUserDto, true));
    }
}