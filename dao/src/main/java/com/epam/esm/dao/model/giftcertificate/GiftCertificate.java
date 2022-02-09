package com.epam.esm.dao.model.giftcertificate;

import com.epam.esm.dao.model.Entity;
import com.epam.esm.dao.model.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class GiftCertificate implements Entity {
    Integer id;
    String name;
    String description;
    Double price;
    Long duration;
    LocalDateTime createDate;
    LocalDateTime lastUpdateDate;
    List<Tag> tags;
}
