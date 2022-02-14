package com.epam.esm.controller.controller;

import com.epam.esm.controller.exceptionhandler.ExceptionHandlerSupport;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.math.NumberUtils;
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

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/certificates")
public class CertificatesController {

    @Autowired
    private GiftCertificateService service;

    @Autowired
    private ExceptionHandlerSupport exceptionHandlerSupport;

    @GetMapping()
    public ResponseEntity<List<GiftCertificateDto>> getAll(
            @RequestParam(value = "tagId", required = false) String tagId,
            @RequestParam(value = "namePart", required = false ) String namePart,
            @RequestParam(value = "descriptionPart", required = false ) String descriptionPart,
            @RequestParam(value = "sortBy", required = false ) String sortBy,
            @RequestParam(value = "ascending", required = false ) String ascending) throws ServiceException {

        Boolean sortByName;
        Boolean sortByDate;
        Boolean ascendingBoolean;
        Integer tagIdInteger = null;

        if (Objects.equals(sortBy, "name")){
            sortByName = true;
            sortByDate = false;
        } else if (Objects.equals(sortBy, "date")){
            sortByName = false;
            sortByDate = true;
        } else if (Objects.equals(sortBy, "name+date")) {
            sortByName = true;
            sortByDate = true;
        } else {
            sortByName = false;
            sortByDate = false;
        }

        ascendingBoolean = !Objects.equals(ascending, "false");
        if (NumberUtils.isParsable(tagId)){
            tagIdInteger = Integer.parseInt(tagId);
        }

        if ((Objects.isNull(tagIdInteger)
                && Objects.isNull(namePart)
                && Objects.isNull(descriptionPart)
                && Objects.isNull(sortBy))){
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(service.getAllWithParameters(tagIdInteger, namePart, descriptionPart, sortByName, sortByDate, ascendingBoolean), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GiftCertificateDto> getById(@PathVariable("id") Integer id) throws ServiceException {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id) throws ServiceException {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody GiftCertificateDto giftCertificateDto) throws ServiceException {
        service.create(giftCertificateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Integer id, @RequestBody GiftCertificateDto giftCertificateDto) throws ServiceException {
        giftCertificateDto.setId(id);
        service.update(giftCertificateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<String> handleException(ServiceException serviceException){
        return exceptionHandlerSupport.handleException(serviceException, LocaleContextHolder.getLocale());
    }
}
