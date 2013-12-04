/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component.login;

import com.issuetracker.pages.ForgotPassword;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author mgottval
 */
public class ForgotPasswordLink extends Panel{
    private Link signUpLink;
    
    public ForgotPasswordLink(String id) {
        super(id);
        signUpLink = new Link("forgotPasswordLink") {
            @Override
            public void onClick() {
                setResponsePage(ForgotPassword.class);
            }
        };
        add(signUpLink);
    }
}
