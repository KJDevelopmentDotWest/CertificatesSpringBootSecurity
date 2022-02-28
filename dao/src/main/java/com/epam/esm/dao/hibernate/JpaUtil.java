package com.epam.esm.dao.hibernate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

public class JpaUtil {

    //@PersistenceUnit(name = "Certificates")
    private static EntityManagerFactory entityManagerFactory;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("Certificates");
    }

    public static EntityManagerFactory getEntityManagerFactory(){
        return entityManagerFactory;
    }

}
