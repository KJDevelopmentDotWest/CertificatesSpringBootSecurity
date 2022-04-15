package com.epam.esm.service.converter.impl;

import com.epam.esm.dao.model.user.User;
import com.epam.esm.service.dto.role.Role;
import com.epam.esm.service.dto.user.UserDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserConverterTest {

    private final UserConverter converter = new UserConverter();

    private final User correctUser = new User(1, "name", "surname", "username", "password", 1);
    private final UserDto correctUserDto = new UserDto(1, "name", "surname", "username", "password", Role.USER);

    @Test
    void convertFromModel() {
        assertEquals(correctUserDto, converter.convert(correctUser));
    }

    @Test
    void convertFromDto() {
        assertEquals(correctUser, converter.convert(correctUserDto));
    }
}