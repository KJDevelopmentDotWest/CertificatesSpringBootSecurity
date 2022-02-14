package com.epam.esm.service;

import com.epam.esm.dao.impl.GiftCertificateDao;
import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.service.converter.impl.GiftCertificateConverter;
import com.epam.esm.service.converter.impl.TagConverter;
import com.epam.esm.service.impl.GiftCertificateService;
import com.epam.esm.service.impl.TagService;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import com.epam.esm.service.validator.impl.TagValidator;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class ServiceTestConfig {

    @Bean
    public GiftCertificateService giftCertificateService(){
        return new GiftCertificateService();
    }

    @Bean
    public TagService tagService(){
        return new TagService();
    }

    @Bean
    public GiftCertificateValidator giftCertificateValidator(){
        return new GiftCertificateValidator();
    }

    @Bean
    public TagValidator tagValidator(){
        return new TagValidator();
    }

    @Bean
    public GiftCertificateConverter giftCertificateConverter(){
        return new GiftCertificateConverter();
    }

    @Bean
    public TagConverter tagConverter(){
        return new TagConverter();
    }

    @Bean
    public GiftCertificateDao giftCertificateDao(){
        return Mockito.mock(GiftCertificateDao.class);
    }

    @Bean
    public TagDao tagDao(){
        return Mockito.mock(TagDao.class);
    }

    @Bean(name = "dataSource")
    public DataSource dataSource(){
        return new DriverManagerDataSource();
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }
}
