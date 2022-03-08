package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.OrderDao;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
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
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OrderService implements Service<OrderDto> {

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
            throw new ServiceException(ExceptionCode.ENTITY_NOT_FOUND, ExceptionMessage.THERE_IS_NO_GIFT_CERTIFICATE_WITH_PROVIDED_ID);
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
    public List<OrderDto> getByUserId(Integer id, Integer pageNumber) {
        if (pageNumber < 1){
            pageNumber = 1;
        }

        List<Order> daoResult = dao.findOrdersByUserId(id, pageNumber);

        return daoResult.stream().map(converter::convert).collect(Collectors.toList());
    }
}
