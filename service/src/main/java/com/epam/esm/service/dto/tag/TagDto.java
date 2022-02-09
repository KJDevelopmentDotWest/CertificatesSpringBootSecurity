package com.epam.esm.service.dto.tag;

import com.epam.esm.service.dto.AbstractDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TagDto extends AbstractDto {
    private String name;

    public TagDto(String name) {
        this.name = name;
    }

    public TagDto(Integer id,String name) {
        this.id = id;
        this.name = name;
    }
}
