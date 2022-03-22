package com.epam.esm.dao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Configuration class for spring jdbc template
 */

@Configuration
public class SpringConfig {

    @Bean
    public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor (){
        return new PersistenceAnnotationBeanPostProcessor();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory (){
        return Persistence.createEntityManagerFactory("Certificates");
    }
}
