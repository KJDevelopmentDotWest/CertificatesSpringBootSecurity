package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.UserDao;
import com.epam.esm.dao.model.user.User;
import com.epam.esm.service.converter.impl.UserConverter;
import com.epam.esm.service.dto.role.Role;
import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.impl.UserValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    private final UserDao dao = Mockito.mock(UserDao.class);
    private final UserConverter converter = Mockito.mock(UserConverter.class);
    private final UserValidator validator = Mockito.mock(UserValidator.class);
    private final BCryptPasswordEncoder encoder = Mockito.mock(BCryptPasswordEncoder.class);

    private final UserService service = new UserService(dao, converter, encoder, validator);

    private final User correctUser = new User(1, "name", "surname", "username", "password", 1);
    private final UserDto correctUserDto = new UserDto(1, "name", "surname", "username", "password", Role.USER);

    @BeforeAll
    void setup() {
        Mockito.when(dao.saveEntity(Mockito.any())).thenReturn(correctUser);
        Mockito.when(dao.findEntityById(Mockito.any())).thenReturn(correctUser);
        Mockito.when(dao.findAllEntities()).thenReturn(new ArrayList<>(List.of(correctUser)));
        Mockito.when(dao.findAllEntities(Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>(List.of(correctUser)));
        Mockito.when(dao.findByUsername("name1")).thenReturn(correctUser);
        Mockito.when(dao.findByUsername("name2")).thenReturn(null);

        Mockito.when(converter.convert(correctUser)).thenReturn(correctUserDto);
        Mockito.when(converter.convert(correctUserDto)).thenReturn(correctUser);

        Mockito.when(encoder.encode("password")).thenReturn("password");
    }

    @Test
    void create() throws ServiceException {
        assertEquals(correctUserDto, service.create(correctUserDto));
    }

    @Test
    void update() {
        assertThrows(ServiceException.class, () -> service.update(correctUserDto));
    }

    @Test
    void delete() {
        assertThrows(ServiceException.class, () -> service.delete(1));
    }

    @Test
    void getById() throws ServiceException {
        assertEquals(correctUserDto, service.getById(1));
    }

    @Test
    void getAll() throws ServiceException {
        assertEquals(new ArrayList<>(List.of(correctUserDto)), service.getAll());
        assertEquals(new ArrayList<>(List.of(correctUserDto)), service.getAll("1", "1"));
    }

    @Test
    void getByUsername() throws ServiceException {
        assertEquals(correctUserDto, service.getByUsername("name1"));
    }
}