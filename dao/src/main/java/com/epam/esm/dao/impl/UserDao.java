package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.model.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class UserDao implements Dao<User> {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

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
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        criteriaQuery.from(User.class);
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        List<User> result = query.getResultList();
        return result;
    }

    @Override
    public User findEntityById(Integer id) {
        return entityManager.find(User.class, id);
    }

    /**
     * returns list of users by page number
     * @param pageNumber number of page
     * @param pageSize size of page
     * @return list of gift certificates by page number
     */
    public List<User> findAllEntities(Integer pageNumber, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        criteriaQuery.from(User.class);
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery).setFirstResult((pageNumber -1) * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }
}
