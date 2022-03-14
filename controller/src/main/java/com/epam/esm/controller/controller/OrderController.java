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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public ResponseEntity<EntityModel<OrderDto>> getById(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) throws ServiceException {
        OrderDto orderDto = service.getById(id, userId);

        Link selfLink = linkTo(methodOn(OrderController.class).getById(id, userId)).withSelfRel();
        Link getByUserId = linkTo(methodOn(OrderController.class).getByUserId(userId, "1", "10")).withRel("get_by_user_id");

        return new ResponseEntity<>(EntityModel.of(orderDto, selfLink, getByUserId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<OrderDto>> getByUserId(@PathVariable("userId") Integer userId,
                                                 @RequestParam(value = "page", defaultValue = "1") String page,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") String pageSize) throws ServiceException {
        Link selfLink = linkTo(methodOn(OrderController.class).getByUserId(userId, "1", pageSize)).withSelfRel();
        List<OrderDto> orderDto = service.getByUserId(userId, page, pageSize);
        return new ResponseEntity<>(CollectionModel.of(orderDto, selfLink), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EntityModel<OrderDto>> create(@PathVariable("userId") Integer userId, @RequestBody OrderDto order) throws ServiceException {
        order.setUser(new UserDto(userId, null));
        OrderDto createdDto = service.create(order);
        HttpHeaders headers = new HttpHeaders();
        Link getById = linkTo(methodOn(OrderController.class).getById(createdDto.getId(), userId)).withSelfRel();
        Link getByUserId = linkTo(methodOn(OrderController.class).getByUserId(userId, "1", "10")).withRel("get_by_user_id");
        headers.add("Location", createdDto.getId().toString());
        return new ResponseEntity<>(EntityModel.of(createdDto, getById, getByUserId), headers, HttpStatus.CREATED);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception exception, HttpServletRequest request){
        RequestContext requestContext = new RequestContext(request);
        return exceptionHandlerSupport.handleException(exception, requestContext.getLocale());
    }
}
