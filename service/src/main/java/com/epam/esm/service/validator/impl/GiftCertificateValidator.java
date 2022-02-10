package com.epam.esm.service.validator.impl;

import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.exception.ExceptionCode;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.api.Validator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class GiftCertificateValidator implements Validator<GiftCertificateDto> {

    private static final Integer NAME_MIN_LENGTH = 2;
    private static final Integer NAME_MAX_LENGTH = 30;

    private static final String WHITESPACE = " ";

    @Override
    public void validate(GiftCertificateDto value, Boolean checkId) throws ServiceException {

        if (Objects.isNull(value)){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_CANNOT_BE_NULL);
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
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_ID_CANNOT_BE_NULL);
        }
        if (id < 0){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_ID_CANNOT_BE_NEGATIVE);
        }
    }

    private void validateName(String name) throws ServiceException{
        if (Objects.isNull(name)){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_NAME_CANNOT_BE_NULL);
        }
        if (name.length() < NAME_MIN_LENGTH) {
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_NAME_TOO_SHORT);
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_NAME_TOO_LONG);
        }
        if (name.startsWith(WHITESPACE)){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_NAME_STARTS_WITH_WHITESPACE);
        }
        if (name.endsWith(WHITESPACE)){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_NAME_ENDS_WITH_WHITESPACE);
        }
    }

    private void validateDescription(String description) throws ServiceException{
        if (Objects.isNull(description)){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_DESCRIPTION_CANNOT_BE_NULL);
        }
        if (description.startsWith(WHITESPACE)){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_DESCRIPTION_STARTS_WITH_WHITESPACE);
        }
        if (description.endsWith(WHITESPACE)){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_DESCRIPTION_ENDS_WITH_WHITESPACE);
        }
    }

    private void validatePrice(Double price) throws ServiceException{
        if (Objects.isNull(price)){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_PRICE_CANNOT_BE_NULL);
        }
        if (price.isInfinite() || price.isNaN() || price < 0){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_PRICE_IS_NOT_POSITIVE_REAL_NUMBER);
        }
    }

    private void validateDuration(Long duration) throws ServiceException{
        if (Objects.isNull(duration)){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_DURATION_CANNOT_BE_NULL);
        }
        if (duration < 0){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_DURATION_IS_NEGATIVE);
        }
    }

    private void validateCreateDate(LocalDateTime createDate) throws ServiceException{
        if (Objects.isNull(createDate)){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_CREATE_DATE_CANNOT_BE_NULL);
        }

        if (createDate.isAfter(LocalDateTime.now())){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_CREATE_DATE_CANNOT_POINT_TO_FUTURE);
        }
    }

    private void validateLastUpdateDate(LocalDateTime lastUpdateDate) throws ServiceException{
        if (Objects.isNull(lastUpdateDate)){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_LAST_UPDATE_DATE_CANNOT_BE_NULL);
        }

        if (lastUpdateDate.isAfter(LocalDateTime.now())){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_LAST_UPDATE_DATE_CANNOT_POINT_TO_FUTURE);
        }
    }

    private void validateTags(List<TagDto> tags) throws ServiceException{
        if (Objects.isNull(tags)){
            throw new ServiceException(ExceptionCode.GIFT_CERTIFICATE_TAG_LIST_CANNOT_BE_NULL);
        }
    }
}
