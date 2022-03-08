package com.epam.esm.service.dto.user;

import com.epam.esm.service.dto.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Dto {
    private Integer id;
    private String name;
}
