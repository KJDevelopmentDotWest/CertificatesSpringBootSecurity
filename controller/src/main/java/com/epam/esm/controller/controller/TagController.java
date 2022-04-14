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

import java.util.ArrayList;
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
                                          @RequestParam(value = "pageSize", defaultValue = "20") String pageSize) throws ServiceException {
        return new ResponseEntity<>(CollectionModel.of(service.getAll(page, pageSize), getGetAllLinks()), HttpStatus.OK);
    }

    @GetMapping("/most-popular")
    public ResponseEntity<EntityModel<TagDto>> getMostWidelyUsedTagForUserWithHighestCostOfAllOrders() {
        return new ResponseEntity<>(EntityModel.of(service.getMostWidelyUsedTagForUserWithHighestCostOfAllOrders(), getGetMostWidelyUsedTagForUserWithHighestCostOfAllOrdersLinks()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<TagDto>> getById(@PathVariable("id") Integer id) throws ServiceException {
        return new ResponseEntity<>(EntityModel.of(service.getById(id), getGetByIdLinks(id)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id) throws ServiceException {
        service.delete(id);
        return new ResponseEntity<>(CollectionModel.of(getDeleteLinks()), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<EntityModel<TagDto>> create(@RequestBody TagDto tagDto) throws ServiceException {
        TagDto createdDto = service.create(tagDto);
        HttpHeaders headers = new HttpHeaders();
        List<Link> links = getCreateLinks(createdDto.getId());
        headers.add("Location", links.get(0).getHref());
        return new ResponseEntity<>(EntityModel.of(createdDto, links), headers, HttpStatus.CREATED);
    }

    private List<Link> getGetAllLinks() throws ServiceException {
        List<Link> result = new ArrayList<>();
        Link selfLink = linkTo(methodOn(TagController.class).getAll( "1", "20")).withSelfRel();
        result.add(selfLink);
        return result;
    }

    private List<Link> getGetByIdLinks(Integer id) throws ServiceException {
        List<Link> result = new ArrayList<>();
        Link selfLink = linkTo(methodOn(TagController.class).getById(id)).withSelfRel();
        Link delete = linkTo(methodOn(TagController.class).delete(id)).withRel("delete");
        Link root = linkTo(methodOn(TagController.class).getAll("1", "20")).withRel("all");
        result.add(selfLink);
        result.add(delete);
        result.add(root);
        return result;
    }

    private List<Link> getDeleteLinks() throws ServiceException {
        List<Link> result = new ArrayList<>();
        Link create = linkTo(methodOn(TagController.class).create(null)).withRel("create");
        Link root = linkTo(methodOn(TagController.class).getAll("1", "20")).withRel("all");
        result.add(create);
        result.add(root);
        return result;
    }

    private List<Link> getCreateLinks(Integer id) throws ServiceException {
        List<Link> result = new ArrayList<>();
        Link getById = linkTo(methodOn(TagController.class).getById(id)).withRel("getById");
        result.add(getById);
        return result;
    }

    private List<Link> getGetMostWidelyUsedTagForUserWithHighestCostOfAllOrdersLinks(){
        List<Link> result = new ArrayList<>();
        Link selfLink = linkTo(methodOn(TagController.class).getMostWidelyUsedTagForUserWithHighestCostOfAllOrders()).withSelfRel();
        result.add(selfLink);
        return result;
    }
}
