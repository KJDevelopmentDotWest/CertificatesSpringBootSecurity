package com.epam.ems.service.converter.api;

import com.epam.ems.service.dto.AbstractDto;
import com.epam.esm.dao.model.Entity;

public interface Converter<T extends Entity, V extends AbstractDto> {
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