package com.epam.esm.controller.controller;

import com.epam.esm.controller.exceptionhandler.ExceptionHandlerSupport;
import com.epam.esm.service.dto.order.OrderDto;
import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ExceptionCode;
import com.epam.esm.service.exception.ExceptionMessage;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users/{userId}/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @Autowired
    private ExceptionHandlerSupport exceptionHandlerSupport;

    @GetMapping(value = "/{id}")
    public EntityModel<OrderDto> getById(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) throws ServiceException {
        OrderDto orderDto = service.getById(id);

        //im not sure if a have to implement this
        if (!Objects.equals(orderDto.getUser().getId(), userId)){
            throw new ServiceException(ExceptionCode.VALIDATION_FAILED_EXCEPTION, ExceptionMessage.ACCESS_DENIED);
        }

        Link selfLink = linkTo(methodOn(OrderController.class).getById(id, userId)).withSelfRel();
        Link create = linkTo(methodOn(OrderController.class).create(userId,null)).withRel("create");
        Link getByUserId = linkTo(methodOn(OrderController.class).getByUserId(userId, "1")).withRel("get_by_user_id");

        return EntityModel.of(orderDto, selfLink, create, getByUserId);
    }

    @GetMapping()
    public CollectionModel<OrderDto> getByUserId(@PathVariable("userId") Integer userId, @RequestParam(value = "page", defaultValue = "1") String page) throws ServiceException {
        int pageNumberInteger;

        try {
            pageNumberInteger = Integer.parseInt(page);
        } catch (NumberFormatException e){
            pageNumberInteger = 1;
        }

        Link selfLink = linkTo(methodOn(OrderController.class).getByUserId(userId, "1")).withSelfRel();
        Link create = linkTo(methodOn(OrderController.class).create(userId,null)).withRel("create");

        List<OrderDto> orderDto = service.getByUserId(userId, pageNumberInteger);
        return CollectionModel.of(orderDto, selfLink, create);
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(@PathVariable("userId") Integer userId, @RequestBody OrderDto order) throws ServiceException {
        order.setUser(new UserDto(userId, null));
        return new ResponseEntity<>(service.create(order), HttpStatus.CREATED);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception exception, HttpServletRequest request){
        RequestContext requestContext = new RequestContext(request);
        return exceptionHandlerSupport.handleException(exception, requestContext.getLocale());
    }
}
