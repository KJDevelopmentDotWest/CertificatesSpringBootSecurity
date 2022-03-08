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

/**
 * Service interface implementation for GiftCertificateDto with ability to perform CRUD operations
 */

@Component
public class GiftCertificateService implements Service<GiftCertificateDto> {

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

        try {
            GiftCertificate daoResult = dao.updateEntity(converter.convert(value));
            if (!Objects.isNull(daoResult)){
                result = converter.convert(daoResult);
            } else {
                result = null;
            }
        } catch (DataAccessException e){
            logger.error(e);
            throw new ServiceException(e.getMessage(), ExceptionCode.INTERNAL_DB_EXCEPTION, ExceptionMessage.INTERNAL_DB_EXCEPTION);
        }

        return result;
    }

    @Override
    public Boolean delete(Integer id) throws ServiceException {
        validator.validateIdNotNullAndPositive(id);
        Boolean result;

        try {
            result = dao.deleteEntity(id);
        } catch (DataAccessException e){
            logger.error(e);
            throw new ServiceException(e.getMessage(), ExceptionCode.INTERNAL_DB_EXCEPTION, ExceptionMessage.INTERNAL_DB_EXCEPTION);
        }

        return result;
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
     * @return list of gift certificates by page number
     */
    public List<GiftCertificateDto> getAll(Integer pageNumber) throws ServiceException {
        List<GiftCertificate> daoResult;

        if (pageNumber < 1){
            pageNumber = 1;
        }

        daoResult = dao.findAllEntities(pageNumber);

        return daoResult.stream().map(converter::convert).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * returns filtered list of gift certificates
     * @param tagNames names of tag to be searched, empty list if no need to search by tag
     * @param namePart part of name to be filtered, null if no need to filter by name part
     * @param descriptionPart part of description to be filtered, null if no need to filter description part
     * @param orderByName true for ordering by name, false otherwise
     * @param orderByDate true for ordering by date, false otherwise
     * @param ascending true for ascending order, false if descending. ignored if orderByName and orderByDate is null. true if null
     * @param pageNumber number of page
     * @return list of gift certificates that match parameters
     * @throws ServiceException if database error occurred
     */
    public List<GiftCertificateDto> getAllWithParameters(List<String> tagNames, String namePart, String descriptionPart, Boolean orderByName, Boolean orderByDate, Boolean ascending, Integer pageNumber) throws ServiceException {
        List<GiftCertificate> daoResult;

        if (pageNumber < 1) {
            pageNumber = 1;
        }

        try {
            daoResult = dao.findGiftCertificatesWithParameters(tagNames, namePart, descriptionPart, orderByName, orderByDate, ascending, pageNumber);
        } catch (DataAccessException e){
            logger.error(e);
            throw new ServiceException(e.getMessage(), ExceptionCode.INTERNAL_DB_EXCEPTION, ExceptionMessage.INTERNAL_DB_EXCEPTION);
        }

        return daoResult.stream().map(converter::convert).collect(Collectors.toCollection(ArrayList::new));
    }
}
