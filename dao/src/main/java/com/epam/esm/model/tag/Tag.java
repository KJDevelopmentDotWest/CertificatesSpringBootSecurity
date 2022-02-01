package com.epam.esm.model.tag;

import com.epam.esm.model.Entity;

import java.util.Objects;

public record Tag(Integer id, String name) implements Entity {
    public Tag {
        Objects.requireNonNull(name);
    }
}
