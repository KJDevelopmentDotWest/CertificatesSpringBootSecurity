package com.epam.esm.dao.model.giftcertificate;

import com.epam.esm.dao.model.Entity;
import com.epam.esm.dao.model.tag.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GiftCertificate implements Entity {
    Integer id;
    String name;
    String description;
    Double price;
    Long duration;
    LocalDateTime createDate;
    LocalDateTime lastUpdateDate;
    List<Tag> tags;

    public GiftCertificate(Integer id, String name, String description, Double price, Long duration, LocalDateTime createDate, LocalDateTime lastUpdateDate, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public GiftCertificate(String name, String description, Double price, Long duration, LocalDateTime createDate, LocalDateTime lastUpdateDate, List<Tag> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

}
