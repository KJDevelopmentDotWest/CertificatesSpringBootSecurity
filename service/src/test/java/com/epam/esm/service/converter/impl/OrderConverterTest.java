package com.epam.esm.service.converter.impl;

import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.model.order.Order;
import com.epam.esm.dao.model.user.User;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.dto.order.OrderDto;
import com.epam.esm.service.dto.role.Role;
import com.epam.esm.service.dto.user.UserDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderConverterTest {

    private final OrderConverter converter = new OrderConverter();

    private final GiftCertificateConverter giftCertificateConverter = Mockito.mock(GiftCertificateConverter.class);
    private final UserConverter userConverter = Mockito.mock(UserConverter.class);

    private final LocalDateTime nowTime = LocalDateTime.now();

    private final GiftCertificate correctGiftCertificate = new GiftCertificate(1, "name", "description", 200d, 100L,
            nowTime, nowTime, new ArrayList<>());
    private final GiftCertificateDto correctGiftCertificateDto = new GiftCertificateDto(1, "name", "description", 200d, 100L,
            nowTime, nowTime, new ArrayList<>());

    private final User correctUser = new User(1, "name", "surname", "username", "password", 1);
    private final UserDto correctUserDto = new UserDto(1, "name", "surname", "username", "password", Role.USER);

    private final Order correctOrder = new Order(1, correctUser, correctGiftCertificate, nowTime, 200d);
    private final OrderDto correctOrderDto = new OrderDto(1, correctUserDto, correctGiftCertificateDto, nowTime, 200d);

    @BeforeAll
    void setup() {
        Mockito.when(giftCertificateConverter.convert(correctGiftCertificate)).thenReturn(correctGiftCertificateDto);
        Mockito.when(giftCertificateConverter.convert(correctGiftCertificateDto)).thenReturn(correctGiftCertificate);
        Mockito.when(userConverter.convert(correctUser)).thenReturn(correctUserDto);
        Mockito.when(userConverter.convert(correctUserDto)).thenReturn(correctUser);

        ReflectionTestUtils.setField(converter, "userConverter", userConverter, UserConverter.class);
        ReflectionTestUtils.setField(converter, "giftCertificateConverter", giftCertificateConverter, GiftCertificateConverter.class);
    }

    @Test
    void convertFromModel() {
        assertEquals(correctOrderDto, converter.convert(correctOrder));
    }

    @Test
    void convertFromDto() {
        assertEquals(correctOrder, converter.convert(correctOrderDto));
    }
}