package com.issuetracker.dao.api;


import com.issuetracker.model.User;

import javax.ejb.Local;
import java.util.List;

/**
 *
 * @author mgottval
 */
@Local
public interface UserDao {
    
    User getUserById(Long id);
    
    User getUserByEmail(String email);
    
    User getUserByUsername(String username);
    
    User getUserByName(String username);
    
    void insert(User user);
    
    void update(User user);
    
    void remove(User user);
    
    boolean isUsernameInUse(String username);
    
    boolean isEmailInUse(String email);
    
    List<User> getUsers();
    
    User loadUserIfPasswordMatches(String name, String password);
}
