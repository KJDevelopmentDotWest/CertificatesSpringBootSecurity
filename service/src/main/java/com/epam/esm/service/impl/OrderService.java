package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.OrderDao;
import com.epam.esm.dao.model.order.Order;
import com.epam.esm.service.api.Service;
import com.epam.esm.service.converter.impl.OrderConverter;
import com.epam.esm.service.dto.order.OrderDto;
import com.epam.esm.service.exception.ExceptionCode;
import com.epam.esm.service.exception.ExceptionMessage;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.impl.OrderValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OrderService extends Service<OrderDto> {

    @Autowired
    private OrderDao dao;

    @Autowired
    private OrderConverter converter;

    @Autowired
    private OrderValidator validator;

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    @Override
    public OrderDto create(OrderDto value) throws ServiceException {
        validator.validate(value, false);
        Order savedOrder = dao.saveEntity(converter.convert(value));

        if (Objects.isNull(savedOrder)){
            throw new ServiceException(ExceptionCode.INTERNAL_DB_EXCEPTION, ExceptionMessage.INTERNAL_DB_EXCEPTION);
        }

        return converter.convert(savedOrder);
    }

    @Override
    public OrderDto update(OrderDto value) throws ServiceException {
        return null;
    }

    @Override
    public Boolean delete(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public OrderDto getById(Integer id) throws ServiceException {
        Order result;
        result = dao.findEntityById(id);

        if (Objects.isNull(result)){
            throw new ServiceException(ExceptionCode.ENTITY_NOT_FOUND, ExceptionMessage.THERE_IS_NO_ORDER_WITH_PROVIDED_ID);
        }

        return converter.convert(result);
    }

    /**
     * validates id and sends get by id request to corresponding dao class
     * @param id order id
     * @param userId user id
     * @return order with id == value.id
     * @throws ServiceException if there is no value with provided id, id is null, database error occurred, or order not belongs to user
     */
    public OrderDto getById(Integer id, Integer userId) throws ServiceException {
        Order result = dao.findEntityById(id);

        if (Objects.isNull(result)){
            throw new ServiceException(ExceptionCode.ENTITY_NOT_FOUND, ExceptionMessage.THERE_IS_NO_ORDER_WITH_PROVIDED_ID);
        }

        if (!Objects.equals(result.getUser().getId(), userId)){
            throw new ServiceException(ExceptionCode.ENTITY_NOT_FOUND, ExceptionMessage.THERE_IS_NO_ORDER_WITH_PROVIDED_ID_THAT_BELONG_TO_THIS_USER);
        }

        return converter.convert(result);
    }

    @Override
    public List<OrderDto> getAll() throws ServiceException {
        return null;
    }

    /**
     * returns list of orders by user id and page number
     * @param id id of user
     * @param pageNumber number of page
     * @return list of orders by user id and page number
     */
    public List<OrderDto> getByUserId(Integer id, String pageNumber, String pageSize) {
        List<Order> daoResult = dao.findOrdersByUserId(id, parsePageNumber(pageNumber), parsePageSize(pageSize));
        return daoResult.stream().map(converter::convert).collect(Collectors.toList());
    }
}
