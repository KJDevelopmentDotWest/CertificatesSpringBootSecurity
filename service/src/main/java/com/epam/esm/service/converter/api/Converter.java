package com.epam.esm.service.converter.api;

import com.epam.esm.service.dto.Dto;
import com.epam.esm.dao.model.Entity;

public interface Converter<T extends Entity, V extends Dto> {
    /**
     *
     * @param value dto value to be converted
     * @return converted entity
     */
    T convert(V value);

    /**
     *
     * @param value entity to be converted
     * @return converted dto
     */
    V convert(T value);
}