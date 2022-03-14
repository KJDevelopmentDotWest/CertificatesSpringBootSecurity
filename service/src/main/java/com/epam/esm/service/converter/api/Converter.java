package com.epam.esm.service.converter.api;

import com.epam.esm.service.dto.Dto;
import com.epam.esm.dao.model.EntityModel;

/**
 * Interface for converter classes
 * @param <T> type of entity
 * @param <V> type of dto
 */
public interface Converter<T extends EntityModel, V extends Dto> {
    /**
     * returns entity converted from dto
     * @param value dto value to be converted
     * @return converted entity
     */
    T convert(V value);

    /**
     * returns dto converted from entity
     * @param value entity to be converted
     * @return converted dto
     */
    V convert(T value);
}