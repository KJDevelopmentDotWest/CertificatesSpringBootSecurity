package com.epam.esm.api;

import com.epam.esm.model.Entity;

import java.util.List;

public interface Dao<T extends Entity> {

    /**
     *
     * @param entity entity to be saved
     * @return saved entity
     */
    T saveEntity(T entity);

    /**
     *
     * @param entity entity to be updated
     * @return true if entity updated successfully, false otherwise
     */
    Boolean updateEntity(T entity);

    /**
     *
     * @param entity entity to be deleted
     * @return true if entity deleted successfully, false otherwise
     */
    Boolean deleteEntity(T entity);

    /**
     *
     * @return list of entities
     */
    List<T> findAllEntities();

    /**
     *
     * @param id entity id
     * @return entity with id == entity.id
     */
    T findEntityById(Integer id);

    /**
     *
     * @return number of entities in database
     */
    Integer getRowsNumber();

}
