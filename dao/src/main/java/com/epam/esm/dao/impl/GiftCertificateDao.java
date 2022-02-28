package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.hibernate.JpaUtil;
import com.epam.esm.dao.mapper.IdMapper;
import com.epam.esm.dao.model.tag.Tag;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.sqlgenerator.SqlGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private final EntityManagerFactory entityManagerFactory = JpaUtil.getEntityManagerFactory();

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

    private final IdMapper integerMapper = new IdMapper();

    @Override
    @Transactional
    public GiftCertificate saveEntity(GiftCertificate entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entity.setCreateDate(LocalDateTime.now());
        entity.setLastUpdateDate(LocalDateTime.now());

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(entity);
        entityManager.flush();
        transaction.commit();
        entityManager.close();

        return findEntityById(entity.getId());
    }

    @Override
    @Transactional
    public GiftCertificate updateEntity(GiftCertificate entity) {

        //todo add null fields support, tag update works not like expected, new tag support
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entity.setLastUpdateDate(LocalDateTime.now());

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(entity);
        entityManager.flush();
        transaction.commit();
        entityManager.close();

        return findEntityById(entity.getId());
    }

    @Override

    public Boolean deleteEntity(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        GiftCertificate entity = findEntityById(id);

        if (Objects.nonNull(entity)){
            entity = entityManager.merge(entity);
            entityManager.remove(entity);
        }

        entityManager.flush();
        transaction.commit();
        entityManager.close();
        return Objects.nonNull(entity);
    }

    @Override
    public List<GiftCertificate> findAllEntities() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);

        criteriaQuery.from(GiftCertificate.class);

        Query query = entityManager.createQuery(criteriaQuery);

        List<GiftCertificate> result = query.getResultList();

        entityManager.close();

        return result;
    }

    @Override
    public GiftCertificate findEntityById(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        GiftCertificate result = entityManager.find(GiftCertificate.class, id);
        entityManager.close();
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
        //todo this method not working as expected
        List<GiftCertificate> result;
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        CriteriaQuery<Object[]> criteriaQueryObject = criteriaBuilder.createQuery(Object[].class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        Root<GiftCertificate> rootObject = criteriaQueryObject.from(GiftCertificate.class);
        Join<Object, Object> tagJoin = root.join("tags");
        Join<Object, Object> tagJoinObject = rootObject.join("tags");
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.like(tagJoin.get("name"), "%" + tagName + "%"));
        predicates.add(criteriaBuilder.like(tagJoin.get("name"), "%" + ",tag," + "%"));

        //criteriaQuery = criteriaQuery.select(root).groupBy(root, criteriaBuilder.concat(tagJoin.get("name"), ",")).having(predicates.toArray(Predicate[]::new));

        criteriaQueryObject.groupBy(rootObject);
        criteriaQueryObject.multiselect(rootObject, criteriaBuilder.sum(tagJoinObject.get("id")));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQueryObject);

        List<Object[]> resultTyped = typedQuery.getResultList();

        resultTyped.forEach(objects -> {
            System.out.println(objects[0]);
            System.out.println(objects[1]);
            System.out.println("=============");
        });

        //result = entityManager.createQuery(criteriaQuery).getResultList().stream().distinct().collect(Collectors.toList());

        return null;
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
