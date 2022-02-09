package com.epam.esm.service.api;

import com.epam.esm.service.dto.Dto;
import com.epam.esm.service.expecption.ServiceException;

import java.util.List;

/**
 *
 * @param <T> abstract dto that class will operate
 */
public interface Service<T extends Dto> {
    /**
     * @param value value to be saved
     * @return saved value
     * @throws ServiceException if value is invalid or value cannot be created
     */
    T create(T value) throws ServiceException;

    /**
     * @param value value to be updated
     * @return true if value updated successfully, false otherwise
     * @throws ServiceException if value is invalid
     */
    Boolean update(T value) throws ServiceException;

    /**
     * @param id of value to be deleted
     * @return true if value deleted successfully, false otherwise
     * @throws ServiceException if value is invalid
     */
    Boolean delete(Integer id) throws ServiceException;

    /**
     * @param id value id
     * @return value with id == value.id
     * @throws ServiceException if there is no value with provided id or id is null
     */
    T getById(Integer id) throws ServiceException;

    /**
     *
     * @return list of values
     * @throws ServiceException if database is empty
     */
    List<T> getAll() throws ServiceException;
}
