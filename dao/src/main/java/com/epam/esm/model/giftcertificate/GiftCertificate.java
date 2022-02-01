package com.epam.esm.model.giftcertificate;

import com.epam.esm.model.Entity;
import com.epam.esm.model.tag.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record GiftCertificate(Integer id, String name, String description, Double price, Long duration, LocalDateTime createDate, LocalDateTime lastUpdateDate, List<Tag> tags) implements Entity {
    public GiftCertificate {
        Objects.requireNonNull(name);
        Objects.requireNonNull(price);
        Objects.requireNonNull(duration);
        Objects.requireNonNull(createDate);
        Objects.requireNonNull(lastUpdateDate);
    }
}
