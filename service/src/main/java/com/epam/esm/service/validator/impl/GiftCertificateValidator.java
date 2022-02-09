package com.epam.esm.service.validator.impl;

import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.expecption.ServiceException;
import com.epam.esm.service.validator.api.Validator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class GiftCertificateValidator implements Validator<GiftCertificateDto> {

    private static final Integer NAME_MIN_LENGTH = 2;
    private static final Integer NAME_MAX_LENGTH = 30;

    @Override
    public void validate(GiftCertificateDto value, Boolean checkId) throws ServiceException {

        if (Objects.isNull(value)){
            throw new ServiceException("gift certificate cannot be null");
        }

        if (checkId) {
            validateId(value.getId());
        }

        validateName(value.getName());
        validateDescription(value.getDescription());
        validatePrice(value.getPrice());
        validateDuration(value.getDuration());
        validateCreateDate(value.getCreateDate());
        validateLastUpdateDate(value.getLastUpdateDate());
        validateTags(value.getTags());

    }

    private void validateId(Integer id) throws ServiceException{
        if (Objects.isNull(id)){
            throw new ServiceException("id cannot be null");
        }
        if (id < 0){
            throw new ServiceException("id cannot be negative");
        }
    }

    private void validateName(String name) throws ServiceException{
        if (Objects.isNull(name)){
            throw new ServiceException("name cannot be null");
        }
        if (name.length() < NAME_MIN_LENGTH) {
            throw new ServiceException("name length cannot be less than " + NAME_MIN_LENGTH);
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new ServiceException("name length cannot be more than " + NAME_MAX_LENGTH);
        }
        if (name.startsWith(" ")){
            throw new ServiceException("name cannot start with white space");
        }
        if (name.endsWith(" ")){
            throw new ServiceException("name cannot end with white space");
        }
    }

    private void validateDescription(String description) throws ServiceException{
        if (Objects.isNull(description)){
            throw new ServiceException("description cannot be null");
        }
        if (description.startsWith(" ")){
            throw new ServiceException("description cannot start with white space");
        }
        if (description.endsWith(" ")){
            throw new ServiceException("description cannot end with white space");
        }
    }

    private void validatePrice(Double price) throws ServiceException{
        if (Objects.isNull(price)){
            throw new ServiceException("price cannot be null");
        }
        if (price.isInfinite() || price.isNaN() || price < 0){
            throw new ServiceException("price must be positive real number");
        }
    }

    private void validateDuration(Long duration) throws ServiceException{
        if (Objects.isNull(duration)){
            throw new ServiceException("duration cannot be null");
        }
        if (duration < 0){
            throw new ServiceException("duration must be positive");
        }
    }

    private void validateCreateDate(LocalDateTime createDate) throws ServiceException{
        if (Objects.isNull(createDate)){
            throw new ServiceException("createDate cannot be null");
        }

        if (createDate.isAfter(LocalDateTime.now())){
            throw new ServiceException("createDate cannot point to future");
        }
    }

    private void validateLastUpdateDate(LocalDateTime lastUpdateDate) throws ServiceException{
        if (Objects.isNull(lastUpdateDate)){
            throw new ServiceException("lastUpdateDate cannot be null");
        }

        if (lastUpdateDate.isAfter(LocalDateTime.now())){
            throw new ServiceException("lastUpdateDate cannot point to future");
        }
    }

    private void validateTags(List<TagDto> tags) throws ServiceException{
        if (Objects.isNull(tags)){
            throw new ServiceException("tags list cannot be null");
        }
        Validator<TagDto> tagValidator = new TagValidator();
        for (TagDto tagDto : tags){
            tagValidator.validate(tagDto, true);
        }
    }
}
