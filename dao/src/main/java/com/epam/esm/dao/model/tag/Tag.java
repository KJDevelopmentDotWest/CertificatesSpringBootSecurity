package com.epam.esm.dao.model.tag;

import com.epam.esm.dao.model.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Tag implements Entity {
    Integer id;
    String name;

    public Tag(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag(String name) {
        this.name = name;
    }
}
