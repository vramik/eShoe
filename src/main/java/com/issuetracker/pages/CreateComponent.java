/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.ComponentDao;
import com.issuetracker.dao.api.IssueTypeDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.IssueType;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class CreateComponent extends PageLayout{
    
    private Form<Component> insertComponentForm;
    private Form<IssueType> insertIssueTypeForm;

    private Component component;
    private IssueType issueType;
    
    @Inject
    private ComponentDao componentDao;
    @Inject
    private IssueTypeDao issueTypeDao;

    public CreateComponent() {
        component = new Component();
        issueType = new IssueType();

        insertComponentForm = new Form<Component>("insertComponentForm") {
            @Override
            protected void onSubmit() {
                componentDao.insertComponent(component);
                component = new Component();
            }
        };

        insertComponentForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "component.name")));
        add(insertComponentForm);
        
        
        insertIssueTypeForm = new Form<IssueType>("insertIssueTypeForm") {
            @Override
            protected void onSubmit() {
                issueTypeDao.insertIssueType(issueType);
                issueType = new IssueType();
            }
        };

        insertIssueTypeForm.add(new RequiredTextField<String>("issueType", new PropertyModel<String>(this, "issueType.name")));
        add(insertIssueTypeForm);
 
    }
    
      public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }
    
    
}
