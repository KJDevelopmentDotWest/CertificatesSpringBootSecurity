package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.hibernate.JpaUtil;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.model.order.Order;
import com.epam.esm.dao.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class OrderDao implements Dao<Order> {

    @Autowired
    private GiftCertificateDao giftCertificateDao;

    @Autowired
    private UserDao userDao;

    private final EntityManagerFactory entityManagerFactory = JpaUtil.getEntityManagerFactory();

    private static final Integer MAX_ITEMS_IN_PAGE = 5;

    @Override
    @Transactional
    public Order saveEntity(Order entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entity.setId(null);
        entity.setTime(LocalDateTime.now());

        GiftCertificate giftCertificate = giftCertificateDao.findEntityById(entity.getGiftCertificate().getId());
        if (Objects.nonNull(giftCertificate)){
            entity.setCost(giftCertificate.getPrice());
            entity.setGiftCertificate(giftCertificate);
        } else {
            return null;
        }

        User user = userDao.findEntityById(entity.getUser().getId());
        if (Objects.nonNull(user)){
            entity.setUser(user);
        } else {
            return null;
        }

        entityManager.persist(entity);
        entityManager.flush();
        transaction.commit();
        entityManager.close();

        return findEntityById(entity.getId());
    }

    @Override
    public Order updateEntity(Order entity) {
        return null;
    }

    @Override
    public Boolean deleteEntity(Integer id) {
        return null;
    }

    @Override
    public List<Order> findAllEntities() {
        return null;
    }

    @Override
    public Order findEntityById(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Order result = entityManager.find(Order.class, id);
        entityManager.close();
        return result;
    }

    /**
     * returns list of orders by user id and page number
     * @param id id of user
     * @param pageNumber number of page
     * @return list of orders by user id and page number
     */
    public List<Order> findOrdersByUserId(Integer id, Integer pageNumber) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("user"), id));

        Query query = entityManager.createQuery(criteriaQuery).setFirstResult((pageNumber - 1) * MAX_ITEMS_IN_PAGE).setMaxResults(MAX_ITEMS_IN_PAGE);

        List<Order> result = query.getResultList();

        if (result.isEmpty() && pageNumber > 1){
            entityManager.close();
            return findOrdersByUserId(id, pageNumber);
        }

        entityManager.close();

        return result;
    }
}
