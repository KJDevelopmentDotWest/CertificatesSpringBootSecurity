package com.epam.esm.service.dto.order;

import com.epam.esm.service.dto.Dto;
import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto implements Dto {

    private Integer id;
    private UserDto user;
    private GiftCertificateDto giftCertificate;
    private LocalDateTime time;
    private Double cost;

}
