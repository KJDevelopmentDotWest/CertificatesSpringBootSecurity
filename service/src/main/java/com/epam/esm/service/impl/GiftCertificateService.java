package com.epam.esm.service.impl;

import com.epam.esm.service.api.Service;
import com.epam.esm.service.converter.impl.GiftCertificateConverter;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.exception.ExceptionCode;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import com.epam.esm.dao.impl.GiftCertificateDao;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class GiftCertificateService implements Service<GiftCertificateDto> {

    @Autowired
    GiftCertificateDao dao;

    @Autowired
    GiftCertificateConverter converter;

    @Autowired
    GiftCertificateValidator validator;

    @Override
    public GiftCertificateDto create(GiftCertificateDto value) throws ServiceException {
        validator.validate(value, false);
        GiftCertificate savedGiftCertificate;

        try {
            savedGiftCertificate = dao.saveEntity(converter.convert(value));
        } catch (DataAccessException e){
            throw new ServiceException(e.getMessage(), ExceptionCode.INTERNAL_DB_EXCEPTION);
        }

        if (Objects.isNull(savedGiftCertificate)){
            throw new ServiceException(ExceptionCode.INTERNAL_DB_EXCEPTION);
        }

        return converter.convert(savedGiftCertificate);
    }

    @Override
    public Boolean update(GiftCertificateDto value) throws ServiceException {
        validator.validateIdNotNull(value.getId());

        Boolean result;

        try {
            result = dao.updateEntity(converter.convert(value));
        } catch (DataAccessException e){
            throw new ServiceException(e.getMessage(), ExceptionCode.INTERNAL_DB_EXCEPTION);
        }

        return result;
    }

    @Override
    public Boolean delete(Integer id) throws ServiceException {
        validator.validateIdNotNull(id);
        Boolean result;

        try {
            result = dao.deleteEntity(id);
        } catch (DataAccessException e){
            throw new ServiceException(e.getMessage(), ExceptionCode.INTERNAL_DB_EXCEPTION);
        }

        return result;
    }

    @Override
    public GiftCertificateDto getById(Integer id) throws ServiceException {
        validator.validateIdNotNull(id);
        GiftCertificate result;

        try {
            result = dao.findEntityById(id);
        } catch (DataAccessException e){
            throw new ServiceException(e.getMessage(), ExceptionCode.INTERNAL_DB_EXCEPTION);
        }

        if (Objects.isNull(result)){
            throw new ServiceException(ExceptionCode.THERE_IS_NO_GIFT_CERTIFICATE_WITH_PROVIDED_ID);
        }

        return converter.convert(result);
    }

    @Override
    public List<GiftCertificateDto> getAll() throws ServiceException {
        List<GiftCertificate> daoResult;

        try {
            daoResult = dao.findAllEntities();
        } catch (DataAccessException e){
            throw new ServiceException(e.getMessage(), ExceptionCode.INTERNAL_DB_EXCEPTION);
        }

        return daoResult.stream().map(converter::convert).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * returns filtered list of gift certificates
     * @param tagId id of tag to be searched, null if no need to search by tag
     * @param namePart part of name to be filtered, null if no need to filter by name part
     * @param descriptionPart part of description to be filtered, null if no need to filter description part
     * @param sortByName true for sorting by name, false otherwise
     * @param sortByDescription true for sorting by description, false otherwise
     * @param ascending true for ascending sort, false if descending. ignored if sortByName and sortByDescription is null. true if null or empty
     * @return list of gift certificates that match parameters
     * @throws ServiceException if there is no gift certificates with provided parameters
     */
    public List<GiftCertificateDto> getAllWithParameters(Integer tagId, String namePart, String descriptionPart, Boolean sortByName, Boolean sortByDescription, List<Boolean> ascending) throws ServiceException {
        List<GiftCertificate> daoResult;

        try {
            daoResult = dao.findGiftCertificatesWithParameters(tagId, namePart, descriptionPart, sortByName, sortByDescription, ascending);
        } catch (DataAccessException e){
            throw new ServiceException(e.getMessage(), ExceptionCode.INTERNAL_DB_EXCEPTION);
        }

        return daoResult.stream().map(converter::convert).collect(Collectors.toCollection(ArrayList::new));
    }
}
