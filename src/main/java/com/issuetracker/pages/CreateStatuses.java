/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.StatusDao;
import com.issuetracker.dao.api.WorkflowDao;
import com.issuetracker.model.Project;
import com.issuetracker.model.Status;
//import com.issuetracker.model.Transition;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.component.status.StatusListView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.AbstractPropertyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author mgottval
 */
public class CreateStatuses extends PageLayout{
    
    private Workflow workflow;

    @Inject
    private StatusDao statusDao;
    private Status status;
    private List<Status> statuses;
    
    private Form<Status> insertStatusForm;
    
    
    public CreateStatuses() {
        
        status = new Status();
        statuses = statusDao.getStatuses();
        if (statuses == null) {
            statuses = new ArrayList<Status>();
        }
        insertStatusForm = new Form<Status>("insertStatusForm") {
            @Override
            protected void onSubmit() {
                statusDao.insert(status); 
                statuses = statusDao.getStatuses();
                status = new Status();
                
            }         
        };
        insertStatusForm.add(new RequiredTextField("statusName", new PropertyModel<String>(this, "status.name")));
        add(insertStatusForm); 
        
        
        IModel<List<Status>> statusesModel = new CompoundPropertyModel<List<Status>>(statuses) {
            @Override
            public List<Status> getObject() {
                return statusDao.getStatuses(); 
            }            
         };
        
        add(new StatusListView("statusListView", statusesModel, null));
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    } 
    
}
