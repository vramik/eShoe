/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.User;
import javax.inject.Inject;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class Login extends WebPage {

    @Inject
    private UserDao userDao;

    private Form<User> insertForm;
    
    private String email;
    private String password;
    private User user;

    
    public Login() {
        add(new FeedbackPanel("feedback"));

        insertForm = new Form<User>("insertForm") {
            @Override
            protected void onSubmit() {
                user = userDao.getUserByEmail(email);
                if (user != null && user.getPassword().equals(password)) {
                    setResponsePage(Register.class); 
                }

            }
        };

        insertForm.add(new RequiredTextField<String>("email", new PropertyModel<String>(this, "email")));
        insertForm.add(new PasswordTextField("password", new PropertyModel<String>(this, "password")));
        add(insertForm);

        add(new Link<Void>("linkForgot") {
            @Override
            public void onClick() {
                setResponsePage(ForgotPassword.class);
            }
        });
    }
}
