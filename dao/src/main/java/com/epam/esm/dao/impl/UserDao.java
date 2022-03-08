package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.hibernate.JpaUtil;
import com.epam.esm.dao.model.user.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Component
public class UserDao implements Dao<User> {

    private final EntityManagerFactory entityManagerFactory = JpaUtil.getEntityManagerFactory();

    private static final Integer MAX_ITEMS_IN_PAGE = 5;

    @Override
    public User saveEntity(User entity) {
        return null;
    }

    @Override
    public User updateEntity(User entity) {
        return null;
    }

    @Override
    public Boolean deleteEntity(Integer id) {
        return null;
    }

    @Override
    public List<User> findAllEntities() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        criteriaQuery.from(User.class);
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        List<User> result = query.getResultList();
        entityManager.close();
        return result;
    }

    @Override
    public User findEntityById(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User result = entityManager.find(User.class, id);
        entityManager.close();
        return result;
    }

    /**
     * returns list of users by page number
     * @param pageNumber number of page
     * @return list of gift certificates by page number
     */
    public List<User> findAllEntities(Integer pageNumber) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        criteriaQuery.from(User.class);
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery).setFirstResult((pageNumber -1) * MAX_ITEMS_IN_PAGE).setMaxResults(MAX_ITEMS_IN_PAGE);
        List<User> result = query.getResultList();
        if (result.isEmpty() && pageNumber > 1){
            entityManager.close();
            findAllEntities(1);
        }
        entityManager.close();
        return result;
    }
}
