package com.epam.esm.dao.sqlgenerator;

import com.epam.esm.dao.model.giftcertificate.GiftCertificate;

import java.util.List;
import java.util.Objects;

/**
 * Util class for generation sql queries
 */
public class SqlGenerator {

    private static final String SQL_SELECT = "SELECT ";
    private static final String SQL_DELETE = "DELETE ";
    private static final String SQL_FROM = "FROM ";
    private static final String SQL_JOIN = "JOIN ";
    private static final String SQL_OR = " OR ";
    private static final String SQL_ON = "ON ";
    private static final String SQL_IN = "IN ";
    private static final String SQL_LIKE = "LIKE ";
    private static final String SQL_ORDER_BY = "ORDER BY ";
    private static final String SQL_DESC = "DESC ";
    private static final String SQL_WHERE = "WHERE ";
    private static final String SQL_AND = "AND ";
    private static final String SQL_UPDATE = "UPDATE ";
    private static final String SQL_SET = "SET ";

    private static final String WHITE_SPACE_SYMBOL = " ";
    private static final String COMMA_SYMBOL = ", ";
    private static final String EQUAL_SYMBOL = " = ";
    private static final String NOT_EQUAL_SYMBOL = " != ";
    private static final String QUESTION_SYMBOL = "?";
    private static final String EQUAL_QUESTION_MARK_SYMBOL = " = ?";
    private static final String DOT_SYMBOL = ".";
    private static final String APOSTROPHE_SYMBOL = "'";
    private static final String PERCENT_SYMBOL = "%";
    private static final String PARENTHESES_START_SYMBOL = "(";
    private static final String PARENTHESES_END_SYMBOL = ")";

    private static final String SQL_ID_COLUMN = "id";
    private static final String SQL_NAME_COLUMN = "name";
    private static final String SQL_DESCRIPTION_COLUMN = "description";
    private static final String SQL_PRICE_COLUMN = "price";
    private static final String SQL_DURATION_COLUMN = "duration";
    private static final String SQL_CREATE_DATE_COLUMN = "create_date";
    private static final String SQL_LAST_UPDATE_DATE_COLUMN = "last_update_date";

    private static final String SQL_GIFT_CERTIFICATE_ID_COLUMN = "gift_certificate_id";
    private static final String SQL_TAG_ID_COLUMN = "tag_id";

    private static final String GIFT_CERTIFICATE_TABLE = "gift_certificate";
    private static final String GIFT_CERTIFICATE_TO_TAG_TABLE = "gift_certificate_to_tag";
    private static final String TAG_TABLE = "tag";

    /**
     * generates sql query with ? in place of passed params in order:join column, where columns, order by columns. null fields will be ignored
     * @param filterByTag if true, join will be added with constraint gift_certificate.id to gift_certificate_to_tag.gift_certificate_id
     * @param whereStringLikeColumnNames list of names of columns that will be used in "WHERE whereStringLikeColumns[i] LIKE '%?%'"
     * @param orderByColumnNames list of names of columns that will be used in "ORDER BY orderByColumns[i]"
     * @param ascending true for ascending sort, false if descending. ignored if sortByName and sortByDate is null.
     * @return generated sql query
     */
    public static String generateSQLForGiftCertificateFindWithParameters(Boolean filterByTag, List<String> whereStringLikeColumnNames, List<String> orderByColumnNames, Boolean ascending){
        StringBuilder sqlFindWithParametersString = new StringBuilder();

        sqlFindWithParametersString.append(SQL_SELECT).append(GIFT_CERTIFICATE_TABLE).append(DOT_SYMBOL).append(SQL_ID_COLUMN).append(COMMA_SYMBOL)
                .append(GIFT_CERTIFICATE_TABLE).append(DOT_SYMBOL).append(SQL_NAME_COLUMN).append(COMMA_SYMBOL)
                .append(SQL_DESCRIPTION_COLUMN).append(COMMA_SYMBOL)
                .append(SQL_PRICE_COLUMN).append(COMMA_SYMBOL)
                .append(SQL_DURATION_COLUMN).append(COMMA_SYMBOL)
                .append(SQL_CREATE_DATE_COLUMN).append(COMMA_SYMBOL)
                .append(SQL_LAST_UPDATE_DATE_COLUMN).append(WHITE_SPACE_SYMBOL)
                .append(SQL_FROM)
                .append(GIFT_CERTIFICATE_TABLE).append(WHITE_SPACE_SYMBOL);

        if (filterByTag){
            sqlFindWithParametersString.append(SQL_JOIN).append(GIFT_CERTIFICATE_TO_TAG_TABLE).append(WHITE_SPACE_SYMBOL).append(SQL_ON)
                    .append(GIFT_CERTIFICATE_TABLE).append(DOT_SYMBOL).append(SQL_ID_COLUMN)
                    .append(EQUAL_SYMBOL).append(SQL_GIFT_CERTIFICATE_ID_COLUMN)
                    .append(WHITE_SPACE_SYMBOL).append(SQL_WHERE)
                    .append(SQL_TAG_ID_COLUMN).append(EQUAL_QUESTION_MARK_SYMBOL).append(WHITE_SPACE_SYMBOL);
        }

        if (Objects.nonNull(whereStringLikeColumnNames) && !whereStringLikeColumnNames.isEmpty()){

            if (!filterByTag){
                sqlFindWithParametersString.append(SQL_WHERE);
            } else {
                sqlFindWithParametersString.append(SQL_AND);
            }

            for (int i = 0; i < whereStringLikeColumnNames.size(); i++){
                sqlFindWithParametersString.append(whereStringLikeColumnNames.get(i)).append(WHITE_SPACE_SYMBOL).append(SQL_LIKE)
                        .append(QUESTION_SYMBOL).append(WHITE_SPACE_SYMBOL);
                if (i < whereStringLikeColumnNames.size() - 1){
                    sqlFindWithParametersString.append(SQL_AND);
                }
            }
        }

        if (Objects.nonNull(orderByColumnNames) && !orderByColumnNames.isEmpty()){
            sqlFindWithParametersString.append(SQL_ORDER_BY);
            for (int i = 0; i < orderByColumnNames.size(); i++){
                sqlFindWithParametersString.append(orderByColumnNames.get(i)).append(WHITE_SPACE_SYMBOL);
                if (!ascending){
                    sqlFindWithParametersString.append(SQL_DESC);
                }
                if (i < orderByColumnNames.size() - 1){
                    sqlFindWithParametersString.append(COMMA_SYMBOL);
                }
            }
        }
        return sqlFindWithParametersString.toString();
    }

    /**
     * returns sqk query with ? in place of required params`
     * @param entity entity generate sql query for
     * @return sql query
     */
    public static String generateUpdateGiftCertificateColumnsString(GiftCertificate entity){
        StringBuilder sqlUpdateString = new StringBuilder();
        Boolean isFirst = true;

        sqlUpdateString.append(SQL_UPDATE).append(GIFT_CERTIFICATE_TABLE).append(WHITE_SPACE_SYMBOL).append(SQL_SET);

        if (Objects.nonNull(entity.getName())){
            sqlUpdateString.append(SQL_NAME_COLUMN);
            sqlUpdateString.append(EQUAL_QUESTION_MARK_SYMBOL);
            isFirst = false;
        }

        if (Objects.nonNull(entity.getDescription())){
            if (!isFirst){
                sqlUpdateString.append(COMMA_SYMBOL);
            } else {
                isFirst = false;
            }

            sqlUpdateString.append(SQL_DESCRIPTION_COLUMN);
            sqlUpdateString.append(EQUAL_QUESTION_MARK_SYMBOL);
        }

        if (Objects.nonNull(entity.getPrice())){
            if (!isFirst){
                sqlUpdateString.append(COMMA_SYMBOL);
            } else {
                isFirst = false;
            }

            sqlUpdateString.append(SQL_PRICE_COLUMN);
            sqlUpdateString.append(EQUAL_QUESTION_MARK_SYMBOL);

        }

        if (Objects.nonNull(entity.getDuration())){
            if (!isFirst){
                sqlUpdateString.append(COMMA_SYMBOL);
            } else {
                isFirst = false;
            }

            sqlUpdateString.append(SQL_DURATION_COLUMN);
            sqlUpdateString.append(EQUAL_QUESTION_MARK_SYMBOL);
        }

        if (!isFirst){
            sqlUpdateString.append(COMMA_SYMBOL);
        }

        sqlUpdateString.append(WHITE_SPACE_SYMBOL)
                .append(SQL_LAST_UPDATE_DATE_COLUMN)
                .append(EQUAL_QUESTION_MARK_SYMBOL)
                .append(WHITE_SPACE_SYMBOL);

        sqlUpdateString.append(SQL_WHERE).append(SQL_ID_COLUMN).append(EQUAL_QUESTION_MARK_SYMBOL);

        return sqlUpdateString.toString();
    }

    /**
     * returns sql query with ? in place of required params
     * @param amountOfId amount of id
     * @return sql query
     */
    public static String generateFindTagsByIdArray(Integer amountOfId){
        StringBuilder resultSql = new StringBuilder();

        resultSql.append(SQL_SELECT).append(SQL_ID_COLUMN).append(COMMA_SYMBOL)
                .append(SQL_NAME_COLUMN).append(WHITE_SPACE_SYMBOL)
                .append(SQL_FROM).append(TAG_TABLE).append(WHITE_SPACE_SYMBOL)
                .append(SQL_WHERE).append(SQL_ID_COLUMN).append(WHITE_SPACE_SYMBOL).append(SQL_IN).append(PARENTHESES_START_SYMBOL);

        for (int i = 0; i < amountOfId; i++){
            resultSql.append(QUESTION_SYMBOL);

            if (i < amountOfId - 1){
                resultSql.append(COMMA_SYMBOL);
            }
        }

        resultSql.append(PARENTHESES_END_SYMBOL);

        return resultSql.toString();
    }

    /**
     * returns sql query with ? in place of required params
     * @param amountOfTagIds amount of tags id
     * @return sql query, null if amountOfTagIds < 1
     */
    public static String generateDeleteGiftCertificateToTagEntriesByGiftCertificateAndTagsIds(Integer amountOfTagIds){

        if (amountOfTagIds < 1) {
            return null;
        }

        StringBuilder resultSql = new StringBuilder();

        resultSql.append(SQL_DELETE).append(SQL_FROM).append(GIFT_CERTIFICATE_TO_TAG_TABLE)
                .append(WHITE_SPACE_SYMBOL).append(SQL_WHERE).append(SQL_GIFT_CERTIFICATE_ID_COLUMN).append(EQUAL_QUESTION_MARK_SYMBOL)
                .append(WHITE_SPACE_SYMBOL).append(SQL_AND).append(SQL_TAG_ID_COLUMN)
                .append(WHITE_SPACE_SYMBOL).append(SQL_IN).append(PARENTHESES_START_SYMBOL);

        for (int i = 0; i < amountOfTagIds; i++){
            resultSql.append(QUESTION_SYMBOL);

            if (i < amountOfTagIds - 1){
                resultSql.append(COMMA_SYMBOL);
            }
        }

        resultSql.append(PARENTHESES_END_SYMBOL);
        return resultSql.toString();
    }
}
