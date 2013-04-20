/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.User;
import javax.inject.Inject;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class Register extends WebPage {

    private User user;
    @Inject
    private UserDao userDao;
    private Form<User> insertForm;

    public Register() {
        user = new User();
        add(new FeedbackPanel("feedback"));

        insertForm = new Form<User>("insertForm") {
            @Override
            protected void onSubmit() {
                userDao.addUser(user);
                setResponsePage(Login.class);
            }
        };

        insertForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(user, "name")));
        insertForm.add(new RequiredTextField<String>("lastName", new PropertyModel<String>(user, "lastName")));
        insertForm.add(new RequiredTextField<String>("email", new PropertyModel<String>(user, "email")));
        insertForm.add(new PasswordTextField("password", new PropertyModel<String>(user, "password")));
        add(insertForm);
    }
}
