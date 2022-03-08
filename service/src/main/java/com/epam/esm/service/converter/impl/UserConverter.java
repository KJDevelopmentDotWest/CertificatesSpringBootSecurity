package com.epam.esm.service.converter.impl;

import com.epam.esm.dao.model.user.User;
import com.epam.esm.service.converter.api.Converter;
import com.epam.esm.service.dto.user.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User, UserDto> {
    @Override
    public User convert(UserDto value) {
        return new User(value.getId(), value.getName());
    }

    @Override
    public UserDto convert(User value) {
        return new UserDto(value.getId(), value.getName());
    }
}
