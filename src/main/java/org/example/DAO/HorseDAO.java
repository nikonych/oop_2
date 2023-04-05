package org.example.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.HibernateUtil;
import org.example.model.Horse;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;


public class HorseDAO {


    public HorseDAO() {
    }


    public boolean addHose(Horse horse){
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.persist(horse);
            transaction.commit();
            session.close();
            return true;
        }catch (Exception e){
        }
        return false;
    }


    public List<Horse> getHorses() {
        EntityManager entityManager = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Horse> query = builder.createQuery(Horse.class);
            Root<Horse> root = query.from(Horse.class);
            query.select(root);
//            query.select(root).where(builder.equal(root.get("role"), Role.CANDIDATE)).orderBy(builder.asc(root.get("voteCount")).reverse());
            List<Horse> resultList = entityManager.createQuery(query).getResultList();
            return resultList;
        } finally {
            entityManager.close();
        }
    }

    public void updateHorse(Horse horse) {
        EntityManager entityManager = HibernateUtil.getSessionFactory().openSession();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(horse);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            entityManager.close();
        }
    }
}
