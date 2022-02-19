package com.epam.esm.dao;

import com.epam.esm.dao.impl.GiftCertificateDao;
import com.epam.esm.dao.impl.TagDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class TestConfig {

    @Bean
    public GiftCertificateDao giftCertificateDao(){
        return new GiftCertificateDao();
    }

    @Bean
    public TagDao tagDao(){
        return new TagDao();
    }

    @Bean(name = "dataSource")
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        dataSource.setUrl("jdbc:derby:embedded;create=true");

        return dataSource;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }

}