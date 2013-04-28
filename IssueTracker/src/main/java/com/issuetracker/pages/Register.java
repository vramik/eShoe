/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Project;
import com.issuetracker.model.User;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 *
 * @author mgottval
 */
@SuppressWarnings("serial")
public class Register extends PageLayout {

    private User user;
    
    @Inject
    private UserDao userDao;
    @Inject
    private ProjectDao projectDao;
    @Inject
    private IssueDao issueDao;
    
    private Form<User> insertForm;

    public Register() {
        user = new User();
        add(new FeedbackPanel("feedback"));
        setDefaultModel(new CompoundPropertyModel(user));

        insertForm = new Form<User>("insertForm") {
            @Override
            protected void onSubmit() {
              //  user = new User();
                userDao.addUser(user);
                setResponsePage(HomePage.class);
            }
        };

//        insertForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "user.name")));
//        insertForm.add(new RequiredTextField<String>("lastName", new PropertyModel<String>(this, "user.lastName")));
//        insertForm.add(new RequiredTextField<String>("email", new PropertyModel<String>(this, "user.email")));
//        insertForm.add(new PasswordTextField("password", new PropertyModel<String>(this, "user.password")));

        insertForm.add(new RequiredTextField<String>("name"));
        insertForm.add(new RequiredTextField<String>("lastName"));
        insertForm.add(new RequiredTextField<String>("email"));
        insertForm.add(new RequiredTextField<String>("username"));
        insertForm.add(new PasswordTextField("password"));
        add(insertForm);

        getMenuPanel().setVisible(true);

        
        final Link homeLink = new Link("homeLink") {
            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        };

        add(homeLink);

      
    }

    //<editor-fold defaultstate="collapsed" desc="get/set">
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    
     //</editor-fold>
}
