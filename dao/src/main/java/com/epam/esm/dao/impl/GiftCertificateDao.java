package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.hibernate.JpaUtil;
import com.epam.esm.dao.model.tag.Tag;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.sqlgenerator.SqlGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TagDao tagDao;

    private final EntityManagerFactory entityManagerFactory = JpaUtil.getEntityManagerFactory();

    private static final Logger logger = LogManager.getLogger(GiftCertificateDao.class);

    private static final Integer MAX_ITEMS_IN_PAGE = 5;

    @Override
    @Transactional
    public GiftCertificate saveEntity(GiftCertificate entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entity.setId(null);
        entity.setCreateDate(LocalDateTime.now());
        entity.setLastUpdateDate(LocalDateTime.now());

        addTagsIfNotExists(entity.getTags());

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

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        addTagsIfNotExists(entity.getTags());

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        GiftCertificate oldGiftCertificate = findEntityById(entity.getId());

        if (Objects.isNull(oldGiftCertificate)){
            return null;
        }

        if (Objects.isNull(entity.getName())){
            entity.setName(oldGiftCertificate.getName());
        }

        if (Objects.isNull(entity.getDescription())){
            entity.setDescription(oldGiftCertificate.getDescription());
        }

        if (Objects.isNull(entity.getPrice())){
            entity.setPrice(oldGiftCertificate.getPrice());
        }

        if (Objects.isNull(entity.getDuration())){
            entity.setDuration(entity.getDuration());
        }

        entity.setLastUpdateDate(LocalDateTime.now());
        entity.setCreateDate(oldGiftCertificate.getCreateDate());

        if (Objects.isNull(entity.getTags())){
            entity.setTags(oldGiftCertificate.getTags());
        }

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

        TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery);

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
     * returns list of gift certificates by page number
     * @param pageNumber number of page
     * @return list of gift certificates by page number
     */
    public List<GiftCertificate> findAllEntities(Integer pageNumber) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);

        criteriaQuery.from(GiftCertificate.class);

        TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery).setFirstResult((pageNumber -1) * MAX_ITEMS_IN_PAGE).setMaxResults(MAX_ITEMS_IN_PAGE);

        List<GiftCertificate> result = query.getResultList();

        if (result.isEmpty() && pageNumber > 1){
            entityManager.close();
            findAllEntities(1);
        }

        entityManager.close();
        return result;
    }

    /**
     * returns filtered list of gift certificates
     * @param tagNames names of tag to be searched, empty list if no need to search by tag
     * @param namePart part of name to be filtered, null if no need to filter by name part
     * @param descriptionPart part of description to be filtered, null if no need to filter description part
     * @param orderByName true for ordering by name, false otherwise
     * @param orderByDate true for ordering by date, false otherwise
     * @param ascending true for ascending order, false if descending. ignored if orderByName and orderByDate is null. true if null
     * @param pageNumber page number
     * @throws org.springframework.dao.DataAccessException if database error occurred
     * @return list of gift certificates that match parameters
     */
    public List<GiftCertificate> findGiftCertificatesWithParameters (List<String> tagNames, String namePart, String descriptionPart, Boolean orderByName,
                                                                     Boolean orderByDate, Boolean ascending, Integer pageNumber){
        //todo this method not working as expected
        List<GiftCertificate> result;
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        if (tagNames.isEmpty()){
            CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
            List<Predicate> predicates = new ArrayList<>();
            List<javax.persistence.criteria.Order> orders = new ArrayList<>();

            if (Objects.nonNull(namePart)){
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + namePart + "%"));
            }
            if (Objects.nonNull(descriptionPart)){
                predicates.add(criteriaBuilder.like(root.get("description"), "%" + descriptionPart + "%"));
            }
            if (orderByName) {
                if (Objects.isNull(ascending) || ascending){
                    orders.add(criteriaBuilder.asc(root.get("name")));
                } else {
                    orders.add(criteriaBuilder.desc(root.get("name")));
                }
            }
            if (orderByDate) {
                if (Objects.isNull(ascending) || ascending){
                    orders.add(criteriaBuilder.asc(root.get("date")));
                } else {
                    orders.add(criteriaBuilder.desc(root.get("date")));
                }
            }

            if (!predicates.isEmpty()){
                criteriaQuery.where(predicates.toArray(Predicate[]::new));
            }
            if (!orders.isEmpty()){
                criteriaQuery.orderBy(orders);
            }

            TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery).setFirstResult((pageNumber -1) * MAX_ITEMS_IN_PAGE).setMaxResults(MAX_ITEMS_IN_PAGE);

            result = query.getResultList();
        } else {
            List<Object> objectsToAdd = tagNames.stream().map(tagName -> "%," + tagName + ",%").collect(Collectors.toCollection(ArrayList::new));
            List<String> whereStringLikeColumnNames = new ArrayList<>();
            if (Objects.nonNull(namePart)){
                whereStringLikeColumnNames.add("name");
                objectsToAdd.add(namePart);
            }
            if (Objects.nonNull(descriptionPart)){
                whereStringLikeColumnNames.add("description");
                objectsToAdd.add(descriptionPart);
            }

            List<String> orderByColumnNames = new ArrayList<>();
            if (orderByName){
                orderByColumnNames.add("name");
            }
            if (orderByDate){
                orderByColumnNames.add("last_update_date");
            }

            objectsToAdd.add(MAX_ITEMS_IN_PAGE);
            objectsToAdd.add((pageNumber -1) * MAX_ITEMS_IN_PAGE);

            Query query = entityManager.createNativeQuery(SqlGenerator.generateSQLForGiftCertificateFindWithParameters(tagNames.size(), whereStringLikeColumnNames, orderByColumnNames, ascending));

            for (int i = 0; i < objectsToAdd.size(); i++) {
                query.setParameter(i+1, objectsToAdd.get(i));
            }

            List<Object> ids = query.getResultList();
            result = ids.stream().map(obj -> findEntityById((Integer) obj)).collect(Collectors.toCollection(ArrayList::new));
        }

        if (result.isEmpty() && pageNumber > 1){
            entityManager.close();
            return findGiftCertificatesWithParameters(tagNames, namePart, descriptionPart, orderByName,
                    orderByDate, ascending, 1);
        }

        entityManager.close();
        return result;
    }

    private void addTagsIfNotExists(List<Tag> tags){
        tags.forEach(tag -> {
            if (Objects.isNull(tag.getId())
                    || Objects.isNull(tagDao.findEntityById(tag.getId()))) {
                Tag foundByName = tagDao.findTagByName(tag.getName());
                Integer tagId;
                if (Objects.isNull(foundByName)){
                    tagId = tagDao.saveEntity(tag).getId();
                } else {
                    tagId = foundByName.getId();
                }
                tag.setId(tagId);
            }
        });
    }
}
