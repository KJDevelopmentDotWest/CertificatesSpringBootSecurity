package com.epam.esm.dao.model.tag;

import com.epam.esm.dao.model.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
public class Tag implements Entity {
    Integer id;
    String name;
}
