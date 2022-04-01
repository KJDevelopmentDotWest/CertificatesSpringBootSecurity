package com.epam.esm.controller.controller;

import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.GiftCertificateService;
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
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/certificates")
public class CertificatesController {

    @Autowired
    private GiftCertificateService service;

    @GetMapping()
    public ResponseEntity<CollectionModel<GiftCertificateDto>> getAll(
            @RequestParam(value = "tagName", required = false) String tagName,
            @RequestParam(value = "searchPart", required = false ) String searchPart,
            @RequestParam(value = "orderBy", required = false ) String orderBy,
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "pageSize", defaultValue = "20") String pageSize) throws ServiceException {

        List<GiftCertificateDto> giftCertificateDtos;
        if ((Objects.isNull(tagName)
                && Objects.isNull(searchPart)
                && Objects.isNull(orderBy))){
            giftCertificateDtos = service.getAll(page, pageSize);
        } else {
            giftCertificateDtos = service.getAllWithParameters(tagName, searchPart, orderBy, page, pageSize);
        }
        return new ResponseEntity<>(CollectionModel.of(giftCertificateDtos, getGetAllLinks()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> getById(@PathVariable("id") Integer id) throws ServiceException {
        GiftCertificateDto giftCertificateDto = service.getById(id);
        return new ResponseEntity<>(EntityModel.of(giftCertificateDto, getGetByIdLinks(id)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<EntityModel<Object>> delete(@PathVariable("id") Integer id) throws ServiceException {
        service.delete(id);
        return new ResponseEntity<>(EntityModel.of(getDeleteLinks()), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<EntityModel<GiftCertificateDto>> create(@RequestBody GiftCertificateDto giftCertificateDto) throws ServiceException {
        GiftCertificateDto createdDto = service.create(giftCertificateDto);
        List<Link> links = getCreateLinks(createdDto.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", links.get(0).getHref());
        return new ResponseEntity<>(EntityModel.of(createdDto, links), headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> update(@PathVariable("id") Integer id, @RequestBody GiftCertificateDto giftCertificateDto) throws ServiceException {
        giftCertificateDto.setId(id);
        return new ResponseEntity<>(EntityModel.of(service.update(giftCertificateDto), getUpdateLinks(id)), HttpStatus.OK);
    }

    private List<Link> getGetAllLinks() throws ServiceException {
        List<Link> result = new ArrayList<>();
        Link selfLink = linkTo(methodOn(CertificatesController.class).getAll(null, null, null, "1", "20")).withSelfRel();
        result.add(selfLink);
        return result;
    }

    private List<Link> getGetByIdLinks(Integer id) throws ServiceException {
        List<Link> result = new ArrayList<>();
        Link selfLink = linkTo(methodOn(CertificatesController.class).getById(id)).withSelfRel();
        Link update = linkTo(methodOn(CertificatesController.class).update(id, null)).withRel("update");
        Link delete = linkTo(methodOn(CertificatesController.class).delete(id)).withRel("delete");
        Link root = linkTo(methodOn(CertificatesController.class).getAll(null, null, null, null, null)).withRel("all");
        result.add(selfLink);
        result.add(update);
        result.add(delete);
        result.add(root);
        return result;
    }

    private List<Link> getDeleteLinks() throws ServiceException {
        List<Link> result = new ArrayList<>();
        Link create = linkTo(methodOn(CertificatesController.class).create(null)).withRel("create");
        Link root = linkTo(methodOn(CertificatesController.class).getAll(null, null, null, "1", "20")).withRel("all");
        result.add(create);
        result.add(root);
        return result;
    }

    private List<Link> getCreateLinks(Integer id) throws ServiceException {
        List<Link> result = new ArrayList<>();
        Link getById = linkTo(methodOn(CertificatesController.class).getById(id)).withRel("getById");
        result.add(getById);
        return result;
    }

    private List<Link> getUpdateLinks(Integer id) throws ServiceException {
        List<Link> result = new ArrayList<>();
        Link getById = linkTo(methodOn(CertificatesController.class).getById(id)).withSelfRel();
        result.add(getById);
        return result;
    }
}
