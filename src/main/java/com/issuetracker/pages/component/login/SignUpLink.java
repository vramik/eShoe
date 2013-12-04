/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component.login;

import com.issuetracker.pages.Register;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author mgottval
 */
public class SignUpLink  extends Panel{
    private Link signUpLink;
    
    public SignUpLink(String id) {
        super(id);
        signUpLink = new Link("signUpLink") {
            @Override
            public void onClick() {
                setResponsePage(Register.class);
            }
        };
        add(signUpLink);
    }
     

}
