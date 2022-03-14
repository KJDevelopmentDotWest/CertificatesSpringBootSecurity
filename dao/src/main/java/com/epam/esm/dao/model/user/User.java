package com.epam.esm.dao.model.user;

import com.epam.esm.dao.model.EntityModel;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "user_table")
public class User implements EntityModel {
    @Id
    @SequenceGenerator(name = "idSequenceUser", sequenceName = "user_table_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "idSequenceUser")
    private Integer id;
    private String name;
}
