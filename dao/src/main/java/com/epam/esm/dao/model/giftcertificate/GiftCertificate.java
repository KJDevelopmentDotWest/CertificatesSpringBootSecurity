package com.epam.esm.dao.model.giftcertificate;

import com.epam.esm.dao.model.Entity;
import com.epam.esm.dao.model.tag.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record GiftCertificate(Integer id, String name, String description, Double price, Long duration, LocalDateTime createDate, LocalDateTime lastUpdateDate, List<Tag> tags) implements Entity {
}
