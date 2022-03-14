package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.UserDao;
import com.epam.esm.dao.model.user.User;
import com.epam.esm.service.api.Service;
import com.epam.esm.service.converter.impl.UserConverter;
import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ExceptionCode;
import com.epam.esm.service.exception.ExceptionMessage;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserService extends Service<UserDto> {

    @Autowired
    private UserDao dao;

    @Autowired
    private UserConverter converter;

    @Override
    public UserDto create(UserDto value) throws ServiceException {
        throw new ServiceException(ExceptionCode.OPERATION_IS_NOT_SUPPORTED);
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
        List<User> daoResult;
        daoResult = dao.findAllEntities(parsePageNumber(pageNumber), parsePageSize(pageSize));
        return daoResult.stream().map(converter::convert).collect(Collectors.toCollection(ArrayList::new));
    }
}
