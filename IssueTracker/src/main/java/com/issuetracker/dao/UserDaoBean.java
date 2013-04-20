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

/**
 *
 * @author mgottval
 */
@Stateless
public class UserDaoBean implements UserDao{
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> getUsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        List<User> results = em.createQuery("SELECT u FROM User u WHERE email = :email", User.class).setParameter("email", email).getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }
    
    
}
