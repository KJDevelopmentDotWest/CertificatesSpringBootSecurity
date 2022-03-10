package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.model.giftcertificate.GiftCertificate;
import com.epam.esm.dao.model.order.Order;
import com.epam.esm.dao.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderDao implements Dao<Order> {

    @Autowired
    private GiftCertificateDao giftCertificateDao;

    @Autowired
    private UserDao userDao;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @Override
    @Transactional
    public Order saveEntity(Order entity) {
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
        return entity;
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
        return entityManager.find(Order.class, id);
    }

    /**
     * returns list of orders by user userId and page number
     * @param userId userId of user
     * @param pageNumber number of page
     * @param pageSize size of page
     * @return list of orders by user userId and page number
     */
    public List<Order> findOrdersByUserId(Integer userId, Integer pageNumber, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("user"), userId));

        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery).setFirstResult((pageNumber - 1) * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }
}
