package com.epam.esm.dao.api;

import com.epam.esm.dao.model.Entity;

import java.util.List;

/**
 * Interface for data access object classes
 * @param <T> type of entity this dao will operate
 */
public interface Dao<T extends Entity> {

    /**
     * saves entity and returns saved entity with id
     * @param entity entity to be saved
     * @throws org.springframework.dao.DataAccessException if database error occurred
     * @return saved entity
     */
    T saveEntity(T entity);

    /**
     * updates entity with entity.id(), null fields won't be updated
     * @param entity entity to be updated
     * @throws org.springframework.dao.DataAccessException if database error occurred
     * @return updated entity
     */
    T updateEntity(T entity);

    /**
     * deletes entity with provided id
     * @param id entity to be deleted
     * @throws org.springframework.dao.DataAccessException if database error occurred
     * @return true if entity deleted successfully, false otherwise
     */
    Boolean deleteEntity(Integer id);

    /**
     * returns list of all entities
     * @throws org.springframework.dao.DataAccessException if database error occurred
     * @return list of entities
     */
    List<T> findAllEntities();

    /**
     * returns entity with provided id
     * @param id entity id
     * @throws org.springframework.dao.DataAccessException if database error occurred
     * @return entity with id == entity.id, null if there is no entity with provided id
     */
    T findEntityById(Integer id);
}
