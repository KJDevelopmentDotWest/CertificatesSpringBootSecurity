package com.epam.esm.service.impl;

import com.epam.esm.service.api.Service;
import com.epam.esm.service.converter.impl.GiftCertificateConverter;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.exception.ExceptionCode;
import com.epam.esm.service.exception.ExceptionMessage;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import com.epam.esm.dao.impl.GiftCertificateDao;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service interface implementation for GiftCertificateDto with ability to perform CRUD operations
 */

@Component
public class GiftCertificateService extends Service<GiftCertificateDto> {

    @Autowired
    private GiftCertificateDao dao;

    @Autowired
    private GiftCertificateConverter converter;

    @Autowired
    private GiftCertificateValidator validator;

    private static final Logger logger = LogManager.getLogger(GiftCertificateService.class);

    @Override
    public GiftCertificateDto create(GiftCertificateDto value) throws ServiceException {
        validator.validate(value, false);
        GiftCertificate savedGiftCertificate = dao.saveEntity(converter.convert(value));

        if (Objects.isNull(savedGiftCertificate)){
            throw new ServiceException(ExceptionCode.INTERNAL_DB_EXCEPTION, ExceptionMessage.INTERNAL_DB_EXCEPTION);
        }

        return converter.convert(savedGiftCertificate);
    }

    @Override
    public GiftCertificateDto update(GiftCertificateDto value) throws ServiceException {
        validator.validate(value, true, true);

        GiftCertificateDto result;
        GiftCertificate daoResult = dao.updateEntity(converter.convert(value));
        if (!Objects.isNull(daoResult)){
            result = converter.convert(daoResult);
        } else {
            throw new ServiceException(ExceptionCode.ENTITY_NOT_FOUND, ExceptionMessage.THERE_IS_NO_GIFT_CERTIFICATE_WITH_PROVIDED_ID);
        }

        return result;
    }

    @Override
    public Boolean delete(Integer id) throws ServiceException {
        validator.validateIdNotNullAndPositive(id);
        return dao.deleteEntity(id);
    }

    @Override
    public GiftCertificateDto getById(Integer id) throws ServiceException {
        validator.validateIdNotNullAndPositive(id);
        GiftCertificate result;

        result = dao.findEntityById(id);

        if (Objects.isNull(result)){
            throw new ServiceException(ExceptionCode.ENTITY_NOT_FOUND, ExceptionMessage.THERE_IS_NO_GIFT_CERTIFICATE_WITH_PROVIDED_ID);
        }

        return converter.convert(result);
    }

    @Override
    public List<GiftCertificateDto> getAll() throws ServiceException {
        List<GiftCertificate> daoResult;

        daoResult = dao.findAllEntities();

        return daoResult.stream().map(converter::convert).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * returns list of gift certificates by page number
     * @param pageNumber number of page
     * @param pageSize size of page
     * @return list of gift certificates by page number
     */
    public List<GiftCertificateDto> getAll(String pageNumber, String pageSize) throws ServiceException {
        List<GiftCertificate> daoResult;

        int pageNumberInteger;
        int pageSizeInteger;
        pageNumberInteger = parsePageNumber(pageNumber);
        pageSizeInteger = parsePageSize(pageSize);

        daoResult = dao.findAllEntities(pageNumberInteger, pageSizeInteger);

        return daoResult.stream().map(converter::convert).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * returns filtered list of gift certificates
     * @param tagNames names of tag to be searched, null list if no need to search by tag
     * @param searchPart part of name or description to be filtered, null if no need to filter by name or description part
     * @param orderBy order by string, null if no need to order
     * @param pageNumber number of page
     * @param pageSize size of page
     * @return list of gift certificates that match parameters
     * @throws ServiceException if database error occurred
     */
    public List<GiftCertificateDto> getAllWithParameters(String tagNames, String searchPart, String orderBy, String pageNumber, String pageSize) throws ServiceException {
        List<GiftCertificate> daoResult;

        boolean orderByName = false;
        boolean orderByDate = false;
        boolean ascendingBoolean = true;
        List<String> tagNamesList = new ArrayList<>();
        int pageNumberInteger;
        int pageSizeInteger;

        if (Objects.nonNull(orderBy)){
            ascendingBoolean = !orderBy.startsWith("-");
            if (orderBy.startsWith("-") || orderBy.startsWith("+")){
                orderBy = orderBy.substring(1).toLowerCase();
            }
            List<String> names = Stream.of(orderBy.split(",")).map(String::trim).toList();
            orderByName = names.contains("name");
            orderByDate = names.contains("date");
        }

        if (Objects.nonNull(tagNames)){
            tagNamesList.addAll(List.of(tagNames.split(",")));
        }

        pageNumberInteger = parsePageNumber(pageNumber);
        pageSizeInteger = parsePageSize(pageSize);

        try {
            daoResult = dao.findGiftCertificatesWithParameters(tagNamesList, searchPart, orderByName, orderByDate, ascendingBoolean, pageNumberInteger, pageSizeInteger);
        } catch (DataAccessException e){
            logger.error(e);
            throw new ServiceException(e.getMessage(), ExceptionCode.INTERNAL_DB_EXCEPTION, ExceptionMessage.INTERNAL_DB_EXCEPTION);
        }

        return daoResult.stream().map(converter::convert).collect(Collectors.toCollection(ArrayList::new));
    }
}
