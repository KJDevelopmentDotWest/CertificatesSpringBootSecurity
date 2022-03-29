package com.epam.esm.service.validator.impl;

import com.epam.esm.service.dto.giftcertificate.GiftCertificateDto;
import com.epam.esm.service.dto.tag.TagDto;
import com.epam.esm.service.exception.ExceptionCode;
import com.epam.esm.service.exception.ExceptionMessage;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.api.Validator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Validator implementation for GiftCertificateDto
 */

@Component
public class GiftCertificateValidator implements Validator<GiftCertificateDto> {

    private static final Integer NAME_MIN_LENGTH = 2;
    private static final Integer NAME_MAX_LENGTH = 30;

    private static final String WHITESPACE = " ";

    private List<ExceptionMessage> exceptionMessages;

    @Override
    public void validate(GiftCertificateDto value, Boolean checkId) throws ServiceException {
        exceptionMessages = new ArrayList<>();

        if (Objects.isNull(value)){
            throw new ServiceException(ExceptionCode.VALIDATION_FAILED_EXCEPTION, ExceptionMessage.GIFT_CERTIFICATE_CANNOT_BE_NULL);
        }

        if (checkId) {
            validateIdNotNullAndPositive(value.getId());
        }

        validateName(value.getName(), false);
        validateDescription(value.getDescription(), false);
        validatePrice(value.getPrice(), false);
        validateDuration(value.getDuration(), false);
        validateTags(value.getTags(), false);

        if (!exceptionMessages.isEmpty()){
            throw new ServiceException(ExceptionCode.VALIDATION_FAILED_EXCEPTION, exceptionMessages);
        }
    }

    /**
     * validates value
     * @param value value to be validated
     * @param checkId true if id must be validated, false otherwise
     * @param fieldsCanBeNull true if all fields except id can be null
     */
    public void validate(GiftCertificateDto value, Boolean checkId, Boolean fieldsCanBeNull) throws ServiceException {
        exceptionMessages = new ArrayList<>();

        if (Objects.isNull(value)){
            throw new ServiceException(ExceptionCode.VALIDATION_FAILED_EXCEPTION, ExceptionMessage.GIFT_CERTIFICATE_CANNOT_BE_NULL);
        }

        if (checkId) {
            validateIdNotNullAndPositive(value.getId());
        }

        validateName(value.getName(), fieldsCanBeNull);
        validateDescription(value.getDescription(), fieldsCanBeNull);
        validatePrice(value.getPrice(), fieldsCanBeNull);
        validateDuration(value.getDuration(), fieldsCanBeNull);
        validateTags(value.getTags(), fieldsCanBeNull);

        if (!exceptionMessages.isEmpty()){
            throw new ServiceException(ExceptionCode.VALIDATION_FAILED_EXCEPTION, exceptionMessages);
        }
    }

    private void validateName(String name, Boolean canBeNull) {

        if (Objects.isNull(name)){
            if (!canBeNull) {
                exceptionMessages.add(ExceptionMessage.GIFT_CERTIFICATE_NAME_CANNOT_BE_NULL);
            }
            return;
        }

        if (name.length() < NAME_MIN_LENGTH) {
            exceptionMessages.add(ExceptionMessage.GIFT_CERTIFICATE_NAME_TOO_SHORT);
        }
        if (name.length() > NAME_MAX_LENGTH) {
            exceptionMessages.add(ExceptionMessage.GIFT_CERTIFICATE_NAME_TOO_LONG);
        }
        if (name.startsWith(WHITESPACE)){
            exceptionMessages.add(ExceptionMessage.GIFT_CERTIFICATE_NAME_STARTS_WITH_WHITESPACE);
        }
        if (name.endsWith(WHITESPACE)){
            exceptionMessages.add(ExceptionMessage.GIFT_CERTIFICATE_NAME_ENDS_WITH_WHITESPACE);
        }
    }

    private void validateDescription(String description, Boolean canBeNull) {

        if (Objects.isNull(description)){
            if (!canBeNull) {
                exceptionMessages.add(ExceptionMessage.GIFT_CERTIFICATE_DESCRIPTION_CANNOT_BE_NULL);
            }
            return;
        }

        if (description.startsWith(WHITESPACE)){
            exceptionMessages.add(ExceptionMessage.GIFT_CERTIFICATE_DESCRIPTION_STARTS_WITH_WHITESPACE);
        }
        if (description.endsWith(WHITESPACE)){
            exceptionMessages.add(ExceptionMessage.GIFT_CERTIFICATE_DESCRIPTION_ENDS_WITH_WHITESPACE);
        }
    }

    private void validatePrice(Double price, Boolean canBeNull) {

        if (Objects.isNull(price)){
            if (!canBeNull) {
                exceptionMessages.add(ExceptionMessage.GIFT_CERTIFICATE_PRICE_CANNOT_BE_NULL);
            }
            return;
        }

        if (price.isInfinite() || price.isNaN() || price < 0){
            exceptionMessages.add(ExceptionMessage.GIFT_CERTIFICATE_PRICE_IS_NOT_POSITIVE_REAL_NUMBER);
        }
    }

    private void validateDuration(Long duration, Boolean canBeNull) {

        if (Objects.isNull(duration)){
            if (!canBeNull) {
                exceptionMessages.add(ExceptionMessage.GIFT_CERTIFICATE_DURATION_CANNOT_BE_NULL);
            }
            return;
        }

        if (duration < 0){
            exceptionMessages.add(ExceptionMessage.GIFT_CERTIFICATE_DURATION_IS_NEGATIVE);
        }
    }

    private void validateTags(List<TagDto> tags, Boolean canBeNull) {

        if (Objects.isNull(tags)){
            if (!canBeNull) {
                exceptionMessages.add(ExceptionMessage.GIFT_CERTIFICATE_TAG_LIST_CANNOT_BE_NULL);
            }
            return;
        }

        tags.forEach(tagDto -> {
            if (Objects.isNull(tagDto.getId())
                    && Objects.isNull(tagDto.getName())){
                exceptionMessages.add(ExceptionMessage.TAG_ID_AND_NAME_CANNOT_BE_NULL_SIMULTANEOUSLY);
            }
        });
    }
}
