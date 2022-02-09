package com.epam.esm.service.impl;

import com.epam.esm.service.api.Service;
import com.epam.esm.service.converter.api.Converter;
import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.expecption.ExceptionCode;
import com.epam.esm.service.expecption.ServiceException;
import com.epam.esm.service.validator.api.Validator;
import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.dao.model.tag.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TagService implements Service<TagDto> {

    @Autowired
    Dao<Tag> dao;

    @Autowired
    Converter<Tag, TagDto> converter;

    @Autowired
    Validator<TagDto> validator;

    /**
     *
     * @param value value to be saved
     * @return saved tag
     * @throws ServiceException if value is invalid, value cannot be created or tag with provided name already exists
     */
    @Override
    public TagDto create(TagDto value) throws ServiceException {
        validator.validate(value, false);

        if (!Objects.isNull(((TagDao)dao).findTagByName(value.getName()))){
            throw new ServiceException(ExceptionCode.TAG_NAME_MUST_BE_UNIQUE);
        }

        Tag savedTag = dao.saveEntity(converter.convert(value));

        if (Objects.isNull(savedTag)){
            throw new ServiceException(ExceptionCode.INTERNAL_DB_EXCEPTION);
        }

        return converter.convert(savedTag);
    }

    /**
     * @throws ServiceException always, update operation is not supported for tags
     */
    @Override
    public Boolean update(TagDto value) throws ServiceException {
        throw new ServiceException(ExceptionCode.OPERATION_IS_NOT_SUPPORTED);
    }

    @Override
    public Boolean delete(Integer id) throws ServiceException {
        validator.validateIdNotNull(id);
        return dao.deleteEntity(id);
    }

    @Override
    public TagDto getById(Integer id) throws ServiceException {
        validator.validateIdNotNull(id);

        Tag daoResult = dao.findEntityById(id);

        if (Objects.isNull(daoResult)){
            throw new ServiceException(ExceptionCode.THERE_IS_NO_TAG_WITH_PROVIDED_ID);
        }

        return converter.convert(daoResult);
    }

    @Override
    public List<TagDto> getAll() throws ServiceException {
        List<Tag> daoResult = dao.findAllEntities();
        return daoResult.stream().map(converter::convert).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     *
     * @param name name to be searched
     * @return tag with provided name
     * @throws ServiceException if there is no tag with provided name or if name is null
     */
    public TagDto getByName(String name) throws ServiceException{
        if (Objects.isNull(name)){
            throw new ServiceException(ExceptionCode.TAG_NAME_CANNOT_BE_NULL);
        }

        Tag daoResult = ((TagDao)dao).findTagByName(name);

        if (Objects.isNull(daoResult)){
            throw new ServiceException(ExceptionCode.THERE_IS_NO_TAG_WITH_PROVIDED_NAME);
        }

        return converter.convert(daoResult);
    }
}
