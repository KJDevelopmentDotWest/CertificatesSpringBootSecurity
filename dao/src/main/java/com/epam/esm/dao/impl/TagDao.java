package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.dao.hibernate.JpaUtil;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.dao.model.tag.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Dao interface implementation for Tag with ability to perform CRUD operations
 */

@Component
public class TagDao implements Dao<Tag> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final EntityManagerFactory entityManagerFactory = JpaUtil.getEntityManagerFactory();

    private static final Logger logger = LogManager.getLogger(TagDao.class);

    private static final String SQL_SAVE_TAG = "INSERT INTO tag (name) VALUES (?)";

    private static final String SQL_UPDATE_TAG_BY_ID = "UPDATE tag SET name = ? WHERE id = ?";

    private static final String SQL_DELETE_TAG_BY_ID = "DELETE FROM tag WHERE id = ?";
    private static final String SQL_DELETE_GIFT_CERTIFICATE_TO_TAG_ENTRY_BY_TAG_ID = "DELETE FROM gift_certificate_to_tag WHERE tag_id = ?";

    private static final String SQL_FIND_ALL_TAGS = "SELECT id, name FROM tag";
    private static final String SQL_FIND_TAG_BY_ID = "SELECT id, name FROM tag WHERE id = ?";
    private static final String SQL_FIND_TAG_BY_NAME = "SELECT id, name FROM tag WHERE name = ?";

    private final TagMapper tagMapper = new TagMapper();

    @Override
    @Transactional
    public Tag saveEntity(Tag entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(entity);
        entityManager.flush();
        transaction.commit();
        entityManager.close();

        return findEntityById(entity.getId());
    }

    @Override
    public Tag updateEntity(Tag entity) {
//        //todo add null fields support
//        Session session = entityManagerFactory.openSession();
//        Transaction transaction = session.beginTransaction();
//        session.update(entity);
//        session.flush();
//        transaction.commit();
//        session.close();

        return findEntityById(entity.getId());
    }

    @Override
    @Transactional
    public Boolean deleteEntity(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.remove(findEntityById(id));
        entityManager.flush();
        transaction.commit();
        entityManager.close();
        return true;
    }

    @Override
    public List<Tag> findAllEntities() {

//        Session session = entityManagerFactory.openSession();
//        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
//        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
//
//        Root<Tag> root = criteriaQuery.from(Tag.class);
//
//        Query<Tag> query = session.createQuery(criteriaQuery);
//
//        List<Tag> result = query.getResultList();
//
//        session.close();

        return null;
    }

    @Override
    public Tag findEntityById(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Tag result = entityManager.find(Tag.class, id);
        entityManager.close();
        return result;
    }

    /**
     * returns tag with provided name
     * @param name name to be searched
     * @return tag with provided name, null if there is no such tag
     */
    public Tag findTagByName(String name){
//        Session session = entityManagerFactory.openSession();
//        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
//        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
//
//        Root<Tag> root = criteriaQuery.from(Tag.class);
//
//        criteriaQuery.select(root).where(criteriaBuilder.like(root.get("name"), name));
//
//        Query<Tag> query = session.createQuery(criteriaQuery);
//
//        List<Tag> result = query.getResultList();
//
//        session.close();
//
//        return result.stream().findFirst().orElse(null);
        return null;
    }

    public List<Tag> findTagsByNames(List<String> names){
//        Session session = entityManagerFactory.openSession();
//        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
//        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
//
//        Root<Tag> root = criteriaQuery.from(Tag.class);
//
//        criteriaQuery.select(root).where(root.get("name").in(names));
//
//        Query<Tag> query = session.createQuery(criteriaQuery);
//
//        List<Tag> result = query.getResultList();
//
//        session.close();

        return null;
    }

    public List<Tag> findTagsById(List<Integer> ids){
//        Session session = entityManagerFactory.openSession();
//        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
//        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
//
//        Root<Tag> root = criteriaQuery.from(Tag.class);
//
//        criteriaQuery.select(root).where(root.get("id").in(ids));
//
//        Query<Tag> query = session.createQuery(criteriaQuery);
//
//        List<Tag> result = query.getResultList();
//
//        session.close();

        return null;
    }
}
