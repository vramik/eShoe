/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.User;
import com.issutracker.form.LoginForm;
import javax.inject.Inject;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

/**
 *
 * @author mgottval
 */
public class HomePage extends PageLayout {

    private Form form;
    private DropDownChoice<User> personsList;
    @Inject
    private UserDao userDao;
    
    
    

    public HomePage() {
        //TODO logged user

        final Link searchIssueLink = new Link("searchIssueLink") {
            @Override
            public void onClick() {
                setResponsePage(ListIssues.class);
            }
        };

        // searchIssueLink.add(new Label("SearchName", "Search for issue"));
        add(searchIssueLink);

        final Link createIssueLink = new Link("createIssueLink") {
            @Override
            public void onClick() {
                setResponsePage(CreateIssue.class);
            }
        };

        add(createIssueLink);


        final Link createProjectLink = new Link("createProjectLink") {
            @Override
            public void onClick() {
                setResponsePage(CreateProject.class);
            }
        };

        add(createProjectLink);
        
        final Link createIssuetypeLink = new Link("createIssueTypeLink") {
            @Override
            public void onClick() {
                setResponsePage(CreateComponent.class);
            }
        };

        add(createIssuetypeLink);

        add(new LoginForm("loginForm"));

        final Link registerLink = new Link("registerLink") {
            @Override
            public void onClick() {
                setResponsePage(Register.class);
            }
        };

        add(registerLink);

        final Link forgotPasswdLink = new Link("forgotPasswdLink") {
            @Override
            public void onClick() {
                setResponsePage(ForgotPassword.class);
            }
        };

        add(forgotPasswdLink);






//        Model<User> listModel = new Model<User>();
//        ChoiceRenderer<User> personRender = new ChoiceRenderer<User>("name");
//        personsList = new DropDownChoice<User>("persons", listModel, userDao.getUsers(),
//                personRender) {
//            @Override
//            protected boolean wantOnSelectionChangedNotifications() {
//                return true;
//            }
//        };
//        add(personsList);
//        form = new Form("form", new CompoundPropertyModel<User>(listModel));
//        form.add(new RequiredTextField("name"));
//        form.add(new RequiredTextField("lastname"));
//        form.add(new RequiredTextField("username"));
//        form.add(new RequiredTextField("email"));
//        add(form);


    }
}
