package com.epam.esm.controller.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService service;

    @GetMapping()
    public ResponseEntity<CollectionModel<TagDto>> getAll(@RequestParam(value = "page", defaultValue = "1") String page,
                                          @RequestParam(value = "pageSize", defaultValue = "10") String pageSize) throws ServiceException {
        Link selfLink = linkTo(methodOn(TagController.class).getAll( "1", pageSize)).withSelfRel();
        return new ResponseEntity<>(CollectionModel.of(service.getAll(page, pageSize), selfLink), HttpStatus.OK);
    }

    @GetMapping("/most-popular")
    public ResponseEntity<EntityModel<TagDto>> getMostWidelyUsedTagForUserWithHighestCostOfAllOrders() {
        Link selfLink = linkTo(methodOn(TagController.class).getMostWidelyUsedTagForUserWithHighestCostOfAllOrders()).withSelfRel();
        return new ResponseEntity<>(EntityModel.of(service.getMostWidelyUsedTagForUserWithHighestCostOfAllOrders(), selfLink), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<TagDto>> getById(@PathVariable("id") Integer id) throws ServiceException {
        Link selfLink = linkTo(methodOn(TagController.class).getById(id)).withSelfRel();
        Link delete = linkTo(methodOn(TagController.class).delete(id)).withRel("delete");
        Link root = linkTo(methodOn(TagController.class).getAll("1", "10")).withRel("all");
        return new ResponseEntity<>(EntityModel.of(service.getById(id), selfLink, delete, root), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id) throws ServiceException {
        Link create = linkTo(methodOn(TagController.class).create(null)).withRel("create");
        Link root = linkTo(methodOn(TagController.class).getAll("1", "10")).withRel("all");
        service.delete(id);
        return new ResponseEntity<>(EntityModel.of(create, root), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<EntityModel<TagDto>> create(@RequestBody TagDto tagDto) throws ServiceException {
        TagDto createdDto = service.create(tagDto);
        HttpHeaders headers = new HttpHeaders();
        Link getById = linkTo(methodOn(TagController.class).getById(createdDto.getId())).withRel("getById");
        headers.add("Location", getById.getHref());
        return new ResponseEntity<>(EntityModel.of(createdDto, getById), headers, HttpStatus.CREATED);
    }
}
