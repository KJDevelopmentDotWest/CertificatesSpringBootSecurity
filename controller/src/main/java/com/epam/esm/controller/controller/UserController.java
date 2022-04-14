package com.epam.esm.controller.controller;

import com.epam.esm.service.dto.role.Role;
import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        List<Link> links = getCreateLinks(userDto.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", links.get(0).getHref());
        return new ResponseEntity<>(EntityModel.of(createdUser, links), headers, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<UserDto>> getAll(@RequestParam(value = "page", defaultValue = "1") String page,
                                           @RequestParam(value = "pageSize", defaultValue = "20") String pageSize) throws ServiceException {
        return new ResponseEntity<>(CollectionModel.of(service.getAll(page, pageSize), getGetAllLinks()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<UserDto>> getById(@PathVariable("id") Integer id) throws ServiceException {
        return new ResponseEntity<>(EntityModel.of(service.getById(id), getGetByIdLinks(id)), HttpStatus.OK);
    }

    private List<Link> getGetAllLinks() throws ServiceException {
        List<Link> result = new ArrayList<>();
        Link selfLink = linkTo(methodOn(UserController.class).getAll("1", "20")).withRel("all");
        result.add(selfLink);
        return result;
    }

    private List<Link> getGetByIdLinks(Integer id) throws ServiceException {
        List<Link> result = new ArrayList<>();
        Link selfLink = linkTo(methodOn(UserController.class).getById(id)).withSelfRel();
        Link root = linkTo(methodOn(UserController.class).getAll("1", "20")).withRel("all");
        result.add(selfLink);
        result.add(root);
        return result;
    }

    private List<Link> getCreateLinks(Integer id) throws ServiceException {
        List<Link> result = new ArrayList<>();
        Link getById = linkTo(methodOn(UserController.class).getById(id)).withSelfRel();
        result.add(getById);
        return result;
    }
}
