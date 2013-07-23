/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.register;

import com.issuetracker.authentication.RegistrationEndpoint;
import com.issuetracker.authentication.RegistrationRequest;
import com.issuetracker.pages.HomePage;
import com.issuetracker.pages.PageLayout;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

/**
 *
 * @author mgottval
 */
@SuppressWarnings("serial")
public class RegisterReq extends PageLayout {

    private RegistrationRequest request;
    
    @Inject
    private RegistrationEndpoint endpoint;
    
    private Form<RegistrationRequest> insertForm;

    public RegisterReq() {
        request = new RegistrationRequest();
        add(new FeedbackPanel("feedback"));
      //  setDefaultModel(new CompoundPropertyModel(user));

        insertForm = new Form<RegistrationRequest>("insertForm") {
            @Override
            protected void onSubmit() {
                endpoint.register(request);
                setResponsePage(LoginReq.class);
            }
        };

//        insertForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "user.name")));
//        insertForm.add(new RequiredTextField<String>("lastName", new PropertyModel<String>(this, "user.lastName")));
//        insertForm.add(new RequiredTextField<String>("email", new PropertyModel<String>(this, "user.email")));
//        insertForm.add(new PasswordTextField("password", new PropertyModel<String>(this, "user.password")));

        
        
        insertForm.add(new RequiredTextField<String>("firstName"));
        insertForm.add(new RequiredTextField<String>("lastName"));
        
        RequiredTextField<String> email = new RequiredTextField<String>("email");
        email.add(EmailAddressValidator.getInstance());
        insertForm.add(email);
        insertForm.add(new RequiredTextField<String>("userName"));
        insertForm.add(new PasswordTextField("password"));
        insertForm.add(new PasswordTextField("confirmPassword"));
        insertForm.setDefaultModel(new CompoundPropertyModel(request));
        add(insertForm);

        
        final Link homeLink = new Link("homeLink") {
            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        };

        add(homeLink);

      
    }

    
    
     //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="get/set">
    public RegistrationRequest getRegRequest() {
        return request;
    }

    public void setRegRequest(RegistrationRequest request) {
        this.request = request;
    }
}
