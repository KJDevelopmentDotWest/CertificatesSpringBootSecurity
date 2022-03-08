package com.epam.esm.service.impl;

import com.epam.esm.service.api.Service;
import com.epam.esm.service.converter.impl.TagConverter;
import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.exception.ExceptionCode;
import com.epam.esm.service.exception.ExceptionMessage;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.dao.model.tag.Tag;
import com.epam.esm.service.validator.impl.TagValidator;
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
 * Service interface implementation for TagDto with ability to perform CRD operations
 */

@Component
public class TagService implements Service<TagDto> {

    @Autowired
    private TagDao dao;

    @Autowired
    private TagConverter converter;

    @Autowired
    private TagValidator validator;

    private static final Logger logger = LogManager.getLogger(TagService.class);

    /**
     * validates value and sends save request to tag dao class
     * @param value value to be saved
     * @return saved tag
     * @throws ServiceException if value is invalid, value cannot be created or tag with provided name already exists
     */
    @Override
    public TagDto create(TagDto value) throws ServiceException {
        validator.validate(value, false);

        if (!Objects.isNull(dao.findTagByName(value.getName()))){
            throw new ServiceException(ExceptionCode.VALIDATION_FAILED_EXCEPTION, ExceptionMessage.TAG_NAME_MUST_BE_UNIQUE);
        }

        Tag savedTag = dao.saveEntity(converter.convert(value));

        if (Objects.isNull(savedTag)){
            throw new ServiceException(ExceptionCode.INTERNAL_DB_EXCEPTION, ExceptionMessage.INTERNAL_DB_EXCEPTION);
        }

        return converter.convert(savedTag);
    }

    /**
     * update operation is not supported for tags
     * @throws ServiceException always, update operation is not supported for tags
     */
    @Override
    public TagDto update(TagDto value) throws ServiceException {
        throw new ServiceException(ExceptionCode.OPERATION_IS_NOT_SUPPORTED);
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
    public TagDto getById(Integer id) throws ServiceException {
        validator.validateIdNotNullAndPositive(id);

        Tag daoResult;

        try {
            daoResult = dao.findEntityById(id);
        } catch (DataAccessException e){
            logger.error(e);
            throw new ServiceException(e.getMessage(), ExceptionCode.INTERNAL_DB_EXCEPTION, ExceptionMessage.INTERNAL_DB_EXCEPTION);
        }

        if (Objects.isNull(daoResult)){
            throw new ServiceException(ExceptionCode.ENTITY_NOT_FOUND, ExceptionMessage.THERE_IS_NO_TAG_WITH_PROVIDED_ID);
        }

        return converter.convert(daoResult);
    }

    @Override
    public List<TagDto> getAll() throws ServiceException {
        List<Tag> daoResult;

        try {
            daoResult = dao.findAllEntities();
        } catch (DataAccessException e){
            throw new ServiceException(e.getMessage(), ExceptionCode.INTERNAL_DB_EXCEPTION, ExceptionMessage.INTERNAL_DB_EXCEPTION);
        }

        return daoResult.stream().map(converter::convert).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * returns list of tags by page number
     * @param pageNumber number of page
     * @return list of gift certificates by page number
     */
    public List<TagDto> getAll(Integer pageNumber) throws ServiceException {
        List<Tag> daoResult;

        if (pageNumber < 1){
            pageNumber = 1;
        }

        try {
            daoResult = dao.findAllEntities(pageNumber);
        } catch (DataAccessException e){
            throw new ServiceException(e.getMessage(), ExceptionCode.INTERNAL_DB_EXCEPTION, ExceptionMessage.INTERNAL_DB_EXCEPTION);
        }

        return daoResult.stream().map(converter::convert).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * returns tag with provided
     * @param name name to be searched
     * @return tag with provided name
     * @throws ServiceException if there is no tag with provided name, name is null, or database error occurred
     */
    public TagDto getByName(String name) throws ServiceException{
        if (Objects.isNull(name)){
            throw new ServiceException(ExceptionCode.VALIDATION_FAILED_EXCEPTION, ExceptionMessage.TAG_NAME_CANNOT_BE_NULL);
        }

        Tag daoResult;

        try {
            daoResult = dao.findTagByName(name);
        } catch (DataAccessException e){
            logger.error(e);
            throw new ServiceException(e.getMessage(), ExceptionCode.INTERNAL_DB_EXCEPTION, ExceptionMessage.INTERNAL_DB_EXCEPTION);
        }

        if (Objects.isNull(daoResult)){
            throw new ServiceException(ExceptionCode.ENTITY_NOT_FOUND, ExceptionMessage.THERE_IS_NO_TAG_WITH_PROVIDED_NAME);
        }

        return converter.convert(daoResult);
    }

    /**
     * returns most widely used tag for user with the highest cost of all orders
     * @return most widely used tag for user with the highest cost of all orders
     */
    public TagDto getMostWidelyUsedTagForUserWithHighestCostOfAllOrders(){
        return converter.convert(dao.findMostWidelyUsedTagForUserWithHighestCostOfAllOrders());
    }
}
