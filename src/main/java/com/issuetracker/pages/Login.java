/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.pages.component.login.ForgotPasswordLink;
import com.issuetracker.pages.component.login.LoginForm;
import com.issuetracker.pages.component.login.SignUpLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 *
 * @author mgottval
 */
public class Login extends PageLayout {

    private LoginForm loginForm;
    private SignUpLink signUpLink;
    private ForgotPasswordLink forgotPasswordLink;


    public Login() {
        add(new FeedbackPanel("feedback"));


        loginForm = new LoginForm("loginForm");
        add(loginForm);

        forgotPasswordLink = new ForgotPasswordLink("forgotPasswordLink");
        add(forgotPasswordLink);
        
        signUpLink = new SignUpLink("signUpLink");
        add(signUpLink);

    }
}
