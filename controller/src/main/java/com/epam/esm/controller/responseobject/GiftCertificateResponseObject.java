package com.epam.esm.controller.responseobject;

import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.dto.tag.TagDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificateResponseObject {

    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Long duration;
    private String createDate;
    private String lastUpdateDate;
    private List<TagDto> tags;

    public GiftCertificateResponseObject(GiftCertificateDto giftCertificateDto){
        id = giftCertificateDto.getId();
        name = giftCertificateDto.getName();
        description = giftCertificateDto.getDescription();
        price = giftCertificateDto.getPrice();
        duration = giftCertificateDto.getDuration();
        createDate = giftCertificateDto.getCreateDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        lastUpdateDate = giftCertificateDto.getLastUpdateDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        tags = giftCertificateDto.getTags();
    }

}
