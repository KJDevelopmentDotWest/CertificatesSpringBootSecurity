package com.epam.esm.service.dto.giftcertificate;

import com.epam.esm.service.dto.Dto;
import com.epam.esm.service.dto.tag.TagDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO class that represents GiftCertificate
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificateDto implements Dto {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Long duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<TagDto> tags;
}
