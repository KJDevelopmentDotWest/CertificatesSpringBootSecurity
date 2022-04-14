package com.epam.esm.controller.controller;

import com.epam.esm.controller.exceptionhandler.ExceptionHandlerSupport;
import com.epam.esm.service.dto.order.OrderDto;
import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users/{userId}/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<OrderDto>> getById(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) throws ServiceException {
        OrderDto orderDto = service.getById(id, userId);

        return new ResponseEntity<>(EntityModel.of(orderDto, getLinks(id, userId)), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<OrderDto>> getByUserId(@PathVariable("userId") Integer userId,
                                                 @RequestParam(value = "page", defaultValue = "1") String page,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") String pageSize) throws ServiceException {
        List<OrderDto> orderDto = service.getByUserId(userId, page, pageSize);
        return new ResponseEntity<>(CollectionModel.of(orderDto, getGetByUserIdLinks(userId)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EntityModel<OrderDto>> create(@PathVariable("userId") Integer userId, @RequestBody OrderDto order) throws ServiceException {
        order.setUser(new UserDto(userId, null, null, null, null,null));
        OrderDto createdDto = service.create(order);
        List<Link> links = getLinks(createdDto.getId(), userId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", links.get(0).getHref());
        return new ResponseEntity<>(EntityModel.of(createdDto, links), headers, HttpStatus.CREATED);
    }

    private List<Link> getGetByUserIdLinks(Integer userId) throws ServiceException {
        List<Link> result = new ArrayList<>();
        Link selfLink = linkTo(methodOn(OrderController.class).getByUserId(userId, "1", "20")).withSelfRel();
        result.add(selfLink);
        return result;
    }

    private List<Link> getLinks(Integer id, Integer userId) throws ServiceException {
        List<Link> result = new ArrayList<>();
        Link getById = linkTo(methodOn(OrderController.class).getById(id, userId)).withSelfRel();
        Link getByUserId = linkTo(methodOn(OrderController.class).getByUserId(userId, "1", "20")).withRel("get_by_user_id");
        result.add(getById);
        result.add(getByUserId);
        return result;
    }
}
