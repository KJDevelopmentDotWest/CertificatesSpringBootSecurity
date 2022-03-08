package com.epam.esm.controller.controller;

import com.epam.esm.controller.exceptionhandler.ExceptionHandlerSupport;
import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private ExceptionHandlerSupport exceptionHandlerSupport;

    @GetMapping()
    public CollectionModel<UserDto> getAll(@RequestParam(value = "page", defaultValue = "1") String page) throws ServiceException {
        int pageNumberInteger;

        try {
            pageNumberInteger = Integer.parseInt(page);
        } catch (NumberFormatException e){
            pageNumberInteger = 1;
        }
        Link selfLink = linkTo(methodOn(UserController.class).getAll("1")).withRel("all");
        return CollectionModel.of(service.getAll(pageNumberInteger), selfLink);
    }

    @GetMapping(value = "/{id}")
    public EntityModel<UserDto> getById(@PathVariable("id") Integer id) throws ServiceException {
        Link selfLink = linkTo(methodOn(UserController.class).getById(id)).withSelfRel();
        Link root = linkTo(methodOn(UserController.class).getAll("1")).withRel("all");
        return EntityModel.of(service.getById(id), selfLink, root);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception exception, HttpServletRequest request){
        RequestContext requestContext = new RequestContext(request);
        return exceptionHandlerSupport.handleException(exception, requestContext.getLocale());
    }
}
