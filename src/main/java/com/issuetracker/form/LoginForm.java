package com.issuetracker.form;

import com.issuetracker.dao.api.UserDao;
//import com.issuetracker.model.Credentials;
import com.issuetracker.model.User;
import javax.inject.Inject;

import com.issuetracker.service.api.UserService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 *
 * @author mgottval
 */
public class LoginForm extends Form {

    private String username;
    private String password;
    private String loginStatus;
    
//    private Credentials credentials;
    
    @Inject
    private UserService userService;

    public LoginForm(String id) {
        super(id);
        setDefaultModel(new CompoundPropertyModel(this));
        
        add(new RequiredTextField("username"));
        add(new PasswordTextField("password"));
        add(new Label("loginStatus"));

    }

    @Override
    public final void onSubmit() {
        User user = userService.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            loginStatus = "Congratulations!";
//            credentials = new Credentials();
//            credentials.setPassword(password);
//            credentials.setUserName(username);
        } else {
            loginStatus = "Wrong username or password!";
        }
    }

//    public Credentials getCredentials() {
//        return credentials;
//    }
//
//    public void setCredentials(Credentials credentials) {
//        this.credentials = credentials;
//    }
}
