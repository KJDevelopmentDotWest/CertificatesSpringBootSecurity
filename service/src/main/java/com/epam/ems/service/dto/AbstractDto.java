package com.epam.ems.service.dto;

public class AbstractDto {
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
