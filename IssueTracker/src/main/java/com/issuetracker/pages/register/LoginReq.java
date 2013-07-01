/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.register;

import com.issuetracker.authentication.RegistrationEndpoint;
import com.issuetracker.authentication.RegistrationRequest;
import com.issuetracker.pages.ForgotPassword;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.picketbox.http.wrappers.RequestWrapper;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.picketbox.core.UserContext;

/**
 *
 * @author mgottval
 */
public class LoginReq extends WebPage {

    private Form<RegistrationRequest> insertForm;
    private RegistrationRequest regRequest;

    public LoginReq() {
        add(new FeedbackPanel("feedback"));

        insertForm = new Form<RegistrationRequest>("insertForm") {
            @Override
            protected void onSubmit() {
//                user = userDao.getUserByEmail(email);
//                if (user != null && user.getPassword().equals(password)) {
             //   RequestWrapper requestWrapper = (RequestWrapper) regRequest;
    //UserContext userContext = requestWrapper.getUserContext();
      //          Logger.getLogger(RegistrationEndpoint.class.getName()).log(Level.SEVERE, request.getEmail());
            
                   // setResponsePage("index.jsp");
//                }
                
                
            }
        };

        insertForm.add(new RequiredTextField<String>("userName", new PropertyModel<String>(this, "regRequest.userName")));
        insertForm.add(new PasswordTextField("password", new PropertyModel<String>(this, "regRequest.password")));
        add(insertForm);

//        add(new Link("linkForgot") {
//            @Override
//            public void onClick() {
//                setResponsePage(ForgotPassword.class);
//            }
//        });

        final Link actionLink = new Link("actionLink") {
            @Override
            public void onClick() {
                setResponsePage(ForgotPassword.class);
            }
        };

        add(actionLink);



    }

    public RegistrationRequest getRegRequest() {
        return regRequest;
    }

    public void setRegRequest(RegistrationRequest regRequest) {
        this.regRequest = regRequest;
    }

 
    
}
