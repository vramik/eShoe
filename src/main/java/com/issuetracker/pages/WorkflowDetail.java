package com.issuetracker.pages;

import com.issuetracker.model.Status;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.component.status.StatusListView;
import com.issuetracker.pages.component.workflow.WorkflowTransitionsListView;
import com.issuetracker.service.api.StatusService;
import com.issuetracker.service.api.WorkflowService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

//import com.issuetracker.model.Transition;

/**
 *
 * @author mgottval
 */
public class WorkflowDetail extends PageLayout{
    
    private Workflow workflow;
    @Inject
    private WorkflowService workflowService;
    @Inject
    private StatusService statusService;
//    private List<Transition> transitionsList;
//    private Transition transition;
    private Status status;
    private List<Status> statuses;
    
    private Form<Status> insertWorkflowStatusForm;
    
    
    public WorkflowDetail(PageParameters parameters) {
        Long workflowId = parameters.get("workflow").toLong();
        workflow = workflowService.getWorkflowById(workflowId);
        status = new Status();
        
        statuses = statusService.getStatuses();
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
                return statusService.getStatuses();
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
