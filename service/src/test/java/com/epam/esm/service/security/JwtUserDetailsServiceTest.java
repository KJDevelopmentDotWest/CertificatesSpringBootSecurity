package com.epam.esm.service.security;

import com.epam.esm.dao.model.user.User;
import com.epam.esm.service.dto.role.Role;
import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.UserService;
import com.epam.esm.service.security.jwt.JwtUser;
import com.epam.esm.service.security.jwt.JwtUserFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtUserDetailsServiceTest {

    private final UserService userService = Mockito.mock(UserService.class);
    private final JwtUserFactory jwtUserFactory = Mockito.mock(JwtUserFactory.class);

    private final JwtUserDetailsService service = new JwtUserDetailsService(userService, jwtUserFactory);

    private final UserDto correctUserDto = new UserDto(1, "name", "surname", "username", "password", Role.USER);
    private final JwtUser correctUserDetails =
            new JwtUser(1, "name", "surname", "username", "password", List.of(new SimpleGrantedAuthority(Role.USER.name())));

    @BeforeAll
    void setup () throws ServiceException {
        Mockito.when(userService.getByUsername(Mockito.any())).thenReturn(correctUserDto);
        Mockito.when(jwtUserFactory.create(Mockito.any())).thenReturn(correctUserDetails);
    }

    @Test
    void loadUserByUsername() {
        assertEquals(correctUserDetails, service.loadUserByUsername("username"));
    }
}