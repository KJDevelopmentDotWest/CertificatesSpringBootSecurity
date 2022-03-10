package com.epam.esm.dao.model.tag;

import com.epam.esm.dao.model.EntityModel;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * Model class that represents Tag
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Tag implements EntityModel {
    @Id
    @SequenceGenerator(name = "idSequenceTag", sequenceName = "tag_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "idSequenceTag")
    private Integer id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Tag tag = (Tag) o;
        return id != null && Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
