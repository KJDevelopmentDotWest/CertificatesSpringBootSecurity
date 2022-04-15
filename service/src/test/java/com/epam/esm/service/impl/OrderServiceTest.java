package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.OrderDao;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.model.order.Order;
import com.epam.esm.dao.model.user.User;
import com.epam.esm.service.converter.impl.OrderConverter;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.dto.order.OrderDto;
import com.epam.esm.service.dto.role.Role;
import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.impl.OrderValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderServiceTest {

    private final OrderService service = new OrderService();
    private final LocalDateTime nowTime = LocalDateTime.now();

    private final OrderDao orderDao = Mockito.mock(OrderDao.class);
    private final OrderConverter orderConverter = Mockito.mock(OrderConverter.class);
    private final OrderValidator orderValidator = Mockito.mock(OrderValidator.class);

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
        Mockito.when(orderDao.saveEntity(Mockito.any())).thenReturn(correctOrder);
        Mockito.when(orderDao.findEntityById(Mockito.anyInt())).thenReturn(correctOrder);
        Mockito.when(orderDao.findOrdersByUserId(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn( new ArrayList<>(List.of(correctOrder)));
        Mockito.when(orderDao.findAllEntities()).thenReturn(new ArrayList<>(List.of(correctOrder)));
        Mockito.when(orderDao.findAllEntities(Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>(List.of(correctOrder)));

        Mockito.when(orderConverter.convert(correctOrderDto)).thenReturn(correctOrder);
        Mockito.when(orderConverter.convert(correctOrder)).thenReturn(correctOrderDto);

        ReflectionTestUtils.setField(service, "dao",  orderDao, OrderDao.class);
        ReflectionTestUtils.setField(service, "validator",  orderValidator, OrderValidator.class);
        ReflectionTestUtils.setField(service, "converter",  orderConverter, OrderConverter.class);
    }

    @Test
    void create() throws ServiceException {
        assertEquals(correctOrderDto, service.create(correctOrderDto));
    }

    @Test
    void update() throws ServiceException {
        assertNull(service.update(correctOrderDto));
    }

    @Test
    void delete() throws ServiceException {
        assertNull(service.delete(1));
    }

    @Test
    void getById() throws ServiceException {
        assertEquals(correctOrderDto, service.getById(1));
        assertEquals(correctOrderDto, service.getById(1, 1));

    }

    @Test
    void getAll() throws ServiceException {
        assertEquals(new ArrayList<>(List.of(correctOrderDto)), service.getAll());
        assertEquals(new ArrayList<>(List.of(correctOrderDto)), service.getAll("1", "1"));
    }

    @Test
    void getByUserId() {
        assertEquals(new ArrayList<>(List.of(correctOrderDto)), service.getByUserId(1, "1", "1"));
    }
}