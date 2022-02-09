package com.epam.esm.controller.controller;

import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.expecption.ServiceException;
import com.epam.esm.service.impl.GiftCertificateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificates")
public class CertificatesController {

    @Autowired
    private GiftCertificateService service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(produces = "application/json")
    public String getAll() throws ServiceException, JsonProcessingException {
        return objectMapper.writeValueAsString(service.getAll());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public String getById(@PathVariable("id") Integer id) throws ServiceException, JsonProcessingException {
        return objectMapper.writeValueAsString(service.getById(id));
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer id) throws ServiceException {
        service.delete(id);
    }

    @PostMapping()
    public void create(@RequestParam String giftCertificateJson) throws ServiceException, JsonProcessingException {
        GiftCertificateDto giftCertificate = objectMapper.readValue(giftCertificateJson, GiftCertificateDto.class);
        service.create(giftCertificate);
    }

    @PutMapping(value = "/{id}")
    public void update(@PathVariable("id") Integer id, @RequestParam String giftCertificateJson) throws ServiceException, JsonProcessingException {
        GiftCertificateDto giftCertificate = objectMapper.readValue(giftCertificateJson, GiftCertificateDto.class);
        giftCertificate.setId(id);
        service.update(giftCertificate);
    }
}
