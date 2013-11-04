package com.issuetracker.pages;

import com.issuetracker.dao.api.StatusDao;
import com.issuetracker.model.Status;
//import com.issuetracker.model.Transition;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.component.status.StatusListView;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

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
        add(new FeedbackPanel("feedbackPanel"));
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
