package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.dao.mapper.IntegerMapper;
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
import java.util.stream.Collectors;

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
    private static final String SQL_DELETE_GIFT_CERTIFICATE_TO_TAG_ENTRY_BY_GIFT_CERTIFICATE_ID_AND_TAG_ID = "DELETE FROM gift_certificate_to_tag WHERE gift_certificate_id = ? AND tag_id = ?";

    private final GiftCertificateMapper mapper = new GiftCertificateMapper();
    private final IntegerMapper integerMapper = new IntegerMapper();

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
            preparedStatement.setTimestamp(5, Timestamp.valueOf(entity.getCreateDate()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(entity.getLastUpdateDate()));
            return preparedStatement;
        }, keyHolder);

        Integer generatedEntityId = keyHolder.getKeyAs(Integer.class);

        entity.getTags().forEach(tag -> {

            Integer tagIdToBeAdded = tag.getId();

            if (Objects.isNull(tag.getId())){
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
    public Boolean updateEntity(GiftCertificate entity) {

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

        if (Objects.nonNull(entity.getCreateDate())){
            valuesToInsert.add(entity.getCreateDate());
        }

        if (!valuesToInsert.isEmpty()){
            valuesToInsert.add(LocalDateTime.now());

            valuesToInsert.add(entity.getId());

            return jdbcTemplate.update(SqlGenerator.generateUpdateColString(entity), valuesToInsert.toArray()) != 0;
        } else {
            return false;
        }
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

        result.forEach(giftCertificate -> {
            List<Integer> tagsId = jdbcTemplate.query(SQL_FIND_TAGS_ID_BY_GIFT_CERTIFICATE_ID, integerMapper, giftCertificate.getId());

            List<Tag> tags  = new ArrayList<>();

            tagsId.forEach(tagId -> tags.add(tagDao.findEntityById(tagId)));

            giftCertificate.setTags(tags);
        });

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
     *
     * @param tagId id of tag to be searched, null if no need to search by tag
     * @param namePart part of name to be filtered, null if no need to filter by name part
     * @param descriptionPart part of description to be filtered, null if no need to filter description part
     * @param sortBy sort by code, null if no need to sort
     * @param ascending true for ascending sort, false if descending. ignored if sortBy is null
     * @return list of gift certificates that match parameters
     */
    public List<GiftCertificate> findGiftCertificatesWithParameters (Integer tagId, String namePart, String descriptionPart, SqlGenerator.SortByCode sortBy, Boolean ascending){
        return jdbcTemplate.query(SqlGenerator.generateSQLForGiftCertificateFindWithParameters(tagId, namePart, descriptionPart, sortBy, ascending),
                new GiftCertificateMapper());
    }

    private void updateGiftCertificateToTagEntries(GiftCertificate giftCertificate) {
        List<Tag> tagsCurrent = giftCertificate.getTags();
        List<Tag> tagsWithNullId = tagsCurrent.stream().filter(tag -> Objects.isNull(tag.getId())).toList();
        List<Tag> tagsWithNonNullId = tagsCurrent.stream().filter(tag -> Objects.nonNull(tag.getId())).toList();
        List<Integer> tagsIdCurrent = tagsWithNonNullId.stream().map(Tag::getId).collect(Collectors.toCollection(ArrayList::new));

        tagsWithNullId.forEach(tag -> {
            Integer tagIdToBeAdded;
            Tag foundByName = tagDao.findTagByName(tag.getName());
            if (Objects.isNull(foundByName)){
                tagIdToBeAdded = tagDao.saveEntity(tag).getId();
            } else {
                tagIdToBeAdded = foundByName.getId();
            }
            tagsIdCurrent.add(tagIdToBeAdded);
            jdbcTemplate.update(SQL_SAVE_GIFT_CERTIFICATE_TO_TAG_ENTRY, giftCertificate.getId(), tagIdToBeAdded);
        });

        List<Integer> tagsIdPrevious = jdbcTemplate.query(SQL_FIND_TAGS_ID_BY_GIFT_CERTIFICATE_ID, integerMapper, giftCertificate.getId());

        List<Tag> tagsToAdd = new ArrayList<>();
        List<Integer> tagsIdToRemove = new ArrayList<>();

        tagsWithNonNullId.forEach(tag -> {
            if (!tagsIdPrevious.contains(tag.getId())){
                tagsToAdd.add(tag);
            }
        });

        tagsIdPrevious.forEach(tagId -> {
            if (!tagsIdCurrent.contains(tagId)){
                tagsIdToRemove.add(tagId);
            }
        });

        for(Tag tag : tagsToAdd){
            jdbcTemplate.update(SQL_SAVE_GIFT_CERTIFICATE_TO_TAG_ENTRY, giftCertificate.getId(), tag.getId());
        }

        for(Integer tagId : tagsIdToRemove){
            jdbcTemplate.update(SQL_DELETE_GIFT_CERTIFICATE_TO_TAG_ENTRY_BY_GIFT_CERTIFICATE_ID_AND_TAG_ID, giftCertificate.getId(), tagId);
        }
    }
}
