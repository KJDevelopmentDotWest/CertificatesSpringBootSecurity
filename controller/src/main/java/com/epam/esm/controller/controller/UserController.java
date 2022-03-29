package com.epam.esm.controller.controller;

import com.epam.esm.service.dto.role.Role;
import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<EntityModel<UserDto>> create(@RequestBody UserDto userDto) throws ServiceException {
        userDto.setRole(Role.USER);
        UserDto createdUser = service.create(userDto);
        Link getById = linkTo(methodOn(UserController.class).getById(createdUser.getId())).withSelfRel();
        Link root = linkTo(methodOn(UserController.class).getAll("1", "10")).withRel("all");
        return new ResponseEntity<>(EntityModel.of(createdUser, getById, root), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<UserDto>> getAll(@RequestParam(value = "page", defaultValue = "1") String page,
                                           @RequestParam(value = "pageSize", defaultValue = "10") String pageSize) throws ServiceException {
        Link selfLink = linkTo(methodOn(UserController.class).getAll("1", "10")).withRel("all");
        return new ResponseEntity<>(CollectionModel.of(service.getAll(page, pageSize), selfLink), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<UserDto>> getById(@PathVariable("id") Integer id) throws ServiceException {
        Link selfLink = linkTo(methodOn(UserController.class).getById(id)).withSelfRel();
        Link root = linkTo(methodOn(UserController.class).getAll("1", "10")).withRel("all");
        return new ResponseEntity<>(EntityModel.of(service.getById(id), selfLink, root), HttpStatus.OK);
    }
}
