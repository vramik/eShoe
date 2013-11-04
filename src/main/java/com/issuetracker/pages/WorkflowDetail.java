package com.issuetracker.pages;

import com.issuetracker.dao.api.StatusDao;
import com.issuetracker.dao.api.WorkflowDao;
import com.issuetracker.model.Status;
//import com.issuetracker.model.Transition;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.component.status.StatusListView;
import com.issuetracker.pages.component.workflow.WorkflowTransitionsListView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author mgottval
 */
public class WorkflowDetail extends PageLayout{
    
    private Workflow workflow;
    @Inject
    private WorkflowDao workflowDao;
    @Inject
    private StatusDao statusDao;
//    private List<Transition> transitionsList;
//    private Transition transition;
    private Status status;
    private List<Status> statuses;
    
    private Form<Status> insertWorkflowStatusForm;
    
    
    public WorkflowDetail(PageParameters parameters) {
        Long workflowId = parameters.get("workflow").toLong();
        workflow = workflowDao.getWorkflowById(workflowId);
        status = new Status();
        
        statuses = statusDao.getStatuses();
        if (statuses == null) {
            statuses = new ArrayList<Status>();
        }
        
        add(new Label("workflowName", workflow.getName()));
        
        add(new Label("transitionsHeader", "Transitions:"));
        add(new WorkflowTransitionsListView("transitionListView", new PropertyModel<Workflow>(this, "workflow")));
        
        add(new Label("statusesHeader", "Click status to create transition."));
        IModel<List<Status>> statusesModel = new CompoundPropertyModel<List<Status>>(statuses) {
            @Override
            public List<Status> getObject() {
                return statusDao.getStatuses(); 
            }            
         };
              
        add(new StatusListView("statusListView", statusesModel, new CompoundPropertyModel<Workflow>(workflow)));
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    } 

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }
    
    
    
}
