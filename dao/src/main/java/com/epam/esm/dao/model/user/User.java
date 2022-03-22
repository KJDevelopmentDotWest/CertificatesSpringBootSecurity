package com.epam.esm.dao.model.user;

import com.epam.esm.dao.model.EntityModel;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user_table")
public class User implements EntityModel {
    @Id
    @SequenceGenerator(name = "idSequenceUser", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "idSequenceUser")
    private Integer id;

    private String firstname;

    private String lastname;

    //as login, unique
    private String username;

    private String password;

    @Column(name = "role_id")
    private Integer roleId;
}
