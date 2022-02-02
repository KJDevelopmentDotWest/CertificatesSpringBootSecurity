package com.epam.ems.service.impl;

import com.epam.ems.service.api.Service;
import com.epam.ems.service.converter.api.Converter;
import com.epam.ems.service.converter.impl.TagConverter;
import com.epam.ems.service.dto.tag.TagDto;
import com.epam.ems.service.expecption.ServiceException;
import com.epam.ems.service.validator.api.Validator;
import com.epam.ems.service.validator.impl.TagValidator;
import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.dao.model.tag.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TagService implements Service<TagDto> {

    Dao<Tag> dao = new TagDao();
    Converter<Tag, TagDto> converter = new TagConverter();
    Validator<TagDto> validator = new TagValidator();

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
            throw new ServiceException("tag with provided name already exists");
        }

        Tag savedTag = dao.saveEntity(converter.convert(value));

        if (Objects.isNull(savedTag)){
            throw new ServiceException("Tag wasn't created because of internal error");
        }

        return converter.convert(savedTag);
    }

    /**
     * @throws ServiceException always, update operation is not supported for tags
     */
    @Override
    public Boolean update(TagDto value) throws ServiceException {
        throw new ServiceException("update operation is not supported for tags");
    }

    @Override
    public Boolean delete(TagDto value) throws ServiceException {
        validator.validate(value, true);
        return dao.deleteEntity(converter.convert(value));
    }

    @Override
    public TagDto getById(Integer id) throws ServiceException {
        validator.validateIdNotNull(id);

        Tag daoResult = dao.findEntityById(id);

        if (Objects.isNull(daoResult)){
            throw new ServiceException("there is no tag with provided id");
        }

        return converter.convert(daoResult);
    }

    @Override
    public List<TagDto> getAll() throws ServiceException {
        List<Tag> daoResult = dao.findAllEntities();

        if (daoResult.isEmpty()){
            throw new ServiceException("repository is empty or cant be accessed");
        }

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
            throw new ServiceException("name cannot be null");
        }

        Tag daoResult = ((TagDao)dao).findTagByName(name);

        if (Objects.isNull(daoResult)){
            throw new ServiceException("there is no tag with provided name");
        }

        return converter.convert(daoResult);
    }
}
