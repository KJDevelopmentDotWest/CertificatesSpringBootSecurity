package com.epam.esm.service.converter.impl;

import com.epam.esm.dao.model.order.Order;
import com.epam.esm.service.converter.api.Converter;
import com.epam.esm.service.dto.order.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter implements Converter<Order, OrderDto> {

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private GiftCertificateConverter giftCertificateConverter;

    @Override
    public Order convert(OrderDto value) {
        return new Order(value.getId(),
                userConverter.convert(value.getUser()),
                giftCertificateConverter.convert(value.getGiftCertificate()),
                value.getTime(),
                value.getCost());
    }

    @Override
    public OrderDto convert(Order value) {
        return new OrderDto(value.getId(),
                userConverter.convert(value.getUser()),
                giftCertificateConverter.convert(value.getGiftCertificate()),
                value.getTime(),
                value.getCost());
    }
}
