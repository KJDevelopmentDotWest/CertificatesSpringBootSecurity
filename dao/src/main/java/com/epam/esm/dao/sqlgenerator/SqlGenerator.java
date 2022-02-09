package com.epam.esm.dao.sqlgenerator;

import com.epam.esm.dao.model.giftcertificate.GiftCertificate;

import java.util.Objects;


public class SqlGenerator {

    private static final String SQL_UPDATE_GIFT_CERTIFICATE_START = "UPDATE gift_certificate SET ";
    private static final String SQL_UPDATE_GIFT_CERTIFICATE_END = "WHERE id = ? ";

    private static final String SQL_FIND_START = "SELECT gift_certificate.id, gift_certificate.name, description, price, duration, create_date, last_update_date FROM gift_certificate ";
    private static final String SQL_WHERE = "WHERE ";
    private static final String SQL_FIND_JOIN_PART = "JOIN gift_certificate_to_tag ON gift_certificate.id = gift_certificate_id WHERE tag_id = ";
    private static final String SQL_AND = "AND ";
    private static final String SQL_FIND_NAME_LIKE_START = "name LIKE '%";
    private static final String SQL_FIND_DESCRIPTION_LIKE_START = "description LIKE '%";
    private static final String SQL_FIND_LIKE_END = "%' ";
    private static final String SQL_FIND_ORDER_BY_NAME = "ORDER BY name ";
    private static final String SQL_FIND_ORDER_BY_DATE = "ORDER BY last_update_date ";
    private static final String SQL_FIND_ORDER_BY_DESC = "DESC ";

    private static final String WHITE_SPACE_SYMBOL = " ";
    private static final String COMMA_SYMBOL = ", ";
    private static final String EQUAL_SYMBOL = " = ";
    private static final String NOT_EQUAL_SYMBOL = " != ";
    private static final String QUESTION_SYMBOL = " ?";
    private static final String EQUAL_QUESTION_MARK_SYMBOL = " = ?";

    private static final String SQL_FIND_TAG_START = "SELECT id, name FROM tag WHERE ";

    private static final String SQL_OR = " OR ";

    private static final String SQL_ID_COLUMN = "id";
    private static final String SQL_NAME_COLUMN = "name";
    private static final String SQL_DESCRIPTION_COLUMN = "description";
    private static final String SQL_PRICE_COLUMN = "price";
    private static final String SQL_DURATION_COLUMN = "duration";
    private static final String SQL_CREATE_DATE_COLUMN = "create_date";
    private static final String SQL_LAST_UPDATE_DATE_COLUMN = "last_update_date";

    public static String generateSQLForGiftCertificateFindWithParameters(Integer tagId, String namePart, String descriptionPart, SortByCode sortBy, Boolean ascending){
        StringBuilder generatedSQLQuery = new StringBuilder();
        generatedSQLQuery.append(SQL_FIND_START);

        if (!Objects.isNull(tagId) ||
                !Objects.isNull(namePart) ||
                !Objects.isNull(descriptionPart) ||
                !Objects.isNull(sortBy)){

            if (!Objects.isNull(tagId)){
                generatedSQLQuery.append(SQL_FIND_JOIN_PART);
                generatedSQLQuery.append(tagId);
                generatedSQLQuery.append(WHITE_SPACE_SYMBOL);

                if (!Objects.isNull(namePart) ||
                        !Objects.isNull(descriptionPart)){
                    generatedSQLQuery.append(SQL_AND);
                }

            } else {
                if (!Objects.isNull(namePart) ||
                        !Objects.isNull(descriptionPart)){
                    generatedSQLQuery.append(SQL_WHERE);
                }
            }

            if (!Objects.isNull(namePart)){
                generatedSQLQuery.append(SQL_FIND_NAME_LIKE_START);
                generatedSQLQuery.append(namePart);
                generatedSQLQuery.append(SQL_FIND_LIKE_END);

                if (!Objects.isNull(descriptionPart)) {
                    generatedSQLQuery.append(SQL_AND);
                }
            }

            if (!Objects.isNull(descriptionPart)){
                generatedSQLQuery.append(SQL_FIND_DESCRIPTION_LIKE_START);
                generatedSQLQuery.append(descriptionPart);
                generatedSQLQuery.append(SQL_FIND_LIKE_END);
            }

            if (!Objects.isNull(sortBy)){
                if (sortBy.equals(SortByCode.SORT_BY_NAME)){
                    generatedSQLQuery.append(SQL_FIND_ORDER_BY_NAME);
                    if (!ascending){
                        generatedSQLQuery.append(SQL_FIND_ORDER_BY_DESC);
                    }
                } else if (sortBy.equals(SortByCode.SORT_BY_DESCRIPTION)){
                    generatedSQLQuery.append(SQL_FIND_ORDER_BY_DATE);
                    if (!ascending){
                        generatedSQLQuery.append(SQL_FIND_ORDER_BY_DESC);
                    }
                } else if (sortBy.equals(SortByCode.SORT_BY_NAME_AND_DESCRIPTION)){
                    generatedSQLQuery.append(SQL_FIND_ORDER_BY_NAME);
                    if (!ascending){
                        generatedSQLQuery.append(SQL_FIND_ORDER_BY_DESC);
                    }
                    generatedSQLQuery.append(COMMA_SYMBOL);
                    generatedSQLQuery.append(SQL_LAST_UPDATE_DATE_COLUMN);
                    generatedSQLQuery.append(WHITE_SPACE_SYMBOL);
                    if (!ascending){
                        generatedSQLQuery.append(SQL_FIND_ORDER_BY_DESC);
                    }
                }
            }
        }
        return generatedSQLQuery.toString();
    }

    public static String generateUpdateColString(GiftCertificate entity){
        StringBuilder sqlUpdateString = new StringBuilder();
        Boolean isFirst = true;

        sqlUpdateString.append(SQL_UPDATE_GIFT_CERTIFICATE_START);

        if (Objects.nonNull(entity.getName())){
            isFirst = false;
            sqlUpdateString.append(SQL_NAME_COLUMN);
            sqlUpdateString.append(EQUAL_QUESTION_MARK_SYMBOL);
        }

        if (Objects.nonNull(entity.getDescription())){

            if (!isFirst){
                sqlUpdateString.append(COMMA_SYMBOL);
                isFirst = false;
            }

            sqlUpdateString.append(SQL_DESCRIPTION_COLUMN);
            sqlUpdateString.append(EQUAL_QUESTION_MARK_SYMBOL);

        }

        if (Objects.nonNull(entity.getPrice())){

            if (!isFirst){
                sqlUpdateString.append(COMMA_SYMBOL);
                isFirst = false;
            }

            sqlUpdateString.append(SQL_PRICE_COLUMN);
            sqlUpdateString.append(EQUAL_QUESTION_MARK_SYMBOL);

        }

        if (Objects.nonNull(entity.getDuration())){

            if (!isFirst){
                sqlUpdateString.append(COMMA_SYMBOL);
                isFirst = false;
            }

            sqlUpdateString.append(SQL_DURATION_COLUMN);
            sqlUpdateString.append(EQUAL_QUESTION_MARK_SYMBOL);

        }

        if (Objects.nonNull(entity.getCreateDate())){

            if (!isFirst){
                sqlUpdateString.append(COMMA_SYMBOL);
                isFirst = false;
            }

            sqlUpdateString.append(SQL_CREATE_DATE_COLUMN);
            sqlUpdateString.append(EQUAL_QUESTION_MARK_SYMBOL);

        }

        if (!isFirst){
            sqlUpdateString.append(COMMA_SYMBOL);
            sqlUpdateString.append(SQL_LAST_UPDATE_DATE_COLUMN);
            sqlUpdateString.append(EQUAL_QUESTION_MARK_SYMBOL);
        }

        sqlUpdateString.append(SQL_UPDATE_GIFT_CERTIFICATE_END);

        return sqlUpdateString.toString();
    }

    public static String generateFindTagsByIdArray(Integer amountOfId){
        StringBuilder resultSql = new StringBuilder();

        resultSql.append(SQL_FIND_TAG_START);
        resultSql.append(SQL_ID_COLUMN);
        resultSql.append(EQUAL_QUESTION_MARK_SYMBOL);

        for (int i = 0; i < amountOfId - 1; i++){
            resultSql.append(SQL_OR);
            resultSql.append(SQL_ID_COLUMN);
            resultSql.append(EQUAL_QUESTION_MARK_SYMBOL);
        }

        return resultSql.toString();
    }

    public enum SortByCode{
        SORT_BY_NAME,
        SORT_BY_DESCRIPTION,
        SORT_BY_NAME_AND_DESCRIPTION
    }

}
