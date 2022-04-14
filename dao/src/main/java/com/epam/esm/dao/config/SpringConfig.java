package com.epam.esm.dao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Configuration class for spring jdbc template
 */

@Configuration
@EnableTransactionManagement
public class SpringConfig {

    @Bean
    public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor (){
        return new PersistenceAnnotationBeanPostProcessor();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory (){
        return Persistence.createEntityManagerFactory("Certificates");
    }

    @Bean
    public TransactionManager transactionManager (){
        return new JpaTransactionManager();
    }
}
