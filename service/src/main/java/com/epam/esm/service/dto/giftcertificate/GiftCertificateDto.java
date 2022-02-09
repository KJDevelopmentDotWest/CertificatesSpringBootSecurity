package com.epam.esm.service.dto.giftcertificate;

import com.epam.esm.service.dto.AbstractDto;
import com.epam.esm.service.dto.tag.TagDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GiftCertificateDto extends AbstractDto{
    private String name;
    private String description;
    private Double price;
    private Long duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<TagDto> tags;

    public GiftCertificateDto(String name, String description, Double price, Long duration, LocalDateTime createDate, LocalDateTime lastUpdateDate, List<TagDto> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public GiftCertificateDto(Integer id, String name, String description, Double price, Long duration, LocalDateTime createDate, LocalDateTime lastUpdateDate, List<TagDto> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }
}
