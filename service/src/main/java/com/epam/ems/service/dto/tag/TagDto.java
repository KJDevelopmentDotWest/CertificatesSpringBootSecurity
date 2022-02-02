package com.epam.ems.service.dto.tag;

import com.epam.ems.service.dto.AbstractDto;

import java.util.Objects;

public class TagDto extends AbstractDto {
    private String name;

    public TagDto(String name) {
        this.name = name;
    }

    public TagDto(Integer id,String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDto tagDto = (TagDto) o;
        return Objects.equals(id, tagDto.getId()) && Objects.equals(name, tagDto.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
