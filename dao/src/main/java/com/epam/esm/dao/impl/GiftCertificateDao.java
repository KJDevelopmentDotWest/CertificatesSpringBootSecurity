package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.dao.mapper.IdMapper;
import com.epam.esm.dao.model.tag.Tag;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.sqlgenerator.SqlGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Dao interface implementation for GiftCertificate with ability to perform CRUD operations
 */

@Component
public class GiftCertificateDao implements Dao<GiftCertificate> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TagDao tagDao;

    private static final Logger logger = LogManager.getLogger(GiftCertificateDao.class);

    private static final String SQL_SAVE_GIFT_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SAVE_GIFT_CERTIFICATE_TO_TAG_ENTRY = "INSERT INTO gift_certificate_to_tag (gift_certificate_id, tag_id) VALUES (?, ?)";

    private static final String SQL_FIND_ALL_GIFT_CERTIFICATES = "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate";
    private static final String SQL_FIND_GIFT_CERTIFICATE_BY_ID = "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate WHERE id = ?";

    private static final String SQL_FIND_TAGS_ID_BY_GIFT_CERTIFICATE_ID = "SELECT tag_id FROM gift_certificate_to_tag WHERE gift_certificate_id = ?";

    private static final String SQL_DELETE_GIFT_CERTIFICATE_BY_ID = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String SQL_DELETE_GIFT_CERTIFICATE_TO_TAG_ENTRY_BY_GIFT_CERTIFICATE_ID = "DELETE FROM gift_certificate_to_tag WHERE gift_certificate_id = ?";

    private static final String SQL_NAME_COLUMN = "name";
    private static final String SQL_DESCRIPTION_COLUMN = "description";
    private static final String SQL_LAST_UPDATE_DATE_COLUMN = "last_update_date";

    private final GiftCertificateMapper mapper = new GiftCertificateMapper();
    private final IdMapper integerMapper = new IdMapper();

    @Override
    @Transactional
    public GiftCertificate saveEntity(GiftCertificate entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_GIFT_CERTIFICATE, new String[] {"id"});
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setDouble(3, entity.getPrice());
            preparedStatement.setLong(4, entity.getDuration());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            return preparedStatement;
        }, keyHolder);

        Integer generatedEntityId = keyHolder.getKeyAs(Integer.class);

        entity.getTags().forEach(tag -> {

            Integer tagIdToBeAdded = tag.getId();

            if (Objects.isNull(tag.getId())
                    || Objects.isNull(tagDao.findEntityById(tag.getId()))){
                Tag foundByName = tagDao.findTagByName(tag.getName());
                if (Objects.isNull(foundByName)){
                    tagIdToBeAdded = tagDao.saveEntity(tag).getId();
                } else {
                    tagIdToBeAdded = foundByName.getId();
                }
            }

            jdbcTemplate.update(SQL_SAVE_GIFT_CERTIFICATE_TO_TAG_ENTRY, generatedEntityId, tagIdToBeAdded);
        });

        return findEntityById(generatedEntityId);
    }

    @Override
    @Transactional
    public GiftCertificate updateEntity(GiftCertificate entity) {

        if (Objects.nonNull(entity.getTags())){
            updateGiftCertificateToTagEntries(entity);
        }

        List<Object> valuesToInsert = new ArrayList<>();

        if (Objects.nonNull(entity.getName())){
            valuesToInsert.add(entity.getName());
        }

        if (Objects.nonNull(entity.getDescription())){
            valuesToInsert.add(entity.getDescription());
        }

        if (Objects.nonNull(entity.getPrice())){
            valuesToInsert.add(entity.getPrice());
        }

        if (Objects.nonNull(entity.getDuration())){
            valuesToInsert.add(entity.getDuration());
        }

        if (!valuesToInsert.isEmpty() || Objects.nonNull(entity.getTags())){
            entity.setLastUpdateDate(LocalDateTime.now());
            valuesToInsert.add(LocalDateTime.now());
            valuesToInsert.add(entity.getId());
            jdbcTemplate.update(SqlGenerator.generateUpdateGiftCertificateColumnsString(entity), valuesToInsert.toArray());
        }
        return findEntityById(entity.getId());
    }

    @Override
    @Transactional
    public Boolean deleteEntity(Integer id) {
        jdbcTemplate.update(SQL_DELETE_GIFT_CERTIFICATE_TO_TAG_ENTRY_BY_GIFT_CERTIFICATE_ID, id);
        return jdbcTemplate.update(SQL_DELETE_GIFT_CERTIFICATE_BY_ID, id) != 0;
    }

    @Override
    public List<GiftCertificate> findAllEntities() {
        List<GiftCertificate> result = jdbcTemplate.query(SQL_FIND_ALL_GIFT_CERTIFICATES, mapper);

        addTagsToGiftCertificates(result);

        return result;
    }

    @Override
    public GiftCertificate findEntityById(Integer id) {
        GiftCertificate result = jdbcTemplate.query(SQL_FIND_GIFT_CERTIFICATE_BY_ID, mapper, id)
                .stream().findAny().orElse(null);

        List<Integer> tagsId = jdbcTemplate.query(SQL_FIND_TAGS_ID_BY_GIFT_CERTIFICATE_ID, integerMapper, id);

        List<Tag> tags = tagDao.findTagsById(tagsId);

        if (Objects.nonNull(result)){
            result.setTags(tags);
        }

        return result;
    }

    /**
     * returns filtered list of gift certificates
     * @param tagName name of tag to be searched, null if no need to search by tag
     * @param namePart part of name to be filtered, null if no need to filter by name part
     * @param descriptionPart part of description to be filtered, null if no need to filter description part
     * @param sortByName true for sorting by name, false otherwise
     * @param sortByDate true for sorting by date, false otherwise
     * @param ascending true for ascending sort, false if descending. ignored if sortByName and sortByDate is null. true if null
     * @throws org.springframework.dao.DataAccessException if database error occurred
     * @return list of gift certificates that match parameters
     */
    public List<GiftCertificate> findGiftCertificatesWithParameters (String tagName, String namePart, String descriptionPart, Boolean sortByName, Boolean sortByDate, Boolean ascending){

        List<String> whereStringLikeColumnNames = new ArrayList<>();
        List<String> orderByColumnNames = new ArrayList<>();
        List<Object> objectsToAdd = new ArrayList<>();

        if (Objects.nonNull(tagName)){
            Tag tag = tagDao.findTagByName(tagName);

            if (Objects.nonNull(tag)){
                objectsToAdd.add(tag.getId());
            } else {
                return new ArrayList<>();
            }
        }

        if (Objects.nonNull(namePart)){
            whereStringLikeColumnNames.add(SQL_NAME_COLUMN);
            objectsToAdd.add("%" + namePart + "%");
        }

        if (Objects.nonNull(descriptionPart)){
            whereStringLikeColumnNames.add(SQL_DESCRIPTION_COLUMN);
            objectsToAdd.add("%" + descriptionPart + "%");
        }

        if (Objects.nonNull(sortByName) && sortByName) {
            orderByColumnNames.add(SQL_NAME_COLUMN);
        }

        if (Objects.nonNull(sortByDate) && sortByDate) {
            orderByColumnNames.add(SQL_LAST_UPDATE_DATE_COLUMN);
        }

        String sqlQuery = SqlGenerator.generateSQLForGiftCertificateFindWithParameters(Objects.nonNull(tagName), whereStringLikeColumnNames, orderByColumnNames, Optional.of(ascending).orElse(true));
        List<GiftCertificate> result = jdbcTemplate.query(sqlQuery, new GiftCertificateMapper(), objectsToAdd.toArray());
        addTagsToGiftCertificates(result);
        return result;
    }

    private void updateGiftCertificateToTagEntries(GiftCertificate giftCertificate) {
        List<Tag> currentTags = giftCertificate.getTags();

        //tags with id that not present in database considered as tags with null id
        currentTags.forEach(tag -> {
            if (Objects.nonNull(tag.getId()) && Objects.isNull(tagDao.findEntityById(tag.getId()))){
                System.out.println(tag.getId());
                tag.setId(null);
            }
        });

        List<Tag> currentTagsWithNullId = currentTags.stream().filter(tag -> Objects.isNull(tag.getId())).collect(Collectors.toCollection(ArrayList::new));
        List<Tag> currentTagsWithNonNullId = currentTags.stream().filter(tag -> Objects.nonNull(tag.getId())).collect(Collectors.toCollection(ArrayList::new));
        List<Integer> currentTagsId = currentTagsWithNonNullId.stream().map(Tag::getId).collect(Collectors.toCollection(ArrayList::new));
        List<Integer> previousTagsId;

        currentTagsWithNullId.forEach(tag -> {
            Tag tagToBeAdded;
            Tag foundByName = tagDao.findTagByName(tag.getName());
            if (Objects.isNull(foundByName)){
                tagToBeAdded = tagDao.saveEntity(tag);
            } else {
                tagToBeAdded = foundByName;
            }
            currentTagsId.add(tagToBeAdded.getId());
            currentTagsWithNonNullId.add(tagToBeAdded);
        });

        previousTagsId = jdbcTemplate.query(SQL_FIND_TAGS_ID_BY_GIFT_CERTIFICATE_ID, integerMapper, giftCertificate.getId());

        List<Tag> tagsToAdd = new ArrayList<>();
        List<Integer> tagsIdToRemove = new ArrayList<>();

        currentTagsWithNonNullId.forEach(tag -> {
            if (!previousTagsId.contains(tag.getId())){
                tagsToAdd.add(tag);
            }
        });

        previousTagsId.forEach(tagId -> {
            if (!currentTagsId.contains(tagId)){
                tagsIdToRemove.add(tagId);
            }
        });

        for(Tag tag : tagsToAdd){
            jdbcTemplate.update(SQL_SAVE_GIFT_CERTIFICATE_TO_TAG_ENTRY, giftCertificate.getId(), tag.getId());
        }

        ArrayList<Object> queryParams = new ArrayList<>(tagsIdToRemove);
        queryParams.add(0, giftCertificate.getId());

        if (tagsIdToRemove.size() > 0) {
            jdbcTemplate.update(SqlGenerator.generateDeleteGiftCertificateToTagEntriesByGiftCertificateAndTagsIds(tagsIdToRemove.size()),
                    queryParams.toArray());
        }
    }

    private void addTagsToGiftCertificates(List<GiftCertificate> giftCertificates){
        giftCertificates.forEach(giftCertificate -> {
            List<Integer> tagsId = jdbcTemplate.query(SQL_FIND_TAGS_ID_BY_GIFT_CERTIFICATE_ID, integerMapper, giftCertificate.getId());

            List<Tag> tags  = new ArrayList<>();

            tagsId.forEach(tagId -> tags.add(tagDao.findEntityById(tagId)));

            giftCertificate.setTags(tags);
        });
    }
}
