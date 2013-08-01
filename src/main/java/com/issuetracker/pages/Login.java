/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.pages.component.login.LoginForm;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 *
 * @author mgottval
 */
public class Login extends PageLayout {

    private LoginForm loginForm;


    public Login() {
        add(new FeedbackPanel("feedback"));


        loginForm = new LoginForm("loginForm");
        add(loginForm);

        final Link forgotPasswordLink = new Link("forgotPasswordLink") {
            @Override
            public void onClick() {
                setResponsePage(ForgotPassword.class);
            }
        };

        add(forgotPasswordLink);

        final Link signUpLink = new Link("signUpLink") {
            @Override
            public void onClick() {
                setResponsePage(Register.class);
            }
        };

        add(signUpLink);



    }
}
