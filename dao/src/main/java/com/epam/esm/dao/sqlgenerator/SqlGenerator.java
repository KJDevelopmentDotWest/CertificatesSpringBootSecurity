package com.epam.esm.dao.sqlgenerator;

import java.util.Objects;

public class SqlGenerator {

    private static SqlGenerator instance;

    private static final String SQL_UPDATE_GIFT_CERTIFICATE_START = "UPDATE gift_certificate SET ";
    private static final String SQL_NAME_COLUMN = "name";
    private static final String SQL_DESCRIPTION_COLUMN = "description";
    private static final String SQL_PRICE_COLUMN = "price";
    private static final String SQL_DURATION_COLUMN = "duration";
    private static final String SQL_CREATE_DATE_COLUMN = "create_date";
    private static final String SQL_LAST_UPDATE_DATE_COLUMN = "last_update_date";
    private static final String SQL_UPDATE_GIFT_CERTIFICATE_END = "WHERE id = ? AND ";

    private static final String SQL_FIND_START = "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate ";
    private static final String SQL_FIND_WHERE = "WHERE ";
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

    public static SqlGenerator getInstance() {
        if (Objects.isNull(instance)){
            instance = new SqlGenerator();
        }
        return instance;
    }

    private SqlGenerator(){}

    public String generateSQLForGiftCertificateFindWithParameters(Integer tagId, String namePart, String descriptionPart, SortByCode sortBy, Boolean ascending){
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
                    generatedSQLQuery.append(SQL_FIND_WHERE);
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

    public String generateUpdateColString(String columnName){
        StringBuilder sqlUpdateString = new StringBuilder();
        sqlUpdateString.append(SQL_UPDATE_GIFT_CERTIFICATE_START);
        sqlUpdateString.append(columnName);
        sqlUpdateString.append(EQUAL_SYMBOL);
        sqlUpdateString.append(QUESTION_SYMBOL);
        sqlUpdateString.append(SQL_UPDATE_GIFT_CERTIFICATE_END);
        sqlUpdateString.append(columnName);
        sqlUpdateString.append(NOT_EQUAL_SYMBOL);
        sqlUpdateString.append(QUESTION_SYMBOL);
        return sqlUpdateString.toString();
    }

    public enum SortByCode{
        SORT_BY_NAME,
        SORT_BY_DESCRIPTION,
        SORT_BY_NAME_AND_DESCRIPTION
    }

}
