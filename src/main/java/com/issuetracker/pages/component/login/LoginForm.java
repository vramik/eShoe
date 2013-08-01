/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component.login;

import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.User;
import com.issuetracker.pages.HomePage;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class LoginForm extends Panel {

    @Inject
    private UserDao userDao;
    private Form<User> insertForm;
//    private String email;
//    private String password;
    private User user;

    public LoginForm(String id) {
        super(id);
        user = new User();
        insertForm = new Form<User>("insertForm", new CompoundPropertyModel<User>(user)) {
            @Override
            protected void onSubmit() {
                user = userDao.getUserByEmail(user.getEmail());
                if (user != null && user.getPassword().equals(user.getPassword())) {
                    setResponsePage(HomePage.class);
                }
            }
        };

        insertForm.add(new RequiredTextField<String>("email"));
        insertForm.add(new PasswordTextField("password"));
        add(insertForm);
    }

    //    public String getEmail() {
    //        return email;
    //    }
    //
    //    public void setEmail(String email) {
    //        this.email = email;
    //    }
    //
    //    public String getPassword() {
    //        return password;
    //    }
    //
    //    public void setPassword(String password) {
    //        this.password = password;
    //    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}
