package com.issuetracker.dao;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.Issue;
import com.issuetracker.model.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author mgottval
 */
public class UserDaoBean implements UserDao{
    
    @PersistenceContext(unitName = "com_IssueTracker_war_1.0-SNAPSHOTPU2")
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
    
    
}
