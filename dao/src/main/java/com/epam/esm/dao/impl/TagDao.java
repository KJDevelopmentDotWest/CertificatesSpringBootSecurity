package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.model.tag.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Dao interface implementation for Tag with ability to perform CRUD operations
 */

@Repository
public class TagDao implements Dao<Tag> {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    private static final String GET_USER_ID_WITH_HIGHEST_COST_OF_ALL_ORDERS = "SELECT subselect.user_id FROM (" +
            "SELECT user_id, sum(cost) as cost_sum FROM public.order_table GROUP BY user_id" +
            ") as subselect GROUP BY user_id ORDER BY max(cost_sum) DESC LIMIT 1";

    private static final String GET_MOST_WIDELY_USED_TAG_ID_BY_USER_ID = "SELECT gift_certificate_to_tag.tag_id " +
            "FROM public.order_table " +
            "JOIN gift_certificate ON gift_certificate.id = order_table.gift_certificate_id " +
            "JOIN gift_certificate_to_tag ON gift_certificate.id = gift_certificate_to_tag.gift_certificate_id " +
            "WHERE order_table.user_id = ? GROUP BY gift_certificate_to_tag.tag_id ORDER BY count(gift_certificate_to_tag.tag_id) DESC LIMIT 1";

    @Override
    public Tag saveEntity(Tag entity) {
        entity.setId(null);
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(entity);
        entityManager.flush();
        transaction.commit();
        return entity;
    }

    @Override
    public Tag updateEntity(Tag entity) {
        return null;
    }

    @Override
    public Boolean deleteEntity(Integer id) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Tag entity = findEntityById(id);
        Query query = entityManager.createNativeQuery("DELETE FROM gift_certificate_to_tag WHERE tag_id = ?");
        query.setParameter(1, entity.getId());
        query.executeUpdate();
        entity = entityManager.merge(entity);
        entityManager.remove(entity);
        entityManager.flush();
        transaction.commit();
        return true;
    }

    @Override
    public List<Tag> findAllEntities() {
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        criteriaQuery.from(Tag.class);
        TypedQuery<Tag> query = entityManager.createQuery(criteriaQuery);
        List<Tag> result = query.getResultList();
        return result;
    }

    @Override
    public Tag findEntityById(Integer id) {
        Tag result = entityManager.find(Tag.class, id);
        return result;
    }

    /**
     * returns list of tags by page number
     * @param pageNumber number of page
     * @param pageSize size of page
     * @return list of gift certificates by page number
     */
    public List<Tag> findAllEntities(Integer pageNumber, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        criteriaQuery.from(Tag.class);
        TypedQuery<Tag> query = entityManager.createQuery(criteriaQuery).setFirstResult((pageNumber -1) * pageSize).setMaxResults(pageSize);
        List<Tag> result = query.getResultList();
        return result;
    }

    /**
     * returns tag with provided name
     * @param name name to be searched
     * @return tag with provided name, null if there is no such tag
     */
    public Tag findTagByName(String name){
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).where(criteriaBuilder.like(root.get("name"), name));
        TypedQuery<Tag> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    /**
     * returns most widely used tag for user with the highest cost of all orders
     * @return most widely used tag for user with the highest cost of all orders
     */
    public Tag findMostWidelyUsedTagForUserWithHighestCostOfAllOrders() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Query getUserIdWithHighestCostOfAllOrders = entityManager.createNativeQuery(GET_USER_ID_WITH_HIGHEST_COST_OF_ALL_ORDERS);
        Integer userId = (Integer)getUserIdWithHighestCostOfAllOrders.getResultList().get(0);
        Query getMostWidelyUsedTagIdByUserId = entityManager.createNativeQuery(GET_MOST_WIDELY_USED_TAG_ID_BY_USER_ID);
        getMostWidelyUsedTagIdByUserId.setParameter(1, userId);
        Integer tagId = (Integer) getMostWidelyUsedTagIdByUserId.getResultList().get(0);
        Tag tag = findEntityById(tagId);
        transaction.commit();
        return tag;
    }
}
