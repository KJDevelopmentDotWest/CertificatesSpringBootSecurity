package com.epam.esm.service.dto.user;

import com.epam.esm.service.dto.Dto;
import com.epam.esm.service.dto.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Dto {
    private Integer id;
    private String firstname;
    private String lastname;
    //as login, unique
    private String username;
    private String password;
    private Role role;
}
