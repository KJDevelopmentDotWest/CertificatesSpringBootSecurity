package com.epam.esm.dao.model.order;

import com.epam.esm.dao.model.EntityModel;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "order_table")
public class Order implements EntityModel {

    @Id
    @SequenceGenerator(name = "idSequenceOrder", sequenceName = "order_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "idSequenceOrder")
    private Integer id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(targetEntity = GiftCertificate.class)
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate giftCertificate;

    private LocalDateTime time;

    private Double cost;
}
