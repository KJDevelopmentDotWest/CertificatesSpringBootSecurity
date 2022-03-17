package com.epam.esm.controller.application;

import com.epam.esm.service.impl.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(value = "com.epam.esm")
@PropertySource(value = {"/application.properties"})
@EntityScan(value = "com.epam.esm.dao.model")
public class Application {

    @Autowired
    TagService service;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
