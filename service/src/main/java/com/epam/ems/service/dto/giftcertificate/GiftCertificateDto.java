package com.epam.ems.service.dto.giftcertificate;

import com.epam.ems.service.dto.AbstractDto;
import com.epam.ems.service.dto.tag.TagDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class GiftCertificateDto extends AbstractDto {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificateDto that = (GiftCertificateDto) o;
        return Objects.equals(id, that.getId())
                && Objects.equals(name, that.getName())
                && Objects.equals(description, that.getDescription())
                && Objects.equals(price, that.getPrice())
                && Objects.equals(duration, that.getDuration())
                && Objects.equals(createDate, that.getCreateDate())
                && Objects.equals(lastUpdateDate, that.getLastUpdateDate())
                && Objects.equals(tags, that.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tags, name, description, price, duration, createDate, lastUpdateDate, tags);
    }
}
