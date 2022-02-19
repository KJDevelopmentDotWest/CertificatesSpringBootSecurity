package com.epam.esm.service.api;

import com.epam.esm.service.dto.Dto;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

/**
 * Interface for service classes
 * @param <T> abstract dto that class will operate
 */
public interface Service<T extends Dto> {
    /**
     * validates value and sends save request to corresponding dao class
     * @param value value to be saved
     * @return saved value
     * @throws ServiceException if value is invalid, value cannot be created, or database error occurred
     */
    T create(T value) throws ServiceException;

    /**
     * validates value and sends update request to corresponding dao class
     * @param value value to be updated
     * @return value if value updated successfully
     * @throws ServiceException if value is invalid or database error occurred
     */
    T update(T value) throws ServiceException;

    /**
     * validates id and sends delete request to corresponding dao class
     * @param id of value to be deleted
     * @return true if value deleted successfully, false otherwise
     * @throws ServiceException if value is invalid or database error occurred
     */
    Boolean delete(Integer id) throws ServiceException;

    /**
     * validates id and sends get by id request to corresponding dao class
     * @param id value id
     * @return value with id == value.id
     * @throws ServiceException if there is no value with provided id, id is null, or database error occurred
     */
    T getById(Integer id) throws ServiceException;

    /**
     * sends get all request to corresponding dao class
     * @return list of values
     * @throws ServiceException if database error occurred
     */
    List<T> getAll() throws ServiceException;
}
