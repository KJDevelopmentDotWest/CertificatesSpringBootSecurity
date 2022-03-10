package com.epam.esm.dao.model.user;

import com.epam.esm.dao.model.EntityModel;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_table")
public class User implements EntityModel {
    @Id
    @SequenceGenerator(name = "idSequenceUser", sequenceName = "user_table_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "idSequenceUser")
    private Integer id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        User that = (User) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
