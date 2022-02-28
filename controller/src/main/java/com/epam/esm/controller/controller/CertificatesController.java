package com.epam.esm.controller.controller;

import com.epam.esm.controller.exceptionhandler.ExceptionHandlerSupport;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<List<GiftCertificateDto>> getAll(
            @RequestParam(value = "tagName", required = false) String tagName,
            @RequestParam(value = "namePart", required = false ) String namePart,
            @RequestParam(value = "descriptionPart", required = false ) String descriptionPart,
            @RequestParam(value = "sortBy", required = false ) String sortBy) throws ServiceException {

        boolean sortByName = false;
        boolean sortByDate = false;
        boolean ascendingBoolean = true;

        if (Objects.nonNull(sortBy)){
            ascendingBoolean = !sortBy.startsWith("-");
            if (sortBy.startsWith("-") || sortBy.startsWith("+")){
                sortBy = sortBy.substring(1).toLowerCase();
            }
            List<String> names = Stream.of(sortBy.split(",")).map(String::trim).toList();
            sortByName = names.contains("name");
            sortByDate = names.contains("date");
        }

        List<GiftCertificateDto> giftCertificateDtos;
        if ((Objects.isNull(tagName)
                && Objects.isNull(namePart)
                && Objects.isNull(descriptionPart)
                && Objects.isNull(sortBy))){
            giftCertificateDtos = service.getAll();
        } else {
            giftCertificateDtos = service.getAllWithParameters(tagName, namePart, descriptionPart, sortByName, sortByDate, ascendingBoolean);
        }
        return new ResponseEntity<>(giftCertificateDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GiftCertificateDto> getById(@PathVariable("id") Integer id) throws ServiceException {
        GiftCertificateDto giftCertificateDto = service.getById(id);

        return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
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
