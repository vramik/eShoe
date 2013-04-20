/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao.api;


import com.issuetracker.model.User;
import java.util.List;

/**
 *
 * @author mgottval
 */
public interface UserDao {
    
     List<User> getUsers();
    
    User getUserById(Long id);
    
    void addUser(User user);
    
    void updateUser(User user);
    
    void removeUser(User user);
    
}
