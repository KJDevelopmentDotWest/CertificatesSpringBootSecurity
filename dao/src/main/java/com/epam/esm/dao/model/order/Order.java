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
@Entity
@Table(name = "order_table")
public class Order implements EntityModel {

    @Id
    @SequenceGenerator(name = "idSequenceOrder", sequenceName = "order_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idSequenceOrder")
    private Integer id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(targetEntity = GiftCertificate.class)
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate giftCertificate;

    private LocalDateTime time;

    private Double cost;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Order that = (Order) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
