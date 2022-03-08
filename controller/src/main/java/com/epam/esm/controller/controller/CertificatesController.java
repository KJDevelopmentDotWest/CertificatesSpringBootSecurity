package com.epam.esm.controller.controller;

import com.epam.esm.controller.exceptionhandler.ExceptionHandlerSupport;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.RequestContext;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping("/certificates")
public class CertificatesController {

    @Autowired
    private GiftCertificateService service;

    @Autowired
    private ExceptionHandlerSupport exceptionHandlerSupport;

    @GetMapping()
    public CollectionModel<GiftCertificateDto> getAll(
            @RequestParam(value = "tagName", required = false) String tagName,
            @RequestParam(value = "namePart", required = false ) String namePart,
            @RequestParam(value = "descriptionPart", required = false ) String descriptionPart,
            @RequestParam(value = "sortBy", required = false ) String sortBy,
            @RequestParam(value = "page", defaultValue = "1") String page) throws ServiceException {

        boolean sortByName = false;
        boolean sortByDate = false;
        boolean ascendingBoolean = true;
        List<String> tagNames = new ArrayList<>();
        int pageNumberInteger;

        if (Objects.nonNull(sortBy)){
            ascendingBoolean = !sortBy.startsWith("-");
            if (sortBy.startsWith("-") || sortBy.startsWith("+")){
                sortBy = sortBy.substring(1).toLowerCase();
            }
            List<String> names = Stream.of(sortBy.split(",")).map(String::trim).toList();
            sortByName = names.contains("name");
            sortByDate = names.contains("date");
        }

        if (Objects.nonNull(tagName)){
            tagNames.addAll(List.of(tagName.split(",")));
        }

        try {
            pageNumberInteger = Integer.parseInt(page);
        } catch (NumberFormatException e){
            pageNumberInteger = 1;
        }

        List<GiftCertificateDto> giftCertificateDtos;
        if ((Objects.isNull(tagName)
                && Objects.isNull(namePart)
                && Objects.isNull(descriptionPart)
                && Objects.isNull(sortBy))){
            giftCertificateDtos = service.getAll(pageNumberInteger);
        } else {
            giftCertificateDtos = service.getAllWithParameters(tagNames, namePart, descriptionPart, sortByName, sortByDate, ascendingBoolean, pageNumberInteger);
        }
        Link selfLink = linkTo(methodOn(CertificatesController.class).getAll(tagName, namePart, descriptionPart, sortBy, page)).withSelfRel();
        return CollectionModel.of(giftCertificateDtos, selfLink);
    }

    @GetMapping(value = "/{id}")
    public EntityModel<GiftCertificateDto> getById(@PathVariable("id") Integer id) throws ServiceException {
        GiftCertificateDto giftCertificateDto = service.getById(id);

        Link selfLink = linkTo(methodOn(CertificatesController.class).getById(id)).withSelfRel();
        Link create = linkTo(methodOn(CertificatesController.class).create(null)).withRel("create");
        Link update = linkTo(methodOn(CertificatesController.class).update(id, null)).withRel("update");
        Link delete = linkTo(methodOn(CertificatesController.class).delete(id)).withRel("delete");
        Link root = linkTo(methodOn(CertificatesController.class).getAll(null, null, null, null, null)).withRel("all");

        return EntityModel.of(giftCertificateDto, selfLink, create, update, delete, root);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id) throws ServiceException {
        return new ResponseEntity<>(service.delete(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<GiftCertificateDto> create(@RequestBody GiftCertificateDto giftCertificateDto) throws ServiceException {
        GiftCertificateDto createdDto = service.create(giftCertificateDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", createdDto.getId().toString());
        return new ResponseEntity<>(createdDto, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<GiftCertificateDto> update(@PathVariable("id") Integer id, @RequestBody GiftCertificateDto giftCertificateDto) throws ServiceException {
        giftCertificateDto.setId(id);
        return new ResponseEntity<>(service.update(giftCertificateDto), HttpStatus.OK);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception exception, HttpServletRequest request){
        RequestContext requestContext = new RequestContext(request);
        return exceptionHandlerSupport.handleException(exception, requestContext.getLocale());
    }
}
