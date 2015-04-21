package com.issuetracker.pages.workflow;

import com.issuetracker.model.Project;
import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.model.Status;
import static com.issuetracker.model.TypeId.global;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.component.status.StatusListView;
import com.issuetracker.pages.component.workflow.WorkflowTransitionsListView;
import com.issuetracker.pages.permissions.AccessDenied;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.service.api.SecurityService;
import com.issuetracker.service.api.StatusService;
import com.issuetracker.service.api.WorkflowService;
import static com.issuetracker.web.Constants.HOME_PAGE;
import static com.issuetracker.web.Constants.roles;
import com.issuetracker.web.quilifiers.ViewPageConstraint;
import java.util.ArrayList;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.List;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.util.string.StringValue;

/**
 *
 * @author mgottval
 */
public class WorkflowDetail extends PageLayout {
    
    private Workflow workflow;
    @Inject
    private WorkflowService workflowService;
    @Inject
    private StatusService statusService;
    @Inject
    private ProjectService projectService;
    @Inject
    private SecurityService securityService;
    private List<Project> selectedProjects;
    
    @ViewPageConstraint(allowedAction = "it.workflow")
    public WorkflowDetail(PageParameters parameters) {
        StringValue workflowId = parameters.get("workflow");
        if (workflowId.equals(StringValue.valueOf((String)null))) {
            throw new RedirectToUrlException(HOME_PAGE);
        }
        
        workflow = workflowService.getWorkflowById(workflowId.toLong());
        
        selectedProjects = new ArrayList<>();
        
        add(new Label("workflowName", workflow.getName()));
        add(new Link("back") {
            @Override
            public void onClick() {
                setResponsePage(CreateWorkflow.class);
            }
        });
        
        Form<Workflow> setWorkflowToProjects = new Form<Workflow>("setWorkflowToProjects") {
            @Override
            protected void onSubmit() {
                if (!securityService.canUserPerformAction(global, 0L, roles.getProperty("it.workflow"))) {
                    setResponsePage(AccessDenied.class);
                }
                for (Project project : selectedProjects) {
                    project.setWorkflow(workflow);
                    projectService.update(project);
                }
            }
        };
        add(setWorkflowToProjects);
        List<Project> projects = projectService.getProjectsByWorkflow(workflow);
        final ListMultipleChoice<Project> projectMultipleChoise = new ListMultipleChoice<>(
                "projectMultipleChoise", 
                new PropertyModel<List<Project>>(this, "selectedProjects"), 
                projects,
                new ChoiceRenderer<Project>("name"));
        projectMultipleChoise.setMaxRows(projects.size());
        setWorkflowToProjects.add(projectMultipleChoise);
        
                
        add(new Label("transitionsHeader", "Transitions:"));
        add(new WorkflowTransitionsListView("transitionListView", new PropertyModel<Workflow>(this, "workflow")));
        
        add(new Label("statusesHeader", "Click status to create transition."));
        IModel<List<Status>> statusesModel = new CompoundPropertyModel<List<Status>>(statusService.getStatuses()) {
            @Override
            public List<Status> getObject() {
                return statusService.getStatuses();
            }            
         };
              
        add(new StatusListView("statusListView", statusesModel, new CompoundPropertyModel<>(workflow)));
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public List<Project> getSelectedProjects() {
        return selectedProjects;
    }

    public void setSelectedProjects(List<Project> selectedProjects) {
        this.selectedProjects = selectedProjects;
    }
}
