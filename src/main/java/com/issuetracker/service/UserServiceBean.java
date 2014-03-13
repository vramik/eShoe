package com.issuetracker.service;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.User;
import com.issuetracker.service.api.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mgottval
 */
@Stateless
public class UserServiceBean implements UserService, Serializable {

    @Inject
    private UserDao userDao;

    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public void insert(User user) {
        userDao.insert(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void remove(User user) {
        userDao.remove(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }
    
    @Override
    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    @Override
    public boolean isUsernameInUse(String username) {
        return userDao.isUsernameInUse(username);
    }
    
    @Override
    public boolean isEmailInUse(String email) {
        return userDao.isEmailInUse(email);
    }

    @Override
    public List<User> getUsers() {
        return userDao.getUsers();
    }

    @Override
    public User loadUserIfPasswordMatches(String name, String password) {
        return userDao.loadUserIfPasswordMatches(name, password);
    }
}
