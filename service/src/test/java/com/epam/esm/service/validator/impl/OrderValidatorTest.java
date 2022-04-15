package com.epam.esm.service.validator.impl;

import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.model.order.Order;
import com.epam.esm.dao.model.user.User;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.dto.order.OrderDto;
import com.epam.esm.service.dto.role.Role;
import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OrderValidatorTest {

    private final OrderValidator validator = new OrderValidator();

    private final LocalDateTime nowTime = LocalDateTime.now();

    private final GiftCertificateDto correctGiftCertificateDto = new GiftCertificateDto(1, "name", "description", 200d, 100L,
            nowTime, nowTime, new ArrayList<>());
    private final GiftCertificateDto incorrectGiftCertificateDto = new GiftCertificateDto(-1, "name", "description", 200d, 100L,
            nowTime, nowTime, new ArrayList<>());

    private final UserDto correctUserDto = new UserDto(1, "name", "surname", "username", "password", Role.USER);
    private final UserDto incorrectUserDto = new UserDto(-1, "name", "surname", "username", "password", Role.USER);

    private final OrderDto correctOrderDto = new OrderDto(1, correctUserDto, correctGiftCertificateDto, nowTime, 200d);
    private final OrderDto incorrectOrderDto = new OrderDto(null, incorrectUserDto, incorrectGiftCertificateDto, nowTime, -200d);

    @Test
    void validate() {
        assertDoesNotThrow(() -> validator.validate(correctOrderDto, true));
        assertThrows(ServiceException.class, () -> validator.validate(incorrectOrderDto, true));
    }
}