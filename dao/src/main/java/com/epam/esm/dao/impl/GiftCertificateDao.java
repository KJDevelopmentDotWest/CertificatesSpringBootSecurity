package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.model.tag.Tag;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.sqlgenerator.SqlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
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

@Repository
public class GiftCertificateDao implements Dao<GiftCertificate> {

    @Autowired
    private TagDao tagDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public GiftCertificate saveEntity(GiftCertificate entity) {
        entity.setId(null);
        entity.setCreateDate(LocalDateTime.now());
        entity.setLastUpdateDate(LocalDateTime.now());
        addTagsIfNotExists(entity.getTags());
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    @Override
    @Transactional
    public GiftCertificate updateEntity(GiftCertificate entity) {
        if (Objects.nonNull(entity.getTags())){
            addTagsIfNotExists(entity.getTags());
        }
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
        return entity;
    }

    @Override
    @Transactional
    public Boolean deleteEntity(Integer id) {
        GiftCertificate entity = findEntityById(id);

        if (Objects.nonNull(entity)){
            Query query = entityManager.createQuery("DELETE FROM Order s WHERE s.giftCertificate.id = :id");
            query.setParameter("id", entity.getId());
            query.executeUpdate();
            entity = entityManager.merge(entity);
            entityManager.remove(entity);
        }

        entityManager.flush();

        return Objects.nonNull(entity);
    }

    @Override
    public List<GiftCertificate> findAllEntities() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);

        criteriaQuery.from(GiftCertificate.class);

        TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public GiftCertificate findEntityById(Integer id) {
        return entityManager.find(GiftCertificate.class, id);
    }

    /**
     * returns list of gift certificates by page number
     * @param pageNumber number of page
     * @param pageSize size of page
     * @return list of gift certificates by page number
     */
    public List<GiftCertificate> findAllEntities(Integer pageNumber, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);

        criteriaQuery.from(GiftCertificate.class);

        TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery)
                .setFirstResult((pageNumber -1) * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }

    /**
     * returns filtered list of gift certificates
     * @param tagNames names of tag to be searched, empty list if no need to search by tag
     * @param searchPart part of name or  to be filtered, null if no need to filter by name part
     * @param orderByName true for ordering by name, false otherwise
     * @param orderByDate true for ordering by date, false otherwise
     * @param ascending true for ascending order, false if descending. ignored if orderByName and orderByDate is null. true if null
     * @param pageNumber number of page
     * @param pageSize size of page
     * @return list of gift certificates that match parameters
     */
    public List<GiftCertificate> findGiftCertificatesWithParameters (List<String> tagNames, String searchPart, Boolean orderByName,
                                                                     Boolean orderByDate, Boolean ascending, Integer pageNumber, Integer pageSize){
        List<GiftCertificate> result;

        if (tagNames.isEmpty()){
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
            List<Predicate> predicates = new ArrayList<>();
            List<javax.persistence.criteria.Order> orders = new ArrayList<>();

            if (Objects.nonNull(searchPart)){
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("name"), "%" + searchPart + "%"),
                        criteriaBuilder.like(root.get("description"), "%" + searchPart + "%")));
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
                    orders.add(criteriaBuilder.asc(root.get("lastUpdateDate")));
                } else {
                    orders.add(criteriaBuilder.desc(root.get("lastUpdateDate")));
                }
            }

            if (!predicates.isEmpty()){
                criteriaQuery.where(predicates.toArray(Predicate[]::new));
            }
            if (!orders.isEmpty()){
                criteriaQuery.orderBy(orders);
            }

            TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery).setFirstResult((pageNumber -1) * pageSize).setMaxResults(pageSize);

            result = query.getResultList();
        } else {
            result = findGiftCertificatesWithParametersViaNativeQuery(tagNames, searchPart, orderByName,
                    orderByDate, ascending, pageNumber, pageSize);
        }
        return result;
    }

    private List<GiftCertificate> findGiftCertificatesWithParametersViaNativeQuery (List<String> tagNames, String searchPart, Boolean orderByName,
                                                                                    Boolean orderByDate, Boolean ascending, Integer pageNumber, Integer pageSize) {
        List<Object> objectsToAdd = tagNames.stream().map(tagName -> "%," + tagName + ",%").collect(Collectors.toCollection(ArrayList::new));
        List<String> whereStringLikeColumnNames = new ArrayList<>();
        if (Objects.nonNull(searchPart)){
            whereStringLikeColumnNames.add("name");
            objectsToAdd.add(searchPart);
            whereStringLikeColumnNames.add("description");
            objectsToAdd.add(searchPart);
        }

        List<String> orderByColumnNames = new ArrayList<>();
        if (orderByName){
            orderByColumnNames.add("name");
        }
        if (orderByDate){
            orderByColumnNames.add("last_update_date");
        }

        objectsToAdd.add(pageSize);
        objectsToAdd.add((pageNumber -1) * pageSize);

        Query query = entityManager.createNativeQuery(SqlGenerator.generateSQLForGiftCertificateFindWithParameters(tagNames.size(), whereStringLikeColumnNames, orderByColumnNames, ascending));

        for (int i = 0; i < objectsToAdd.size(); i++) {
            query.setParameter(i+1, objectsToAdd.get(i));
        }

        List<Object> ids = query.getResultList();
        return ids.stream().map(obj -> findEntityById((Integer) obj)).collect(Collectors.toCollection(ArrayList::new));
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
