package com.epam.esm.api;

import com.epam.esm.model.Entity;

import java.util.List;

public interface Dao<T extends Entity> {

    /**
     *
     * @param entity entity to be saved
     * @return saved entity
     */
    T save(T entity);

    /**
     *
     * @param entity entity to be updated
     * @return true if entity updated successfully, false otherwise
     */
    Boolean update(T entity);

    /**
     *
     * @param entity entity to be deleted
     * @return true if entity deleted successfully, false otherwise
     */
    Boolean delete(T entity);

    /**
     *
     * @return list of entities
     */
    List<T> findAll();

    /**
     *
     * @param id entity id
     * @return entity with id == entity.id
     */
    T findById(Integer id);

    /**
     *
     * @return number of entities in database
     */
    Integer getRowsNumber();

}
