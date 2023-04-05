package org.example.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.HibernateUtil;
import org.example.enums.Role;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;


public class UserDAO {

    private final SessionFactory sessionFactory;

    public UserDAO() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public User authenticate(String name) {
        boolean result = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createNamedQuery("User.authenticate");
            query.setParameter("name", name);
            User user = (User) query.uniqueResult();
            if (user != null) {
                return user;
            }
        } finally {
            session.close();
        }
        return null;
    }



//    public List<User> getCandidates() {
//        EntityManager entityManager = HibernateUtil.getSessionFactory().openSession();
//        try {
//            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//            CriteriaQuery<User> query = builder.createQuery(User.class);
//            Root<User> root = query.from(User.class);
//            query.select(root).where(builder.equal(root.get("role"), Role.CANDIDATE)).orderBy(builder.asc(root.get("voteCount")).reverse());
//            List<User> resultList = entityManager.createQuery(query).getResultList();
//            return resultList;
//        } finally {
//            entityManager.close();
//        }
//    }

    public void updateUser(User user) {
        EntityManager entityManager = HibernateUtil.getSessionFactory().openSession();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            entityManager.close();
        }
    }
}
