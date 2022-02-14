package com.epam.esm.dao.model.tag;

import com.epam.esm.dao.model.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Model class that represents Tag
 */

@Data
@AllArgsConstructor
public class Tag implements Entity {
    private Integer id;
    private String name;
}
