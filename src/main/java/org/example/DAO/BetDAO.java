package org.example.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.HibernateUtil;
import org.example.enums.BetStatus;
import org.example.model.Bet;
import org.example.model.Horse;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;


public class BetDAO {


    public BetDAO() {
    }


    public boolean addBet(Bet bet){
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.persist(bet);
            transaction.commit();
            session.close();
            return true;
        }catch (Exception e){
        }
        return false;
    }


    public List<Bet> getBets() {
        EntityManager entityManager = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Bet> query = builder.createQuery(Bet.class);
            Root<Bet> root = query.from(Bet.class);
//            query.select(root);
//            query.select(root).where(builder.equal(root.get("betStatus"), BetStatus.ACTIVE)).orderBy(builder.asc(root.get("voteCount")).reverse());
            query.select(root).where(builder.equal(root.get("betStatus"), BetStatus.ACTIVE));
            List<Bet> resultList = entityManager.createQuery(query).getResultList();
            return resultList;
        } finally {
            entityManager.close();
        }
    }

    public List<Bet> getBets(User user) {
        EntityManager entityManager = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Bet> query = builder.createQuery(Bet.class);
            Root<Bet> root = query.from(Bet.class);
//            query.select(root);
//            query.select(root).where(builder.equal(root.get("betStatus"), BetStatus.ACTIVE)).orderBy(builder.asc(root.get("voteCount")).reverse());
            query.select(root).where(builder.equal(root.get("user"), user));
            List<Bet> resultList = entityManager.createQuery(query).getResultList();
            return resultList;
        } finally {
            entityManager.close();
        }
    }


    public void updateBet(Bet bet) {
        EntityManager entityManager = HibernateUtil.getSessionFactory().openSession();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(bet);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            entityManager.close();
        }
    }
}
