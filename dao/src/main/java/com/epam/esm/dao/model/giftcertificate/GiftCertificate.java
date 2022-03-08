package com.epam.esm.dao.model.giftcertificate;

import com.epam.esm.dao.model.EntityModel;
import com.epam.esm.dao.model.tag.Tag;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Model class that represents GiftCertificate
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gift_certificate")
public class GiftCertificate implements EntityModel {

    @Id
    @SequenceGenerator(name = "idSequenceGiftCertificate", sequenceName = "gift_certificate_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idSequenceGiftCertificate")
    private Integer id;

    private String name;

    private String description;

    private Double price;

    private Long duration;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @ManyToMany(targetEntity = Tag.class, fetch = FetchType.EAGER)
    @JoinTable(name = "gift_certificate_to_tag",
            joinColumns = {@JoinColumn(name = "gift_certificate_id", referencedColumnName="id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName="id")})
    private List<Tag> tags;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        GiftCertificate that = (GiftCertificate) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
