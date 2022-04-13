package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.UserDao;
import com.epam.esm.dao.model.user.User;
import com.epam.esm.service.api.Service;
import com.epam.esm.service.converter.impl.UserConverter;
import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ExceptionCode;
import com.epam.esm.service.exception.ExceptionMessage;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.impl.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserService extends Service<UserDto> {

    private final UserDao dao;

    private final UserConverter converter;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserValidator validator;

    @Autowired
    public UserService(UserDao dao, UserConverter converter, BCryptPasswordEncoder passwordEncoder, UserValidator validator) {
        this.dao = dao;
        this.converter = converter;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    /**
     * validates value and sends save request to corresponding dao class
     * note that password in method argument must be decrypted, but returned user will have encrypted password
     * @param value value to be saved
     * @return saved value
     * @throws ServiceException if user is invalid, value cannot be created, or database error occurred
     */
    @Override
    public UserDto create(UserDto value) throws ServiceException {
        validator.validate(value, false);
        if (Objects.nonNull(dao.findByUsername(value.getUsername()))){
            throw new ServiceException(ExceptionCode.VALIDATION_FAILED_EXCEPTION, ExceptionMessage.USER_USERNAME_MUST_BE_UNIQUE);
        }
        value.setPassword(passwordEncoder.encode(value.getPassword()));
        return converter.convert(dao.saveEntity(converter.convert(value)));
    }

    @Override
    public UserDto update(UserDto value) throws ServiceException {
        throw new ServiceException(ExceptionCode.OPERATION_IS_NOT_SUPPORTED);
    }

    @Override
    public Boolean delete(Integer id) throws ServiceException {
        throw new ServiceException(ExceptionCode.OPERATION_IS_NOT_SUPPORTED);
    }

    @Override
    public UserDto getById(Integer id) throws ServiceException {
        User result;

        result = dao.findEntityById(id);

        if (Objects.isNull(result)){
            throw new ServiceException(ExceptionCode.ENTITY_NOT_FOUND, ExceptionMessage.THERE_IS_NO_GIFT_CERTIFICATE_WITH_PROVIDED_ID);
        }

        return converter.convert(result);
    }

    @Override
    public List<UserDto> getAll() throws ServiceException {
        List<User> daoResult;
        daoResult = dao.findAllEntities();
        return daoResult.stream().map(converter::convert).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * returns list of users by page number
     * @param pageNumber number of page
     * @return list of gift certificates by page number
     */
    public List<UserDto> getAll(String pageNumber, String pageSize) throws ServiceException {
        List<User> daoResult = dao.findAllEntities(parsePageNumber(pageNumber), parsePageSize(pageSize));
        return daoResult.stream().map(converter::convert).collect(Collectors.toCollection(ArrayList::new));
    }

    public UserDto getByUsername(String username) throws ServiceException {
        User result = dao.findByUsername(username);

        if (Objects.isNull(result)){
            throw new ServiceException("");
        }

        return converter.convert(result);
    }
}
