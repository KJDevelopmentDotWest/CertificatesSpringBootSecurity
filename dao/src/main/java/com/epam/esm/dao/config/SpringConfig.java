package com.epam.esm.dao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Configuration class for spring jdbc template
 */

@Configuration
@Component
@PropertySource("classpath:jdbcconfig.properties")
public class SpringConfig {

    @Autowired
    Environment environment;

    @Bean(name = "dataSource")
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(environment.getProperty("dbdriver"));
        dataSource.setUrl(environment.getProperty("dburl"));
        dataSource.setUsername(environment.getProperty("dbuser"));
        dataSource.setPassword(environment.getProperty("dbpass"));

        return dataSource;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }
}
