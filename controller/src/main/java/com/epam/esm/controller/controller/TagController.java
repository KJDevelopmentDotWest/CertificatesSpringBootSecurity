package com.epam.esm.controller.controller;

import com.epam.esm.controller.exceptionhandler.ExceptionHandlerSupport;
import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService service;

    @Autowired
    private ExceptionHandlerSupport exceptionHandlerSupport;

    @GetMapping()
    public CollectionModel<TagDto> getAll(@RequestParam(value = "page", defaultValue = "1") String page) throws ServiceException {
        int pageNumberInteger;

        try {
            pageNumberInteger = Integer.parseInt(page);
        } catch (NumberFormatException e){
            pageNumberInteger = 1;
        }

        Link selfLink = linkTo(methodOn(TagController.class).getAll( "1")).withSelfRel();

        return CollectionModel.of(service.getAll(pageNumberInteger), selfLink);
    }

    @GetMapping("/most-popular")
    public EntityModel<TagDto> getMostWidelyUsedTagForUserWithHighestCostOfAllOrders() {
        Link selfLink = linkTo(methodOn(TagController.class).getMostWidelyUsedTagForUserWithHighestCostOfAllOrders()).withSelfRel();
        return EntityModel.of(service.getMostWidelyUsedTagForUserWithHighestCostOfAllOrders(), selfLink);
    }

    @GetMapping(value = "/{id}")
    public EntityModel<TagDto> getById(@PathVariable("id") Integer id) throws ServiceException {
        Link selfLink = linkTo(methodOn(TagController.class).getById(id)).withSelfRel();
        Link create = linkTo(methodOn(TagController.class).create(null)).withRel("create");
        Link update = linkTo(methodOn(TagController.class).update(id, null)).withRel("update");
        Link delete = linkTo(methodOn(TagController.class).delete(id)).withRel("delete");
        Link root = linkTo(methodOn(TagController.class).getAll("1")).withRel("all");
        return EntityModel.of(service.getById(id), selfLink, create, update, delete, root);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id) throws ServiceException {
        return new ResponseEntity<>(service.delete(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<TagDto> create(@RequestBody TagDto tagDto) throws ServiceException {
        TagDto createdDto = service.create(tagDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", createdDto.getId().toString());
        return new ResponseEntity<>(createdDto, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TagDto> update(@PathVariable("id") Integer id, @RequestBody TagDto tagDto) throws ServiceException {
        tagDto.setId(id);
        return new ResponseEntity<>(service.update(tagDto), HttpStatus.OK);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception exception, HttpServletRequest request){
        RequestContext requestContext = new RequestContext(request);
        return exceptionHandlerSupport.handleException(exception, requestContext.getLocale());
    }
}
