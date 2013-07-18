/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component;

import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.User;
import javax.inject.Inject;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

/**
 *
 * @author mgottval
 */
public class UsernameValidator implements IValidator<String> {
    
    @Inject
    private UserDao userDao;

    @Override
    public void validate(IValidatable<String> validatable) {
        
        String username = validatable.getValue();
        
        User user = userDao.getUserByUsername(username);
        if (user != null) {

          //  error(validatable,"Username already exists");
           
        }
    }

}
