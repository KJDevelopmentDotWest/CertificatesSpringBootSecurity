package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.model.user.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class UserDao implements Dao<User> {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @Override
    @Transactional
    public User saveEntity(User entity) {
        entity.setId(null);
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        criteriaQuery.from(User.class);
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public User findEntityById(Integer id) {
        return entityManager.find(User.class, id);
    }

    public User findByUsername(String username) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("username"), username));
        TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList().stream().findFirst().orElse(null);
    }

    /**
     * returns list of users by page number
     * @param pageNumber number of page
     * @param pageSize size of page
     * @return list of gift certificates by page number
     */
    public List<User> findAllEntities(Integer pageNumber, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        criteriaQuery.from(User.class);
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery).setFirstResult((pageNumber -1) * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }
}
