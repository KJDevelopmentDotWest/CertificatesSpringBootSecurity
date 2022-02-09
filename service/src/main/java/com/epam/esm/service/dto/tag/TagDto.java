package com.epam.esm.service.dto.tag;

import com.epam.esm.service.dto.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
@AllArgsConstructor
public class TagDto implements Dto {
    Integer id;
    private String name;
}
