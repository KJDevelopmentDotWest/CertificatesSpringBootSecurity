package com.epam.esm.dao.model.tag;

import com.epam.esm.dao.model.Entity;

import java.util.Objects;

public record Tag(Integer id, String name) implements Entity {
}
