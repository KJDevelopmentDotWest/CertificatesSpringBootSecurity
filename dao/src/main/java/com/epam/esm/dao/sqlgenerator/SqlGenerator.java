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
    private static final String SQL_LIMIT = "LIMIT ";
    private static final String SQL_OFFSET = "OFFSET ";

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

    private static final String TAG_NAMES_COLUMN = "tag_names";

    private static final String FIND_GIFT_CERTIFICATE_IDS_WITH_PARAMETERS = "SELECT id FROM (" +
            "SELECT gift_certificate.id, create_date, description, duration, last_update_date, gift_certificate.name, price, concat(',', STRING_AGG (tag.name, ','), ',') as tag_names \n" +
            "FROM public.gift_certificate " +
            "JOIN gift_certificate_to_tag ON gift_certificate.id = gift_certificate_id " +
            "JOIN tag ON tag_id = tag.id " +
            "GROUP BY gift_certificate.id " +
            ") as subselect ";

    /**
     * generates sql query with ? in place of passed params in order:join column, where columns, order by columns. null fields will be ignored
     * @param tagAmount amount of tags to be searched
     * @param whereStringLikeColumnNames list of names of columns that will be used in "WHERE whereStringLikeColumns[i] LIKE '%?%'"
     * @param orderByColumnNames list of names of columns that will be used in "ORDER BY orderByColumns[i]"
     * @param ascending true for ascending sort, false if descending. ignored if sortByName and sortByDate is null.
     * @return generated sql query
     */
    public static String generateSQLForGiftCertificateFindWithParameters(Integer tagAmount, List<String> whereStringLikeColumnNames,
                                                                         List<String> orderByColumnNames, Boolean ascending){
        StringBuilder sqlFindWithParametersString = new StringBuilder();

        sqlFindWithParametersString.append(FIND_GIFT_CERTIFICATE_IDS_WITH_PARAMETERS)
                .append(SQL_WHERE);

        for (int i = 0; i < tagAmount; i++) {
            sqlFindWithParametersString.append(TAG_NAMES_COLUMN).append(WHITE_SPACE_SYMBOL).append(SQL_LIKE).append(QUESTION_SYMBOL).append(WHITE_SPACE_SYMBOL);
            if (i < tagAmount - 1){
                sqlFindWithParametersString.append(SQL_AND);
            }
        }

        if (Objects.nonNull(whereStringLikeColumnNames) && !whereStringLikeColumnNames.isEmpty()){
            sqlFindWithParametersString.append(SQL_AND);

            for (int i = 0; i < whereStringLikeColumnNames.size(); i++){
                sqlFindWithParametersString.append(whereStringLikeColumnNames.get(i)).append(WHITE_SPACE_SYMBOL).append(SQL_LIKE)
                        .append(QUESTION_SYMBOL).append(WHITE_SPACE_SYMBOL);
                if (i < whereStringLikeColumnNames.size() - 1){
                    sqlFindWithParametersString.append(SQL_OR);
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
        sqlFindWithParametersString.append(SQL_LIMIT).append(QUESTION_SYMBOL).append(WHITE_SPACE_SYMBOL);
        sqlFindWithParametersString.append(SQL_OFFSET).append(QUESTION_SYMBOL).append(WHITE_SPACE_SYMBOL);
        return sqlFindWithParametersString.toString();
    }
}
