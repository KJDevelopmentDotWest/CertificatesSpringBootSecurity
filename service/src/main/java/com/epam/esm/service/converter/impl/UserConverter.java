package com.epam.esm.service.converter.impl;

import com.epam.esm.dao.model.user.User;
import com.epam.esm.service.converter.api.Converter;
import com.epam.esm.service.dto.role.Role;
import com.epam.esm.service.dto.user.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User, UserDto> {
    @Override
    public User convert(UserDto value) {
        return new User(value.getId(), value.getFirstname(), value.getLastname(), value.getUsername(), value.getPassword(), value.getRole().getId());
    }

    @Override
    public UserDto convert(User value) {
        return new UserDto(value.getId(), value.getFirstname(), value.getLastname(), value.getUsername(), value.getPassword(), Role.getById(value.getRoleId()));
    }
}
