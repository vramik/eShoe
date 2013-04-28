/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issutracker.form;

import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.Credentials;
import com.issuetracker.model.User;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

/**
 *
 * @author mgottval
 */
public class LoginForm extends Form {

    private String username;
    private String password;
    private String loginStatus;
    
    private Credentials credentials;
    
    @Inject
    private UserDao userDao;

    public LoginForm(String id) {
        super(id);
        setDefaultModel(new CompoundPropertyModel(this));

        add(new TextField("username"));
        add(new PasswordTextField("password"));
        add(new Label("loginStatus"));

    }

    @Override
    public final void onSubmit() {
        User user = userDao.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            loginStatus = "Congratulations!";
            credentials = new Credentials();
            credentials.setPassword(password);
            credentials.setUserName(username);
        } else {
            loginStatus = "Wrong username or password!";
        }
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
