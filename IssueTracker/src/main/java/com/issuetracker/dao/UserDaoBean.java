package com.issuetracker.dao;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

/**
 *
 * @author mgottval
 */
@Stateless
public class UserDaoBean implements UserDao {

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder qb;

    @Override
    public List<User> getUsers() {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<User> c = qb.createQuery(User.class);
        Root<User> u = c.from(User.class);
        TypedQuery<User> q = em.createQuery(c);
        return q.getResultList();

    }

    @Override
    public User getUserById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addUser(User user) {
        em.persist(user);
    }

    @Override
    public void updateUser(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeUser(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User getUserByEmail(String email) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<User> c = qb.createQuery(User.class);
        Root<User> u = c.from(User.class);
        Predicate condition = qb.equal(u.get("email"), email);
        c.where(condition);
        TypedQuery<User> q = em.createQuery(c);
        List<User> results = q.getResultList();

        //  List<User> results = em.createQuery("SELECT u FROM User u WHERE email = :email", User.class).setParameter("email", email).getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<User> c = qb.createQuery(User.class);
        Root<User> u = c.from(User.class);
        Predicate condition = qb.equal(u.get("username"), username);
        c.where(condition);
        TypedQuery<User> q = em.createQuery(c);
        List<User> results = q.getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }
}
