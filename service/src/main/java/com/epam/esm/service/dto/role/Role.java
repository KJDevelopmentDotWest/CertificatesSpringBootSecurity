package com.epam.esm.service.dto.role;

import java.util.Arrays;
import java.util.Objects;

public enum Role {
    USER(1),
    ADMIN(2);

    private final Integer id;

    Role(Integer id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static Role getById(Integer id) {
        return Arrays.stream(Role.values())
                .filter(role -> Objects.equals(role.getId(), id))
                .findFirst().orElse(null);
    }

    public static Role getByName(String name) {
        return Arrays.stream(Role.values()).filter(role -> role.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
