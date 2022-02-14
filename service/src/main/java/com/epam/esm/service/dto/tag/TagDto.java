package com.epam.esm.service.dto.tag;

import com.epam.esm.service.dto.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class that represents Tag
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto implements Dto {
    Integer id;
    private String name;
}
