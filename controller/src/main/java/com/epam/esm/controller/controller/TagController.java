package com.epam.esm.controller.controller;

import com.epam.esm.controller.exceptionhandler.ExceptionHandlerSupport;
import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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

import java.util.List;
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService service;

    @Autowired
    private ExceptionHandlerSupport exceptionHandlerSupport;

    @GetMapping()
    public ResponseEntity<List<TagDto>> getAll() throws ServiceException {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TagDto> getById(@PathVariable("id") Integer id) throws ServiceException {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id) throws ServiceException {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody TagDto tagDto) throws ServiceException {
        service.create(tagDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Integer id, @RequestBody TagDto tagDto) throws ServiceException {
        tagDto.setId(id);
        service.update(tagDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<String> handleException(ServiceException serviceException){
        return exceptionHandlerSupport.handleException(serviceException, LocaleContextHolder.getLocale());
    }
}
