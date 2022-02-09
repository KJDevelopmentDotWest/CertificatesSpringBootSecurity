package com.epam.esm.service.dto;

import lombok.Data;

@Data
public abstract class AbstractDto {
    protected Integer id;

    /**
     *
     * @return id of abstract dto
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id id to be set
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
