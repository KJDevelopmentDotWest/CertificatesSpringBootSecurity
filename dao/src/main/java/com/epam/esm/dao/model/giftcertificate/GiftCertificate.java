package com.epam.esm.dao.model.giftcertificate;

import com.epam.esm.dao.model.Entity;
import com.epam.esm.dao.model.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Model class that represents GiftCertificate
 */

@Data
@AllArgsConstructor
public class GiftCertificate implements Entity {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Long duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<Tag> tags;
}
